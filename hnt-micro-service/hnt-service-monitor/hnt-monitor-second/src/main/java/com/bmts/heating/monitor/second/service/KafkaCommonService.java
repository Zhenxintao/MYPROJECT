package com.bmts.heating.monitor.second.service;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.entiy.second.request.device.DeviceFaultDto;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.monitor.second.config.TopicsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: KafkaCommonService
 * @Description: kafka 消息处理类
 * @Author: pxf
 * @Date: 2020/8/13 15:39
 * @Version: 1.0
 */

@Component
public class KafkaCommonService {


    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private TopicsConfig topicsConfig;


    /**
     * 组装kafka 发送消息实体
     *
     * @param DeviceInfoDto
     */
    public void sendDeviceInfo(DeviceInfoDto info) {
        product(topicsConfig.getDevice_info().getTopicName(), info);
    }

    /**
     * 组装kafka 发送消息实体
     *
     * @param DeviceFaultDto
     */
    public void sendDeviceFault(DeviceFaultDto dto) {
        product(topicsConfig.getDevice_fault().getTopicName(), dto);
    }


    public void product(String topicName, Object obj) {
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
        try {
            kafkaManager.produceDatasBySingleton(topicName, 3, obj);
        } catch (KafkaException e) {
            e.printStackTrace();
        }

    }

}
