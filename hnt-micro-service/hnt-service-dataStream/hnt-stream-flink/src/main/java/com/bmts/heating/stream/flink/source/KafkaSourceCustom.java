package com.bmts.heating.stream.flink.source;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfigChild;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.db.service.EnergyCollectConfigChildService;
import com.bmts.heating.commons.db.service.EnergyCollectConfigService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.stream.flink.app.SpringBootFlinkApplication;
import com.bmts.heating.stream.flink.history.EnergyComputeNode;
import com.bmts.heating.stream.flink.history.EnergyRule;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: KafkaSource
 * @Description: 自定义数据源
 * @Author: pxf
 * @Date: 2021/7/26 14:34
 * @Version: 1.0
 */

@Component
public class KafkaSourceCustom extends RichParallelSourceFunction<Message_Gather> implements RetracementHandle {

    private Queue<Message_Gather> listQueue = new ConcurrentLinkedQueue<Message_Gather>();

    private RouteAdapter routeAdapter;

    private EnergyCollectConfigService energyCollectConfigService;

    private EnergyCollectConfigChildService energyCollectConfigChildService;

    public void consume() {
        // 监控 kafka
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        try {
//            KafKaConfig.SendQueue send_queue = kafkaConfig.getSend_queue();
            ConsumerOrder consumerOrder = new ConsumerOrder();
            consumerOrder.setTopicName("queue-monitor-send-monitor64");
            consumerOrder.setPolicy(1);
            // send_queue.getGroup_id()  send_queue_monitor  monitor_flink
            consumerOrder.setGroup_id("send_queue_monitor");
            consumerOrder.setApplication_num(3);
            KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
            kafkaManager.consumeDatas(consumerOrder, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callBackPattern(Object... objects) {
        // 一个站的 所有系统的 数据
        Message_Gather messageGather = (Message_Gather) objects[0];
        if (messageGather != null) {
            // 直接进行能耗的计算
            EnergyComputeNode energyComputeNode = new EnergyComputeNode();
            energyComputeNode.entry(messageGather);
            listQueue.add(messageGather);
        }

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        // 可以加载spring 容器
        // 开启 spring 容器
        SpringBootFlinkApplication.run();
        this.routeAdapter = SpringBeanFactory.getBean(RouteAdapter.class);

        this.energyCollectConfigService = SpringBeanFactory.getBean(EnergyCollectConfigService.class);
        this.energyCollectConfigChildService = SpringBeanFactory.getBean(EnergyCollectConfigChildService.class);

        EnergyRule energyRule = new EnergyRule();
        List<EnergyNodeSource> energyNodeSources = energyCollectConfigService.loadEnergySourceRule();
        List<EnergyNodeSource> energyStationRule = energyCollectConfigService.loadEnergyStationRule();
        List<EnergyCollectConfigChild> children = energyCollectConfigChildService.list();

        energyRule.initMaster(energyNodeSources, TreeLevel.HeatSource.level());
        energyRule.initMaster(energyStationRule, TreeLevel.HeatStation.level());
        energyRule.initChild(children);

        // 监控kafka
        consume();
    }


    @Override
    public void run(SourceContext<Message_Gather> sourceContext) throws Exception {
        while (true) {
            if (!listQueue.isEmpty()) {
                Message_Gather poll = listQueue.poll();
                if (Optional.ofNullable(poll).isPresent()) {
                    sourceContext.collect(poll);
                }
            }
        }
    }

    @Override
    public void cancel() {

    }

}
