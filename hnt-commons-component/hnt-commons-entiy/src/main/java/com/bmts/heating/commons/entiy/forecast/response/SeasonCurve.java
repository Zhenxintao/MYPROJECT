package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeasonCurve {

    /**
     * 预测热量
     */
    @ApiModelProperty("预测热量")
    private BigDecimal forecastHot;

    /**
     * 预测流量
     */
    @ApiModelProperty("预测流量")
    private BigDecimal forecastFlow;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
