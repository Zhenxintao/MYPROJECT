package com.bmts.heating.colony.adapter;

import com.bmts.heating.colony.pojo.Kafka_Colony;
import com.bmts.heating.colony.pojo.Redis_Colony;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("routeAdapter")
@Description("路由器")
public class RouteAdapter {
    private Logger Logger = LoggerFactory.getLogger(RoutePolicy.class);

    public <T> T getManager(RoutePolicy routePolicy){
        T object = null;
        switch (routePolicy.getType()){
            case RoutePolicy.Type.KAFKA:
                KafkaManager kafkaManager= null;
                Map<Kafka_Colony.Knode, KafkaManager> kafkaManagerMap = (Map<Kafka_Colony.Knode, KafkaManager>) SpringBeanFactory.getBean("kafkaManagerMap");
                if(kafkaManagerMap.size()==1){
                    for (Map.Entry<Kafka_Colony.Knode, KafkaManager> entry : kafkaManagerMap.entrySet()) {
                        kafkaManager = entry.getValue();
                        break;
                    }
                }else{
                    for (Map.Entry<Kafka_Colony.Knode, KafkaManager> entry : kafkaManagerMap.entrySet()) {
                        Kafka_Colony.Knode node = entry.getKey();
                        String[] datasets= node.getDatasets();
                        for(String s: datasets){
                            if(s.equals(routePolicy.getDataSet())){
                                kafkaManager=entry.getValue();
                                break;
                            }
                        }
                    }
                }
                object = (T) (KafkaManager.class).cast(kafkaManager);
                break;
            case RoutePolicy.Type.REDIS:
//                RedisManager redisManager= null;
//                Map<Redis_Colony.Rnode, RedisManager> redisManagerMap =(Map<Redis_Colony.Rnode, RedisManager>) SpringBeanFactory.getBean("redisManagerMap");
//                if(redisManagerMap.size()==1){
//                    redisManager=redisManagerMap.get(0);
//                }
//                object = (T) (RedisManager.class).cast(redisManager);
                break;
            case RoutePolicy.Type.EL:
                break;
            default:
                Logger.info("类型匹配错误.....");
        }
        return  object;
    }
}
