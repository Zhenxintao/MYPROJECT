package com.bmts.heating.monitor.boot.config;

import com.alibaba.fastjson.JSON;
import com.bmts.heating.commons.heartbeat.pojo.Constants;
import com.bmts.heating.commons.heartbeat.pojo.Monitor_Heart_Message;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.distribution.config.GovernConfig;
import com.bmts.heating.monitor.dirver.common.SessionMonitorHolder;
import com.bmts.heating.monitor.dirver.config.GrpcModel;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.dirver.config.MonitorType;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Order(1)
public class ServiceInitConfig {

    private Logger LOGGER = LoggerFactory.getLogger(ServiceInitConfig.class);

    @Autowired
    private MonitorType monitorType;
    @Autowired
    private MonitorMuster monitorMuster;
    @Autowired
    @Qualifier("monitorCenterAdapter")
    private DistributionCenterAdapter governCenterAdapter;
    @Autowired
    private GovernConfig governConfig;
    @Autowired
    private GrpcModel grpcModel;

    @Getter
    @Setter
    @Value("${spring.application.name}")
    private String application_name;
    @Getter
    @Setter
    @Value("${spring.application.id}")
    private Long application_id;
    @Getter
    @Setter
    @Value("${spring.application.redundant_id}")
    private Long redundant_id;
    @Getter
    @Setter
    @Value("${server.port}")
    private Long port;

    @Getter
    @Setter
    @Value("${grpc.server.host}")
    @ApiParam("本地服务地址")
    private String service_host;


    /**
     * 根据配置文件装载任务实例bean
     *
     * @return
     */
    @Bean("MonitorProtery_list")
    public List<MonitorProtery> initMonitorProtery() {
        List<MonitorType.Pattern> patterns = monitorType.getPatterns();
        List<MonitorMuster.Plugin> plugins = monitorMuster.getPlugins();

        List<MonitorProtery> mplist = new ArrayList<MonitorProtery>();
        for (MonitorType.Pattern p : patterns) {
            MonitorProtery monitorProtery = new MonitorProtery();
            monitorProtery.setPattern(p);
            for (MonitorMuster.Plugin plugin : plugins) {
                if (p.getModel().equals(plugin.getModel())) {
                    monitorProtery.getPluginList().add(plugin);
                }
            }
            mplist.add(monitorProtery);
        }
        return mplist;
    }

    /**
     * 初始化当前服务的monitor采集实例patterns路径bean
     *
     * @return
     */
    @Bean("monitorPath")
    public String monitorPath() {
        String identity = monitorType.getIdentity();
        StringBuilder stringBuilder = new StringBuilder(governConfig.getMonitor_url());
        stringBuilder.append("/").append(application_name).append("/").append(identity).append("/patterns");
        return stringBuilder.toString();
    }

    /**
     * 初始化当前服务的monitor服务实例路径(ex:monitor1)
     *
     * @return
     */
    @Bean("applicationPath")
    public String applicationPath() {
        String identity = monitorType.getIdentity();
        StringBuilder stringBuilder = new StringBuilder(governConfig.getMonitor_url());
        stringBuilder.append("/").append(application_name);
        return stringBuilder.toString();
    }

    /**
     * 初始化当前服务application_id
     *
     * @return
     */
    @Bean("application_id")
    public Long applicationId() {
        return this.getApplication_id();
    }

    /**
     * 初始化当前服务application_name
     *
     * @return
     */
    @Bean("application_name")
    public String applicationName() {
        return this.getApplication_name();
    }

    /**
     * 初始化监控任务下发队列
     *
     * @return
     */
    @Bean("issue_queue")
    public String getIssue_queue() {
        return monitorType.getIssue_queue();
    }

    /**
     * 初始化当前服务的monitor状态status路径
     *
     * @return
     */
    @Bean("statusPath")
    public String monitorStatus() {
        StringBuilder stringBuilder = new StringBuilder(governConfig.getMonitor_url());
        stringBuilder.append("/").append(application_name).append("/").append(monitorType.getIdentity()).append("/status");
        return stringBuilder.toString();
    }

    /**
     * 初始化当前服务节点状态(master、slave)
     *
     * @return
     */
    @Bean("identity_status")
    public String getServerIdentity() {
        String identity = monitorType.getIdentity();
        return identity;
    }

    /**
     * 初始化当前服务所投递消息队列(send_queue_name)队列模式
     *
     * @return
     */
    @Bean("send_queue_name")
    public String getQueueSend() {
        String queue_name = monitorType.getSend_queue_name();
        return queue_name;
    }

    /**
     * 初始化当前服务监控消息队列(monitor_queue_name)队列模式
     *
     * @return
     */
    @Bean("monitor_queue_name")
    public String getQueueMonitor() {
        String queue_name = monitorType.getMonitor_queue_name();
        return queue_name;
    }

