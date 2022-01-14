package com.bmts.heating.service.task.transport.service.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.db.service.EnergyDeviceService;
import com.bmts.heating.commons.entiy.baseInfo.enums.AbnormalLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.service.task.transport.config.JobComputation;
import com.bmts.heating.service.task.transport.utils.JobMonitorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @ClassName: HeatSourceAsync
 * @Description: 处理异步业务
 * @Author: pxf
 * @Date: 2021/7/12 17:42
 * @Version: 1.0
 */

@Slf4j
@Component
public class HeatSourceAsync {

    @Autowired
    private RedisPointService redisPointService;

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    @Autowired
    private JobComputation jobComputation;

    @Autowired
    private EnergyDeviceService energyDeviceService;

    @Async("asyncJobReal")
    public void sendReal(List<PointL> plList, Map<String, Object> eneryConfigMap) {
        // 设置值
        setPointValue(plList);
        // 没有 获取计算点 ，无法进行计算
        // compute(plList);

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


            List<PointL> listEnery = new ArrayList<>();
            List<PointL> listAbnormal = new ArrayList<>();
            plList.stream().forEach(pl -> {
                if (pl != null) {
                    // 判断质量戳
                    if (pl.getQualityStrap() != null && pl.getQualityStrap() > 0) {
                        qualityStarp.set(pl.getQualityStrap());
                    }
                    filterEnergy(eneryConfigMap, listEnery, listAbnormal, pl);
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
                    jobComputation.setEneryMap(listEnery);
                }
                // 进入异常数据库
                if (!CollectionUtils.isEmpty(listAbnormal)) {
                    listAbnormal.stream().forEach(e -> {
                        sendAbnormal(e);
                    });
                }

                log.info("JobHeatSource---推送到实时库---数据：releverceId={}, total ={}, timeStamp={}",
                        relevanceId, plList.size(), maxTimeStamp);

            } catch (Exception e) {
                log.error("ERRORJobHeatSource---异常数据---:releverceId={},批号 =={},total={},timeStamp={},e ={}",
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
            PointL lastPoint = jobComputation.getEneryMap(pl.getPointId());
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
        log.info("sendAbnormal----{} , value---{}, status---{}",
                pointL.getPointName(), pointL.getValue(), aBoolean);
    }


    // 设置值
    private void setPointValue(List<PointL> plList) {
        plList.stream().forEach(e -> {
            if (e != null) {
                String newValue = null;
                String oldValue = e.getOldValue();
                if (Objects.equals(oldValue, "true")) {
                    newValue = "1";
                }
                if (Objects.equals(oldValue, "false")) {
                    newValue = "0";
                }
                if (StringUtils.isNotBlank(oldValue)) {
                    newValue = JobMonitorUtil.type(e.getType(), oldValue);
                }
                if (newValue == null) {
                    e.setHealthSign(HealthStatus.MISSING_DATA.status());
                } else {
                    e.setValue(newValue);
                    e.setHealthSign(HealthStatus.HEALTH_DATA.status());
                }

            }
        });


    }

    private void compute(List<PointL> plList) {
        //  Map
        Map<String, String> collectMap = new HashMap<>();
        plList.stream().forEach(e -> {
            if (e != null) {
                if (StringUtils.isNotBlank(e.getPointName()) && StringUtils.isNotBlank(e.getValue())) {
                    collectMap.put(e.getPointName(), e.getValue());
                }
            }
        });
        plList.stream().forEach(point -> {
            if (point != null && point.getPointConfig() == PointProperties.Compute.type()) {
                if (StringUtils.isNotBlank(point.getExpression())) {
                    try {
                        JEP jep = new JEP();
                        // 计算表达式
                        String expression = point.getExpression();
                        // 进行正则验证 表达式  [^a-zA-Z0-9]
                        String trim = Pattern.compile("[^_a-zA-Z0-9]").matcher(expression).replaceAll(" ").trim();
                        if (StringUtils.isNotBlank(trim)) {
                            // 去除空字符串
                            String[] split = trim.split("\\s+");
                            for (String str : split) {
                                String pointLValue = collectMap.get(str);
                                if (StringUtils.isNotBlank(pointLValue)) {
                                    jep.addVariable(str, Double.parseDouble(pointLValue));
                                }
                            }
                            // 执行计算
                            jep.parseExpression(expression);
                            // 计算结果
                            double result = jep.getValue();
                            point.setValue(String.format("%.2f", result));
                            // 设置时间
                            point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));

                            // 进行计算逻辑处理
                            log.info("JobCompute---计算数据：name={},level={},releverceId={},type={},timeStrap={},value={}",
                                    point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap(), point.getValue());

                        }
                    } catch (Exception e) {
                        log.error("JobCompute---异常数据---:name={},level={},releverceId={},type={},timeStrap={}," +
                                point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap());
                        e.printStackTrace();
                    }
                } else {
                    point.setValue("0.00");
                    // 设置时间
                    point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
                }
            }
        });
        collectMap.clear();
    }


}
