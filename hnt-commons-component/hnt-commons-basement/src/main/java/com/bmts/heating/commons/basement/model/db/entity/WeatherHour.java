package com.bmts.heating.commons.basement.model.db.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WeatherHour implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * 风向
     */
    private String direct;

    /**
     * 风力
     */
    private String power;

    /**
     * 风速
     */
    private BigDecimal speed;

    /**
     * 时间点
     */
    @TableField("weatherTime")
    private LocalDateTime weatherTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 天气情况
     */
    private String info;

    /**
     * 1.实时 2.预测
     */
    @TableField("forecastType")
    private Integer forecastType;

    /**
     * 雨水量
     */
    @TableField("rainOrSnow")
    private BigDecimal rainOrSnow;


}
