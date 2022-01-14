package com.bmts.heating.signalr.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.entiy.baseInfo.response.PointCollectConfigAlarm;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.service.RedisSignalrService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableScheduling
@Slf4j
public class MemoryService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    RedisPointService redisCollectPointService;
    @Autowired
    PointConfigMapper pointCollectMapper;
    @Autowired
    AuthrityService authrityService;
    @Autowired
    HeatSystemMapper heatSystemMapper;
    @Autowired
    RedisSignalrService redisSignalrService;

    /**
     * key pointId   查询采集点报警配置到内存
     */
    @Getter
    Map<Integer, PointCollectConfigAlarm> points;

    @Getter
    List<UserDataPerms> userDataPerms;

    //内存存储报警实时表数据
    @Getter
    @Setter
    List<AlarmReal> alarmReals;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void loadPointConfig() {
        QueryWrapper<PointCollectConfigAlarm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pcc.isAlarm", true);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadAndControl.type(),PointProperties.ReadAndControl.type());
        List<PointCollectConfigAlarm> list = pointCollectMapper.queryConfigAndAlarm(queryWrapper);
        points = new HashMap<>(list.size());
        List<FirstNetBase> firstNetBases = heatSystemMapper.querySystem(new QueryWrapper<FirstNetBase>());
        if (!CollectionUtils.isEmpty(firstNetBases)) {
            /**
             * 以基础数据为主 如果换热站和控制柜都为空 则为垃圾数据 不做缓存处理
             **/
            List<FirstNetBase> collect = firstNetBases.stream().filter(x -> StringUtils.isNoneBlank(x.getHeatCabinetName(), x.getHeatTransferStationName())).collect(Collectors.toList());
            Map<Integer, List<PointCollectConfigAlarm>> pointCollectConfigAlarms = list.stream().collect(Collectors.groupingBy(x -> x.getHeatSystemId()));
            for (FirstNetBase firstNetBase : collect) {
                List<PointCollectConfigAlarm> pointAlarms = pointCollectConfigAlarms.get(firstNetBase.getHeatSystemId());
                if (!CollectionUtils.isEmpty(pointAlarms)) {
                    for (PointCollectConfigAlarm pointAlarm : pointAlarms) {
                        pointAlarm.setHeatSystemName(firstNetBase.getHeatSystemName());
                        pointAlarm.setHeatSystemId(firstNetBase.getHeatSystemId());
                        pointAlarm.setHeatCabinetId(firstNetBase.getHeatCabinetId());
                        pointAlarm.setHeatCabinetName(firstNetBase.getHeatCabinetName());
                        pointAlarm.setHeatTransferStationName(firstNetBase.getHeatTransferStationName());
                        pointAlarm.setHeatTransferStationId(firstNetBase.getHeatTransferStationId());
                        pointAlarm.setHeatStationOrgName(StringUtils.isBlank(firstNetBase.getHeatStationOrgName()) ? "" : firstNetBase.getHeatStationOrgName());
                        points.put(pointAlarm.getId(), pointAlarm);
                    }

                }
            }
            log.info("point has null of station count {}", points.values().stream()
                    .filter(x -> StringUtils.isBlank(x.getHeatTransferStationName()))
                    .collect(Collectors.toList()).size()
            );
        }


    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void loadUserPerms() {
        List<UserDataPerms> userDataPerms = authrityService.queryStationsByAllUsers();
        this.userDataPerms = userDataPerms;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AlarmRealService bean = event.getApplicationContext().getBean(AlarmRealService.class);

        this.alarmReals = bean.list();
    }
}
