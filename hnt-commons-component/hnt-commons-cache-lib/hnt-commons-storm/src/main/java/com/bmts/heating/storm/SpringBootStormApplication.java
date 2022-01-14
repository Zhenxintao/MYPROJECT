package com.bmts.heating.storm;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication(scanBasePackages = {"com.bmts.heating"})
public class SpringBootStormApplication {
    private static ConfigurableApplicationContext context = null;

    public static synchronized void run(String... args) {
        if (context == null) {
            context = SpringApplication.run(SpringBootStormApplication.class, args);
        }
    }
}