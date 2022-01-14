package com.bmts.heating.grpc.dataCleaning.config;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.config.InitColony;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;


@Configuration
@AutoConfigureAfter(InitColony.class)
@Description("队列监控")
public class DataTopicConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataTopicConfig.class);


    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private DataKafKaConfig dataConfig;

    @Bean
    public void initDataCleanTopic() {
        //根据路由获取kafkamanager
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
        // 监控队列
        DataKafKaConfig.DataCenter dataCenter = dataConfig.getDataCenter();
        // 生产队列
        DataKafKaConfig.DataProduce dataProduce = dataConfig.getDataProduce();
        // 下发命令队列
        DataKafKaConfig.DataIssue dataIssue = dataConfig.getDataIssue();
        // 创建队列
//        createTopic(kafkaManager, dataCenter.getTopicName());
//        createTopic(kafkaManager, dataProduce.getTopicName());
//        createTopic(kafkaManager, dataIssue.getTopicName());

//        deleteTopic(kafkaManager, dataCenter.getTopicName());
//        deleteTopic(kafkaManager, dataProduce.getTopicName());
//        deleteTopic(kafkaManager, dataIssue.getTopicName());
//
//        deleteTopic(kafkaManager, "queue-monitor-issue");
//        deleteTopic(kafkaManager, "queue-monitor-monitor100-slave");
//        deleteTopic(kafkaManager, "queue-monitor-monitor100-master");
//        deleteTopic(kafkaManager, "queue-monitor-monitor64-master");
//
//        deleteTopic(kafkaManager, "queue-monitor-send-monitor100");
        deleteTopic(kafkaManager, "queue-monitor-send-monitor64");

//        createTopic(kafkaManager, "queue-monitor-send-monitor64");
        // 开启监控队列
//        PointKafkaHandle.consume();
    }

    public void createTopic(KafkaManager kafkaManager, String topicName) {
        //检测队列是否存在并创建
        boolean targes = kafkaManager.checkTopic(topicName);
        if (!targes) {
            //创建队列
            kafkaManager.createNewTopic(topicName, 3, (short) 1);
        }
    }

    public void deleteTopic(KafkaManager kafkaManager, String topicName) {
        kafkaManager.deleteTopic(topicName);
    }

}
