package com.bmts.heating.grpc.dataCleaning.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;


@Data
@Component
@Description("Kafka 任务配置信息")
@ConfigurationProperties(prefix = "topics")
public class DataKafKaConfig {

    // 下发任务队列
    private DataIssue dataIssue;

    // 生产任务队列
    private DataProduce dataProduce;

    // 监控任务队列
    private DataCenter dataCenter;

    @Data
    public static class DataIssue {

        private String topicName;

        private String group_id;

        private int policy;

        private int application_num;

    }

    @Data
    public static class DataProduce {

        private String topicName;

        private String group_id;

        private int policy;

        private int application_num;
    }

    @Data
    public static class DataCenter {

        private String topicName;

        private String group_id;

        private int policy;

        private int application_num;
    }
}
