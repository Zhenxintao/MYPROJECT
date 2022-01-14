package com.bmts.heating.web.forecast.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author naming
 * @description
 * @date 2021/3/17 16:39
 **/

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@Astrict(servicename = {"bussiness_baseInfomation"}, servicetype = Astrict.EnumService.application)
@MapperScan("com.bmts.heating.commons.db.mapper")
public class ForecastApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    public static void main(String[] args) {
        SpringApplication.run(ForecastApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }
}
