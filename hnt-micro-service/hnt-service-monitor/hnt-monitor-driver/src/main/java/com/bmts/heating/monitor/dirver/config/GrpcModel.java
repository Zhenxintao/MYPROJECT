package com.bmts.heating.monitor.dirver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="grpc.server")
public class GrpcModel {

    //@Value("{server.open}")
    private int open;
    //@Value("{server.host}")
    private String host;
    //@Value("{server.port}")
    private String port;
    //@Value("{server.name}")
    private String name;

    private Long tokens;

}
