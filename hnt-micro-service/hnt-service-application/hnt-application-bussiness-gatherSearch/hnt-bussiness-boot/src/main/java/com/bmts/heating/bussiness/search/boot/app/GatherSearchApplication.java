package com.bmts.heating.bussiness.search.boot.app;

import com.bmts.heating.commons.heartbeat.adapter.handler.application.HeartBeatApplicationClientServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;


@Description("综合数据查询中台服务")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@MapperScan("com.bmts.heating.commons.db.mapper")
public class GatherSearchApplication  implements CommandLineRunner{

    @Autowired
    private HeartBeatApplicationClientServer heartBeatApplicationClientServer;


    public static void main(String[] args) {
        SpringApplication.run(GatherSearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatApplicationClientServer.start();
    }

}
