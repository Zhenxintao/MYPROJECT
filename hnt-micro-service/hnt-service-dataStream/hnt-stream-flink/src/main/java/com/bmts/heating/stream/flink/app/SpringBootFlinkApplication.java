package com.bmts.heating.stream.flink.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@MapperScan("com.bmts.heating.commons.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class SpringBootFlinkApplication {
    private static ConfigurableApplicationContext context = null;

    public static synchronized void run(String... args) {
        if (context == null) {
            context = SpringApplication.run(SpringBootFlinkApplication.class, args);
        }
    }
}
