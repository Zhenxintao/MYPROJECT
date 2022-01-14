package com.bmts.heating.grpc.pattern.boot;

import com.bmts.heating.commons.heartbeat.adapter.handler.grpc.HeartBeatGrpcClientServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class PatternServiceApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatGrpcClientServer heartBeatGrpcClientServer;

    public static void main(String[] args) {
        SpringApplication.run(PatternServiceApplication.class, args);
    }

    /**
     * 启动心跳
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        heartBeatGrpcClientServer.start();
    }
}
