package com.bmts.heating.cache.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName ExecutorConfig
 * @Description TODO
 * @Author naming
 * @Date 2020/11/18 19:24
 **/
@Configuration
public class ExecutorConfig {
    @Bean
    public ThreadPoolTaskExecutor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(32);
        executor.setMaxPoolSize(32);
        executor.setThreadNamePrefix("common-executor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor realDataExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(32);
        executor.setMaxPoolSize(32);
        executor.setThreadNamePrefix("realData-executor-");
        executor.initialize();
        return executor;
    }

}
