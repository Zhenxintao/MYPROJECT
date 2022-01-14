package com.bmts.heating.service.task.transport.service;

import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.second.request.point.TransportPoint;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.service.task.transport.converter.TransportPointConverter;
import com.bmts.heating.service.task.transport.pojo.TransportHistoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: TransportHistoryService
 * @Description: 处理长输入历史业务
 * @Author: pxf
 * @Date: 2021/11/12 11:36
 * @Version: 1.0
 */
@Service
public class TransportHistoryService {

    private static Logger logger = LoggerFactory.getLogger(TransportHistoryService.class);

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    /**
     * 长输采集点入历史库
     *
     * @return void
     * @Method intoHistory
     * @Param [transportHistoryDtos]
     * @Description
     */
    public void intoHistory(List<TransportHistoryDto> transportHistoryDtos) {

        List<InsertHistoryMinuteDto> listDto = new ArrayList<>();

        transportHistoryDtos.stream().forEach(e -> {
            // 组装参数数据
            InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
            historyDto.setDeviceCode("station_" + e.getTreeId());
            historyDto.setGroupId(TreeLevel.HeatSystem.level());
            historyDto.setLevel(TreeLevel.HeatSystem.level());
            historyDto.setTs(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            // 实体类转换
            List<TransportPoint> points = e.getPoints();
            // 过滤掉 不如历史库的点  和  null 值
            List<TransportPoint> filterList = points.stream().filter(x -> !Objects.equals(x.getDataType(), "bool") && x.getValue() != null).collect(Collectors.toList());

            List<PointInfo> pointInfos = TransportPointConverter.INSTANCE.domainToInfo(filterList);
            if (!CollectionUtils.isEmpty(pointInfos)) {
                historyDto.setPoints(pointInfos);
                listDto.add(historyDto);
            }
            
        });
        try {
            if (!CollectionUtils.isEmpty(listDto)) {
                // 写入历史库
                Boolean historyBoolean = historyTdGrpcClient.insertHistoryMinuteToTd(listDto);

                logger.info("TD---长输点数据入历史---：timeStrap={},total={},historyBoolean = {}",
                        LocalDateTime.now(), listDto.size(), historyBoolean);
            }

        } catch (Exception e) {
            logger.error("TD--ERRORTRANSPORT---异常数据：timeStrap={}, total={},e = {}",
                    LocalDateTime.now(), listDto.size(), e);
            e.printStackTrace();
        }


    }

}
