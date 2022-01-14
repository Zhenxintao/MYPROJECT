package com.bmts.heating.web.balance.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author naming
 * @description
 * @date 2021/1/29 10:39
 **/
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@Astrict(servicename = {"application_netbalance","bussiness_baseInfomation","gather_search"}, servicetype = Astrict.EnumService.application)
public class BalanceApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    public static void main(String[] args) {
        SpringApplication.run(BalanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }
}
