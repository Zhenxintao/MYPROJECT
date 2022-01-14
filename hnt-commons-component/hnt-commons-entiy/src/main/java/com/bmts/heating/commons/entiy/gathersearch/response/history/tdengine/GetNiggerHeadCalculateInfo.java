package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 获取折标计算系数实体
 * */
@Data
public class GetNiggerHeadCalculateInfo {

    @ApiModelProperty("天气预报预测时间段均温")
    private BigDecimal forecastTempAvg;

    @ApiModelProperty("折标室外温度")
    private BigDecimal outdoorTemp;

    @ApiModelProperty("室内温度")
    private BigDecimal indoorTemp;
}
