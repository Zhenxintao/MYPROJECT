package com.bmts.heating.monitor.second.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = {"classpath:topics.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "topics")
public class TopicsConfig {

    // 二网设备信息
    private DeviceInfo device_info;

    // 二网设备故障
    private DeviceFault device_fault;


    @Data
    public static class DeviceInfo {

        private String topicName;

        private String group_id;

        private int policy;

        private int application_num;

    }

    @Data
    public static class DeviceFault {

        private String topicName;

        private String group_id;

        private int policy;

        private int application_num;
    }

}
