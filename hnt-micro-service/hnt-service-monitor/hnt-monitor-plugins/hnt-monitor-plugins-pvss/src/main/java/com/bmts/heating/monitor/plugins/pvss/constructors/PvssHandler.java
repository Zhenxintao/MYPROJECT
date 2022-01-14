package com.bmts.heating.monitor.plugins.pvss.constructors;


import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.monitor.dirver.adapter.AssistWorkAware;
import com.bmts.heating.monitor.dirver.adapter.HandlerGenerator;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class PvssHandler implements HandlerGenerator {
    private Logger LOGGER = LoggerFactory.getLogger(PvssHandler.class);
    private ThreadLocal<MonitorMuster.Plugin> work_plugin = new InheritableThreadLocal<MonitorMuster.Plugin>();  //多线程独立使用
    private List<MonitorMuster.Plugin> work_plugins;    //多线程公用

//    private Map<String, List<PointL>> collectionMap = new HashMap<>();

    @Autowired
    private AssistWorkAware assistWorkAware;

    @Autowired
    private RedisCacheService redisCacheService;

    /**
     * 采集任务
     *
     * @param pattern
     */
    @Async("asyncMonitorExecutor")
    @Override
    public void workGahter(MonitorType.Pattern pattern) {
        //System.out.println("patterns:<><><><><><><><><>"+System.identityHashCode(guard_monitor_open));
        LOGGER.info("{}父线程开启pvss采集任务:{}", Thread.currentThread().getName(), this.getWork_plugin().getModel_host());

        IWorkEntrance pvssWorker = (IWorkEntrance) SpringBeanFactory.getBean("PvssWorker");
        assistWorkAware.runningConfig(this.getWork_plugin(), pattern.getCasenum(), "pvssHandler", this.getWork_plugins());
        assistWorkAware.startGaterWork(pvssWorker, pattern, this);
    }

    /**
     * 设置任务实例(采集下发用)
     *
     * @param plugin
     */
    @Override
    public void setWork_plugin(MonitorMuster.Plugin plugin) {
        work_plugin.set(plugin);
    }

    /**
     * 设置所有任务实例-列表(下发用)
     *
     * @param pluginList
     */
    @Override
    public void setWork_pluginList(List<MonitorMuster.Plugin> pluginList) {
        this.work_plugins = pluginList;
    }

    /**
     * 加载点表任务
     *
     * @param plugin  采集实例类型
     * @param casenum 当前实例开启线程数
     * @return
     */
    @SneakyThrows
    @Override
    public List<List<PointL>> getTaskForCache(MonitorMuster.Plugin plugin, int casenum) {
        //获取点表信息
        try {
            List<List<PointL>> listS = new ArrayList<>();


//            String deviceId = plugin.getDevice_id();
//            List<PointL> pointLS = redisCacheService.getPoints(deviceId);
//            if (!CollectionUtils.isEmpty(pointLS)) {
//                for (PointL pl : pointLS) {
//                    pl.setDataType(1);
//                }
//                // 根据机组系统id 进行分组
//                Map<Integer, List<PointL>> collect = pointLS.stream().collect(Collectors.groupingBy(e -> e.getLevel() + e.getRelevanceId()));
//                for (Integer releverceId : collect.keySet()) {
//                    List<PointL> systemList = collect.get(releverceId);
//                    listS.add(systemList);
//                }
////                collectionMap.put(deviceId, pointLS);
//            }


            return listS;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return Collections.emptyList();
    }

    /**
     * 下发任务
     *
     * @param pattern
     * @param pointLS 任务列表
     */
    @Async("asyncMonitorExecutor")
    @Override
    public List<PointL> workIssue(MonitorType.Pattern pattern, List<PointL> pointLS) throws ExecutionException, InterruptedException {
        IWorkEntrance pvssWorker = (IWorkEntrance) SpringBeanFactory.getBean("PvssWorker");
        assistWorkAware.runningConfig(this.getWork_plugin(), pattern.getCasenum(), "pvssHandler", this.getWork_plugins());
        return assistWorkAware.startIssueWork(pvssWorker, pattern, pointLS);
    }

    public MonitorMuster.Plugin getWork_plugin() {
        return work_plugin.get();
    }

    public List<MonitorMuster.Plugin> getWork_plugins() {
        return this.work_plugins;
    }
}
