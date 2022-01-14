package com.bmts.heating.monitor.boot.service;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.boot.service.async.MonitorAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: HistoryService
 * @Description: 处理历史数据
 * @Author: pxf
 * @Date: 2021/7/7 17:32
 * @Version: 1.0
 */
@Service
public class MonitorHistoryService implements RetracementHandle {

    private static Logger logger = LoggerFactory.getLogger(MonitorHistoryService.class);

    @Autowired
    private RouteAdapter routeAdapter;
    @Autowired
    private MonitorAsync monitorAsync;

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
            // send_queue.getGroup_id()  send_queue_monitor
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

        // 系统数据
        List<Message_Point_Gather> batchPoints = messageGather.getBatchPoints();

        // 0 系统的数据
        List<Message_Point_Gather> zeroMessageList = batchPoints.stream().filter(e -> e.getNumber() == 0).collect(Collectors.toList());
        // 不是 0系统的数据
        List<Message_Point_Gather> systemList = batchPoints.stream().filter(e -> e.getNumber() != 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(systemList) && !CollectionUtils.isEmpty(zeroMessageList)) {
            // 写入历史库
            zeroMessageList.stream().forEach(e -> {
                monitorAsync.sendTDHistory(e, messageGather.getTimeStamp(), messageGather.getRelevanceId(), messageGather.getRelevanceType());
            });

        } else {
            systemList.stream().forEach(x -> {
                Map<String, PointL> pointLS = x.getPointLS();
                // 过滤出0 系统的点
                Message_Point_Gather zeroPoints = zeroMessageList.stream()
                        .filter(k -> Objects.equals(k.getHeatCabinetId(), x.getHeatCabinetId()))
                        .findFirst().orElse(null);
                if (zeroPoints != null) {
                    pointLS.putAll(zeroPoints.getPointLS());
                }
                // 写入历史库
                monitorAsync.sendTDHistory(x, messageGather.getTimeStamp(), messageGather.getRelevanceId(), messageGather.getRelevanceType());
            });

        }


    }


}
