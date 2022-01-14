package com.bmts.heating.application.pro.app;

import com.bmts.heating.commons.heartbeat.adapter.handler.application.HeartBeatApplicationClientServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class ProApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatApplicationClientServer heartBeatApplicationClientServer;

    public static void main(String[] args) {
        SpringApplication.run(ProApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatApplicationClientServer.start();
    }
}
