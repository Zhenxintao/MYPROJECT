package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("weather_forecast")
public class WeatherForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 预测最高温度
     */
    @ApiModelProperty("预测最高温度")
    @TableField("highTemperature")
    private BigDecimal highTemperature;
    /**
     * 平均温度
     */
    @ApiModelProperty("平均温度")
    @TableField("avgTemperature")
    private BigDecimal avgTemperature;
    /**
     * 预测最低温度
     */
    @ApiModelProperty("预测最低温度")
    @TableField("lowTemperature")
    private BigDecimal lowTemperature;
    /**
     * 风力(0-12级)
     */
    @ApiModelProperty("风力(0-12级)")
    @TableField("wind")
    private String wind;
    /**
     * 风速
     */
    @ApiModelProperty("风速")
    @TableField("windVelocity")
    private String windVelocity;
    /**
     * 风向(如：东北风)
     */
    @ApiModelProperty("风向(如：东北风)")
    @TableField("windDirection")
    private String windDirection;

    /**
     * 天气状况
     */
    @ApiModelProperty("天气状况")
    @TableField("weatherConditions")
    private String weatherConditions;
    /**
     * 雪况(0-4级)
     */
    @ApiModelProperty("雪况(0-4级)")
    @TableField("snowStatus")
    private Integer snowStatus;


    /**
     * 天气预报采集时间
     */
    @ApiModelProperty("天气预报采集时间")
    @TableField("ncapTime")
    private LocalDateTime ncapTime;
    /**
     * 天气预报预报时间
     */
    @ApiModelProperty("天气预报预报时间")
    @TableField("forecastTime")
    private LocalDateTime forecastTime;
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;
    /**
     * 天气编码
     */
    @ApiModelProperty("天气编码")
    @TableField("cityCode")
    private String cityCode;


}
