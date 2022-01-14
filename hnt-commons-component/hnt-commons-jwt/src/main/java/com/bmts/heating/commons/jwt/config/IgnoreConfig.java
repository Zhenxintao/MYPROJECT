package com.bmts.heating.commons.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@PropertySource(value = "classpath:jwt.properties")
@ConfigurationProperties(prefix = "ignore")
public class IgnoreConfig {
    private String[] urls;
}
