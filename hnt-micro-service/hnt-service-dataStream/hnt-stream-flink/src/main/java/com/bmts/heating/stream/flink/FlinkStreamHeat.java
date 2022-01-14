package com.bmts.heating.stream.flink;

import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.stream.flink.handle.*;
import com.bmts.heating.stream.flink.source.KafkaSourceCustom;
import com.bmts.heating.stream.flink.source.RealSourceCustom;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @ClassName: FlinkStream
 * @Description: 读取kafka 数据进行数据流处理
 * @Author: pxf
 * @Date: 2021/7/22 17:15
 * @Version: 1.0
 */
public class FlinkStreamHeat {

    public static void main(String[] args) throws Exception {
        // 创建flink 环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 开启checkpoint
        env.enableCheckpointing(5000);
        System.out.println("------进入数据流-----");

        // 处理实时库 数据源  业务
        DataStreamSource<Message_Point_Gather> realSource = env.addSource(new RealSourceCustom()).setParallelism(3);
        SingleOutputStreamOperator<Message_Point_Gather> realMessage = realSource.rebalance()
                .map(new RealComputeRichMap()).setParallelism(3)
                .map(new RealAlarmFirstRichMap()).setParallelism(6)
                .map(new RealAlarmSecondRichMap()).setParallelism(6)
                .map(new RealDataRichMap()).setParallelism(6);
//        SingleOutputStreamOperator<Message_Point_Gather> realMessage = computeMessage.rebalance().map(new RealDataRichMap()).setParallelism(6);

        // 处理历史库  数据源 业务
        DataStreamSource<Message_Gather> historySource = env.addSource(new KafkaSourceCustom()).setParallelism(1);
        // 业务处理一
        SingleOutputStreamOperator<Message_Gather> messageMap = historySource.map(new FirstRichMap()).setParallelism(3);
//        // 业务处理二
//        SingleOutputStreamOperator<Message_Gather> map = messageMap.map(new SecondRichMap()).setParallelism(3);

        /**
         * 设置重启策略/5次尝试/每次尝试间隔50s
         */
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(5, 50000));

//        stream.print();
        System.out.println("打印信息--------------------------");
        env.execute();

    }

}
