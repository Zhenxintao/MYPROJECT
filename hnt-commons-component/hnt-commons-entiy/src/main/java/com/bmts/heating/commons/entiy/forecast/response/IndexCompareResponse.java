package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("预测首页昨日对比及同比响应类")
public class IndexCompareResponse {
    /**
     * 室外温度
     * */
    @ApiModelProperty("室外温度")
    private BigDecimal forecastTemp;
    /**
     * 昨日室外温度
     * */
    @ApiModelProperty("昨日室外温度")
    private BigDecimal ayerTemp;
    /**
     * 同比室外温度
     * */
    @ApiModelProperty("同比室外温度")
    private BigDecimal yoyTemp;
    /**
     * 预测热负荷
     * */
    @ApiModelProperty("预测热负荷")
    private BigDecimal forecastHeatLoad;
    /**
     * 昨日预测热负荷
     * */
    @ApiModelProperty("昨日预测热负荷")
    private BigDecimal ayerHeatLoad;
    /**
     * 同比预测热负荷
     * */
    @ApiModelProperty("同比预测热负荷")
    private BigDecimal yoyHeatLoad;
    /**
     * 日热单耗
     * */
    @ApiModelProperty("日热单耗")
    private BigDecimal heatUnitConsumption;
    /**
     * 昨日热单耗
     * */
    @ApiModelProperty("昨日热单耗")
    private BigDecimal ayerHeatUnitConsumption;
    /**
     * 同比热单耗
     * */
    @ApiModelProperty("同比热单耗")
    private BigDecimal yoyHeatUnitConsumption;
}
