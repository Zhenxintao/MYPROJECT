package com.bmts.heating.web.netApp.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.application.HeartBeatApplicationClientServer;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( scanBasePackages = {"com.bmts.heating"})
@Astrict(servicename = {"application_pro1"}, servicetype = Astrict.EnumService.application)
public class NetAppApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;
    public static void main(String[] args) {
        SpringApplication.run(NetAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }
}
