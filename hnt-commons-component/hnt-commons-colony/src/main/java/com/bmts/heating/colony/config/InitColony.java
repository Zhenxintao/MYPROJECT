package com.bmts.heating.colony.config;

import com.bmts.heating.colony.pojo.Kafka_Colony;
import com.bmts.heating.colony.pojo.Redis_Colony;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.config.KafkaMqBeanConfig;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Description("加载分布式集群配置信息")
public class InitColony {

    @Autowired
    private Kafka_Colony kafka_colony;
    //@Autowired
    //private Redis_Colony redis_colony;

    private Map<Kafka_Colony.Knode, KafkaManager> kafkaMap = new ConcurrentHashMap<Kafka_Colony.Knode, KafkaManager>();
    //private Map<Redis_Colony.Rnode, RedisManager> redisMap = new ConcurrentHashMap<Redis_Colony.Rnode, RedisManager>();

    /**
     * 加载分布式kafka集群信息
     * @return
     */
    @Bean
    public String initKafkaConfig() {
        List<Kafka_Colony.Knode> knodes = kafka_colony.getKnodes();
        for (Kafka_Colony.Knode node : knodes) {
            if(node.isOpen()){
                KafkaMqBeanConfig config = SpringBeanFactory.getBean(KafkaMqBeanConfig.class);
                config.setBootstrapservers(Arrays.asList(node.getKcolony_ips()));
                KafkaManager kafkaManager = SpringBeanFactory.getBean(KafkaManager.class);
                kafkaManager.initManager(config);
                kafkaMap.put(node, kafkaManager);
            }
        }
        System.out.println(kafkaMap);
        return "kafkaMap";
    }

    /**
     * 加载分布式redis集群信息
     * @return

    @Bean
    public String initRedisConfig(){
        RedisManager redisManager= SpringBeanFactory.getBean(RedisManager.class);
        Redis_Colony.Rnode rnode= new Redis_Colony.Rnode();
        redisMap.put(rnode,redisManager);
        return "redisMap";
    }*/

    @Bean( "kafkaManagerMap" )
    public Map<Kafka_Colony.Knode, KafkaManager> getKafkaManager() {
        return this.kafkaMap;
    }

//    @Bean( "redisManagerMap" )
//    public Map<Redis_Colony.Rnode, RedisManager> getRedisManager() {
//        return this.redisMap;
//    }
}
