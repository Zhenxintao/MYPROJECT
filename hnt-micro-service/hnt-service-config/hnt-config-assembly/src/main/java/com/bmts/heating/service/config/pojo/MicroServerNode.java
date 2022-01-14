package com.bmts.heating.service.config.pojo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

@Data
@Component
@Description("本地微服务信息")
public class MicroServerNode {

    @Value("${micro.server.host}")
    @ApiParam("本地服务端口")
    private String service_host;

    @Value("${micro.server.port}")
    @ApiParam("本地服务地址")
    private String service_port;

    @Value("${micro.server.name}")
    @ApiParam("本地服务名称")
    private String service_name;

    @Value("${spring.application.name}")
    @ApiParam("本地应用服务名称")
    private String application_name;

    @Value("${spring.application.id}")
    @ApiParam("本地服务id")
    private Long application_id;    //客户端唯一ID

    @Value("${micro.server.tokens}")
    @ApiParam("剩余令牌数")
    private Long tokens;  //令牌数

    @ApiParam("最大令牌数")
    private Long original_tokens;    //原始令牌数
}
