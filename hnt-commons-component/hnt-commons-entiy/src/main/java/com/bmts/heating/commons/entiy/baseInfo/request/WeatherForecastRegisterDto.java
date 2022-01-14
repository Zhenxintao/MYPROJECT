package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("注册客户端")
public class WeatherForecastRegisterDto {
    private String url;
    private String code;
}
