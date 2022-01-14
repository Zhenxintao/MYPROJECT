package com.bmts.heating.monitor.dirver.process;

import com.bmts.heating.commons.mq.kafka.adapter.KafkaManager;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.adapter.ConstructorsAware;
import com.bmts.heating.monitor.dirver.common.MonitorTaskUtils;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Description("守护线程,监控monitor消息队列获取冗余服务器投递的任务")
public class GuardMonitorMqProcess implements IWorkEntrance {

    private Logger LOGGER= LoggerFactory.getLogger(GuardMonitorMqProcess.class);

//    @Resource
//    private javax.jms.ConnectionFactory connectionFactory;

//    @Autowired
//    @Qualifier("jkHandler")
//    private HandlerGenerator jkHandler;

    @Autowired
    private ConstructorsAware constructorsAware;

    @Autowired
    private MonitorTaskUtils monitorTaskUtils;

    private KafkaManager kafkaManager;

    @Override
    public void invoke(List<PointL> taskArray, int flag) {
//        System.out.println("已经开启监控线程............................");
//        try {
//            Connection connection=connectionFactory.createConnection();
//            connection.start();
//            //创建会话
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            //创建目标(监控队列)
//            String monitor_queue_name= SpringBeanFactory.getBean("monitor_queue_name").toString();
//            Destination destination =  session.createQueue(monitor_queue_name);
//            MessageConsumer consumer =  session.createConsumer(destination);
//
//            consumer.setMessageListener(new MessageListener() {
//                @Override
//                public void onMessage(Message message) {
//
//                    try {
//                        Thread.sleep(2000);
//                        if(message instanceof TextMessage){
//                            TextMessage msg = (TextMessage) message;
//                            String text = msg.getText();
//                            Monitor_Message mm = JSON.parseObject(text, Monitor_Message.class);
//                            LOGGER.info("获得消息：==========================={}",mm.getPlugin().getModel_host());
//                            //生成采集任务
//                            MonitorProtery monitorProtery= new MonitorProtery();
//                            monitorProtery.setPattern(mm.getPattern());
//                            MonitorMuster.Plugin plug=mm.getPlugin();
//                            List<MonitorMuster.Plugin> pluginList= new ArrayList<MonitorMuster.Plugin>();
//                            //加入采集队列
//                            pluginList.add(plug);
//                            //可采集状态的实例注册到monitorsession中
//                            SessionMonitorHolder.saveSession(plug.getModel_host(),mm.getPlugin());
//                            //修改etcd任务状态为有效
//                            plug.setModel_status(1);
//                            //GovernCenter governCenter=SpringBeanFactory.getBean(GovernCenter.class);
//                            GovernCenterAdapter governCenterAdapter=(GovernCenterAdapter) SpringBeanFactory.getBean("monitorCenterAdapter");
//
//                            //修改当前节点采集该服务注册在etcd的状态信息(model_status:1->0)
//                            String path= SpringBeanFactory.getBean("monitorPath").toString();
//                            //获得路径
//                            path+="/"+mm.getPattern().getModel()+"/"+plug.getModel_host();
//                            //governCenter.registerToEtcdByPath(path,plug ,1);
//                            governCenterAdapter.registerServiceToCenter(path,JSON.toJSONString(plug),new Long(1));
//
//                            monitorProtery.setPluginList(pluginList);
//                            //获取处理handler
//                            HandlerGenerator handlerGenerator=(HandlerGenerator) SpringBeanFactory.getBean(mm.getHandler());
//                            //启动采集进程
//                            constructorsAware.startConstructorService(handlerGenerator,monitorProtery);
//
//                        }else if(message instanceof MapMessage){
//                            MapMessage msg = (MapMessage) message;
//                            String rstatus=msg.getString("redundant_status");
//                            LOGGER.info("冗余服务器节点状态为{},已经宕机,启动所有任务实例接管..........",rstatus);
//                            //修改主采集节点为当前节点
//                            GovernCenterAdapter governCenterAdapter=(GovernCenterAdapter) SpringBeanFactory.getBean("monitorCenterAdapter");
//                            boolean result=governCenterAdapter.grabCreateMainKey(SpringBeanFactory.getBean("applicationPath")+"/lock_key", SpringBeanFactory.getBean("identity_status").toString());
//                            //获取当前不采集实例,全部启动,接管所有采集实例任务
//                            List<MonitorProtery> mlist=monitorTaskUtils.getBanRunMonitorProtery();
//                            for(MonitorProtery mp :mlist){
//                                List<MonitorMuster.Plugin> list = mp.getPluginList();
//                                for(MonitorMuster.Plugin pl: list){
//                                    //可采集状态的实例注册到monitorsession中
//                                    SessionMonitorHolder.saveSession(pl.getModel_host(),pl);
//                                }
//                                //启动处理插件
//                                ConstructionGenerator constructionGenerator = (ConstructionGenerator) SpringBeanFactory.getBean(mp.getPattern().getModel());
//                                constructionGenerator.init(mp);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Object invokeForBack(List<PointL> taskArray, int flag) {
        return null;
    }

    @Override
    public void config(Object... objs) {}

    @Override
    public int getGather_count() {
        return 0;
    }

}
