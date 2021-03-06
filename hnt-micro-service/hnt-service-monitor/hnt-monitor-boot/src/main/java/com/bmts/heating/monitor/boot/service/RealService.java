package com.bmts.heating.monitor.boot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.PointAlarmViewService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.baseInfo.enums.AbnormalLevel;
import com.bmts.heating.commons.entiy.baseInfo.enums.PointStandardLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.mq.kafka.adapter.ConsumerOrder;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.msmq.PointType;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.monitor.boot.service.alarm.AlarmHandleService;
import com.bmts.heating.monitor.boot.service.async.MonitorAsync;
import com.bmts.heating.monitor.boot.utils.MonitorHandleUtil;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @ClassName: RealService
 * @Description: ????????????????????????
 * @Author: pxf
 * @Date: 2021/7/7 17:31
 * @Version: 1.0
 */
@Service
public class RealService implements RetracementHandle {

    private static Logger logger = LoggerFactory.getLogger(RealService.class);

    @Autowired
    private RouteAdapter routeAdapter;
    @Autowired
    private MonitorAsync monitorAsync;
    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;
    @Autowired
    private AlarmHandleService alarmHandleService;
    @Autowired
    private StationFirstNetBaseViewService stationViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceViewService;
    @Autowired
    private PointAlarmViewService pointAlarmViewService;
    @Autowired
    private WebPageConfigService webPageConfigService;


