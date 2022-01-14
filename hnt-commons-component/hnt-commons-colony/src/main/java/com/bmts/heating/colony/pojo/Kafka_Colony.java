package com.bmts.heating.colony.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@Description("Kafka分布式集群信息")
@ConfigurationProperties(prefix="colony.kafka")
public class Kafka_Colony {

    private List<Knode> knodes = new ArrayList<Knode>();

    @Data
    public static class Knode{
        private String kcolony_id;      //集群id
        private String[] kcolony_ips;   //集群ip
        private boolean open;   //是否开启分布式集群功能
        private String[] datasets;  //数据分布集合
    }
}
