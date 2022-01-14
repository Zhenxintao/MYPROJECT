package com.bmts.heating.bussiness.baseInformation.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weatherforecast")
public class WeatherForecastConfig {
    //本机ip地址(例:10.0.2.64:4590)
    private  String localipaddress;
    //天气预报请求ip地址(例:10.0.2.53:5001)
    private String ipaddress;
    //注册地址
    private String register;
    //销毁地址
    private String logout;
    //错误请求地址
    private String error;
    //天气预报城市code
    private String citycode;
}
