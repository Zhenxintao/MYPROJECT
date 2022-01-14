package com.bmts.heating.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:swagger.properties" )
@ConfigurationProperties(prefix = "swagger",ignoreUnknownFields = false)
public class DocConfig {

    //@Value("${title}")
    private String title; //文档标题
    private String description; //文档描述
    private String version; //版本
}
