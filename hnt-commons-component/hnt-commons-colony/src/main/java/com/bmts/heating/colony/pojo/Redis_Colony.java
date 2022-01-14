package com.bmts.heating.colony.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix="colony.redis")
public class Redis_Colony {

    private List<Rnode> clusters = new ArrayList<Rnode>();

    @Data
    public static class Rnode{
        private String rcolony_id;
        private String[] rcolony_ips;
        private boolean open;   //是否开启分布式集群功能
        private String[] datasets;  //数据分布集合
    }
}
