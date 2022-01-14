package com.bmts.heating.bussiness.baseInformation.app.app;


import com.bmts.heating.commons.heartbeat.adapter.handler.application.HeartBeatApplicationClientServer;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Description;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@Description("基础信息中台服务")
@EnableCaching
@EnableAsync
public class BaseInfoApplication implements CommandLineRunner {

    private Logger LOGGER = LoggerFactory.getLogger(BaseInfoApplication.class);
    @Autowired
    private HeartBeatApplicationClientServer heartBeatApplicationClientServer;


    public static void main(String[] args) {
        SpringApplication.run(BaseInfoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatApplicationClientServer.start();
    }
}

