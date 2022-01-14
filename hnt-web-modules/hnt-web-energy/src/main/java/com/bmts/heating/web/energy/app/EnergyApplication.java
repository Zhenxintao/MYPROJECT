package com.bmts.heating.web.energy.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@Astrict(servicename = {"bussiness_baseInfomation","gather_search"}, servicetype = Astrict.EnumService.application)
public class EnergyApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    public static void main(String[] args) {
        SpringApplication.run(EnergyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }

}
