package com.bmts.heating.service.task.storm.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.enums.PointDataType;
import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.service.task.storm.config.Constants;
import com.bmts.heating.service.task.storm.converter.PointCacheConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: KafkaService
 * @Description: kafka 消息处理类
 * @Author: pxf
 * @Date: 2020/8/13 15:39
 * @Version: 1.0
 */

@Service
public class KafkaService {

    @Autowired
    private RouteAdapter routeAdapter;

    @Autowired
    private RedisPointService redisPointService;

    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;

    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

    /**
     * 组装数据
     */
    public void queryPointList(long timeStamp) throws ExecutionException, InterruptedException {
        //  从实时库获取所有的点
        List<PointCache> pointCaches = redisPointService.queryAllPointsCache();
        // 进行类型转换
        List<PointL> pointList = PointCacheConverter.INSTANCE.toList(pointCaches);
        // 实时库里的所有点
        Map<Integer, List<PointL>> systemPointMap = pointList.stream().collect(Collectors.groupingBy(e -> e.getRelevanceId()));

        // 获取热力站 关系数据   (过滤掉冻结站的数据）
        List<StationFirstNetBaseView> listStation = stationFirstNetBaseViewService.list(Wrappers.<StationFirstNetBaseView>lambdaQuery().eq(StationFirstNetBaseView::getStatus, true));

        // 进行数据分组
        Map<Integer, List<StationFirstNetBaseView>> stationMap = listStation.stream().collect(Collectors.groupingBy(e -> e.getHeatTransferStationId()));
        for (Integer stationId : stationMap.keySet()) {
            List<StationFirstNetBaseView> listSystem = stationMap.get(stationId);
            List<Message_Point_Gather> listMessagePoint = new ArrayList<>();
            int total = 0;
            for (StationFirstNetBaseView stationSystem : listSystem) {
                total = getTotal(listMessagePoint, systemPointMap, total,
                        stationSystem.getHeatSystemId(), Integer.parseInt(stationSystem.getNumber()), stationSystem.getHeatCabinetId());
            }
            // 向kafka 推送消息
            if (!CollectionUtils.isEmpty(listMessagePoint)) {
                setMessageProduce(listMessagePoint, stationId, TreeLevel.HeatStation.level(), total, timeStamp);
            }
        }
        stationMap.clear();

        // 获取热源 关系数据
        List<SourceFirstNetBaseView> listSource = sourceFirstNetBaseViewService.list();
        Map<Integer, List<SourceFirstNetBaseView>> sourceMap = listSource.stream().collect(Collectors.groupingBy(e -> e.getHeatSourceId()));
        for (Integer sourceId : sourceMap.keySet()) {
            List<SourceFirstNetBaseView> listSystem = sourceMap.get(sourceId);
            List<Message_Point_Gather> listMessagePoint = new ArrayList<>();
            int total = 0;
            for (SourceFirstNetBaseView sourceSystem : listSystem) {
                total = getTotal(listMessagePoint, systemPointMap, total, sourceSystem.getHeatSystemId(), sourceSystem.getNumber(), sourceSystem.getHeatCabinetId());
            }
            // 向kafka 推送消息
            if (!CollectionUtils.isEmpty(listMessagePoint)) {
                setMessageProduce(listMessagePoint, sourceId, TreeLevel.HeatSource.level(), total, timeStamp);
            }
        }
        sourceMap.clear();
    }

    private int getTotal(List<Message_Point_Gather> listMessagePoint, Map<Integer, List<PointL>> systemPointMap, int total,
                         Integer heatSystemId, Integer number, Integer heatCabinetId) {
        List<PointL> pointLS = systemPointMap.get(heatSystemId);
        if (!CollectionUtils.isEmpty(pointLS)) {
            Message_Point_Gather messagePointGather = new Message_Point_Gather();
            messagePointGather.setRelevanceId(heatSystemId);
            messagePointGather.setLevel(1);
            messagePointGather.setData_type(PointDataType.FIRST_NET.type());
            messagePointGather.setNumber(number);
            messagePointGather.setHeatCabinetId(heatCabinetId);
            Map<String, PointL> collect = pointLS.stream().collect(Collectors.toMap(e -> String.valueOf(e.getPointId()), Function.identity()));
            messagePointGather.setPointLS(collect);
            listMessagePoint.add(messagePointGather);
            // 站 下面所有点的总数
            total += pointLS.size();
        }
        return total;
    }


    /**
     * 组装kafka 发送消息实体
     *
     * @param list
     * @param relevanceId
     * @param relevanceType
     */
    private void setMessageProduce(List<Message_Point_Gather> list, Integer relevanceId, int relevanceType, int total, long timeStamp) {
        Message_Gather messageGather = new Message_Gather();
        messageGather.setRelevanceId(relevanceId);
        messageGather.setRelevanceType(relevanceType);
        messageGather.setTotal(total);
        messageGather.setBatchPoints(list);
        messageGather.setTimeStamp(timeStamp);
        product(Constants.topicName, messageGather);
    }


    private void product(String topicName, Object obj) {
        RoutePolicy routePolicy = new RoutePolicy();
        routePolicy.setType(RoutePolicy.Type.KAFKA);
        routePolicy.setDataSet("all");
        KafkaManager kafkaManager = routeAdapter.getManager(routePolicy);
        try {
            kafkaManager.produceDatasBySingleton(topicName, 3, obj);
        } catch (KafkaException e) {
            e.printStackTrace();
        }

    }

}
