package com.bmts.heating.middleground.history.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.db.service.CommonHeatSeasonService;
import com.bmts.heating.commons.db.service.PointAlarmViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.baseInfo.request.AbnormalDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTdDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTypeTd;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.QueryBaseHistoryResponse;
import com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class AbnormalServiceImpl implements AbnormalService {

    @Autowired
    private HistoryTdGrpcClient tdGrpcClient;

    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;

    @Autowired
    private CommonHeatSeasonService commonHeatSeasonService;


    @Override
    public List<Abnormal> pageAbnormal(AbnormalDto dto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("heatSystemId", dto.getGroupId());
        if (StringUtils.isNotBlank(dto.getStationName())){
            queryWrapper.like("heatTransferStationName",dto.getStationName());
        }
        List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list(queryWrapper);
        List<Integer> list1 = new ArrayList<>();
        for (StationFirstNetBaseView stationFirstNetBaseView : list) {
            list1.add(stationFirstNetBaseView.getHeatSystemId());
        }
        dto.setGroupId(list1);
        QueryTdDto queryTdDto = setQueryTdDto(dto);
        QueryBaseHistoryResponse response = tdGrpcClient.queryTdEngineData(queryTdDto);
        JSONArray array = response.getJsonData();
        List<Abnormal> abnormals = JSONArray.parseArray(array.toJSONString(), Abnormal.class);
        for (StationFirstNetBaseView stationFirstNetBaseView : list) {
            String heatTransferStationName = stationFirstNetBaseView.getHeatTransferStationName();
            Integer heatSystemId = stationFirstNetBaseView.getHeatSystemId();
            for (Abnormal abnormal : abnormals) {
                if (abnormal.getGroupId().equals(heatSystemId)){
                    abnormal.setStationName(heatTransferStationName);
                }
            }
        }
        return abnormals;
    }

    //查询报警统计
    @Override
    public Integer alarmCountIndex(AbnormalDto abnormalDto) {
        //传递参数 系统id level ? 当前传递系统id level
        LocalDateTime nowTime = LocalDateTime.now();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ge("heatEndTime",nowTime);
        queryWrapper.le("heatStartTime",nowTime);
        CommonHeatSeason one = commonHeatSeasonService.getOne(queryWrapper);
        if (one == null){
            throw new RuntimeException("没有查询到当前供暖季基础数据！");
        }
        abnormalDto.setEndTime(nowTime.toString());
        abnormalDto.setStartTime(one.getHeatStartTime().toString());
        QueryTdDto queryTdDto = setQueryTdDto(abnormalDto);
        QueryBaseHistoryResponse response = tdGrpcClient.queryTdEngineData(queryTdDto);
        JSONArray array = response.getJsonData();
        List<Abnormal> abnormals = JSON.parseArray(array.toJSONString(), Abnormal.class);
        return abnormals.size();
    }

    private QueryTdDto setQueryTdDto(AbnormalDto dto) {
        QueryTdDto queryTdDto = new QueryTdDto();
        queryTdDto.setQueryTypeTd(QueryTypeTd.abnormal);
        if (!CollectionUtils.isEmpty(dto.getGroupId())) {
            queryTdDto.setGroupId(dto.getGroupId());
        }
        if (dto.getAbnormalType() != null) {
            queryTdDto.setAbnormalType(dto.getAbnormalType());
        }
        if (dto.getLevel() != null) {
            queryTdDto.setLevel(dto.getLevel());
        }
        queryTdDto.setLimit(dto.getPageCount());
        queryTdDto.setOffset(dto.getCurrentPage() * dto.getPageCount());
        if (StringUtils.isNotBlank(dto.getOrder())) {
            queryTdDto.setOrder(dto.getOrder());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        if (StringUtils.isNotBlank(dto.getStartTime())) {
            LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime(), formatter);
            queryTdDto.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        if (StringUtils.isNotBlank(dto.getEndTime())) {
            LocalDateTime endTime = LocalDateTime.parse(dto.getEndTime(), formatter);
            queryTdDto.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        return queryTdDto;
    }

}
