package com.bmts.heating.grpc.cache.boot;

import com.bmts.heating.commons.heartbeat.adapter.handler.grpc.HeartBeatGrpcClientServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@EnableCaching
public class CacheLibraryApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatGrpcClientServer heartBeatGrpcClientServer;


    public static void main(String[] args) {
        SpringApplication.run(CacheLibraryApplication.class);
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