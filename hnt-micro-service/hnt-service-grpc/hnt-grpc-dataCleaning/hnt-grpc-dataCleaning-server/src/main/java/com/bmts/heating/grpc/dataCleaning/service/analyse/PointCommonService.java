package com.bmts.heating.grpc.dataCleaning.service.analyse;

import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.grpc.dataCleaning.config.DataKafKaConfig;
import com.bmts.heating.grpc.dataCleaning.kafka.handler.PointKafkaHandle;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: PointCommonService
 * @Description: 数据清洗解析类
 * @Author: pxf
 * @Date: 2020/8/13 15:39
 * @Version: 1.0
 */

@Service
public class PointCommonService {

    @Autowired
    private PointKafkaHandle pointKafkaHandle;

    @Autowired
    private DataKafKaConfig dataConfig;


    /**
     * 组装kafka 发送   json  格式消息
     *
     * @param messageMap
     * @param ent
     */
    public void setMessageJson(Map<String, PointL> messageMap, PointL ent) {
        Message_Point_Gather messags = new Message_Point_Gather();
        messags.setPointLS(messageMap);
        messags.setData_type(ent.getDataType());
        messags.setLevel(ent.getLevel());
        messags.setRelevanceId(ent.getRelevanceId());
        pointKafkaHandle.product(dataConfig.getDataProduce().getTopicName(), new Gson().toJson(messags));
        messageMap.clear();
    }

    /**
     * 组装kafka 发送消息实体
     *
     * @param messageMap
     * @param ent
     */
    public void setMessageProduce(Map<String, PointL> messageMap, PointL ent) {
        Message_Point_Gather messags = new Message_Point_Gather();
        messags.setPointLS(messageMap);
        messags.setData_type(ent.getDataType());
        messags.setLevel(ent.getLevel());
        messags.setRelevanceId(ent.getRelevanceId());
        pointKafkaHandle.product("queue-monitor-send-monitor64", messags);
        messageMap.clear();
    }

    /**
     * 根据   一次网、二次网、用户数据 和  站点 进行数据分组
     *
     * @param list
     * @return
     */
//    public Map<String, List<PointL>> group(List<PointL> list) {
//        Map<String, List<PointL>> groupMap = list.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getHeatingSystemId()));
//        return groupMap;
//    }


    /**
     * 根据   一次网、二次网、用户数据 和  站点 进行数据分组
     *
     * @param list
     * @return
     */
    public Map<Integer, List<PointL>> group(List<PointL> list) {
        Map<Integer, List<PointL>> groupMap = list.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel()+e.getRelevanceId()));
        return groupMap;
    }

}
