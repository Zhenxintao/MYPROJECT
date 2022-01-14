package com.bmts.heating.monitor.second.service;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.second.request.device.PointConfigSecondDto;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.monitor.second.config.SecondPointConfig;
import com.bmts.heating.monitor.second.config.TopicsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName: HistoryService
 * @Description: 处理历史数据
 * @Author: pxf
 * @Date: 2021/7/7 17:32
 * @Version: 1.0
 */
@Service
public class SecondDataMapService implements RetracementHandle {

    private static Logger logger = LoggerFactory.getLogger(SecondDataMapService.class);

    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private TopicsConfig topicsConfig;

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    @Autowired
    private SecondPointConfig secondPointConfig;


    public void consume() {
        // 监控 kafka
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        try {
            TopicsConfig.DeviceInfo device_info = topicsConfig.getDevice_info();
            ConsumerOrder consumerOrder = new ConsumerOrder();
            consumerOrder.setTopicName(device_info.getTopicName());
            consumerOrder.setPolicy(device_info.getPolicy());
            consumerOrder.setGroup_id(device_info.getGroup_id());
            consumerOrder.setApplication_num(device_info.getApplication_num());
            KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
            kafkaManager.consumeDatas(consumerOrder, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callBackPattern(Object... objects) {
        // kafka 的  数据
        Map<String, Object> map = (Map<String, Object>) objects[0];
        // 组装成 历史分钟参数进行添加历史数据
        sendTDHistory(map);


    }


    private void sendTDHistory(Map<String, Object> map) {

        List<InsertHistoryMinuteDto> listDto = new ArrayList<>();
        try {
            InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
            historyDto.setLevel(TreeLevel.HeatSystem.level());
            historyDto.setTs(Long.parseLong(map.get("ts").toString()));
            // TD表名
            String tableName = map.get("tableName").toString();
            List<PointConfigSecondDto> listPoints = secondPointConfig.getMap(tableName);
            if (CollectionUtils.isEmpty(listPoints)) {
                return;
            }
            historyDto.setTableName(tableName.concat("_meta"));
            historyDto.setId(Integer.parseInt(map.get("id").toString()));
            historyDto.setGroupId(Integer.parseInt(map.get("groupId").toString()));
            historyDto.setDeviceCode(map.get("deviceCode").toString());
            // 筛选要入库的点数据
            List<PointConfigSecondDto> collect = listPoints.stream().filter(e -> e.getPointType() == 1).collect(Collectors.toList());
            List<PointInfo> pointInfoList = new ArrayList<>();
            collect.stream().forEach(e -> {
                PointInfo pointInfo = new PointInfo();
                pointInfo.setPointName(e.getPointName());
                pointInfo.setValue(map.get(e.getPointName()).toString());
                pointInfoList.add(pointInfo);
            });
            historyDto.setPoints(pointInfoList);

            //  判断对象是否为空
            if (Optional.ofNullable(historyDto).isPresent()) {

                listDto.add(historyDto);

                // 写入历史库
                Boolean historyBoolean = historyTdGrpcClient.insertHistoryMinuteToTd(listDto);

                logger.info("Device---TD---写入历史库---数据：historyBoolean = {}", historyBoolean);
            }

        } catch (Exception e) {
            logger.error("Device---TD--ERRORHistory---异常数据：e = {}", e);
            e.printStackTrace();
        }


    }

}
