package com.bmts.heating.monitor.plugins.pvss.constructors;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.commons.mq.kafka.adapter.RetracementHandle;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.adapter.ConstructionGenerator;
import com.bmts.heating.monitor.dirver.adapter.ConstructorsAware;
import com.bmts.heating.monitor.dirver.adapter.HandlerGenerator;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.plugins.pvss.issue.PvssWorkAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component("PVSS")
public class PvssConstructors implements ConstructionGenerator, RetracementHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(PvssConstructors.class);
    private MonitorProtery monitorProtery;

    @Autowired
    @Qualifier("pvssHandler")
    private HandlerGenerator pvssHandler;

    @Autowired
    private ConstructorsAware constructorsAware;


    @Autowired
    private RouteAdapter routeAdapter;

    /**
     * 采集任务
     */
    @Override
    public void workGather() {
        //启动采集任务
        constructorsAware.startConstructorGahterService(pvssHandler, monitorProtery);
    }

    @Override
    public void init(MonitorProtery monitorProtery) {
        this.monitorProtery = monitorProtery;
        this.workGather();    //采集任务
//        this.workIssue();       //监控队列下发任务
    }

    /**
     * 下发任务
     */
    @Override
    public void workIssue() {
        //开启下发任务,监控下发任务队列
//        RoutePolicy routePolicy = new RoutePolicy();
//        routePolicy.setType(RoutePolicy.Type.KAFKA);
//        routePolicy.setDataSet("all");
//        try{
//            MonitorType.Pattern pattern=monitorProtery.getPattern();
//            ConsumerOrder consumerOrder= new ConsumerOrder();
//            consumerOrder.setTopicName(SpringBeanFactory.getBean("issue_queue").toString());
//            consumerOrder.setPolicy(1);
//            consumerOrder.setGroup_id(SpringBeanFactory.getBean("application_name")+pattern.getModel());
//            consumerOrder.setApplication_num(1);
//            KafkaManager kafkaManager= routeAdapter.getManager(routePolicy);
//            kafkaManager.consumeDatas(consumerOrder,this);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    public void callBackPattern(Object... objects) {
//        MonitorType.Pattern pattern=monitorProtery.getPattern();
//        List<MonitorMuster.Plugin> plugins = monitorProtery.getPluginList();
//        LOGGER.info("{}接收到下发任务消息,开启下发工作..........{}",pattern.getModel(),objects.toString());
//        //判断是否包含当前model（PVSS、JK）任务
//        MonitorMuster.Plugin plugin=null;
//        List<PointL> pointLS= null;
//
//        //启动下发任务
//        constructorsAware.startConstructorIssueService(pvssHandler,pattern,plugin,plugins,pointLS);
    }

    /**
     * 手动下发任务
     */
    @Override
    public List<PointL> handIssue(Object... objects) throws ExecutionException, InterruptedException {
        MonitorType.Pattern pattern = monitorProtery.getPattern();
        List<MonitorMuster.Plugin> plugins = monitorProtery.getPluginList();
        LOGGER.info("{}接收到下发任务消息,开启下发工作..........{}", pattern.getModel(), objects.toString());
        //判断是否包含当前model（PVSS、JK）任务

        List<PointL> returnList = new ArrayList<>();
        List<PointL> pointList = (List<PointL>) objects[0];
        // 根据pointList 的device_id 进行分组匹配
        Map<String, List<PointL>> groupMap = pointList.stream().collect(Collectors.groupingBy(e -> e.getDeviceId()));
        for (MonitorMuster.Plugin plug : plugins) {
            List<PointL> pointLS = groupMap.get(plug.getDevice_id());
            if (!CollectionUtils.isEmpty(pointLS)) {

                // 异步下发任务
                // List<PointL> pointLS1 = constructorsAware.startConstructorIssueService(pvssHandler, pattern, plug, plugins, pointLS);

                // 同步下发任务
                List<PointL> pointReturnList = PvssWorkAware.issueWorker(plug, pointLS);
                returnList.addAll(pointReturnList);
            }
        }
        return returnList;
    }
}
