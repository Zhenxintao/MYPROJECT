package com.bmts.heating.monitor.boot.config;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaException;
import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.redis.second.RedisDeviceService;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.monitor.dirver.common.MonitorTaskUtils;
import com.bmts.heating.monitor.dirver.common.ReloadComputation;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import com.bmts.heating.monitor.plugins.pvss.constructors.service.PVSSCommonService;
import com.bmts.heating.monitor.second.config.TopicsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @ClassName: SaticScheduleTask
 * @Description: 定时任务
 * @Author: pxf
 * @Date: 2021/4/20 19:43
 * @Version: 1.0
 */

@Slf4j
@Component
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private Computation computation;

    private static Random random = new Random();

    @Autowired
    private RedisDeviceService redisDeviceService;

    @Autowired
    private RouteAdapter routeAdapter;
    @Autowired
    private TopicsConfig topicsConfig;
    @Autowired
    private RedisPointService redisPointService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ReloadComputation reloadComputation;

    @Autowired
    private PVSSCommonService pvssCommonService;

    @Autowired
    private MonitorTaskUtils monitorTaskUtils;


    //每五分钟执行一次
    @Scheduled(cron = "0 */5 * * * ? ")
    private void cachePointTasks() {
        // 调用 grpc 服务
        //获取所有可执行的任务实例
        List<MonitorProtery> mplist = monitorTaskUtils.getRunMonitorProtery();
        // 只加载 处理 pvss
        for (MonitorProtery mp : mplist) {
            if (Objects.equals(mp.getPattern().getModel(), "PVSS")) {
                List<MonitorMuster.Plugin> pluginList = mp.getPluginList();
                pluginList.stream().forEach(e -> {
                    // 加载点表数据
                    List<List<PointL>> cachePoints = pvssCommonService.getCachePoints(e.getDevice_id());
                    if (cachePoints != null) {
                        reloadComputation.setMonitorMap(e.getDevice_id(), cachePoints);
                    }
                });
            }
        }

    }


    //每小时执行一次
    @Scheduled(cron = "0 0 0/1 * * ? ")
    private void configureTasks() throws IOException, MicroException {
        // 调用 grpc 服务
        List<PointL> pointLS = redisCacheService.queryComputePoints();
        if (!CollectionUtils.isEmpty(pointLS)) {
            for (PointL pl : pointLS) {
                pl.setDataType(1);
            }
            computation.setMap(pointLS);
        }

    }

    ////每一分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ? ")
    //private void secondRedisTasks() {
    //    String deviceC = "100";
    //    List<DeviceInfoDto> list = new ArrayList<>();
    //    for (int i = 1; i < 101; i++) {
    //        DeviceInfoDto info = new DeviceInfoDto();
    //        info.setDeviceCode(deviceC + i);
    //        info.setDeviceType(i % 3 + 1);
    //        info.setTimestamp(System.currentTimeMillis());
    //        if ((i % 3 + 1) == 3) {
    //            info.setTemperature(new BigDecimal(getRandomDouble(18.0, 24.0)));
    //        } else {
    //            info.setActure_opening(new BigDecimal(getRandomIntInRange(20, 100)));
    //            info.setGiven_opening(new BigDecimal(getRandomIntInRange(20, 100)));
    //            info.setPg(new BigDecimal(getRandomDouble(0.1, 1.9)));
    //            info.setPh(new BigDecimal(getRandomDouble(0.1, 1.5)));
    //            info.setTg(new BigDecimal(getRandomDouble(80.0, 100.0)));
    //            info.setTh(new BigDecimal(getRandomDouble(70.0, 90.0)));
    //        }
    //        list.add(info);
    //    }
    //
    //    if (!CollectionUtils.isEmpty(list)) {
    //        redisDeviceService.setCachePoint(list);
    //    }
    //    log.info("----添加二网缓存数据成功，条数={} ", list.size());
    //
    //}

    ////每一分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ? ")
    //private void secondRedisMap() {
    //    String deviceC = "100";
    //
    //    Map<String, Map<String, String>> maps = new HashMap<>();
    //    for (int i = 1; i < 101; i++) {
    //        String deviceName = SecondDeviceType.getRedisKey(i % 3 + 1);
    //        String deviceCode = deviceC + i;
    //        String key = RedisKeyConst.SECOND_REAL_DATA.concat(deviceName) + deviceCode;
    //
    //        Map<String, String> map = new HashMap<>();
    //        map.put("code", deviceCode);
    //        map.put("deviceType", String.valueOf(i % 3 + 1));
    //        map.put("ts", String.valueOf(System.currentTimeMillis()));
    //
    //        if ((i % 3 + 1) == 3) {
    //            map.put("temperature", String.valueOf(getRandomDouble(18.0, 24.0)));
    //        } else {
    //            map.put("acture_opening", String.valueOf(getRandomIntInRange(20, 100)));
    //            map.put("given_opening", String.valueOf(getRandomIntInRange(20, 100)));
    //            map.put("pg", String.valueOf(getRandomDouble(0.1, 1.9)));
    //            map.put("ph", String.valueOf(getRandomDouble(0.1, 1.5)));
    //            map.put("tg", String.valueOf(getRandomDouble(80.0, 100.0)));
    //            map.put("th", String.valueOf(getRandomDouble(70.0, 90.0)));
    //        }
    //        maps.put(key, map);
    //    }
    //
    //    if (!CollectionUtils.isEmpty(maps)) {
    //        redisDeviceService.setCachMap(maps);
    //    }
    //    log.info("----添加二网HashMap数据成功，条数={} ", maps.size());
    //
    //}


    ////每一分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ? ")
    //private void redisSendKafka() {
    //    //  从实时库获取所有的点
    //    List<DeviceInfoDto> list = redisPointService.querySecondDevice(SecondDeviceType.DEVICE_UNIT_VALVE.getName());
    //    List<DeviceInfoDto> listHv = redisPointService.querySecondDevice(SecondDeviceType.DEVICE_HOUSE_VALVE.getName());
    //    List<DeviceInfoDto> listRt = redisPointService.querySecondDevice(SecondDeviceType.DEVICE_RT.getName());
    //    if (!CollectionUtils.isEmpty(listHv)) {
    //        list.addAll(listHv);
    //    }
    //    if (!CollectionUtils.isEmpty(listRt)) {
    //        list.addAll(listRt);
    //    }
    //    long timeStamp = System.currentTimeMillis();
    //    if (!CollectionUtils.isEmpty(list)) {
    //        list.stream().forEach(e -> {
    //            sendMessage(e, timeStamp);
    //        });
    //    }
    //    log.info("----拉取二网数据成功，条数={} ", list.size());
    //
    //}


    //每一分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ? ")
    //private void secondRedisDataMap() {
    //    Map<String, Map<String, String>> maps = new HashMap<>();
    //    // 获取 keys
    //    Set<String> setKeys = redisTemplate.keys(RedisKeyConst.SECOND_NET_DEVICE + "*");
    //    setKeys.stream().forEach(str -> {
    //        // 截取
    //        String[] split = str.split(":");
    //
    //        // level 层级
    //        //String levelName = split[4];
    //
    //        // 表名 tableName
    //        String tableName = split[5];
    //
    //        // groupId
    //        String groupId_key = "";
    //        String groupId = "";
    //        Matcher matcher_groupId = Pattern.compile(":g_.*?\\d*").matcher(str);
    //        if (matcher_groupId.find()) {
    //            groupId_key = matcher_groupId.group(0);
    //            groupId = groupId_key.substring(3);
    //        } else {
    //            return;
    //        }
    //
    //        // id
    //        String id_key = "";
    //        String id = "";
    //        Matcher matcher_id = Pattern.compile(":id_.*?\\d*").matcher(str);
    //        if (matcher_id.find()) {
    //            id_key = matcher_id.group(0);
    //            id = id_key.substring(4);
    //        } else {
    //            return;
    //        }
    //
    //        // deviceCode
    //        String code_key = "";
    //        String code = "";
    //        Matcher matcher_code = Pattern.compile(":d_.*?\\d*").matcher(str);
    //        if (matcher_code.find()) {
    //            code_key = matcher_code.group(0);
    //            code = code_key.substring(3);
    //        } else {
    //            return;
    //        }
    //        String key = RedisKeyConst.SECOND_REAL_DATA.concat(tableName) + code_key;
    //
    //        Map<String, String> map = new HashMap<>();
    //        map.put("tableName", tableName);
    //        map.put("deviceCode", code);
    //        map.put("groupId", groupId);
    //        map.put("id", id);
    //        map.put("ts", String.valueOf(System.currentTimeMillis()));
    //        map.put("temperature", String.valueOf(getRandomDouble(18.0, 24.0)));
    //        map.put("acture_opening", String.valueOf(getRandomIntInRange(20, 100)));
    //        map.put("given_opening", String.valueOf(getRandomIntInRange(20, 100)));
    //        map.put("pg", String.valueOf(getRandomDouble(0.1, 1.9)));
    //        map.put("ph", String.valueOf(getRandomDouble(0.1, 1.5)));
    //        map.put("tg", String.valueOf(getRandomDouble(80.0, 100.0)));
    //        map.put("th", String.valueOf(getRandomDouble(70.0, 90.0)));
    //        maps.put(key, map);
    //
    //    });
    //
    //    if (!CollectionUtils.isEmpty(maps)) {
    //        redisDeviceService.setCachMap(maps);
    //        log.info("----添加二网HashMap数据成功，条数={} ", maps.size());
    //    }
    //
    //}


    //每一分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ? ")
    //private void sendMapKafka() {
    //    long timestamp = System.currentTimeMillis();
    //    //  从实时库获取所有的点
    //    List<Map<Object, Object>> listMap = redisPointService.querySecondMap();
    //    if (!CollectionUtils.isEmpty(listMap)) {
    //        listMap.stream().forEach(e -> {
    //            sendMapMessage(e, timestamp);
    //        });
    //    }
    //    log.info("----拉取 Map 二网数据成功，条数={} ", listMap.size());
    //
    //}

    private static int getRandomIntInRange(int min, int max) {
        return random.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }

    private static String getRandomDouble(Double min, Double max) {
        return String.format("%.3f", random.doubles(min, (max + 1)).limit(1).findFirst().getAsDouble());
    }


    /**
     * 组装kafka 发送消息实体
     *
     * @param info
     * @param timeStamp
     */
    private void sendMessage(DeviceInfoDto info, long timeStamp) {
        info.setTimestamp(timeStamp);
        product(topicsConfig.getDevice_info().getTopicName(), info);
    }


    /**
     * 组装kafka 发送消息实体
     *
     * @param map
     */
    private void sendMapMessage(Map<Object, Object> map, long timestamp) {
        map.put("ts", timestamp);
        product(topicsConfig.getDevice_info().getTopicName(), map);
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
