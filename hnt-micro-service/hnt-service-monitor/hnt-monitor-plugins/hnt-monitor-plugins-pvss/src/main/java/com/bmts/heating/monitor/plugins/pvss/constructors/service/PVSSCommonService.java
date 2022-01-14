package com.bmts.heating.monitor.plugins.pvss.constructors.service;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: KafkaCommonService
 * @Description: kafka 消息处理类
 * @Author: pxf
 * @Date: 2020/8/13 15:39
 * @Version: 1.0
 */

@Slf4j
@Service
public class PVSSCommonService {


    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private MonitorType monitorType;

    @Autowired
    private RedisCacheService redisCacheService;

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
        product(monitorType.getIssue_queue(), new Gson().toJson(messags));
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
        if (ent.getSystemNum() != null) {
            messags.setNumber(ent.getSystemNum());
        }
        product(monitorType.getIssue_queue(), messags);
        messageMap.clear();
    }

    /**
     * 根据   一次网、二次网、用户数据 和  站点 进行数据分组
     *
     * @param list
     * @return
     */
    public Map<Integer, List<PointL>> group(List<PointL> list) {
        Map<Integer, List<PointL>> groupMap = list.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId()));
        return groupMap;
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


    public List<List<PointL>> getCachePoints(String deviceId) {
        //获取点表信息
        try {
            List<List<PointL>> listS = new ArrayList<>();

            List<PointL> pointLS = redisCacheService.getPoints(deviceId);
            if (!CollectionUtils.isEmpty(pointLS)) {
                for (PointL pl : pointLS) {
                    pl.setDataType(1);
                }
                // 根据机组系统id 进行分组
                Map<Integer, List<PointL>> collect = pointLS.stream().collect(Collectors.groupingBy(e -> e.getLevel() + e.getRelevanceId()));
                for (Integer releverceId : collect.keySet()) {
                    List<PointL> systemList = collect.get(releverceId);
                    listS.add(systemList);
                }
            }
            return listS;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return null;
    }

}
