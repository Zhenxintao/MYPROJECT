package com.bmts.heating.service.task.storm.config;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(2)
public class TopicConfig {

    private Logger LOGGER = LoggerFactory.getLogger(TopicConfig.class);

    @Autowired
    private RouteAdapter routeAdapter;

    @Bean
    public void initTopic() {
        //获取当前服务要投递队列  send_queue_name   是  hnt-monitor-boot  采集服务的 yml 配置文件 的 send_queue_name

        //根据路由获取kafkamanager
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
        //创建投递的任务队列
        createTopic(kafkaManager, Constants.topicName);
    }

    public void createTopic(KafkaManager kafkaManager, String topicName) {
        //检测队列是否存在并创建
        boolean targes = kafkaManager.checkTopic(topicName);
        if (!targes)
            //创建队列
            kafkaManager.createNewTopic(topicName, 3, (short) 1);
    }
}
