package com.bmts.heating.monitor.boot.config;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.second.config.TopicsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
public class MonitorTopicConfig {

    private Logger LOGGER = LoggerFactory.getLogger(MonitorTopicConfig.class);

    @Autowired
    private RouteAdapter routeAdapter;
    @Autowired
    private MonitorType monitorType;
    @Autowired
    private TopicsConfig topicsConfig;


    @Bean
    public void initTopic() {
        //获取当前服务要投递队列send_queue_name
        String send_queue_name = SpringBeanFactory.getBean("send_queue_name").toString();
        //获取当前服务要监控队列monitor_queue_name
        String monitor_queue_name = SpringBeanFactory.getBean("monitor_queue_name").toString();

        //根据路由获取kafkamanager
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
        //创建接收、监控采集任务队列
        createTopic(kafkaManager, send_queue_name);
        createTopic(kafkaManager, monitor_queue_name);
        //创建下发任务队列
        createTopic(kafkaManager, monitorType.getIssue_queue());
        // 创建二网主题队列
        createTopic(kafkaManager, topicsConfig.getDevice_info().getTopicName());
        createTopic(kafkaManager, topicsConfig.getDevice_fault().getTopicName());


        //deleteTopic(kafkaManager, send_queue_name);
        //deleteTopic(kafkaManager, monitor_queue_name);
        //deleteTopic(kafkaManager, monitorType.getIssue_queue());
        //
        //deleteTopic(kafkaManager, topicsConfig.getDevice_info().getTopicName());
        //deleteTopic(kafkaManager, topicsConfig.getDevice_fault().getTopicName());
//        deleteTopic(kafkaManager, "__confluent.support.metrics");
//        deleteTopic(kafkaManager, "__consumer_offsets");


    }

    public void createTopic(KafkaManager kafkaManager, String topicName) {
        //检测队列是否存在并创建
        boolean targes = kafkaManager.checkTopic(topicName);
        if (!targes)
            //创建队列
            kafkaManager.createNewTopic(topicName, 3, (short) 1);
    }


    public void deleteTopic(KafkaManager kafkaManager, String topicName) {
        kafkaManager.deleteTopic(topicName);
    }
}
