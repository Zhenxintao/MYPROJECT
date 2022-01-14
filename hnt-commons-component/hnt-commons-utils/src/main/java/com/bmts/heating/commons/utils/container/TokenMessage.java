package com.bmts.heating.commons.utils.container;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Description("令牌消息")
public class TokenMessage implements Serializable {
    private String server_type;     //服务类型(grpc、application)
    private String application_name;
    private String service_name;
    private String host;
    private String port;
    private long outTime;  //超时时长
    private int needCount;  //需要的令牌数
    private int realityCount;   //实际获取的令牌数
}
