package com.bmts.heating.web.backStage.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableSwaggerBootstrapUI //启用swagger-bootstrap-ui
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@Astrict(servicename = {"bussiness_baseInfomation"}, servicetype = Astrict.EnumService.application)
public class BackStageApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    public static void main(String[] args) {
        SpringApplication.run(BackStageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();
    }

}