    public void consume() {
        // ?????? kafka
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
        // ????????????????????????
        Message_Point_Gather pointGather = (Message_Point_Gather) objects[0];
        Map<String, PointL> pointMap = pointGather.getPointLS();
        // ?????????
        setPointValue(pointMap);
        // ????????????
        compute(pointMap);
        // ?????? ????????????????????????
        Map<String, Object> eneryConfigMap = null;
        WebPageConfig eneryConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "eneryConfig"));
        if (eneryConfig != null && StringUtils.isNotBlank(eneryConfig.getJsonConfig())) {
            eneryConfigMap = JSON.parseObject(eneryConfig.getJsonConfig(), Map.class);
        }

        // ?????????????????????
        monitorAsync.sendReal(pointMap, eneryConfigMap);
        // ??????????????????
        // ??????id
        Integer systemId = pointGather.getRelevanceId();
        String firstKey = pointMap.keySet().stream().findFirst().get();
        PointL firstLPointL = pointMap.get(firstKey);
        // ?????????????????????
        List<PointAlarmView> listPointAlarm = pointAlarmViewService.list(Wrappers.<PointAlarmView>lambdaQuery()
                .eq(PointAlarmView::getRelevanceId, systemId)
                .eq(PointAlarmView::getIsAlarm, true));
        // ???????????????
        if (firstLPointL.getHeatType() == PointStandardLevel.POINTSTANDARD_STATION.getLevel()) {
            StationFirstNetBaseView one = stationViewService.getOne(Wrappers.<StationFirstNetBaseView>lambdaQuery().eq(StationFirstNetBaseView::getHeatSystemId, systemId));
            alarmHandleService.handleStation(one, pointMap, listPointAlarm);
        }

        // ??????????????????
        if (firstLPointL.getHeatType() == PointStandardLevel.POINTSTANDARD_SOURCE.getLevel()) {
            SourceFirstNetBaseView one = sourceViewService.getOne(Wrappers.<SourceFirstNetBaseView>lambdaQuery().eq(SourceFirstNetBaseView::getHeatSystemId, systemId));
            alarmHandleService.handleSource(one, pointMap, listPointAlarm);
        }
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


    private void compute(Map<String, PointL> pointMap) {
        //  Map
        Map<String, String> collectMap = new HashMap<>();
        pointMap.forEach((k, v) -> {
                    if (v != null) {
                        if (StringUtils.isNotBlank(v.getPointName()) && StringUtils.isNotBlank(v.getValue())) {
                            collectMap.put(v.getPointName(), v.getValue());
                        }
                    }
                }
        );
        // ??? Map  ????????????  ??????????????????   ????????????
        pointMap.forEach((pointId, point) -> {
                    if (point != null && point.getPointConfig() == PointProperties.Compute.type()) {
                        if (StringUtils.isNotBlank(point.getExpression())) {
                            try {
                                JEP jep = new JEP();
                                // ???????????????
                                String expression = point.getExpression();
                                // ?????????????????? ?????????  [^a-zA-Z0-9]
                                String trim = Pattern.compile("[^_a-zA-Z0-9]").matcher(expression).replaceAll(" ").trim();
                                if (StringUtils.isNotBlank(trim)) {
                                    // ??????????????????
                                    String[] split = trim.split("\\s+");
                                    for (String str : split) {
                                        String pointLValue = collectMap.get(str);
                                        if (StringUtils.isNotBlank(pointLValue)) {
                                            jep.addVariable(str, Double.parseDouble(pointLValue));
                                        }
                                    }
                                    // ????????????
                                    jep.parseExpression(expression);
                                    // ????????????
                                    double result = jep.getValue();
                                    point.setValue(String.format("%.2f", result));
                                    // ????????????
                                    point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));

                                    // ????????????????????????
                                    logger.info("Compute---???????????????name={},level={},releverceId={},type={},timeStrap={},value={}",
                                            point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap(), point.getValue());

                                }
                            } catch (Exception e) {
                                logger.error("Compute---????????????---:name={},level={},releverceId={},type={},timeStrap={}," +
                                        point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap());
                                e.printStackTrace();
                            }
                        } else {
                            point.setValue("0.00");
                            // ????????????
                            point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
                        }
                    }
                }
        );
        collectMap.clear();
    }

    private void realAlarmOne(PointL pointL) {
        // ?????? ????????????????????????
        if (pointL.getHealthSign() == HealthStatus.HEALTH_DATA.status() && StringUtils.isNotBlank(pointL.getHightLower())) {
            // ?????? pointL ????????? ???????????????????????????????????????
            String hightLower = pointL.getHightLower();
            JSONObject jsonObject = JSONObject.parseObject(hightLower);

            if (pointL.getType() == PointType.POINT_BOOL.getType()) {
                // ??????????????????
                String alarmValue = jsonObject.getString("alarmValue");
                if (StringUtils.isNotBlank(alarmValue) && Objects.equals(alarmValue, pointL.getValue())) {
                    pointL.setQualityStrap(218);
                    pointL.setHealthSign(HealthStatus.OUT_MEASURING_RANGE.status());
                    sendAbnormal(pointL);
                }

            } else {

                // ????????????
                String accidentHigh = jsonObject.getString("accidentHigh");
                // ????????????
                String accidentLower = jsonObject.getString("accidentLower");
                String oldValue = pointL.getValue();
                BigDecimal value = null;
                BigDecimal high = null;
                BigDecimal lower = null;
                Integer qualityStamp = null;
                if (StringUtils.isNotBlank(oldValue)) {
                    value = new BigDecimal(oldValue);
                }
                // ????????????
                if (StringUtils.isNotBlank(accidentHigh)) {
                    high = new BigDecimal(accidentHigh);
                }
                // ????????????
                if (StringUtils.isNotBlank(accidentLower)) {
                    lower = new BigDecimal(accidentLower);
                }

                if (value != null) {
                    if (high != null && value.compareTo(high) == 1) {
                        qualityStamp = 218;
                    } else if (lower != null && value.compareTo(lower) == -1) {
                        qualityStamp = 217;
                    }

                }
                if (qualityStamp != null) {
                    pointL.setQualityStrap(qualityStamp);
                    pointL.setHealthSign(HealthStatus.OUT_MEASURING_RANGE.status());
                    sendAbnormal(pointL);

                }

            }
        }
    }

    private void sendAbnormal(PointL pointL) {
        Abnormal dto = new Abnormal();
        dto.setGroupId(pointL.getRelevanceId());
        dto.setType(HealthStatus.OUT_MEASURING_RANGE.status());
        dto.setMsg("code-1001");
        if (pointL.getHeatType() == 1) {
            dto.setLevel(AbnormalLevel.HEAT_STATION.getLevel());
        }
        if (pointL.getHeatType() == 2) {
            dto.setLevel(AbnormalLevel.HEAT_SOURCE.getLevel());
        }
        dto.setPoint(pointL.getPointName());
        dto.setValue(pointL.getValue());
        dto.setTs(System.currentTimeMillis());
        Boolean aBoolean = historyTdGrpcClient.insertAbnormal(dto);
        System.out.println("----------------" + aBoolean);
    }

    private void realAlarmTwo(PointL pointL) {
        // ?????? ????????????????????????
        if (pointL.getHealthSign() == HealthStatus.HEALTH_DATA.status() && StringUtils.isNotBlank(pointL.getHightLower()) && pointL.getType() != PointType.POINT_BOOL.getType()) {
            // ?????? pointL ????????? ?????????????????????????????????
            String hightLower = pointL.getHightLower();
            JSONObject jsonObject = JSONObject.parseObject(hightLower);
            String runningHigh = jsonObject.getString("runningHigh");
            String runningLower = jsonObject.getString("runningLower");
            String oldValue = pointL.getValue();
            BigDecimal value = null;
            BigDecimal high = null;
            BigDecimal lower = null;
            Integer qualityStamp = null;
            if (StringUtils.isNotBlank(oldValue)) {
                value = new BigDecimal(oldValue);
            }
            // ????????????
            if (StringUtils.isNotBlank(runningHigh)) {
                high = new BigDecimal(runningHigh);
            }
            // ????????????
            if (StringUtils.isNotBlank(runningLower)) {
                lower = new BigDecimal(runningLower);
            }

            if (value != null) {
                if (high != null && value.compareTo(high) == 1) {
                    qualityStamp = 194;
                } else if (lower != null && value.compareTo(lower) == -1) {
                    qualityStamp = 193;
                }

            }
            if (qualityStamp != null) {
                pointL.setQualityStrap(qualityStamp);
                pointL.setHealthSign(HealthStatus.OUT_REASONABLE_RANGE.status());
                Abnormal dto = new Abnormal();
                dto.setGroupId(pointL.getRelevanceId());
                dto.setType(HealthStatus.OUT_REASONABLE_RANGE.status());
                dto.setMsg("code-2001");
                if (pointL.getHeatType() == 1) {
                    dto.setLevel(AbnormalLevel.HEAT_STATION.getLevel());
                }
                if (pointL.getHeatType() == 2) {
                    dto.setLevel(AbnormalLevel.HEAT_SOURCE.getLevel());
                }
                dto.setPoint(pointL.getPointName());
                dto.setValue(pointL.getValue());
                dto.setTs(System.currentTimeMillis());
                Boolean aBoolean = historyTdGrpcClient.insertAbnormal(dto);
                System.out.println("----------------" + aBoolean);
            }

        }

    }


}
