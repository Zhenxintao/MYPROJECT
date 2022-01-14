package com.bmts.heating.monitor.dirver.adapter;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ConstructorsAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConstructorsAware.class);
    private HandlerGenerator handlerGenerator;



    /**
     * 采集
     * @param handlerGenerator
     * @param monitorProtery
     */
    public void startConstructorGahterService(HandlerGenerator handlerGenerator, MonitorProtery monitorProtery){
        this.handlerGenerator=handlerGenerator;

        LOGGER.info("初始化本次{}数据采集任务,数量:{}..........",monitorProtery.getPattern().getModel(),monitorProtery.getPluginList().size());
        LOGGER.info("读取配置............");
        LOGGER.info("配置采集线程............");

        int test_count=1;
        for(MonitorMuster.Plugin plugin: monitorProtery.getPluginList()){
            handlerGenerator.setWork_plugin(plugin);
            handlerGenerator.setWork_pluginList(monitorProtery.getPluginList());
            handlerGenerator.workGahter(monitorProtery.getPattern());  //多个线程执行一个实例
            try {
                //延迟开启新任务
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test_count++;
        }
    }

    /**
     * 下发
     * @param handlerGenerator
     * @param plugin
     */
    public List<PointL> startConstructorIssueService(HandlerGenerator handlerGenerator, MonitorType.Pattern pattern, MonitorMuster.Plugin plugin,List<MonitorMuster.Plugin> plugins, List<PointL> pointLS) throws ExecutionException, InterruptedException {
        handlerGenerator.setWork_plugin(plugin);
        handlerGenerator.setWork_pluginList(plugins);
        return handlerGenerator.workIssue(pattern,pointLS);
    }
}
