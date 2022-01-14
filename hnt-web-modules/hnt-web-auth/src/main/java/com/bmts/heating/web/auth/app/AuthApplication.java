package com.bmts.heating.web.auth.app;


import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@MapperScan("com.bmts.heating.commons.db.mapper")
@EnableAsync
@EnableTransactionManagement
public class AuthApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }
}
