package com.bmts.heating.monitor.boot.service.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.db.service.EnergyDeviceService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.baseInfo.enums.AbnormalLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.InsertHistoryMinuteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.history.service.PointInputService;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.monitor.boot.converter.PointLConverter;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @ClassName: MonitorAsync
 * @Description: 处理异步业务
 * @Author: pxf
 * @Date: 2021/7/12 17:42
 * @Version: 1.0
 */

@Component
public class MonitorAsync {

    private static Logger logger = LoggerFactory.getLogger(MonitorAsync.class);

    @Autowired
    private SignalRTemplate signalRTemplate;

    @Autowired
    private RedisPointService redisPointService;

    @Autowired
    private PointInputService pointInputService;

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    @Autowired
    private PointStandardService pointStandardService;

    @Autowired
    private Computation computation;

    @Autowired
    private EnergyDeviceService energyDeviceService;

    @Async("asyncRealExecutor")
    public void sendReal(Map<String, PointL> pointMap, Map<String, Object> eneryConfigMap) {
        List<PointL> plList = pointMap.values().stream().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(plList)) {
            int relevanceId = plList.get(0).getRelevanceId();
            int level = plList.get(0).getLevel();
            // 取出  plList  中最大的时间戳
            List<PointL> timeStampQualist = new ArrayList<>();
            // 时间戳的点
            PointL timeStampPoint = new PointL();
            timeStampPoint.setLevel(level);
            timeStampPoint.setRelevanceId(relevanceId);
            timeStampPoint.setPointName("timeStamp");
            // 获取最大时间戳
            Long maxTimeStamp = plList.stream().max(Comparator.comparing(PointL::getTimeStrap)).map(PointL::getTimeStrap).orElse(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            timeStampPoint.setValue(maxTimeStamp.toString());
            timeStampQualist.add(timeStampPoint);
            // 质量戳的点
            PointL qualityStrapPoint = new PointL();
            qualityStrapPoint.setPointName("qualityStrap");
            qualityStrapPoint.setLevel(level);
            qualityStrapPoint.setRelevanceId(relevanceId);
            AtomicReference<Integer> qualityStarp = new AtomicReference<>(0);
            // 推送 SignalR
            List<PointL> listEnery = new ArrayList<>();
            List<PointL> listAbnormal = new ArrayList<>();
            plList.stream().forEach(pl -> {
                if (pl != null) {
                    // 判断质量戳
                    if (pl.getQualityStrap() != null && pl.getQualityStrap() > 0) {
                        qualityStarp.set(pl.getQualityStrap());
                    }
                    filterEnergy(eneryConfigMap, listEnery, listAbnormal, pl);


                    //JSONObject jsonObject = new JSONObject();
                    //jsonObject.put("pointId", pl.getPointId());
                    //if (StringUtils.isNotBlank(pl.getValue())) {
                    //    jsonObject.put("value", pl.getValue());
                    //} else {
                    //    jsonObject.put("value", 0);
                    //}
                    //jsonObject.put("timeStrap", pl.getTimeStrap());

                    //try {
                    //    // 推送 signal
                    //    signalRTemplate.send2AllServerSpecial("PushRealValue", jsonObject.toJSONString());
                    //} catch (Exception e) {
                    //    logger.error("SignalR---推送异常数据：name={},level={},releverceId={},type={},timeStrap={}",
                    //            pl.getPointName(), pl.getLevel(), pl.getRelevanceId(), pl.getType(), pl.getTimeStrap());
                    //    e.printStackTrace();
                    //}
                }
            });
            qualityStrapPoint.setValue(String.valueOf(qualityStarp.get()));
            timeStampQualist.add(qualityStrapPoint);


            // 推送到实时库
            try {
                redisPointService.setCachePoint(plList);
                redisPointService.setTimeStampAndQualityStrap(timeStampQualist);
                // 更新完实时库，要对应更新本地缓存
                if (!CollectionUtils.isEmpty(listEnery)) {
                    computation.setEneryMap(listEnery);
                }
                // 进入异常数据库
                if (!CollectionUtils.isEmpty(listAbnormal)) {
                    listAbnormal.stream().forEach(e -> {
                        sendAbnormal(e);
                    });
                }

                logger.info("Actual---推送到实时库---数据：releverceId={}, total ={}, timeStamp={}",
                        relevanceId, plList.size(), maxTimeStamp);

            } catch (Exception e) {
                logger.error("ERRORActual---异常数据---:releverceId={},批号 =={},total={},timeStamp={},e ={}",
                        relevanceId, plList.get(0).getLotNo(), plList.size(), maxTimeStamp, e);
                e.printStackTrace();
            }

        }

    }

