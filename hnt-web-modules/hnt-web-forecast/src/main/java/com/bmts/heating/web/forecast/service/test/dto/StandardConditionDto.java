package com.bmts.heating.web.forecast.service.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测 数据入参
 */
@Data
public class StandardConditionDto {


    /**
     * 室外设计温度
     */
    @ApiModelProperty("室外设计温度")
    private BigDecimal basic_outsideConfigTemp;

    /**
     * 室内设计温度
     */
    @ApiModelProperty("室内设计温度")
    private BigDecimal basic_insideDesignTemp;


    /**
     * 散热器 严寒季供水温度
     */
    @ApiModelProperty("散热器 严寒季供水温度")
    private BigDecimal core_radiatorTg;

    /**
     * 散热器 严寒季回水温度
     */
    @ApiModelProperty("散热器 严寒季回水温度")
    private BigDecimal core_radiatorTh;

    /**
     * 一次网严寒季供水温度 (t´g)
     */
    @ApiModelProperty("一次网严寒季供水温度 (t´g)")
    private BigDecimal core_firstNetTg;

    /**
     * 一次网严寒季回水温度t´h（℃） 给定值或计算
     */
    @ApiModelProperty("一次网严寒季回水温度t´h（℃） 给定值或计算")
    private BigDecimal core_firstNetTh;

    /**
     * 室内温度 (tn)
     */
    @ApiModelProperty("室内温度")
    private BigDecimal core_insideTemp;

    /**
     * 单位面积热指标（w/m2） qf
     */
    @ApiModelProperty("单位面积热指标（w/m2） qf")
    private BigDecimal core_areaHeatingIndex;

    /**
     * 供热面积(万/㎡)
     */
    @ApiModelProperty("供热面积(万/㎡)")
    private BigDecimal core_heatArea;


    /**
     * 二次网相对流量
     */
    @ApiModelProperty("二次网相对流量")
    private BigDecimal season_secondRelativeFlow;


    /**
     * 一次网相对流量 (计算得来)
     */
    @ApiModelProperty("一次网相对流量 (计算得来)")
    private BigDecimal season_firstNetRelativeFlow;


    // 预测计算需要的 温度   （weather_real_temperature 表的数据）
    @ApiModelProperty("天气预报的室外温度")
    private BigDecimal forecastHourAvgTemperature;

    // 时间间隔
    @ApiModelProperty("时间间隔")
    private Integer interval;

}
