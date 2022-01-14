package com.bmts.heating.elasticsearch.boot.app;

import com.bmts.heating.commons.heartbeat.adapter.handler.grpc.HeartBeatGrpcClientServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class ElasticsearchServiceApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    private HeartBeatGrpcClientServer heartBeatGrpcClientServer;

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchServiceApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(ElasticsearchServiceApplication.class);
    }

    /**
     * 启动心跳
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {
        heartBeatGrpcClientServer.start();
    }
}
