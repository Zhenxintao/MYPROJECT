package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("热源负荷预测列表展示相应类")
public class ForecastDataResponse {

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    /**
     * 室内计算温度
     */
    @ApiModelProperty("室内计算温度")
    private BigDecimal indoorTemp;

    /**
     * 预测室外温度
     */
    @ApiModelProperty("预测室外温度")
    private BigDecimal forecastOutTemp;

    /**
     * 实际室外温度
     */
    @ApiModelProperty("实际室外温度")
    private BigDecimal realOutTemp;

    /**
     * 调度热量
     */
    @ApiModelProperty("调度热量")
    private BigDecimal scheduleHot;

    /**
     * 调度流量
     */
    @ApiModelProperty("调度流量")
    private BigDecimal scheduleFlow;

    /**
     * 预测一次供温
     */
    @ApiModelProperty("预测一次供温")
    private BigDecimal forecastTg;

    /**
     * 一次回温
     */
    @ApiModelProperty("一次回温")
    private BigDecimal forecastTh;

    /**
     * 预测热量
     */
    @ApiModelProperty("预测热量")
    private BigDecimal forecastHot;

    /**
     * 预测负荷
     */
    @ApiModelProperty("预测负荷")
    private BigDecimal forecastLoad;

    /**
     * 预测热指标
     */
    @ApiModelProperty("预测热指标")
    private BigDecimal forecastThermalIndex;

    /**
     * 实际热量
     */
    @ApiModelProperty("实际热量")
    private BigDecimal realHot;

    /**
     * 实际负荷
     */
    @ApiModelProperty("实际负荷")
    private BigDecimal realLoad;

    /**
     * 实际热指标
     */
    @ApiModelProperty("实际热指标")
    private BigDecimal realThermalIndex;

    /**
     * 实际流量
     */
    @ApiModelProperty("实际流量")
    private BigDecimal realFlow;

    /**
     * 实际一次供温
     */
    @ApiModelProperty("实际一次供温")
    private BigDecimal realT1g;

    /**
     * 实际一次回温
     */
    @ApiModelProperty("实际一次回温")
    private BigDecimal realT1h;

//    /**
//     * 预测时间
//     */
//    private LocalDateTime forecastTime;

//    /**
//     * 计算方式
//     */
//    private Integer computeType;

//    /**
//     * 1.阶段 2.小时 3.天
//     */
//    private Integer type;
    /**
     * 预测名称
     */
    @ApiModelProperty("预测名称")
    private String forecastSourceName;
    /**
     * 单位面积热指标（w/m2） qf
     */
    @ApiModelProperty("单位面积热指标（w/m2） qf")
    private BigDecimal areaHeatingIndex;

    @ApiModelProperty("forecastSourceCoreId")
    private Integer forecastSourceCoreId;
}