    private void filterEnergy(Map<String, Object> eneryConfigMap, List<PointL> listEnery, List<PointL> listAbnormal, PointL pl) {
        // 进行 水电热 点的值比较配置  从Map 中获取对应的配置
        Object objectConfig = eneryConfigMap.get(pl.getPointName());
        if (objectConfig != null) {
            BigDecimal configValue = new BigDecimal(objectConfig.toString());
            // 进行比较计算   上一次的值
            PointL lastPoint = computation.getEneryMap(pl.getPointId());
            if (lastPoint == null) {
                listEnery.add(pl);
                return;
            }
            String lastValueStr = lastPoint.getValue();
            // 当前值
            String nowValueStr = pl.getValue();
            if (StringUtils.isNotBlank(lastValueStr) && StringUtils.isNotBlank(nowValueStr)) {
                BigDecimal lastValue = new BigDecimal(lastValueStr);
                BigDecimal nowValue = new BigDecimal(nowValueStr);

                // 进行比较  当前值 大于上次值  v1>v2  (v1-v2)/v2
                if (nowValue.compareTo(lastValue) == 1) {
                    // 进行计算判断  (v1-v2)/v2
                    BigDecimal subtract = nowValue.subtract(lastValue);
                    BigDecimal divide = subtract.divide(lastValue, 2, BigDecimal.ROUND_HALF_UP);
                    // 结果和配置的百分数进行比较
                    BigDecimal result = divide.multiply(new BigDecimal(100));

                    if (result.compareTo(configValue) > -1) {
                        // 说明异常 进入异常数据库，并用上次的值进行补值操作
                        PointL abnormalPoint = JSONObject.parseObject(JSON.toJSONString(pl), PointL.class);
                        listAbnormal.add(abnormalPoint);
                        pl.setValue(lastValueStr);
                        if (lastPoint.getOldValue() != null) {
                            pl.setOldValue(lastPoint.getOldValue());
                        }
                    }

                }
                // 当前值 小于 上次值
                if (nowValue.compareTo(lastValue) == -1) {
                    // 查询数据库是否换表
                    EnergyDevice energyDevice = energyDeviceService.getOne(Wrappers.<EnergyDevice>lambdaQuery().eq(EnergyDevice::getRelevanceId, pl.getRelevanceId())
                            .eq(EnergyDevice::getStatus, false)
                            .orderByDesc(EnergyDevice::getCreateTime)
                            .last("limit 1"));
                    if (energyDevice != null) {
                        // 说明换表了 则更新设备状态
                        EnergyDevice updateEnergyDevice = new EnergyDevice();
                        updateEnergyDevice.setId(energyDevice.getId());
                        updateEnergyDevice.setStatus(1);
                        updateEnergyDevice.setUpdateTime(LocalDateTime.now());
                        energyDeviceService.updateById(updateEnergyDevice);

                    } else {
                        // 没有换表，则表示异常，应进行补值操作，并进入异常数据库
                        PointL abnormalPoint = JSONObject.parseObject(JSON.toJSONString(pl), PointL.class);
                        listAbnormal.add(abnormalPoint);
                        pl.setValue(lastValueStr);
                        if (lastPoint.getOldValue() != null) {
                            pl.setOldValue(lastPoint.getOldValue());
                        }
                    }
                }
                listEnery.add(pl);
            }
        }
        // eneryConfigMap.clear();
    }


    //@Async("asyncHistoryExecutor")
    public void sendHistory(Message_Point_Gather pointGather, Long timeStamp, Integer relevanceId, Integer relevanceType) {
        // 系统的  点 集合
        Map<String, PointL> pointLMap = pointGather.getPointLS();
        List<PointL> pointList = pointLMap.values().stream().collect(Collectors.toList());
        // 系统id
        Integer systemId = pointGather.getRelevanceId();
        if (!CollectionUtils.isEmpty(pointList)) {
            // 时间戳 转  时间
            LocalDateTime stampTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

            try {
                // 处理 异常质量戳的值
                List<PointL> exceptionList = pointList.stream().filter(e -> e.getQualityStrap() != 192).collect(Collectors.toList());
                // 组装异常 json 格式
                List<JSONObject> listJson = new ArrayList<>();
                for (PointL excep : exceptionList) {
                    JSONObject jsonObj = new JSONObject(true);
                    jsonObj.put("pointName", excep.getPointName());
                    jsonObj.put("oldValue", excep.getOldValue());
                    jsonObj.put("qualityStrap", excep.getQualityStrap());
                    listJson.add(jsonObj);
                }

                // 批量处理到历史数据
                Map<String, Object> esMap = new HashMap<>();
                for (PointL pointL : pointList) {
                    if (pointL != null) {
                        if (StringUtils.isNotBlank(pointL.getPointName()) && StringUtils.isNotBlank(pointL.getValue())) {
                            esMap.put(pointL.getPointName(), pointL.getValue());
                        }
                    }
                }

                // 站  或是源 下  所有系统  的时间戳是 统一的
                esMap.put("timeStrap", timeStamp);
                // 系统id
                esMap.put("relevanceId", systemId);
                esMap.put("level", 1);
                esMap.put("exception", listJson.toString());

                pointInputService.save(EsIndex.FIRST_REAL_DATA, esMap);

                logger.info("ES---写入历史库---数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},now = {}",
                        relevanceId, stampTime, relevanceType, systemId, pointList.size(), LocalDateTime.now());

            } catch (Exception e) {
                logger.error("ES--ERRORHistory---异常数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},e = {}",
                        relevanceId, stampTime, relevanceType, systemId, pointList.size(), e);
                e.printStackTrace();
            }


        }
    }

