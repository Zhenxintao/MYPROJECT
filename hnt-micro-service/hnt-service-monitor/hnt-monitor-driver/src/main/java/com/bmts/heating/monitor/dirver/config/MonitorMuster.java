package com.bmts.heating.monitor.dirver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix="driver")
public class MonitorMuster {
    private List<Plugin> plugins = new ArrayList<Plugin>();

    @Data
    public static class Plugin implements Serializable {
        private String model;   //设备类型
        private String device_id;   //设备ID
        private String model_host;  //ip地址
        private int model_port;     //端口
        private String model_url;   //采集地址url（pvss）
        private String issue_url;   //下发地址url(pvss)
        private int model_count;
        private int model_status;
        private String pointls_sign;    //对应点表标识
        private long cycle_time;    //循环采集周期(ms)
        private int colony_policy;  //集群策略
    }
}
