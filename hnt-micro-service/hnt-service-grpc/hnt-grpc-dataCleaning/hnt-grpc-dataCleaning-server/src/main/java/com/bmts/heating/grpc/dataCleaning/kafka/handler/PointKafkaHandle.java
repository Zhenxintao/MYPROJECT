package com.bmts.heating.grpc.dataCleaning.kafka.handler;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.grpc.dataCleaning.config.DataKafKaConfig;
import com.bmts.heating.grpc.dataCleaning.kafka.PointKafkaInterface;
import com.bmts.heating.grpc.dataCleaning.service.issue.PointIssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointKafkaHandle implements PointKafkaInterface, RetracementHandle {

    @Autowired
    private DataKafKaConfig dataConfig;

    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private PointIssueService pointIssueService;


    private final Logger LOGGER = LoggerFactory.getLogger(PointKafkaHandle.class);

    @Override
    public void callBackPattern(Object... objects) {
        Message_Point_Issue pointIssue = (Message_Point_Issue) objects[0];
//        if (pointIssue != null && pointIssue.getValidTimeStamp() >= System.currentTimeMillis()) {
//            // 熟数据 转生数据
//            Message_Point_Issue messageIssue = pointIssueService.matureToRaw(pointIssue);
//            if (messageIssue != null) {
//                DataKafKaConfig.DataIssue dataIssue = dataConfig.getDataIssue();
//                // 下发命令任务
//                product(dataIssue.getTopicName(), messageIssue);
//            }
//        }

    }


    @Override
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

    @Override
    public void consume() {
        //监控任务队列
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        try {
            DataKafKaConfig.DataCenter dataCenter = dataConfig.getDataCenter();
            ConsumerOrder consumerOrder = new ConsumerOrder();
            consumerOrder.setTopicName(dataCenter.getTopicName());
            consumerOrder.setPolicy(dataCenter.getPolicy());
            consumerOrder.setGroup_id(dataCenter.getGroup_id());
            consumerOrder.setApplication_num(dataCenter.getApplication_num());
            KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
            kafkaManager.consumeDatas(consumerOrder, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
