package com.bmts.heating.monitor.dirver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix="monitor")
public class MonitorType {

//    @Value("${monitor.pattern}")
//    @ApiParam("采集运行方式")
//    private String pattern;
//
//    @Value("${monitor.model.process}")
//    @ApiParam("采集运行模式:单线程、多线程")
//    private String process;
//
//    @Value("${monitor.model.casenum}")
//    @ApiParam("线程数")
//    private int casenum;

    private List<Pattern> patterns = new ArrayList<Pattern>();

    @Data
    public static class Pattern{
        private String model;
        private String process;
        private int casenum;
    }
    private String issue_queue;     //监控下发队列
    private String identity;
    private String send_queue_name;
    private String monitor_queue_name;
    private String topic_name;
    private String status;
}
