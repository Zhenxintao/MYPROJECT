package com.bmts.heating.signalr.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@MapperScan("com.bmts.heating.commons.db.mapper")
public class SignalrApplication{
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SignalrApplication.class, args);
    }
}
