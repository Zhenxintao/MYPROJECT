//package com.bmts.heating.stream.flink;
//
//import com.bmts.heating.commons.utils.msmq.Message_Gather;
//import com.bmts.heating.stream.flink.handle.FirstRichMap;
//import org.apache.flink.api.common.restartstrategy.RestartStrategies;
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
//
//import java.util.Properties;
//
///**
// * @ClassName: FlinkStream
// * @Description: 读取kafka 数据进行数据流处理
// * @Author: pxf
// * @Date: 2021/7/22 17:15
// * @Version: 1.0
// */
//public class FlinkStream {
//
//    public static void main(String[] args) throws Exception {
//        // 创建flink 环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        // 开启checkpoint
//        env.enableCheckpointing(5000);
//        System.out.println("------进入数据流-----");
//
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", "10.0.31.13:9092,10.0.31.14:9092,10.0.31.32:9092");
//        properties.setProperty("group.id", "send_queue");
//        properties.setProperty("enable.auto.commit", "true");
//        properties.setProperty("auto.commit.interval.ms", "1000");
//        properties.setProperty("auto.offset.reset", "earliest");
//        properties.setProperty("session.timeout.ms", "30000");
//        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
////        properties.setProperty("topic", "queue-monitor-send-monitor64");
//
//        System.out.println("----配置kafka 连接信息------");
//
//        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("queue-monitor-send-monitor64", new SimpleStringSchema(), properties);
//        // 同上面的auto.offset.reset 设置读取偏移量 kafkaConsumer.setStartFromGroupOffsets();
//
//        DataStream<String> stream = env.addSource(kafkaConsumer);
//        // 处理业务一
//        SingleOutputStreamOperator<Message_Gather> map = stream.rebalance().map(new FirstRichMap());
//
//
//        /**
//         * 设置重启策略/5次尝试/每次尝试间隔50s
//         */
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(5, 50000));
//
////        stream.print();
//        System.out.println("打印信息--------------------------");
//        env.execute();
//
//    }
//
//}
