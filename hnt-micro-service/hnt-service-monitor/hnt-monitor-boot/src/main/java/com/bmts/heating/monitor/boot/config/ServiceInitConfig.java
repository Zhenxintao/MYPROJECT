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
    @ApiParam("??????????????????")
    private String service_host;


    /**
     * ????????????????????????????????????bean
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
     * ????????????????????????monitor????????????patterns??????bean
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
     * ????????????????????????monitor??????????????????(ex:monitor1)
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
     * ?????????????????????application_id
     *
     * @return
     */
    @Bean("application_id")
    public Long applicationId() {
        return this.getApplication_id();
    }

    /**
     * ?????????????????????application_name
     *
     * @return
     */
    @Bean("application_name")
    public String applicationName() {
        return this.getApplication_name();
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @Bean("issue_queue")
    public String getIssue_queue() {
        return monitorType.getIssue_queue();
    }

    /**
     * ????????????????????????monitor??????status??????
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
     * ?????????????????????????????????(master???slave)
     *
     * @return
     */
    @Bean("identity_status")
    public String getServerIdentity() {
        String identity = monitorType.getIdentity();
        return identity;
    }

    /**
     * ??????????????????????????????????????????(send_queue_name)????????????
     *
     * @return
     */
    @Bean("send_queue_name")
    public String getQueueSend() {
        String queue_name = monitorType.getSend_queue_name();
        return queue_name;
    }

    /**
     * ???????????????????????????????????????(monitor_queue_name)????????????
     *
     * @return
     */
    @Bean("monitor_queue_name")
    public String getQueueMonitor() {
        String queue_name = monitorType.getMonitor_queue_name();
        return queue_name;
    }

    /**
     * ??????????????????????????????????????????(queue_name)????????????
     *
     * @return
     */
    @Bean("topic_name")
    public String getTopic() {
        String topic_name = monitorType.getTopic_name();
        return topic_name;
    }

    /**
     * ???????????????????????????bean
     *
     * @return
     */
    @Bean(value = "monitor_local_heart_message")
    @Description("?????????monitor??????????????????????????????")
    public Monitor_Heart_Message initHeartBeatInfo() {
        Monitor_Heart_Message hm = new Monitor_Heart_Message();
        hm.setApplication_id(this.getApplication_id()); //????????????id
        hm.setApplication_name(this.getApplication_name()); //????????????
        hm.setRedundant_id(this.getRedundant_id()); //????????????id
        hm.setOwen_status("1"); //???????????????????????????????????????1
        hm.setRedundant_status("1");    //???????????????????????????????????????1
        hm.setIdentity(monitorType.getIdentity());  //??????????????????(?????????)
        hm.setMonitor_queue_name(monitorType.getMonitor_queue_name()); //????????????????????????
//        try {
//            hm.setHost(InetAddress.getLocalHost().toString());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        hm.setHost(this.service_host);
        hm.setPort(this.getPort()); //????????????port
        hm.setServer_code(Constants.ServerType.MONITOR);    //??????????????????
        String statusPath = SpringBeanFactory.getBean("statusPath", String.class);
        hm.setRegister_url(statusPath); //?????????consul????????????????????????url:/root/monitor/monitor1/master/status
        String applicationPath = SpringBeanFactory.getBean("applicationPath", String.class);
        hm.setApplication_url(applicationPath);
        //grpc??????
        if (grpcModel.getOpen() == 1) {
            hm.setOpen(1);
            hm.setGrpc_server_host(grpcModel.getHost());
            hm.setGrpc_server_port(grpcModel.getPort());
            hm.setGrpc_server_name(grpcModel.getName());
            hm.setTokens(grpcModel.getTokens());
            //?????????????????????
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
    @Description("??????????????????????????????????????????")
    public void initServiceToGovern() {
        //???????????????????????????????????????????????????consul
        LOGGER.info("???????????????{}????????????......................", monitorType.getIdentity());

        String identity = monitorType.getIdentity();
        StringBuilder stringBuilder = new StringBuilder(governConfig.getMonitor_url());
        String application_path = stringBuilder.append("/").append(application_name).toString();  //????????????

        //????????????application???????????????identity??????????????????
        String deletePath = application_path + "/" + identity;
        governCenterAdapter.removeFromConsul(deletePath);

        //???????????????????????????(lock_key)
        boolean result = governCenterAdapter.grabCreateMainKey(application_path + "/lock_key", monitorType.getIdentity());

        stringBuilder.append("/").append(identity);
        //??????patterns
        governCenterAdapter.registerServiceToCenter(stringBuilder.toString() + "/patterns", "", new Long(0));
        //????????????id
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
                            pl.setModel_status(1);  //????????????????????????????????????
                            //?????????????????????????????????monitorsession???
                            SessionMonitorHolder.saveSession(pl.getModel_host(), pl);
                        }
                        governCenterAdapter.registerServiceToCenter(filepath, JSON.toJSONString(pl), new Long(1));
                    }
                }
            }
        }
        LOGGER.info("???????????????????????????........................");
    }
}
