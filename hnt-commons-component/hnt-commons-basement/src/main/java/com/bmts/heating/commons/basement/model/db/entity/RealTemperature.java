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
@ApiModel("小时天气预报表")
@TableName("weather_real_temperature")
public class RealTemperature implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小时温度平均值
     */
    @ApiModelProperty("小时温度平均值")
    @TableField("hourAvgTemperature")
    private BigDecimal hourAvgTemperature;

    /**
     * 预测温度
     */
    @ApiModelProperty("预测温度")
    @TableField("forecastHourAvgTemperature")
    private BigDecimal forecastHourAvgTemperature;


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
     * 湿度
     */
    @ApiModelProperty("湿度")
    @TableField("humiDity")
    private Integer humiDity;
    /**
     * 采集时间
     */
    @ApiModelProperty("采集时间")
    @TableField("ncapTime")
    private LocalDateTime ncapTime;
    /**
     * 采集点的名称
     */
    @ApiModelProperty("采集点的名称")
    @TableField("collectName")
    private String collectName;
    /**
     * 是否是预报气温
     */
    @ApiModelProperty("是否是预报气温")
    @TableField("isForecast")
    private Boolean isForecast;
    /**
     * 天气编码
     */
    @ApiModelProperty("天气编码")
    @TableField("cityCode")
    private String cityCode;

    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    @ApiModelProperty("采集类型: 1.实时气温 2.预测气温")
    @TableField("collectType")
    private Integer collectType;
}