    @Async("asyncHistoryExecutor")
    public void sendTDHistory(Message_Point_Gather pointGather, Long timeStamp, Integer relevanceId, Integer relevanceType) {
        // 系统的  点 集合
        Map<String, PointL> pointLMap = pointGather.getPointLS();
        List<PointL> pointList = pointLMap.values().stream().collect(Collectors.toList());
        // 进行标准点表匹配是否入TD历史库
        List<PointStandard> listPointStandar = pointStandardService.list(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getTdColumn, true));
        List<PointL> pointListAdd = new ArrayList<>();
        // List<String> loseList = new ArrayList<>();
        pointList.stream().forEach(e -> {
            if (e.getHeatType() != null) {
                PointStandard pointStandard = listPointStandar.parallelStream().filter(j -> Objects.equals(j.getColumnName(), e.getPointName())
                        && Objects.equals(j.getLevel(), e.getHeatType())).findFirst().orElse(null);
                if (pointStandard != null) {
                    pointListAdd.add(e);
                }
                //else {
                //    loseList.add(e.getPointName());
                //
                //}
            }
        });

        //if (!CollectionUtils.isEmpty(loseList)) {
        //    logger.error("TD--ERROR---relevanceId={}---没有配置的TD库字段--- {}", systemId, loseList.toString());
        //}

        // 系统id
        Integer systemId = pointGather.getRelevanceId();
        if (!CollectionUtils.isEmpty(pointListAdd)) {


            List<PointInfo> pointInfoList = new ArrayList<>();
            try {
                List<InsertHistoryMinuteDto> listDto = new ArrayList<>();

                Integer heatType = pointListAdd.get(0).getHeatType();
                // 热力站的点
                if (heatType == 1) {
                    // 组装参数数据
                    InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
                    historyDto.setDeviceCode("station_minute");
                    historyDto.setGroupId(systemId);
                    historyDto.setLevel(TreeLevel.HeatSystem.level());
                    historyDto.setTs(timeStamp);
                    // 实体类转换
                    List<PointInfo> pointInfos = PointLConverter.INSTANCE.domainToInfo(pointListAdd);
                    historyDto.setPoints(pointInfos);
                    listDto.add(historyDto);
                    pointInfoList.addAll(pointInfos);

                }
                if (heatType == 2) {
                    // 组装参数数据
                    InsertHistoryMinuteDto historyDto = new InsertHistoryMinuteDto();
                    historyDto.setDeviceCode("source_minute");
                    historyDto.setGroupId(systemId);
                    historyDto.setLevel(TreeLevel.HeatSystem.level());
                    historyDto.setTs(timeStamp);
                    // 实体类转换
                    List<PointInfo> pointInfos = PointLConverter.INSTANCE.domainToInfo(pointListAdd);
                    historyDto.setPoints(pointInfos);
                    listDto.add(historyDto);
                    pointInfoList.addAll(pointInfos);
                }


                if (!CollectionUtils.isEmpty(listDto)) {
                    // 写入历史库
                    Boolean historyBoolean = historyTdGrpcClient.insertHistoryMinuteToTd(listDto);

                    // 时间戳 转  时间
                    // LocalDateTime stampTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

                    logger.info("TD---写入历史库---数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},historyBoolean = {}",
                            relevanceId, timeStamp, relevanceType, systemId, pointInfoList.size(), historyBoolean);
                }


            } catch (Exception e) {
                logger.error("TD--ERRORHistory---异常数据：relevanceId={},timeStrap={}, relevanceType={},systemId={} ,total={},e = {}",
                        relevanceId, timeStamp, relevanceType, systemId, pointInfoList.size(), e);
                e.printStackTrace();
            }


        }
    }


    private void sendAbnormal(PointL pointL) {
        Abnormal dto = new Abnormal();
        dto.setGroupId(pointL.getRelevanceId());
        dto.setType(HealthStatus.EXCEPTION_BURR.status());
        dto.setMsg(pointL.getPointName());
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
        logger.info("sendAbnormal----{} , value---{}, status---{}",
                pointL.getPointName(), pointL.getValue(), aBoolean);
    }
}
