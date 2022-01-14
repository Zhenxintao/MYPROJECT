package com.bmts.heating.monitor.dirver.adapter;

import com.bmts.heating.colony.adapter.RouteAdapter;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.utils.compute.CountUtils;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.common.ReloadComputation;
import com.bmts.heating.monitor.dirver.common.SimpleMailUtil;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import com.bmts.heating.monitor.dirver.handler.CallWorkEntranceImpl;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import com.bmts.heating.monitor.dirver.handler.RunWorkEntranceImpl;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class AssistWorkAware {

    private Logger LOGGER = LoggerFactory.getLogger(AssistWorkAware.class);

    private ThreadLocal<MonitorMuster.Plugin> work_plugin = new ThreadLocal<MonitorMuster.Plugin>();  //多个线程执行一个实例,并发不安全
    @Setter
    @Getter
    private List<MonitorMuster.Plugin> pluginList;

    private ThreadLocal<Integer> count = new ThreadLocal<Integer>();
    private ThreadLocal<String> handler = new ThreadLocal<String>();

    @Autowired
    private RouteAdapter routeAdapter;
//    @Autowired
//    private KafkaManager kafkaManager;

    @Autowired
    private ReloadComputation reloadComputation;

    @Autowired
    private WebPageConfigService webPageConfigService;

    @Autowired
    private SimpleMailUtil simpleMailUtil;

    public void runningConfig(MonitorMuster.Plugin plugin, int casenum, String handler, List<MonitorMuster.Plugin> pluginList) {
        count.set(casenum);
        this.setWork_plugin(plugin);
        this.handler.set(handler);
        this.setPluginList(pluginList);
    }

    /**
     * 启动采集任务
     *
     * @param worker           采集线程
     * @param pattern          任务属性
     * @param handlerGenerator 回调接口
     */
    public void startGaterWork(IWorkEntrance worker, MonitorType.Pattern pattern, HandlerGenerator handlerGenerator) {
        //try {
        // 添加线程信息
        // reloadComputation.setThreadMap(Thread.currentThread().getName(), Thread.currentThread());

        int loop = count.get();
        MonitorMuster.Plugin plugin = this.getWork_plugin();
        long cycle_time = plugin.getCycle_time(); //获得采集周期

        //加载点表任务
        // List<List<PointL>> plist = this.getTask(plugin, loop, handlerGenerator);
        while (true) {
            LOGGER.info("本次采集开启子线程数:{}", loop);
            //循环周期执行
            List<List<PointL>> pointLS = reloadComputation.getMonitorMap(plugin.getDevice_id());
            if (!CollectionUtils.isEmpty(pointLS)) {
                // 进行分组
                List<List<PointL>> plist = CountUtils.subListToBySegment(pointLS, loop);

                long start_now = System.currentTimeMillis();//采集开始时间
                CountDownLatch countDownLatch = new CountDownLatch(loop);
                worker.config(countDownLatch, 0, plugin, this.getPluginList());//配置工作线程
                LOGGER.info("启动{}{}采集任务.....................", pattern.getModel(), plugin.getModel_host());


                //启动处理任务线程,为每个线程平均分配任务
                ThreadPoolTaskExecutor asyncWorkerExecutor = (ThreadPoolTaskExecutor) SpringBeanFactory.getBean("asyncWorkerExecutor");
                for (int i = 0; i < loop; i++) {
                    //每个父线程启动多个子线程执行任务,多个子线程运行同一个worker实例处理任务
                    Thread t = new Thread(new RunWorkEntranceImpl(worker, plist.get(i), 0), plugin.getModel_host() + "-" + i);
                    LOGGER.info("开启新子线程<><><><><><><><><><><><>");
                    asyncWorkerExecutor.execute(t);
                }
                try {
                    countDownLatch.await(); //主线程阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("{}父线程执行任务:{}{}完毕", Thread.currentThread().getName(), this.getWork_plugin().getModel(), this.getWork_plugin().getModel_host());
                if (cycle_time != 0) {
                    //判断所有worker执行完所需时间是否超过循环采集周期
                    long over_time = System.currentTimeMillis();  //采集结束时间
                    long use_time = over_time - start_now;  //获取本次采集耗用时间
                    LOGGER.info("本次采集耗时:{}", use_time);
                    if (use_time < cycle_time) {
                        //线程睡眠直到到达周期
                        try {
                            Thread.sleep(cycle_time - use_time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                countDownLatch = null;

            } else {
                LOGGER.info("------{}---当前线程没有点数据---", plugin.getDevice_id());
                try {
                    Thread.sleep(cycle_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        //} catch (Exception e) {
        //    // 采集服务异常发送邮件通知
        //    String subject = "晋城采集任务异常-" + LocalDateTime.now();
        //    String text = "晋城采集服务---" + SpringBeanFactory.getBean("identity_status") + " 节点采集 "
        //        + pattern.getModel() + "：" + this.getWork_plugin().getDevice_id() + "的采集任务异常！时间戳---" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        //    WebPageConfig mailToUser = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "mailToUser"));
        //    if (mailToUser != null && StringUtils.isNotBlank(mailToUser.getJsonConfig())) {
        //              JSONArray jsonArray = JSONArray.parseArray(mailToUser.getJsonConfig());
        //      simpleMailUtil.sendSimpleMail(subject, jsonArray.toArray(new String[0]), text);
        //    }
        //    LOGGER.warn("{}节点采集{}:{}任务发生异常终止........................", SpringBeanFactory.getBean("identity_status"), pattern.getModel(), this.getWork_plugin().getModel_host());
//            //修改当前节点采集该服务注册在consul的状态信息(model_status:1->0)
//            String path= SpringBeanFactory.getBean("monitorPath").toString();
//            //获得路径
//            path+="/"+pattern.getModel()+"/"+this.getWork_plugin().getDevice_id();
//            DistributionCenterAdapter governCenterAdapter=(MonitorCenterAdapter) SpringBeanFactory.getBean("monitorCenterAdapter");
//
//            //从session中获取当前采集对象实例信息
//            MonitorMuster.Plugin plug= SessionMonitorHolder.get(this.getWork_plugin().getModel_host());
//            //修改consul任务状态为失效0
//            plug.setModel_status(0);
//            governCenterAdapter.registerServiceToCenter(path, JSON.toJSONString(plug),new Long(1));
//
//            //从session移除当前可采集对象信息
//            SessionMonitorHolder.removeSession(this.getWork_plugin().getModel_host());
//            //获取当前服务要投递队列send_queue_name
//            String send_queue_name= SpringBeanFactory.getBean("send_queue_name").toString();
//            //配置消息信息
//            Monitor_Message monitor_Message= (Monitor_Message) SpringBeanFactory.getBean("Monitor_Message");
//            monitor_Message.setIdentity(SpringBeanFactory.getBean("identity_status").toString());
//            monitor_Message.setPlugin(plug);
//            monitor_Message.setPattern(pattern);
//            monitor_Message.setHandler(this.handler.get());
//            //发送消息
//            try {
//                RoutePolicy routePolicy = new RoutePolicy();
//                routePolicy.setType(RoutePolicy.Type.KAFKA);
//                routePolicy.setDataSet("all");
//                KafkaManager kafkaManager=routeAdapter.getManager(routePolicy);
//                kafkaManager.produceDatasBySingleton(send_queue_name,0,monitor_Message);
//            } catch (KafkaException ex) {
//                LOGGER.warn("{}",ex.toString());
//            }
        //}
    }

    /**
     * 回调从缓存中获取当前线程执行任务
     *
     * @param plugin
     * @param loop
     * @param handlerGenerator
     * @return
     */
    public List<List<PointL>> getTask(MonitorMuster.Plugin plugin, int loop, HandlerGenerator handlerGenerator) {
        List<List<PointL>> pointLS = handlerGenerator.getTaskForCache(plugin, loop);
        //根据线程数拆分任务
//        List<List<PointL>> plist = CountUtils.subListBySegment(pointLS, loop);
        List<List<PointL>> plist = CountUtils.subListToBySegment(pointLS, loop);
        return plist;
    }

    /**
     * 启动下发任务
     *
     * @param worker  下发线程
     * @param pattern 任务属性
     */
    public List<PointL> startIssueWork(IWorkEntrance worker, MonitorType.Pattern pattern, List<PointL> pointLS) throws ExecutionException, InterruptedException {
        MonitorMuster.Plugin plugin = this.getWork_plugin();
        int loop = count.get();

        //配置下发返回结果集合
        List<PointL> resultList = new ArrayList<PointL>();
        //根据线程拆分点表
        List<List<PointL>> plist = CountUtils.subListBySegment(pointLS, loop);
        //启动处理任务线程,为每个线程平均分配任务
        ThreadPoolTaskExecutor asyncWorkerExecutor = (ThreadPoolTaskExecutor) SpringBeanFactory.getBean("asyncWorkerExecutor");
        CountDownLatch countDownLatch = new CountDownLatch(loop);
        worker.config(countDownLatch, 0, plugin, this.getPluginList()); //配置工作线程
        LOGGER.info("启动{}{}下发任务.....................", pattern.getModel(), plugin.getModel_host());
        for (int i = 0; i < loop; i++) {
            //Thread t = new Thread(new CallWorkEntranceImpl(worker,plist.get(i),1),plugin.getModel_host()+"-"+i);
            Callable call = new CallWorkEntranceImpl(worker, plist.get(i), 1);
            Future future = asyncWorkerExecutor.submit(call);
            List<PointL> res = (List<PointL>) future.get();
            resultList.addAll(res);
        }
        try {
            countDownLatch.await(); //主线程阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("{}父线程执行任务:{}{}完毕", Thread.currentThread().getName(), this.getWork_plugin().getModel(), this.getWork_plugin().getModel_host());
        return resultList;
    }


    public void setWork_plugin(MonitorMuster.Plugin plugin) {
        work_plugin.set(plugin);
    }

    public MonitorMuster.Plugin getWork_plugin() {
        return work_plugin.get();
    }


}
