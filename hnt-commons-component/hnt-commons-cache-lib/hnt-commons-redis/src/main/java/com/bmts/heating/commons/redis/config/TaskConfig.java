package com.bmts.heating.commons.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author naming
 * @description
 * @date 2021/3/19 13:35
 **/
@Component
public class TaskConfig {

    @Bean("getPointCacheTask")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int threadCount = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(10);//核心池大小
        executor.setMaxPoolSize(10);//最大线程数
        executor.setQueueCapacity(1000);//队列程度
        executor.setKeepAliveSeconds(1000);//线程空闲时间
        executor.setThreadNamePrefix("tsak-async");//线程前缀名称
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
        return executor;
    }
}
