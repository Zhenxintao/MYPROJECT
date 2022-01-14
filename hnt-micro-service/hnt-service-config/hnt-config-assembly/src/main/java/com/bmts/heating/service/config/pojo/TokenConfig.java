package com.bmts.heating.service.config.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:tokens.yml" )
@ConfigurationProperties(prefix = "tokens")
public class TokenConfig {

    @Value("${serviceType}")
    private String serviceType; //服务类型
    @Value("${increment}")
    private long increment;   //每次令牌自增数量
    @Value("${circle}")
    private long circle;   //令牌自增周期
}
