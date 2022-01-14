package com.bmts.heating.signalr.customer.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.AlarmHistory;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.db.service.AlarmHistoryService;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.entiy.baseInfo.response.PointCollectConfigAlarm;
import com.bmts.heating.commons.redis.service.RedisAlarmSubUsersService;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.service.RedisSignalrService;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.signalr.config.LocalDataTypeAdapter;
import com.bmts.heating.signalr.entity.AlarmResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlarmService {
    @Autowired
    RedisPointService redisCollectPointService;
    @Autowired
    SignalRTemplate signalRTemplate;
    @Autowired
    MemoryService memoryService;
    @Autowired
    AlarmRealService alarmRealService;
    @Autowired
    RedisSignalrService redisSignalrService;
    @Autowired
    AlarmHistoryService alarmHistoryService;
    @Autowired
    RedisAlarmSubUsersService redisAlarmSubUsersService;

    public void dealFirst(String param) {

//        log.info("接收推送数据:", param);
        Gson goson = new Gson();
        PointL pointL = goson.fromJson(param, PointL.class);
        LocalDateTime now = LocalDateTime.now();
        Double realValue;
        try {
            realValue = Double.valueOf(pointL.getValue());
        } catch (Exception e) {
            log.error("cast pointl value cause exception {}", e);
            return;
        }
        if (pointL.getRelevanceId() == 0 || StringUtils.isEmpty(pointL.getPointName()))
            return;
        if (memoryService.getPoints().isEmpty()) {
            log.error("缓存采集量信息为空");
            return;
        }
        PointCollectConfigAlarm pointCollectConfigAlarm = memoryService.getPoints().get(pointL.getPointId());
        if (pointCollectConfigAlarm == null)
            return;

        //公用报警类型

        dealAlarm(pointL, now, realValue, pointCollectConfigAlarm);
    }

    private void dealAlarm(PointL pointL, LocalDateTime now, Double realValue, PointCollectConfigAlarm pointCollectConfigAlarm) {
        //根据点中文名称获取实时报警数据
        List<AlarmReal> pointAlarms = memoryService.alarmReals.stream().filter(x -> x.getPointName().equals(pointCollectConfigAlarm.getPointStandardName()) && x.getHeatSystemId().equals(pointCollectConfigAlarm.getHeatSystemId()))
                .collect(Collectors.toList());
        String alarmDes = pointCollectConfigAlarm.getPointStandardName().concat(",报警值").concat(realValue.toString());

        String alarmType = "设备故障";
        AlarmReal deviceAlarmCache = getAlarmReal(pointAlarms, alarmType);


        if (pointL.getQualityStrap() == (int)pointCollectConfigAlarm.getAlarmValue()) {

            alarmCore(now, realValue, pointCollectConfigAlarm, deviceAlarmCache, alarmDes, "设备",
                    2, alarmType, 1);
            return;
        } else {
            dealHistory(now, pointCollectConfigAlarm, alarmType, deviceAlarmCache, 2);
        }
        String type = "数据";
        int classify = 1; //分类 1为数据

        alarmType = "事故高";
        deviceAlarmCache = getAlarmReal(pointAlarms, alarmType);
        if (pointCollectConfigAlarm.getAccidentHigh() != null && pointCollectConfigAlarm.getAccidentHigh().doubleValue() <= realValue) {
            alarmCore(now, realValue, pointCollectConfigAlarm, deviceAlarmCache, alarmDes, type, classify, alarmType, 1);
            return;
        } else
            dealHistory(now, pointCollectConfigAlarm, alarmType, deviceAlarmCache, 1);

        alarmType = "运行高";
        deviceAlarmCache = getAlarmReal(pointAlarms, alarmType);

        if (pointCollectConfigAlarm.getRunningHigh() != null && pointCollectConfigAlarm.getRunningHigh().doubleValue() <= realValue) {
            alarmCore(now, realValue, pointCollectConfigAlarm, deviceAlarmCache, alarmDes, type, classify, alarmType, 2);
            return;
        } else dealHistory(now, pointCollectConfigAlarm, alarmType, deviceAlarmCache, 1);
        alarmType = "事故低";
        deviceAlarmCache = getAlarmReal(pointAlarms, alarmType);
        if (pointCollectConfigAlarm.getAccidentLower() != null && pointCollectConfigAlarm.getAccidentLower().doubleValue() >= realValue) {
            //事故低报警
            alarmCore(now, realValue, pointCollectConfigAlarm, deviceAlarmCache, alarmDes, type, classify, alarmType, 2);
            return;
        } else dealHistory(now, pointCollectConfigAlarm, alarmType, deviceAlarmCache, 2);
        alarmType = "运行低";
        deviceAlarmCache = getAlarmReal(pointAlarms, alarmType);
        if (pointCollectConfigAlarm.getRunningLower() != null && pointCollectConfigAlarm.getRunningLower().doubleValue() >= realValue) {
            alarmCore(now, realValue, pointCollectConfigAlarm, deviceAlarmCache, alarmDes, type, classify, alarmType, 2);
        } else dealHistory(now, pointCollectConfigAlarm, alarmType, deviceAlarmCache, 2);
    }

    private AlarmReal getAlarmReal(List<AlarmReal> pointAlarms, String finalAlarmType) {
        AlarmReal deviceAlarmCache;
        deviceAlarmCache = pointAlarms
                .stream()
                .filter(x -> x.getAlarmType().equals(finalAlarmType) && x.getClassify() == 1)
                .findFirst().orElse(null);
        return deviceAlarmCache;
    }

    private void alarmCore(LocalDateTime now, Double realValue, PointCollectConfigAlarm pointCollectConfigAlarm, AlarmReal deviceAlarmCache, String alarmDes, String type, int classify, String alarmType, int level) {
        AlarmResponse build = buildAlarmPushInfo(now, realValue, pointCollectConfigAlarm, alarmType, level, type, alarmDes, pointCollectConfigAlarm.getPointStandardName());
        if (deviceAlarmCache != null) {
            //更新报警值
            if (!deviceAlarmCache.getAlarmValue().equals(realValue)) {
                memoryService.alarmReals.remove(deviceAlarmCache);
                deviceAlarmCache.setAlarmValue(BigDecimal.valueOf(realValue));
                deviceAlarmCache.setAlarmDes(build.getAlarmDes());
                alarmRealService.updateById(deviceAlarmCache);
                //更新本地缓存
                memoryService.alarmReals.add(deviceAlarmCache);
            }
        } else {
            //插入数据
            AlarmReal info = new AlarmReal();
            info
                    .setAlarmValue(BigDecimal.valueOf(realValue))
                    .setAlarmTime(now)
                    .setAlarmDes(build.getAlarmDes())
                    .setAlarmType(alarmType)
                    .setClassify(classify)
                    .setHeatSystemId(pointCollectConfigAlarm.getHeatSystemId())
                    .setStationId(pointCollectConfigAlarm.getHeatTransferStationId())
                    .setStationName(pointCollectConfigAlarm.getHeatTransferStationName())
                    .setPointName(pointCollectConfigAlarm.getPointStandardName());
            alarmRealService.save(info);
            //更新本地缓存
            memoryService.alarmReals.add(info);
            //如果是新的报警数据才推送
            send(pointCollectConfigAlarm, build);
        }

    }


    /**
     * 打包并发送至web报警数据
     *
     * @param now
     * @param realValue
     * @param pointCollectConfigAlarm
     * @param alarmType
     * @param level
     * @param type
     * @param alarmDes
     * @param pointStandardName
     * @return
     */
    private AlarmResponse buildAlarmPushInfo(LocalDateTime now, Double realValue, PointCollectConfigAlarm pointCollectConfigAlarm, String alarmType, int level, String type, String alarmDes, String pointStandardName) {
        AlarmResponse build = AlarmResponse.builder()
                .alarmDes(alarmDes)
                .alarmTitle(alarmType)
                .level(level)
                .time(now)
                .classify(type)
                .value(realValue)
                .alarmType(pointStandardName)
                .station(pointCollectConfigAlarm.getHeatTransferStationName())
                .system(pointCollectConfigAlarm.getHeatSystemName())
                .build();

        return build;
    }

    /**
     * 处理报警历史
     *
     * @param now
     * @param pointCollectConfigAlarm
     * @param alarmType
     * @param deviceAlarmCahce
     */
    private void dealHistory(LocalDateTime now, PointCollectConfigAlarm pointCollectConfigAlarm, String alarmType, AlarmReal deviceAlarmCahce, int classify) {
        //正常数据 1.判断实时 如果有值 移入历史 2.查询历史 如果确认了还未销毁 则更新销毁时间
        if (deviceAlarmCahce != null) {
            Gson gson = new Gson();
            AlarmHistory alarmHistory = gson.fromJson(gson.toJson(deviceAlarmCahce), AlarmHistory.class);
            alarmHistory.setDestroyTime(now);
            alarmHistoryService.saveOrUpdate(alarmHistory);
        } else {
            AlarmHistory history = alarmHistoryService.getOne(
                    Wrappers.<AlarmHistory>lambdaQuery()
                            .isNull(AlarmHistory::getDestroyTime)
                            .eq(AlarmHistory::getAlarmType, alarmType)
                            .eq(AlarmHistory::getClassify, classify)
                            .eq(AlarmHistory::getPointName, pointCollectConfigAlarm.getPointStandardName()));
            if (history != null) {
                history.setDestroyTime(now);
                alarmHistoryService.saveOrUpdate(history);
            }

        }
    }


    /**
     * 推送
     *
     * @param alarm
     * @param alarmResponse
     */
    private void send(PointCollectConfigAlarm alarm, AlarmResponse alarmResponse) {
        List<Integer> onlineUsers = redisSignalrService.queryOnlineUsers();
        Set<Integer> integers = redisAlarmSubUsersService.queryForbiddenUsers();
        //删除禁用报警的用户
        onlineUsers.removeAll(integers);
        //判断点报警该推给哪些在线用户
        memoryService.userDataPerms.forEach(x -> {
            x.getStations().contains(alarm.getHeatTransferStationId());
            if (onlineUsers.contains(x.getUserId())) {
                //在线 推送
                String arg = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.sss")
                        .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter()).create().toJson(alarmResponse);
                signalRTemplate.send2AllServerSpecial("Alarm", x.getUserId() + "", arg);
                log.info("发送报警 用户id：{}，数据：{}", x.getUserId(), arg);
            }
        });
        if (onlineUsers.contains(-1)) {
            String arg = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.sss")
                    .registerTypeAdapter(LocalDateTime.class, new LocalDataTypeAdapter()).create().toJson(alarmResponse);
            signalRTemplate.send2AllServerSpecial("Alarm", "-1" + "", arg);
            log.info("发送报警");
        }
        //在线 推送


    }
}
