package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@ApiModel("天气预报表")
public class WeatherForecastDto implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;
    /**
     * 预测最高温度
     */
    @ApiModelProperty("预测最高温度")

    private BigDecimal highTemperature;
    /**
     * 平均温度
     */
    @ApiModelProperty("平均温度")

    private BigDecimal avgTemperature;
    /**
     * 预测最低温度
     */
    @ApiModelProperty("预测最低温度")

    private BigDecimal lowTemperature;
    /**
     * 风力(0-12级)
     */
    @ApiModelProperty("风力(0-12级)")

    private String wind;
    /**
     * 风速
     */
    @ApiModelProperty("风速")

    private String windVelocity;
    /**
     * 风向(如：东北风)
     */
    @ApiModelProperty("风向(如：东北风)")

    private String windDirection;

    /**
     * 天气状况
     */
    @ApiModelProperty("天气状况")

    private String weatherConditions;
    /**
     * 雪况(0-4级)
     */
    @ApiModelProperty("雪况(0-4级)")

    private Integer snowStatus;


    /**
     * 天气预报采集时间
     */
    @ApiModelProperty("天气预报采集时间")

    private LocalDateTime ncapTime;
    /**
     * 天气预报预报时间
     */
    @ApiModelProperty("天气预报预报时间")

    private LocalDateTime forecastTime;
    @ApiModelProperty("创建时间")

    private LocalDateTime createTime;
    /**
     * 天气编码
     */
    @ApiModelProperty("天气编码")

    private String cityCode;



}
