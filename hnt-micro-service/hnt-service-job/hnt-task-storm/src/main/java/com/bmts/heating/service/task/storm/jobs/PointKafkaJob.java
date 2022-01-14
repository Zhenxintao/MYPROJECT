package com.bmts.heating.service.task.storm.jobs;

import com.bmts.heating.service.task.storm.service.KafkaService;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("point_real_kafka")
public class PointKafkaJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(PointKafkaJob.class);
    @Autowired
    private KafkaService kafkaService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long start = System.currentTimeMillis();
        System.out.println("---start ----" + start);
        kafkaService.queryPointList(start);
        long end = System.currentTimeMillis();
        System.out.println("---end ----" + end);
        System.out.println("-------------" + (System.currentTimeMillis() - start));
        logger.info("---完成定时读取实时库！---");

    }
}
