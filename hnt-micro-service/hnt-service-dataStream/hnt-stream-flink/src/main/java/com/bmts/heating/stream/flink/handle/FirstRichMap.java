package com.bmts.heating.stream.flink.handle;

import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.stream.flink.app.SpringBootFlinkApplication;
import com.bmts.heating.stream.flink.converter.PointLConverter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: FirstRichMap
 * @Description: flink 第一个业务节点
 * @Author: pxf
 * @Date: 2021/7/21 16:59
 * @Version: 1.0
 */

public class FirstRichMap extends RichMapFunction<Message_Gather, Message_Gather> {

    private static Logger logger = LoggerFactory.getLogger(FirstRichMap.class);

    private HistoryTdGrpcClient historyTdGrpcClient;

    // 可以进行初始化spring 容器
    @Override
    public void open(Configuration parameters) throws Exception {
        SpringBootFlinkApplication.run();
        this.historyTdGrpcClient = SpringBeanFactory.getBean(HistoryTdGrpcClient.class);
//        super.open(parameters);
    }

    // 处理业务
    @Override
    public Message_Gather map(Message_Gather messageGather) {
        logger.info("-----First------------- 接收对象数据----- {}", messageGather.getRelevanceId());
        // 系统数据
        List<Message_Point_Gather> batchPoints = messageGather.getBatchPoints();

        // 0 系统的点集合
        Map<String, PointL> zeroSystem = new HashMap<>();
        // 系统的点集合
        for (Message_Point_Gather collect : batchPoints) {
            if (collect.getNumber() == 0) {
                zeroSystem = collect.getPointLS();
            }
        }
        List<Message_Point_Gather> systemList = batchPoints.stream().filter(e -> e.getNumber() != 0).collect(Collectors.toList());

        for (Message_Point_Gather pointGather : systemList) {
            Map<String, PointL> pointLS = pointGather.getPointLS();
            if (zeroSystem.size() > 0) {
                pointLS.putAll(zeroSystem);
            }
            // 写入历史库
            sendTDHistory(pointGather, messageGather.getTimeStamp(), messageGather.getRelevanceId(), messageGather.getRelevanceType());

        }
        return messageGather;
    }

    // 在关闭之前执行的一些操作
    @Override
    public void close() throws Exception {
//        super.close();
    }

    public void sendHistory(Message_Point_Gather pointGather, Long timeStamp, Integer relevanceId, Integer relevanceType) {
        // 系统的  点 集合
        Map<String, PointL> pointLMap = pointGather.getPointLS();
        List<PointL> pointList = pointLMap.values().stream().collect(Collectors.toList());
        // 系统id
        Integer systemId = pointGather.getRelevanceId();
        if (!CollectionUtils.isEmpty(pointList)) {
            // 时间戳 转  时间
            LocalDateTime stampTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            // 取出有设备code 的数据, 并且 按照code 分组
            Map<String, List<PointL>> groupMap = pointList.stream().filter(e -> e.getEquipmentCode() != null).collect(Collectors.groupingBy(e -> e.getEquipmentCode()));


            List<PointInfo> pointInfoList = new ArrayList<>();
            try {
                List<InsertHistoryMinuteDto> listDto = new ArrayList<>();

                for (String key : groupMap.keySet()) {
                    // 组装参数数据
                    InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
                    historyDto.setDeviceCode(key);
                    historyDto.setGroupId(systemId);
                    historyDto.setLevel(TreeLevel.HeatSystem.level());
                    historyDto.setTs(timeStamp);
                    // 实体类转换
                    List<PointL> pointLS = groupMap.get(key);
                    List<PointInfo> pointInfos = PointLConverter.INSTANCE.domainToInfo(pointLS);
                    historyDto.setPoints(pointInfos);
                    listDto.add(historyDto);
                    pointInfoList.addAll(pointInfos);
                }
                if (!CollectionUtils.isEmpty(listDto)) {
                    // 写入历史库
                    Boolean historyBoolean = historyTdGrpcClient.insertHistoryMinuteToTd(listDto);

                    logger.info("WriteHistory---写入历史库---数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},historyBoolean = {}",
                            relevanceId, stampTime, relevanceType, systemId, pointInfoList.size(), historyBoolean);
                }


            } catch (Exception e) {
                logger.error("ERRORHistory---异常数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},e = {}",
                        relevanceId, stampTime, relevanceType, systemId, pointInfoList.size(), e);
                e.printStackTrace();
            }


        }
    }


    public void sendTDHistory(Message_Point_Gather pointGather, Long timeStamp, Integer relevanceId, Integer relevanceType) {
        // 系统的  点 集合
        Map<String, PointL> pointLMap = pointGather.getPointLS();
        List<PointL> pointList = pointLMap.values().stream().collect(Collectors.toList());
        // 系统id
        Integer systemId = pointGather.getRelevanceId();
        if (!CollectionUtils.isEmpty(pointList)) {
            // 时间戳 转  时间
            LocalDateTime stampTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

            List<PointInfo> pointInfoList = new ArrayList<>();
            try {
                List<InsertHistoryMinuteDto> listDto = new ArrayList<>();

                Integer heatType = pointList.get(0).getHeatType();

                // 热力站的点
                if (heatType == 1) {
                    // 组装参数数据
                    InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
                    historyDto.setDeviceCode("station_minute");
                    historyDto.setGroupId(systemId);
                    historyDto.setLevel(TreeLevel.HeatSystem.level());
                    historyDto.setTs(timeStamp);
                    // 实体类转换
                    List<PointInfo> pointInfos = PointLConverter.INSTANCE.domainToInfo(pointList);
                    historyDto.setPoints(pointInfos);
                    listDto.add(historyDto);
                    pointInfoList.addAll(pointInfos);

                }
                // 热源的点
                if (heatType == 2) {
                    // 组装参数数据
                    InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
                    historyDto.setDeviceCode("source_minute");
                    historyDto.setGroupId(systemId);
                    historyDto.setLevel(TreeLevel.HeatSystem.level());
                    historyDto.setTs(timeStamp);
                    // 实体类转换
                    List<PointInfo> pointInfos = PointLConverter.INSTANCE.domainToInfo(pointList);
                    historyDto.setPoints(pointInfos);
                    listDto.add(historyDto);
                    pointInfoList.addAll(pointInfos);
                }

                if (!CollectionUtils.isEmpty(listDto)) {
                    // 写入历史库
                    Boolean historyBoolean = historyTdGrpcClient.insertHistoryMinuteToTd(listDto);

                    logger.info("WriteHistory---写入历史库---数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},historyBoolean = {}",
                            relevanceId, stampTime, relevanceType, systemId, pointInfoList.size(), historyBoolean);
                }


            } catch (Exception e) {
                logger.error("ERRORHistory---异常数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},e = {}",
                        relevanceId, stampTime, relevanceType, systemId, pointInfoList.size(), e);
                e.printStackTrace();
            }


        }
    }

}
