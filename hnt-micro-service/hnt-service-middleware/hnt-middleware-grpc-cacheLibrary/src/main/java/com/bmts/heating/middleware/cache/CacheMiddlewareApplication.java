package com.bmts.heating.middleware.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/7/14 15:36
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class CacheMiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheMiddlewareApplication.class);
    }

}
