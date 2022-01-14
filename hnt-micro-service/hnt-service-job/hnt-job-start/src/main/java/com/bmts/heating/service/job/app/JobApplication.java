package com.bmts.heating.service.job.app;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.heartbeat.adapter.handler.web.HeartBeatWebClietnServer;
import com.bmts.heating.commons.redis.service.RedisEnergyPointService;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.service.task.transport.config.JobComputation;
import com.spring4all.mongodb.EnableMongoPlus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/3/17 9:47
 **/
@EnableMongoPlus
@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
@EnableScheduling
@Astrict(servicename = {"bussiness_baseInfomation", "gather_search"}, servicetype = Astrict.EnumService.application)
public class JobApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatWebClietnServer heartBeatWebClientServer;

    @Autowired
    private RedisEnergyPointService redisEnergyPointService;

    @Autowired
    private JobComputation jobComputation;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatWebClientServer.start();

        // 加载 热源的  能耗点 的实时库数据
        List<PointL> pointLS = redisEnergyPointService.cacheSourceEnery();
        jobComputation.setEneryMap(pointLS);

    }
}
