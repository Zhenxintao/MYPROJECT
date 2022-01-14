package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/4/25 18:58
 **/
@Data
@ApiModel("温度预测方式")
public class TempForecast {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ApiModelProperty("温度值")
    private BigDecimal temp;
    @ApiModelProperty("温度预测类型1.温度改变 2.流量改变")
    private int forecastType;
}
