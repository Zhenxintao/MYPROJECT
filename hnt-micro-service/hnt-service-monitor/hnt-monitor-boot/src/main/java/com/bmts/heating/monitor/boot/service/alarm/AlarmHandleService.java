package com.bmts.heating.monitor.boot.service.alarm;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.enums.DataType;
import com.bmts.heating.commons.db.service.AlarmHistoryService;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.entiy.baseInfo.enums.AlarmStatus;
import com.bmts.heating.commons.entiy.baseInfo.enums.PointStandardLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.AlarmRankJson;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.boot.service.signalr.PushAlarmToWeb;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pxf
 * @description
 * @date 2021/10/18 18:10
 **/
@Component
public class AlarmHandleService {

    private static Logger logger = LoggerFactory.getLogger(AlarmHandleService.class);

    @Autowired
    private Computation computation;

    @Autowired
    private AlarmRealService alarmRealService;
    @Autowired
    private AlarmHistoryService alarmHistoryService;
    @Autowired
    private PushAlarmToWeb pushAlarmToWeb;

    // 处理热力站报警业务
    // @Async("asyncAlarmExecutor")
    public void handleStation(StationFirstNetBaseView stationView, Map<String, PointL> pointMap, List<PointAlarmView> listPointAlarm) {
        List<AlarmReal> addAlarmRealList = new ArrayList<>();
        List<AlarmReal> updateAlarmRealList = new ArrayList<>();
        Map<String, PointAlarmView> pointAlarmMap = listPointAlarm.stream().collect(Collectors.toMap(PointAlarmView::getSyncNumber, PointAlarmView -> PointAlarmView));
        for (String key : pointMap.keySet()) {
            PointL pointL = pointMap.get(key);
            String realMapKey = pointL.getRelevanceId() + pointL.getPointName();
            PointAlarmView pointAlarmView = pointAlarmMap.get(pointL.getSyncNumber());
            // 判断是否报警  根据 pointL.getHightLower() 是否为空 和 pointL.getType() 是否是boolean 量来进行判断
            if (pointAlarmView != null) {
                AlarmReal alarmRealMap = computation.getAlarmRealMap(realMapKey);
                // 说明配置了报警，进行报警判断
                // 判断是开关量还是模拟量
                Integer dataType = pointAlarmView.getDataType();
                if (dataType == null) {
                    continue;
                }
                Integer heatTransferStationId = stationView.getHeatTransferStationId();
                String heatTransferStationName = stationView.getHeatTransferStationName();
                Integer grade = pointAlarmView.getGrade();
                if (dataType == DataType.POINT_BOOL.getType()) {
                    // 是开关量 进行判断
                    if (StringUtils.isNotBlank(pointL.getValue()) &&
                            Objects.equals(Integer.parseInt(pointL.getValue()), pointAlarmView.getAlarmValue())) {

                        // 进行报警，进入报警实时表和内存
                        AlarmReal alarmReal = new AlarmReal();
                        alarmReal.setGrade(grade);
                        alarmReal.setPointName(pointL.getPointStandardName());
                        alarmReal.setColumnName(pointL.getPointName());
                        alarmReal.setAlarmDesc(pointAlarmView.getAlarmDesc());
                        alarmReal.setHeatSystemId(pointL.getRelevanceId());
                        alarmReal.setAddress(pointL.getSyncNumber());
                        alarmReal.setRelevanceId(heatTransferStationId);
                        alarmReal.setRelevanceName(heatTransferStationName);
                        alarmReal.setType(PointStandardLevel.POINTSTANDARD_STATION.getLevel());
                        alarmReal.setAlarmValue(new BigDecimal(pointL.getValue()));
                        alarmReal.setAlarmTime(LocalDateTime.now());
                        if (StringUtils.isNotBlank(pointAlarmView.getRankJson())) {
                            alarmReal.setRankJson(pointAlarmView.getRankJson());
                        }
                        if (alarmRealMap != null) {
                            alarmReal.setId(alarmRealMap.getId());
                            if (alarmRealMap.getConfirmTime() != null) {
                                alarmReal.setConfirmTime(alarmRealMap.getConfirmTime());
                            }
                            if (StringUtils.isNotBlank(alarmRealMap.getConfirmUser())) {
                                alarmReal.setConfirmUser(alarmRealMap.getConfirmUser());
                            }
                            alarmReal.setStatus(alarmRealMap.getStatus());
                            alarmReal.setUpdateTime(LocalDateTime.now());
                            updateAlarmRealList.add(alarmReal);
                        } else {
                            alarmReal.setStatus(AlarmStatus.ALARM_STATUS_ALARM.getStatus());
                            alarmReal.setCreateTime(LocalDateTime.now());
                            addAlarmRealList.add(alarmReal);
                        }

                        // 推送 报警的，但没有确认的, 确认过的就不在推送了
                        if (alarmReal.getStatus() == AlarmStatus.ALARM_STATUS_ALARM.getStatus()) {
                            // 推送报警  组装 json
                            JSONObject queryJson = new JSONObject();
                            queryJson.put("alarmTitle", pointL.getPointStandardName());
                            queryJson.put("alarmPosition", heatTransferStationName + "的" + stationView.getHeatSystemName());
                            queryJson.put("time", LocalDateTime.now());
                            queryJson.put("value", pointL.getValue());
                            queryJson.put("alarmDes", pointAlarmView.getAlarmDesc());
                            queryJson.put("level", grade);

                            pushAlarmToWeb.send(heatTransferStationId, queryJson.toJSONString());

                            // logger.info("AlarmHandleService---推送热力站报警数据：{}---{}", heatTransferStationId, queryJson.toJSONString());

                        }

                    } else {
                        // 不在报警， 相当于自动恢复了
                        if (alarmRealMap != null) {
                            // 进行恢复，删除报警实时表数据，添加报警历史表数据
                            if (addAlarmHistory(pointL, alarmRealMap)) {
                                // 删除报警实时数据表
                                if (alarmRealService.removeById(alarmRealMap.getId())) {
                                    computation.removeAlarmRealMap(realMapKey);
                                }
                            }
                        }

                    }
                } else {
                    // 是模拟量 进行判断
                    String rankJson = pointAlarmView.getRankJson();
                    Integer accidentHighRank = null;
                    Integer runHighRank = null;
                    Integer runLowerRank = null;
                    Integer accidentLowerRank = null;
                    String accidentHighDesc = null;
                    String runHighDesc = null;
                    String runLowerDesc = null;
                    String accidentLowerDesc = null;
                    if (StringUtils.isNotBlank(rankJson)) {
                        AlarmRankJson alarmRankJson = JSONObject.parseObject(rankJson, AlarmRankJson.class);
                        accidentHighRank = alarmRankJson.getAccidentHighRank();
                        runHighRank = alarmRankJson.getRunHighRank();
                        runLowerRank = alarmRankJson.getRunLowerRank();
                        accidentLowerRank = alarmRankJson.getAccidentLowerRank();
                        accidentHighDesc = alarmRankJson.getAccidentHighDesc();
                        runHighDesc = alarmRankJson.getRunHighDesc();
                        runLowerDesc = alarmRankJson.getRunLowerDesc();
                        accidentLowerDesc = alarmRankJson.getAccidentLowerDesc();

                    }
                    // 标记状态 和报警级别
                    Integer status = null;
                    Integer alarmGrade = null;
                    String alarmDesc = null;
                    // 判断报警
                    BigDecimal value = new BigDecimal(pointL.getValue());
                    BigDecimal accidentHigh = pointAlarmView.getAccidentHigh();
                    BigDecimal accidentLower = pointAlarmView.getAccidentLower();
                    if (value != null && accidentHigh != null && accidentLower != null) {
                        // 事故报警
                        if (value.compareTo(accidentHigh) > -1) {
                            if (accidentHighRank != null) {
                                alarmGrade = accidentHighRank;
                                if (accidentHighDesc != null) {
                                    alarmDesc = accidentHighDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                        if (value.compareTo(accidentLower) < 1) {
                            if (accidentLowerRank != null) {
                                alarmGrade = accidentLowerRank;
                                if (accidentLowerDesc != null) {
                                    alarmDesc = accidentLowerDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                    }
                    // 判断运行报警
                    BigDecimal runningHigh = pointAlarmView.getRunningHigh();
                    BigDecimal runningLower = pointAlarmView.getRunningLower();
                    if (value != null && runningHigh != null && runningLower != null) {
                        if (value.compareTo(runningHigh) > -1 && value.compareTo(accidentHigh) < 0) {
                            if (runHighRank != null) {
                                alarmGrade = runHighRank;
                                if (runHighDesc != null) {
                                    alarmDesc = runHighDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                        if (value.compareTo(accidentLower) > 0 && value.compareTo(runningLower) < 1) {
                            if (runLowerRank != null) {
                                alarmGrade = runLowerRank;
                                if (runLowerDesc != null) {
                                    alarmDesc = runLowerDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                    }

                    if (status != null) {
                        AlarmReal alarmReal = new AlarmReal();
                        if (alarmGrade != null) {
                            alarmReal.setGrade(alarmGrade);
                        }
                        alarmReal.setPointName(pointL.getPointStandardName());
                        alarmReal.setColumnName(pointL.getPointName());
                        if (alarmDesc != null) {
                            alarmReal.setAlarmDesc(alarmDesc);
                        }
                        alarmReal.setHeatSystemId(pointL.getRelevanceId());
                        alarmReal.setAddress(pointL.getSyncNumber());
                        alarmReal.setRelevanceId(heatTransferStationId);
                        alarmReal.setRelevanceName(heatTransferStationName);
                        alarmReal.setType(PointStandardLevel.POINTSTANDARD_STATION.getLevel());
                        alarmReal.setAlarmValue(new BigDecimal(pointL.getValue()));
                        alarmReal.setAlarmTime(LocalDateTime.now());
                        if (StringUtils.isNotBlank(pointAlarmView.getRankJson())) {
                            alarmReal.setRankJson(pointAlarmView.getRankJson());
                        }
                        if (alarmRealMap != null) {
                            alarmReal.setId(alarmRealMap.getId());
                            if (alarmRealMap.getConfirmTime() != null) {
                                alarmReal.setConfirmTime(alarmRealMap.getConfirmTime());
                            }
                            if (StringUtils.isNotBlank(alarmRealMap.getConfirmUser())) {
                                alarmReal.setConfirmUser(alarmRealMap.getConfirmUser());
                            }
                            alarmReal.setStatus(alarmRealMap.getStatus());
                            alarmReal.setUpdateTime(LocalDateTime.now());
                            updateAlarmRealList.add(alarmReal);
                        } else {
                            alarmReal.setStatus(status);
                            alarmReal.setCreateTime(LocalDateTime.now());
                            addAlarmRealList.add(alarmReal);
                        }

                        // 推送 报警的，但没有确认的, 确认过的就不在推送了
                        if (alarmReal.getStatus() == AlarmStatus.ALARM_STATUS_ALARM.getStatus()) {
                            // 推送报警
                            // 推送报警  组装 json
                            JSONObject queryJson = new JSONObject();
                            queryJson.put("alarmTitle", pointL.getPointStandardName());
                            queryJson.put("alarmPosition", heatTransferStationName + "的" + stationView.getHeatSystemName());
                            queryJson.put("time", LocalDateTime.now());
                            queryJson.put("value", pointL.getValue());
                            queryJson.put("alarmDes", alarmDesc);
                            queryJson.put("level", alarmGrade);

                            pushAlarmToWeb.send(heatTransferStationId, queryJson.toJSONString());
                            // logger.info("AlarmHandleService---推送热力站报警数据：{}---{}", heatTransferStationId, queryJson.toJSONString());

                        }

                    } else {
                        // 不在报警， 相当于自动恢复了
                        if (alarmRealMap != null) {
                            // 进行恢复，删除报警实时表数据，添加报警历史表数据
                            if (addAlarmHistory(pointL, alarmRealMap)) {
                                // 删除报警实时数据表
                                if (alarmRealService.removeById(alarmRealMap.getId())) {
                                    computation.removeAlarmRealMap(realMapKey);
                                }
                            }
                        }
                    }

                }
            } else {
                // 进行恢复判断

                AlarmReal alarmReal = computation.getAlarmRealMap(realMapKey);
                if (alarmReal != null) {
                    // 进行恢复，删除报警实时表数据，添加报警历史表数据
                    if (addAlarmHistory(pointL, alarmReal)) {
                        // 删除报警实时数据表
                        if (alarmRealService.removeById(alarmReal.getId())) {
                            computation.removeAlarmRealMap(realMapKey);
                        }
                    }
                }

            }

        }
        // 新添加报警实时数据
        if (!CollectionUtils.isEmpty(addAlarmRealList)) {
            if (alarmRealService.saveBatch(addAlarmRealList)) {
                computation.setAlarmRealMap(addAlarmRealList);
            }
        }
        // 更新报警实时数据
        if (!CollectionUtils.isEmpty(updateAlarmRealList)) {
            if (alarmRealService.updateBatchById(updateAlarmRealList)) {
                computation.setAlarmRealMap(updateAlarmRealList);
            }
        }
        // 处理推送业务  只推送状态是报警的数据，状态确认的数据就不用推送了

    }


    // 处理热源报警业务
    // @Async("asyncAlarmExecutor")
    public void handleSource(SourceFirstNetBaseView sourceView, Map<String, PointL> pointMap, List<PointAlarmView> listPointAlarm) {
        List<AlarmReal> addAlarmRealList = new ArrayList<>();
        List<AlarmReal> updateAlarmRealList = new ArrayList<>();
        Map<String, PointAlarmView> pointAlarmMap = listPointAlarm.stream().collect(Collectors.toMap(PointAlarmView::getSyncNumber, PointAlarmView -> PointAlarmView));
        for (String key : pointMap.keySet()) {
            PointL pointL = pointMap.get(key);
            String realMapKey = pointL.getRelevanceId() + pointL.getPointName();
            PointAlarmView pointAlarmView = pointAlarmMap.get(pointL.getSyncNumber());
            // 判断是否报警  根据 pointL.getHightLower() 是否为空 和 pointL.getType() 是否是boolean 量来进行判断
            if (pointAlarmView != null) {
                AlarmReal alarmRealMap = computation.getAlarmRealMap(realMapKey);
                // 说明配置了报警，进行报警判断
                // 判断是开关量还是模拟量
                Integer dataType = pointAlarmView.getDataType();
                if (dataType == null) {
                    continue;
                }
                Integer heatSourceId = sourceView.getHeatSourceId();
                String heatSourceName = sourceView.getHeatSourceName();
                Integer grade = pointAlarmView.getGrade();
                if (dataType == DataType.POINT_BOOL.getType()) {
                    // 是开关量 进行判断
                    if (StringUtils.isNotBlank(pointL.getValue()) &&
                            Objects.equals(Integer.parseInt(pointL.getValue()), pointAlarmView.getAlarmValue())) {
                        // 进行报警，进入报警实时表和内存
                        AlarmReal alarmReal = new AlarmReal();
                        alarmReal.setGrade(grade);
                        alarmReal.setPointName(pointL.getPointStandardName());
                        alarmReal.setColumnName(pointL.getPointName());
                        alarmReal.setAlarmDesc(pointAlarmView.getAlarmDesc());
                        alarmReal.setHeatSystemId(pointL.getRelevanceId());
                        alarmReal.setAddress(pointL.getSyncNumber());
                        alarmReal.setRelevanceId(heatSourceId);
                        alarmReal.setRelevanceName(heatSourceName);
                        alarmReal.setType(PointStandardLevel.POINTSTANDARD_STATION.getLevel());
                        alarmReal.setAlarmValue(new BigDecimal(pointL.getValue()));
                        alarmReal.setAlarmTime(LocalDateTime.now());
                        if (StringUtils.isNotBlank(pointAlarmView.getRankJson())) {
                            alarmReal.setRankJson(pointAlarmView.getRankJson());
                        }
                        if (alarmRealMap != null) {
                            alarmReal.setId(alarmRealMap.getId());

                            if (alarmRealMap.getConfirmTime() != null) {
                                alarmReal.setConfirmTime(alarmRealMap.getConfirmTime());
                            }
                            if (StringUtils.isNotBlank(alarmRealMap.getConfirmUser())) {
                                alarmReal.setConfirmUser(alarmRealMap.getConfirmUser());
                            }
                            alarmReal.setStatus(alarmRealMap.getStatus());
                            alarmReal.setUpdateTime(LocalDateTime.now());
                            updateAlarmRealList.add(alarmReal);
                        } else {
                            alarmReal.setStatus(AlarmStatus.ALARM_STATUS_ALARM.getStatus());
                            alarmReal.setCreateTime(LocalDateTime.now());
                            addAlarmRealList.add(alarmReal);
                        }

                        // 推送 报警的，但没有确认的, 确认过的就不在推送了
                        if (alarmReal.getStatus() == AlarmStatus.ALARM_STATUS_ALARM.getStatus()) {
                            // 推送报警  组装 json
                            JSONObject queryJson = new JSONObject();
                            queryJson.put("alarmTitle", pointL.getPointStandardName());
                            queryJson.put("alarmPosition", heatSourceName);
                            queryJson.put("time", LocalDateTime.now());
                            queryJson.put("value", pointL.getValue());
                            queryJson.put("alarmDes", pointAlarmView.getAlarmDesc());
                            queryJson.put("level", grade);

                            pushAlarmToWeb.sendSource(heatSourceId, queryJson.toJSONString());
                            // logger.info("AlarmHandleService---推送热源报警数据：{}---{}", heatSourceId, queryJson.toJSONString());
                        }

                    } else {
                        // 不在报警， 相当于自动回复了
                        if (alarmRealMap != null) {
                            // 进行恢复，删除报警实时表数据，添加报警历史表数据
                            if (addAlarmHistory(pointL, alarmRealMap)) {
                                // 删除报警实时数据表
                                if (alarmRealService.removeById(alarmRealMap.getId())) {
                                    computation.removeAlarmRealMap(realMapKey);
                                }
                            }
                        }
                    }
                } else {
                    // 是模拟量 进行判断
                    String rankJson = pointAlarmView.getRankJson();
                    Integer accidentHighRank = null;
                    Integer runHighRank = null;
                    Integer runLowerRank = null;
                    Integer accidentLowerRank = null;
                    String accidentHighDesc = null;
                    String runHighDesc = null;
                    String runLowerDesc = null;
                    String accidentLowerDesc = null;
                    if (StringUtils.isNotBlank(rankJson)) {
                        AlarmRankJson alarmRankJson = JSONObject.parseObject(rankJson, AlarmRankJson.class);
                        accidentHighRank = alarmRankJson.getAccidentHighRank();
                        runHighRank = alarmRankJson.getRunHighRank();
                        runLowerRank = alarmRankJson.getRunLowerRank();
                        accidentLowerRank = alarmRankJson.getAccidentLowerRank();
                        accidentHighDesc = alarmRankJson.getAccidentHighDesc();
                        runHighDesc = alarmRankJson.getRunHighDesc();
                        runLowerDesc = alarmRankJson.getRunLowerDesc();
                        accidentLowerDesc = alarmRankJson.getAccidentLowerDesc();

                    }
                    // 标记状态 和报警级别
                    Integer status = null;
                    Integer alarmGrade = null;
                    String alarmDesc = null;
                    // 判断报警
                    BigDecimal value = new BigDecimal(pointL.getValue());
                    BigDecimal accidentHigh = pointAlarmView.getAccidentHigh();
                    BigDecimal accidentLower = pointAlarmView.getAccidentLower();
                    if (value != null && accidentHigh != null && accidentLower != null) {
                        // 事故报警
                        if (value.compareTo(accidentHigh) > -1) {
                            if (accidentHighRank != null) {
                                alarmGrade = accidentHighRank;
                                if (accidentHighDesc != null) {
                                    alarmDesc = accidentHighDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                        if (value.compareTo(accidentLower) < 1) {
                            if (accidentLowerRank != null) {
                                alarmGrade = accidentLowerRank;
                                if (accidentLowerDesc != null) {
                                    alarmDesc = accidentLowerDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                    }
                    // 判断运行报警
                    BigDecimal runningHigh = pointAlarmView.getRunningHigh();
                    BigDecimal runningLower = pointAlarmView.getRunningLower();
                    if (value != null && runningHigh != null && runningLower != null) {
                        if (value.compareTo(runningHigh) > -1 && value.compareTo(accidentHigh) < 0) {
                            if (runHighRank != null) {
                                alarmGrade = runHighRank;
                                if (runHighDesc != null) {
                                    alarmDesc = runHighDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                        if (value.compareTo(accidentLower) > 0 && value.compareTo(runningLower) < 1) {
                            if (runLowerRank != null) {
                                alarmGrade = runLowerRank;
                                if (runLowerDesc != null) {
                                    alarmDesc = runLowerDesc;
                                }
                            }
                            status = AlarmStatus.ALARM_STATUS_ALARM.getStatus();
                        }
                    }


                    if (status != null) {
                        AlarmReal alarmReal = new AlarmReal();
                        if (alarmGrade != null) {
                            alarmReal.setGrade(alarmGrade);
                        }
                        alarmReal.setPointName(pointL.getPointStandardName());
                        alarmReal.setColumnName(pointL.getPointName());
                        if (alarmDesc != null) {
                            alarmReal.setAlarmDesc(alarmDesc);
                        }
                        alarmReal.setHeatSystemId(pointL.getRelevanceId());
                        alarmReal.setAddress(pointL.getSyncNumber());
                        alarmReal.setRelevanceId(heatSourceId);
                        alarmReal.setRelevanceName(heatSourceName);
                        alarmReal.setType(PointStandardLevel.POINTSTANDARD_STATION.getLevel());
                        alarmReal.setAlarmValue(new BigDecimal(pointL.getValue()));
                        alarmReal.setAlarmTime(LocalDateTime.now());
                        if (StringUtils.isNotBlank(pointAlarmView.getRankJson())) {
                            alarmReal.setRankJson(pointAlarmView.getRankJson());
                        }
                        if (alarmRealMap != null) {
                            alarmReal.setId(alarmRealMap.getId());
                            if (alarmRealMap.getConfirmTime() != null) {
                                alarmReal.setConfirmTime(alarmRealMap.getConfirmTime());
                            }
                            if (StringUtils.isNotBlank(alarmRealMap.getConfirmUser())) {
                                alarmReal.setConfirmUser(alarmRealMap.getConfirmUser());
                            }
                            alarmReal.setStatus(alarmRealMap.getStatus());
                            alarmReal.setUpdateTime(LocalDateTime.now());
                            updateAlarmRealList.add(alarmReal);
                        } else {
                            alarmReal.setStatus(status);
                            alarmReal.setCreateTime(LocalDateTime.now());
                            addAlarmRealList.add(alarmReal);
                        }
                        // 推送 报警的，但没有确认的, 确认过的就不在推送了
                        if (alarmReal.getStatus() == AlarmStatus.ALARM_STATUS_ALARM.getStatus()) {
                            // 推送报警  组装 json
                            JSONObject queryJson = new JSONObject();
                            queryJson.put("alarmTitle", pointL.getPointStandardName());
                            queryJson.put("alarmPosition", heatSourceName);
                            queryJson.put("time", LocalDateTime.now());
                            queryJson.put("value", pointL.getValue());
                            queryJson.put("alarmDes", alarmDesc);
                            queryJson.put("level", alarmGrade);

                            pushAlarmToWeb.sendSource(heatSourceId, queryJson.toJSONString());
                            // logger.info("AlarmHandleService---推送热源报警数据：{}---{}", heatSourceId, queryJson.toJSONString());

                        }


                    } else {
                        // 不在报警， 相当于自动回复了
                        if (alarmRealMap != null) {
                            // 进行恢复，删除报警实时表数据，添加报警历史表数据
                            if (addAlarmHistory(pointL, alarmRealMap)) {
                                // 删除报警实时数据表
                                if (alarmRealService.removeById(alarmRealMap.getId())) {
                                    computation.removeAlarmRealMap(realMapKey);
                                }
                            }
                        }
                    }

                }
            } else {
                // 进行恢复判断
                AlarmReal alarmReal = computation.getAlarmRealMap(realMapKey);
                if (alarmReal != null) {
                    // 进行恢复，删除报警实时表数据，添加报警历史表数据
                    if (addAlarmHistory(pointL, alarmReal)) {
                        // 删除报警实时数据表
                        if (alarmRealService.removeById(alarmReal.getId())) {
                            computation.removeAlarmRealMap(realMapKey);
                        }
                    }
                }

            }

        }
        // 新添加报警实时数据
        if (!CollectionUtils.isEmpty(addAlarmRealList)) {
            if (alarmRealService.saveBatch(addAlarmRealList)) {
                computation.setAlarmRealMap(addAlarmRealList);
            }
        }
        // 更新报警实时数据
        if (!CollectionUtils.isEmpty(updateAlarmRealList)) {
            if (alarmRealService.updateBatchById(updateAlarmRealList)) {
                computation.setAlarmRealMap(updateAlarmRealList);
            }
        }
        // 处理推送业务  只推送状态是报警的数据，状态确认的数据就不用推送了

    }


    private boolean addAlarmHistory(PointL pointL, AlarmReal alarmReal) {
        AlarmHistory alarmHistory = new AlarmHistory();
        if (alarmReal.getGrade() != null) {
            alarmHistory.setGrade(alarmReal.getGrade());
        }
        if (StringUtils.isNotBlank(alarmReal.getPointName())) {
            alarmHistory.setPointName(alarmReal.getPointName());
        }
        if (StringUtils.isNotBlank(alarmReal.getColumnName())) {
            alarmHistory.setColumnName(alarmReal.getColumnName());
        }
        if (alarmReal.getAlarmTime() != null) {
            alarmHistory.setAlarmTime(alarmReal.getAlarmTime());
        }
        if (alarmReal.getConfirmTime() != null) {
            alarmHistory.setConfirmTime(alarmReal.getConfirmTime());
        }
        alarmHistory.setDestroyTime(LocalDateTime.now());
        if (StringUtils.isNotBlank(alarmReal.getAlarmDesc())) {
            alarmHistory.setAlarmDesc(alarmReal.getAlarmDesc());
        }
        if (StringUtils.isNotBlank(alarmReal.getConfirmUser())) {
            alarmHistory.setConfirmUser(alarmReal.getConfirmUser());
        }
        if (alarmReal.getHeatSystemId() != null) {
            alarmHistory.setHeatSystemId(alarmReal.getHeatSystemId());
        }
        if (StringUtils.isNotBlank(alarmReal.getAddress())) {
            alarmHistory.setAddress(alarmReal.getAddress());
        }
        if (StringUtils.isNotBlank(alarmReal.getDutyUser())) {
            alarmHistory.setDutyUser(alarmReal.getDutyUser());
        }
        if (alarmReal.getRelevanceId() != null) {
            alarmHistory.setRelevanceId(alarmReal.getRelevanceId());
        }
        if (StringUtils.isNotBlank(alarmReal.getRelevanceName())) {
            alarmHistory.setRelevanceName(alarmReal.getRelevanceName());
        }
        alarmHistory.setType(pointL.getHeatType());
        if (alarmReal.getAlarmValue() != null) {
            alarmHistory.setAlarmValue(alarmReal.getAlarmValue());
        }
        if (StringUtils.isNotBlank(pointL.getValue())) {
            alarmHistory.setRecoverValue(new BigDecimal(pointL.getValue()));
        }
        if (StringUtils.isNotBlank(alarmReal.getRankJson())) {
            alarmHistory.setRankJson(alarmReal.getRankJson());
        }
        alarmHistory.setStatus(AlarmStatus.ALARM_STATUS_RECOVER.getStatus());
        alarmHistory.setCreateTime(LocalDateTime.now());
        return alarmHistoryService.save(alarmHistory);
    }

}
