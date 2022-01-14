package com.bmts.heating.stream.flink.source;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.stream.flink.app.SpringBootFlinkApplication;
import com.bmts.heating.stream.flink.utils.MonitorHandleUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: RealSourceCustom
 * @Description: 实时库数据源
 * @Author: pxf
 * @Date: 2021/7/26 14:34
 * @Version: 1.0
 */

@Component
public class RealSourceCustom extends RichParallelSourceFunction<Message_Point_Gather> implements RetracementHandle {

    private Queue<Message_Point_Gather> realListQueue = new ConcurrentLinkedQueue<Message_Point_Gather>();

    private RouteAdapter routeAdapter;


    public void consume() {
        // 监控 kafka
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        try {
//            KafKaConfig.DataIssue issue_queue = kafkaConfig.getIssue_queue();
            ConsumerOrder consumerOrder = new ConsumerOrder();
            consumerOrder.setTopicName("queue-monitor-issue");
            consumerOrder.setPolicy(1);
            // issue_queue.getGroup_id()  issue_queue
            consumerOrder.setGroup_id("issue_queue");
            consumerOrder.setApplication_num(3);
            KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
            kafkaManager.consumeDatas(consumerOrder, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callBackPattern(Object... objects) {
        // 整个机组的点集合
        Message_Point_Gather pointGather = (Message_Point_Gather) objects[0];
        if (pointGather != null) {
            Map<String, PointL> pointMap = pointGather.getPointLS();
            // 设置值
            setPointValue(pointMap);
            realListQueue.add(pointGather);
        }

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        // 开启 spring 容器
        SpringBootFlinkApplication.run();
        this.routeAdapter = SpringBeanFactory.getBean(RouteAdapter.class);
        // 监控kafka
        consume();
    }


    @Override
    public void run(SourceContext<Message_Point_Gather> sourceContext) throws Exception {
        while (true) {
            if (!realListQueue.isEmpty()) {
                Message_Point_Gather poll = realListQueue.poll();
                if (Optional.ofNullable(poll).isPresent()) {
                    sourceContext.collect(poll);
                }
            }
        }
    }

    @Override
    public void cancel() {

    }

    private void setPointValue(Map<String, PointL> pointMap) {
        pointMap.forEach((k, v) -> {
                    if (v != null) {
                        String newValue = null;
                        String oldValue = v.getOldValue();
                        if (Objects.equals(oldValue, "true")) {
                            newValue = "1";
                        }
                        if (Objects.equals(oldValue, "false")) {
                            newValue = "0";
                        }
                        if (StringUtils.isNotBlank(oldValue)) {
                            newValue = MonitorHandleUtil.type(v.getType(), oldValue);
                        }
                        if (newValue == null) {
                            v.setHealthSign(HealthStatus.MISSING_DATA.status());
                        } else {
                            v.setValue(newValue);
                            v.setHealthSign(HealthStatus.HEALTH_DATA.status());
                        }

                    }
                }
        );
    }

}
