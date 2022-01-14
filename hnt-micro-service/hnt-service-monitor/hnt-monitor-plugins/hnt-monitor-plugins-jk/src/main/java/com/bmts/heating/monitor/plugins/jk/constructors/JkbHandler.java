package com.bmts.heating.monitor.plugins.jk.constructors;

import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.adapter.AssistWorkAware;
import com.bmts.heating.monitor.dirver.adapter.HandlerGenerator;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class JkbHandler implements HandlerGenerator {
    private Logger LOGGER = LoggerFactory.getLogger(JkbHandler.class);
    private ThreadLocal<MonitorMuster.Plugin> work_plugin = new InheritableThreadLocal<MonitorMuster.Plugin>();  //多线程独立使用
    private List<MonitorMuster.Plugin> work_plugins;    //多线程公用

    @Autowired
    private AssistWorkAware assistWorkAware;

//    @Autowired
//    private RedisCacheService redisCacheService;

    @Setter
    @Getter
    private MonitorType.Pattern pattern;

    /**
     * 采集任务
     *
     * @param pattern
     */
    @Async("asyncMonitorExecutor")
    @Override
    public void workGahter(MonitorType.Pattern pattern) {
        //System.out.println("patterns:<><><><><><><><><>"+System.identityHashCode(guard_monitor_open));
        LOGGER.info("{}父线程开启jk任务:{}", Thread.currentThread().getName(), this.getWork_plugin().getModel_host());

        IWorkEntrance jkWorker = (IWorkEntrance) SpringBeanFactory.getBean("JkWorker");
        assistWorkAware.runningConfig(this.getWork_plugin(), pattern.getCasenum(), "jkbHandler", this.getWork_plugins());
        assistWorkAware.startGaterWork(jkWorker, pattern, this);
    }

    /**
     * 加载点表任务
     *
     * @param plugin  采集实例类型
     * @param casenum 当前实例开启线程数
     * @return
     */
    @Override
    public List<List<PointL>> getTaskForCache(MonitorMuster.Plugin plugin, int casenum) {
        List<List<PointL>> listS = new ArrayList<>();
        //模拟缓存中读取点表数据
        List<PointL> pointLS = new ArrayList<PointL>();
        PointL p1 = new PointL();
        PointL p2 = new PointL();
        PointL p3 = new PointL();
        PointL p4 = new PointL();
        PointL p5 = new PointL();

        pointLS.add(p1);
        pointLS.add(p2);
        pointLS.add(p3);
        pointLS.add(p4);
        pointLS.add(p5);

        listS.add(pointLS);
        return listS;
    }

    /**
     * 下发任务
     *
     * @param pattern
     * @param pointLS 任务列表
     */
    @Override
    public List<PointL> workIssue(MonitorType.Pattern pattern, List<PointL> pointLS) throws ExecutionException, InterruptedException {
        IWorkEntrance jkWorker = (IWorkEntrance) SpringBeanFactory.getBean("JkWorker");
        assistWorkAware.runningConfig(this.getWork_plugin(), pattern.getCasenum(), "jkbHandler", this.getWork_plugins());
        return assistWorkAware.startIssueWork(jkWorker, pattern, pointLS);
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

    public List<MonitorMuster.Plugin> getWork_plugins() {
        return this.work_plugins;
    }

    public MonitorMuster.Plugin getWork_plugin() {
        return work_plugin.get();
    }
}
