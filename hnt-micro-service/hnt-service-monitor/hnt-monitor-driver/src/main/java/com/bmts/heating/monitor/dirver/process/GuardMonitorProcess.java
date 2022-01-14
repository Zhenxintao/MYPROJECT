package com.bmts.heating.monitor.dirver.process;

import com.alibaba.fastjson.JSON;
import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.colony.pojo.RoutePolicy;
import com.bmts.heating.commons.mq.kafka.adapter.*;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.distribution.adapter.MonitorCenterAdapter;
import com.bmts.heating.monitor.dirver.adapter.ConstructionGenerator;
import com.bmts.heating.monitor.dirver.adapter.ConstructorsAware;
import com.bmts.heating.monitor.dirver.adapter.HandlerGenerator;
import com.bmts.heating.monitor.dirver.common.MonitorTaskUtils;
import com.bmts.heating.monitor.dirver.common.SessionMonitorHolder;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Description("守护线程,监控monitor消息队列获取冗余服务器投递的任务")
public class GuardMonitorProcess implements RetracementHandle {

    private Logger LOGGER = LoggerFactory.getLogger(GuardMonitorProcess.class);

    @Autowired
    private RouteAdapter routeAdapter;
    @Autowired
    private ConstructorsAware constructorsAware;
    @Autowired
    private MonitorTaskUtils monitorTaskUtils;

    public void consume(){
        ConsumerOrder order = new ConsumerOrder();
        order.setPolicy(ConsumerPolicy.ConsumeType.single);
        //创建目标(监控队列)
        String monitor_queue_name= SpringBeanFactory.getBean("monitor_queue_name").toString();
        order.setTopicName(monitor_queue_name);
        order.setGroup_id(monitor_queue_name);
        try{
            //根据路由获取kafkamanager
            RoutePolicy routePolicy = new RoutePolicy();
            routePolicy.setType(RoutePolicy.Type.KAFKA);
            routePolicy.setDataSet("all");
            KafkaManager kafkaManager=routeAdapter.getManager(routePolicy);
            kafkaManager.consumeDatas(order,this);
        }catch (KafkaException e){
            LOGGER.warn("异常----------------{}",e.toString());
        }
    }

    @Override
    public void callBackPattern(Object... objects) {
        String message = objects[0].toString();
        if(null!=message&&"\"redundant_status\"".equals(message)){
            //服务器失效消息
            LOGGER.info("冗余服务器节点状态为{},已经宕机,启动所有任务实例接管..........",message);
            //修改主采集节点为当前节点
            DistributionCenterAdapter governCenterAdapter=(MonitorCenterAdapter) SpringBeanFactory.getBean("monitorCenterAdapter");
            boolean result=governCenterAdapter.grabCreateMainKey(SpringBeanFactory.getBean("applicationPath")+"/lock_key", SpringBeanFactory.getBean("identity_status").toString());
            //获取当前不采集实例,全部启动,接管所有采集实例任务
            List<MonitorProtery> mlist=monitorTaskUtils.getBanRunMonitorProtery();
            for(MonitorProtery mp :mlist){
                List<MonitorMuster.Plugin> list = mp.getPluginList();
                for(MonitorMuster.Plugin pl: list){
                    //可采集状态的实例注册到monitorsession中
                    SessionMonitorHolder.saveSession(pl.getModel_host(),pl);
                }
                //启动处理插件
                ConstructionGenerator constructionGenerator = (ConstructionGenerator) SpringBeanFactory.getBean(mp.getPattern().getModel());
                constructionGenerator.init(mp);
            }
        }else{
            //任务失效消息
            Gson gson = new Gson();
            Monitor_Message mm = gson.fromJson(message,Monitor_Message.class);
            LOGGER.info("获得消息：==========================={}",mm.getPlugin().getModel_host());
            //生成采集任务
            MonitorProtery monitorProtery= new MonitorProtery();
            monitorProtery.setPattern(mm.getPattern());
            MonitorMuster.Plugin plug=mm.getPlugin();
            List<MonitorMuster.Plugin> pluginList= new ArrayList<MonitorMuster.Plugin>();
            //加入采集队列
            pluginList.add(plug);
            //可采集状态的实例注册到monitorsession中
            SessionMonitorHolder.saveSession(plug.getModel_host(),mm.getPlugin());
            //修改consul任务状态为有效
            plug.setModel_status(1);
            //GovernCenter governCenter=SpringBeanFactory.getBean(GovernCenter.class);
            DistributionCenterAdapter governCenterAdapter=(MonitorCenterAdapter) SpringBeanFactory.getBean("monitorCenterAdapter");

            //修改当前节点采集该服务注册在consul的状态信息(model_status:1->0)
            String path= SpringBeanFactory.getBean("monitorPath").toString();
            //获得路径
            path+="/"+mm.getPattern().getModel()+"/"+plug.getDevice_id();
            //governCenter.registerToEtcdByPath(path,plug ,1);
            governCenterAdapter.registerServiceToCenter(path,JSON.toJSONString(plug),new Long(1));

            monitorProtery.setPluginList(pluginList);
            //获取处理handler
            HandlerGenerator handlerGenerator=(HandlerGenerator) SpringBeanFactory.getBean(mm.getHandler());
            //启动采集进程
            constructorsAware.startConstructorGahterService(handlerGenerator,monitorProtery);
        }
    }
}
