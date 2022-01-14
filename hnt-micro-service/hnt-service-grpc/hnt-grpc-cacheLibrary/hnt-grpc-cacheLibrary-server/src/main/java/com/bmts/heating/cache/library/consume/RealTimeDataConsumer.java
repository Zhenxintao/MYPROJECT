//package com.bmts.heating.cache.library.consume;
//
//
//import com.bmts.heating.cache.library.service.RedisDataOptService;
//import com.bmts.heating.colony.adapter.RouteAdapter;
//import com.bmts.heating.colony.pojo.RoutePolicy;
//import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
//import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
//import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
//import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
//import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
//import com.bmts.heating.commons.utils.msmq.PointL;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.Map;
//
///**
// * @Description
// * @Author fei.chang
// * @Date 2020/7/21 18:09
// * @Version 1.0
// */
//@Slf4j
//public class RealTimeDataConsumer implements RetracementHandle, ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    private RouteAdapter routeAdapter;
//
//    @Autowired
//    private RedisDataOptService realDataOptService;
//
//    @Autowired
//    private ThreadPoolTaskExecutor realDataExecutor;
//
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Value("${topics.consumerRealTimeData.topicName}")
//    private String topicName;
//
//    @Value("${topics.consumerRealTimeData.group_id}")
//    private String groupId;
//
//    @Value("${topics.consumerRealTimeData.policy}")
//    private int policy;
//
//    @Value("${topics.consumerRealTimeData.application_num}")
//    private int applicationNum;
//
//    class RealDataConsumer implements Runnable {
//
//        private Object[] objects;
//
//        public RealDataConsumer(Object... objects) {
//            this.objects = objects;
//        }
//
//        @Override
//        public void run() {
////            for (Object object : objects) {
////                try {
//////                log.info("kafka realtime data = {}", object);
////                    Message_Point_Gather message_point_gather = (Message_Point_Gather) object;
//////                Message_Point_Gather message_point_gather = g.fromJson(object.toString(), Message_Point_Gather.class);
////                    log.info("kafka realtime object {}", message_point_gather);
////                    Map<String, PointL> pointLs = message_point_gather.getPointLS();
////                    realDataOptService.addBatchRealTimePoints(pointLs);
////                } catch (Exception e) {
////                    log.error("kafka message consumer fail message {} e {}", object, e);
////                }
////            }
//        }
//
//    }
//
//    @Override
//    public void callBackPattern(Object... objects) {
//        realDataExecutor.submit(new RealDataConsumer(objects));
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        System.out.println("onApplicationEvent exec!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        if (event.getApplicationContext().getParent() == null) {
//            RoutePolicy routePolicy = new RoutePolicy();
//            routePolicy.setType(RoutePolicy.Type.KAFKA);
//            routePolicy.setDataSet("all");
//            KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
//            try {
//                ConsumerOrder order = new ConsumerOrder();
//                order.setApplication_num(applicationNum);
//                order.setGroup_id(groupId);
//                order.setPolicy(policy);
//                order.setTopicName(topicName);
//                kafkaManager.consumeDatas(order, this);
//            } catch (KafkaException e) {
//                log.error("KafkaException e {}, e");
//            }
//        }
//    }
//}
