package com.bmts.heating.monitor.boot.app;

import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.db.service.second.PointConfigSecondService;
import com.bmts.heating.commons.heartbeat.adapter.handler.monitor.HeartBeatMonitorClientServer;
import com.bmts.heating.commons.redis.service.RedisEnergyPointService;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.monitor.boot.service.MonitorHistoryService;
import com.bmts.heating.monitor.boot.service.RealService;
import com.bmts.heating.monitor.dirver.adapter.ConstructionGenerator;
import com.bmts.heating.monitor.dirver.common.MonitorTaskUtils;
import com.bmts.heating.monitor.dirver.common.ReloadComputation;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.dirver.process.GuardMonitorProcess;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import com.bmts.heating.monitor.plugins.pvss.constructors.service.PVSSCommonService;
import com.spring4all.mongodb.EnableMongoPlus;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@EnableMongoPlus
@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@EnableAsync
//@EnableJms    //??????????????????
public class MonitorApplication implements CommandLineRunner {

    private Logger LOGGER = LoggerFactory.getLogger(MonitorApplication.class);

    @Autowired
    private MonitorType monitorType;
    @Autowired
    private HeartBeatMonitorClientServer heartBeatMonitorClientServer;

    @Autowired
    private GuardMonitorProcess GuardMonitorProcess;

    @Autowired
    private MonitorTaskUtils monitorTaskUtils;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private Computation computation;

    @Autowired
    private MonitorHistoryService historyService;
    @Autowired
    private RealService realService;

    //@Autowired
    //private DeviceHistoryService deviceHistoryService;
    //@Autowired
    //private SecondDataMapService secondDataMapService;
    @Autowired
    private PointConfigSecondService pointConfigSecondService;
    //@Autowired
    //private SecondPointConfig secondPointConfig;

    @Autowired
    private AlarmRealService alarmRealService;

    @Autowired
    private PVSSCommonService pvssCommonService;

    @Autowired
    private ReloadComputation reloadComputation;

    @Autowired
    private RedisEnergyPointService redisEnergyPointService;

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        LOGGER.info("?????????????????????{}??????,??????????????????................", monitorType.getIdentity());
        //????????????????????????,?????????????????????????????????
        LOGGER.info("??????????????????{}????????????????????????,?????????????????????????????????......................", monitorType.getIdentity());
        //new Thread(new RunWorkEntranceImpl(guardMonitorMqProcess)).start();
        //??????????????????
        GuardMonitorProcess.consume();

        //????????????
        heartBeatMonitorClientServer.start();


        //????????????????????????????????????
        List<MonitorProtery> mplist = monitorTaskUtils.getRunMonitorProtery();
        // ????????? ?????? pvss
        for (MonitorProtery mp : mplist) {
            if (Objects.equals(mp.getPattern().getModel(), "PVSS")) {
                List<MonitorMuster.Plugin> pluginList = mp.getPluginList();
                pluginList.stream().forEach(e -> {
                    // ??????????????????
                    List<List<PointL>> cachePoints = pvssCommonService.getCachePoints(e.getDevice_id());
                    if (cachePoints != null) {
                        reloadComputation.setMonitorMap(e.getDevice_id(), cachePoints);
                    }
                });
            }
        }

        // ???????????????  ?????? grpc ??????
        List<PointL> pointLS = redisCacheService.queryComputePoints();
        if (!CollectionUtils.isEmpty(pointLS)) {
            for (PointL pl : pointLS) {
                pl.setDataType(1);
            }
            computation.setMap(pointLS);
        }
        // ???????????????????????????
        //List<PointConfigSecondDto> secondPointList = pointConfigSecondService.secondQueryPoint(new QueryWrapper());
        //if (!CollectionUtils.isEmpty(secondPointList)) {
        //    secondPointConfig.setMap(secondPointList);
        //}

        // ???????????????????????? ??????Map
        List<AlarmReal> alarmRealList = alarmRealService.list();
        computation.setAlarmRealMap(alarmRealList);

        // ?????? ?????????  ????????? ??????????????????
        List<PointL> energyPoints = redisEnergyPointService.cacheSourceEnery();
        computation.setEneryMap(energyPoints);

        // ???????????????????????????????????????
        for (MonitorProtery mp : mplist) {
            //??????????????????
            ConstructionGenerator constructionGenerator = (ConstructionGenerator) SpringBeanFactory.getBean(mp.getPattern().getModel());
            constructionGenerator.init(mp);
        }


        //realService.consume();
        //historyService.consume();
        //deviceHistoryService.consume();
        //secondDataMapService.consume();

    }


}
