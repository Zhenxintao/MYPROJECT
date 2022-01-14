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
@ApiModel("小时天气预报表")
public class RealTemperatureDto implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 小时温度平均值
     */
    @ApiModelProperty("小时温度平均值")

    private BigDecimal hourAvgTemperature;

    /**
     * 预测温度
     */
    @ApiModelProperty("预测温度")

    private BigDecimal forecastHourAvgTemperature;


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
     * 湿度
     */
    @ApiModelProperty("湿度")

    private Integer humiDity;
    /**
     * 采集时间
     */
    @ApiModelProperty("采集时间")

    private LocalDateTime ncapTime;
    /**
     * 采集点的名称
     */
    @ApiModelProperty("采集点的名称")

    private String collectName;
    /**
     * 是否是预报气温
     */
    @ApiModelProperty("是否是预报气温")

    private Boolean isForecast;
    /**
     * 天气编码
     */
    @ApiModelProperty("天气编码")

    private String cityCode;

    @ApiModelProperty("创建时间")

    private LocalDateTime createTime;


}
