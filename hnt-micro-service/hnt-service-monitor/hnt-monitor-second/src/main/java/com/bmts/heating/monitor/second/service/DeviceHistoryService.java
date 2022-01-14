package com.bmts.heating.monitor.second.service;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.second.enums.SecondDeviceType;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.monitor.second.config.TopicsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: HistoryService
 * @Description: 处理历史数据
 * @Author: pxf
 * @Date: 2021/7/7 17:32
 * @Version: 1.0
 */
@Service
public class DeviceHistoryService implements RetracementHandle {

    private static Logger logger = LoggerFactory.getLogger(DeviceHistoryService.class);

    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private TopicsConfig topicsConfig;

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;


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
        // 一个站的 所有系统的 数据
        DeviceInfoDto info = (DeviceInfoDto) objects[0];
        // 组装成 历史分钟参数进行添加历史数据
        sendTDHistory(info);


    }


    private void sendTDHistory(DeviceInfoDto info) {
        long timestamp = info.getTimestamp();

        List<InsertHistoryMinuteDto> listDto = new ArrayList<>();
        try {
            InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
            Integer deviceType = info.getDeviceType();
            // 单元阀
            if (deviceType == SecondDeviceType.DEVICE_UNIT_VALVE.getType()) {
                // 组装参数数据
                historyDto.setDeviceCode("iot_uv_device");
                historyDto.setGroupId(Integer.parseInt(info.getDeviceCode()));
                historyDto.setLevel(TreeLevel.HeatSystem.level());
                historyDto.setTs(timestamp);
                // 实体类转换
                List<PointInfo> pointInfoList = new ArrayList<>();
                PointInfo pointInfo = new PointInfo();
                pointInfo.setPointName("acture_opening");
                pointInfo.setValue(info.getActure_opening().toString());
                PointInfo pointInfo2 = new PointInfo();
                pointInfo2.setPointName("given_opening");
                pointInfo2.setValue(info.getGiven_opening().toString());
                PointInfo pointInfo3 = new PointInfo();
                pointInfo3.setPointName("tg");
                pointInfo3.setValue(info.getTg().toString());
                PointInfo pointInfo4 = new PointInfo();
                pointInfo4.setPointName("th");
                pointInfo4.setValue(info.getTh().toString());
                PointInfo pointInfo5 = new PointInfo();
                pointInfo5.setPointName("pg");
                pointInfo5.setValue(info.getPg().toString());
                PointInfo pointInfo6 = new PointInfo();
                pointInfo6.setPointName("ph");
                pointInfo6.setValue(info.getPh().toString());

                pointInfoList.add(pointInfo);
                pointInfoList.add(pointInfo2);
                pointInfoList.add(pointInfo3);
                pointInfoList.add(pointInfo4);
                pointInfoList.add(pointInfo5);
                pointInfoList.add(pointInfo6);

                historyDto.setPoints(pointInfoList);

            }
            // 户阀
            if (deviceType == SecondDeviceType.DEVICE_HOUSE_VALVE.getType()) {
                // 组装参数数据
                historyDto.setDeviceCode("iot_hv_device");
                historyDto.setGroupId(Integer.parseInt(info.getDeviceCode()));
                historyDto.setLevel(TreeLevel.HeatSystem.level());
                historyDto.setTs(timestamp);
                // 实体类转换
                List<PointInfo> pointInfoList = new ArrayList<>();
                PointInfo pointInfo = new PointInfo();
                pointInfo.setPointName("acture_opening");
                pointInfo.setValue(info.getActure_opening().toString());
                PointInfo pointInfo2 = new PointInfo();
                pointInfo2.setPointName("given_opening");
                pointInfo2.setValue(info.getGiven_opening().toString());
                PointInfo pointInfo3 = new PointInfo();
                pointInfo3.setPointName("tg");
                pointInfo3.setValue(info.getTg().toString());
                PointInfo pointInfo4 = new PointInfo();
                pointInfo4.setPointName("th");
                pointInfo4.setValue(info.getTh().toString());
                PointInfo pointInfo5 = new PointInfo();
                pointInfo5.setPointName("pg");
                pointInfo5.setValue(info.getPg().toString());
                PointInfo pointInfo6 = new PointInfo();
                pointInfo6.setPointName("ph");
                pointInfo6.setValue(info.getPh().toString());

                pointInfoList.add(pointInfo);
                pointInfoList.add(pointInfo2);
                pointInfoList.add(pointInfo3);
                pointInfoList.add(pointInfo4);
                pointInfoList.add(pointInfo5);
                pointInfoList.add(pointInfo6);

                historyDto.setPoints(pointInfoList);
            }
            // 室温
            if (deviceType == SecondDeviceType.DEVICE_RT.getType()) {
                // 组装参数数据
                historyDto.setDeviceCode("iot_rt_device");
                historyDto.setGroupId(Integer.parseInt(info.getDeviceCode()));
                historyDto.setLevel(TreeLevel.HeatSystem.level());
                historyDto.setTs(timestamp);
                // 实体类转换
                List<PointInfo> pointInfoList = new ArrayList<>();
                PointInfo pointInfo = new PointInfo();
                pointInfo.setPointName("temperature");
                pointInfo.setValue(info.getTemperature().toString());

                pointInfoList.add(pointInfo);

                historyDto.setPoints(pointInfoList);
            }
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