    /**
     * 初始化当前服务所使用消息队列(queue_name)主题模式
     *
     * @return
     */
    @Bean("topic_name")
    public String getTopic() {
        String topic_name = monitorType.getTopic_name();
        return topic_name;
    }

    /**
     * 初始化本地心跳数据bean
     *
     * @return
     */
    @Bean(value = "monitor_local_heart_message")
    @Description("将本地monitor服务写入心跳数据包中")
    public Monitor_Heart_Message initHeartBeatInfo() {
        Monitor_Heart_Message hm = new Monitor_Heart_Message();
        hm.setApplication_id(this.getApplication_id()); //当前节点id
        hm.setApplication_name(this.getApplication_name()); //服务名称
        hm.setRedundant_id(this.getRedundant_id()); //冗余节点id
        hm.setOwen_status("1"); //当前节点状态初始化启动默认1
        hm.setRedundant_status("1");    //冗余节点状态初始化启动默认1
        hm.setIdentity(monitorType.getIdentity());  //当前节点标识(主、从)
        hm.setMonitor_queue_name(monitorType.getMonitor_queue_name()); //当前节点监控队列
//        try {
//            hm.setHost(InetAddress.getLocalHost().toString());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        hm.setHost(this.service_host);
        hm.setPort(this.getPort()); //当前节点port
        hm.setServer_code(Constants.ServerType.MONITOR);    //心跳服务类型
        String statusPath = SpringBeanFactory.getBean("statusPath", String.class);
        hm.setRegister_url(statusPath); //注册在consul上当前节点状态的url:/root/monitor/monitor1/master/status
        String applicationPath = SpringBeanFactory.getBean("applicationPath", String.class);
        hm.setApplication_url(applicationPath);
        //grpc信息
        if (grpcModel.getOpen() == 1) {
            hm.setOpen(1);
            hm.setGrpc_server_host(grpcModel.getHost());
            hm.setGrpc_server_port(grpcModel.getPort());
            hm.setGrpc_server_name(grpcModel.getName());
            hm.setTokens(grpcModel.getTokens());
            //装配连接池信息
//            hm.setMaxIdle(grpcModel.getMaxIdle());
//            hm.setMaxTotal(grpcModel.getMaxTotal());
//            hm.setMinIdle(grpcModel.getMinIdle());
//            hm.setMaxWaitMillis(grpcModel.getMaxWaitMillis());
//            hm.setLifo(grpcModel.isLifo());
//            hm.setBlockWhenExhausted(grpcModel.isBlockWhenExhausted());
        } else {
            hm.setOpen(0);
        }
        return hm;
    }

    @Bean
    @Description("初始化服务信息到服务治理中心")
    public void initServiceToGovern() {
        //启动注册当前服务信息到服务治理中心consul
        LOGGER.info("注册初始化{}监控服务......................", monitorType.getIdentity());

        String identity = monitorType.getIdentity();
        StringBuilder stringBuilder = new StringBuilder(governConfig.getMonitor_url());
        String application_path = stringBuilder.append("/").append(application_name).toString();  //应用路径

        //删除当前application应用中所有identity下的所有节点
        String deletePath = application_path + "/" + identity;
        governCenterAdapter.removeFromConsul(deletePath);

        //抢占创建采集主节点(lock_key)
        boolean result = governCenterAdapter.grabCreateMainKey(application_path + "/lock_key", monitorType.getIdentity());

        stringBuilder.append("/").append(identity);
        //注册patterns
        governCenterAdapter.registerServiceToCenter(stringBuilder.toString() + "/patterns", "", new Long(0));
        //注册状态id
        governCenterAdapter.registerServiceToCenter(stringBuilder.toString() + "/status", "1", new Long(1));

        String monitor_path = stringBuilder.toString() + "/patterns";
        List<MonitorProtery> mplist = (List<MonitorProtery>) SpringBeanFactory.getBean("MonitorProtery_list");
        List<MonitorType.Pattern> patterns = monitorType.getPatterns();
        for (MonitorType.Pattern p : patterns) {
            String path = monitor_path + "/" + p.getModel();
            governCenterAdapter.registerServiceToCenter(path, "", new Long(0));

            for (MonitorProtery mp : mplist) {
                if (mp.getPattern().getModel().equals(p.getModel())) {
                    List<MonitorMuster.Plugin> plist = mp.getPluginList();
                    for (MonitorMuster.Plugin pl : plist) {
                        //String filepath=path+"/"+pl.getModel_host();
                        String filepath = path + "/" + pl.getDevice_id();
                        if (result) {
                            pl.setModel_status(1);  //采集主节点下启动所有任务
                            //可采集状态的实例注册到monitorsession中
                            SessionMonitorHolder.saveSession(pl.getModel_host(), pl);
                        }
                        governCenterAdapter.registerServiceToCenter(filepath, JSON.toJSONString(pl), new Long(1));
                    }
                }
            }
        }
        LOGGER.info("监控服务初始化完毕........................");
    }
}
