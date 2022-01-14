package com.bmts.heating.bussiness.netBalance.app;

import com.bmts.heating.commons.heartbeat.adapter.handler.application.HeartBeatApplicationClientServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication( scanBasePackages = {"com.bmts.heating"})
@MapperScan("com.bmts.heating.commons.db.mapper")
@Description("全网平衡")
@EnableTransactionManagement
public class NetBalanceApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatApplicationClientServer heartBeatApplicationClientServer;

    public static void main(String[] args) {
        SpringApplication.run(NetBalanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatApplicationClientServer.start();
    }
}
