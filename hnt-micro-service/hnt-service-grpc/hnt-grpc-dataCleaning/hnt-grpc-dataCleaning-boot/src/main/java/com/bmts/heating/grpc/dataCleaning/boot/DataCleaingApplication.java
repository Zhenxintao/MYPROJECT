package com.bmts.heating.grpc.dataCleaning.boot;

import com.bmts.heating.commons.heartbeat.adapter.handler.grpc.HeartBeatGrpcClientServer;
import com.bmts.heating.grpc.dataCleaning.annotation.Clean;
import com.bmts.heating.grpc.dataCleaning.config.SpringContext;
import com.bmts.heating.grpc.dataCleaning.enums.DataCleanType;
import com.bmts.heating.grpc.dataCleaning.service.strategy.DataCleanStrategy;
import com.bmts.heating.grpc.dataCleaning.utils.CleanUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class DataCleaingApplication implements CommandLineRunner {

    @Autowired
    private HeartBeatGrpcClientServer heartBeatGrpcClientServer;

    public static void main(String[] args) {
        SpringApplication.run(DataCleaingApplication.class, args);
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
        initCleanStrategy();
    }

    private void initCleanStrategy() {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        //实现此接口的所有类
        Map<String, DataCleanStrategy> strategyMap = applicationContext.getBeansOfType(DataCleanStrategy.class);
        strategyMap.forEach((k, v) -> {
            Clean clean = v.getClass().getAnnotation(Clean.class);
            DataCleanType dataCleanType = clean.cleanType();
            CleanUtil.addCleanStrategy(dataCleanType.type(), v);
        });
    }
}
