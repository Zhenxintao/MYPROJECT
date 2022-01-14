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
//@EnableJms    //启动消息队列
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

        LOGGER.info("当前服务节点为{}节点,开启采集任务................", monitorType.getIdentity());
        //开启监控守护进程,监控冗余服务器传递任务
        LOGGER.info("当前服务节点{}开启监控守护线程,监控冗余服务器传递任务......................", monitorType.getIdentity());
        //new Thread(new RunWorkEntranceImpl(guardMonitorMqProcess)).start();
        //启动守护线程
        GuardMonitorProcess.consume();

        //启动心跳
        heartBeatMonitorClientServer.start();


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

        // 加载计算量  调用 grpc 服务
        List<PointL> pointLS = redisCacheService.queryComputePoints();
        if (!CollectionUtils.isEmpty(pointLS)) {
            for (PointL pl : pointLS) {
                pl.setDataType(1);
            }
            computation.setMap(pointLS);
        }
        // 加载二网点配置数据
        //List<PointConfigSecondDto> secondPointList = pointConfigSecondService.secondQueryPoint(new QueryWrapper());
        //if (!CollectionUtils.isEmpty(secondPointList)) {
        //    secondPointConfig.setMap(secondPointList);
        //}

        // 加载实时报警数据 进入Map
        List<AlarmReal> alarmRealList = alarmRealService.list();
        computation.setAlarmRealMap(alarmRealList);

        // 加载 热源的  能耗点 的实时库数据
        List<PointL> energyPoints = redisEnergyPointService.cacheSourceEnery();
        computation.setEneryMap(energyPoints);

        // 初始化所有可执行的任务实例
        for (MonitorProtery mp : mplist) {
            //启动处理插件
            ConstructionGenerator constructionGenerator = (ConstructionGenerator) SpringBeanFactory.getBean(mp.getPattern().getModel());
            constructionGenerator.init(mp);
        }


        //realService.consume();
        //historyService.consume();
        //deviceHistoryService.consume();
        //secondDataMapService.consume();

    }


}
