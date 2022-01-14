package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-03-22
 */
@Data
@TableName("forecast_source_history")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceHistory implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("startTime")
    private LocalDateTime startTime;

    @TableField("endTime")
    private LocalDateTime endTime;

    /**
     * 室内计算温度
     */
    @TableField("indoorTemp")
    private BigDecimal indoorTemp;

    /**
     * 预测室外温度
     */
    @TableField("forecastOutTemp")
    private BigDecimal forecastOutTemp;

    /**
     * 实际室外温度
     */
    @TableField("realOutTemp")
    private BigDecimal realOutTemp;


    /**
     * 预测一次供温
     */
    @TableField("forecast_t1g")
    private BigDecimal forecastT1g;

    /**
     * 预测一次回温
     */
    @TableField("forecast_t1h")
    private BigDecimal forecastT1h;

    /**
     * 预测散热器二次供水温度
     */
    @TableField("radiator_t2g")
    private BigDecimal radiatorT2g;


    /**
     * 预测散热器二次回水温度
     */
    @TableField("radiator_t2h")
    private BigDecimal radiatorT2h;


    /**
     * 预测地板辐射二次供水温度
     */
    @TableField("floor_t2g")
    private BigDecimal floorT2g;

    /**
     * 预测地板辐射二次回水温度
     */
    @TableField("floor_t2h")
    private BigDecimal floorT2h;

    /**
     * 地板表面平均温度
     */
    @TableField("floor_temp_avg")
    private BigDecimal floorTempAvg;

    /**
     * 预测热量
     */
    @TableField("forecastHot")
    private BigDecimal forecastHot;

    /**
     * 预测负荷率
     */
    @TableField("forecastLoadRate")
    private BigDecimal forecastLoadRate;

    /**
     * 预测负荷
     */
    @TableField("forecastLoad")
    private BigDecimal forecastLoad;

    /**
     * 预测热指标
     */
    @TableField("forecastThermalIndex")
    private BigDecimal forecastThermalIndex;

    /**
     * 实际热量
     */
    @TableField("realHot")
    private BigDecimal realHot;

    /**
     * 实际负荷
     */
    @TableField("realLoad")
    private BigDecimal realLoad;

    /**
     * 实际热指标
     */
    @TableField("realThermalIndex")
    private BigDecimal realThermalIndex;

    /**
     * 实际流量
     */
    @TableField("realFlow")
    private BigDecimal realFlow;

    /**
     * 实际一次供温
     */
    @TableField("real_t1g")
    private BigDecimal realT1g;

    /**
     * 实际一次回温
     */
    @TableField("real_t1h")
    private BigDecimal realT1h;

    /**
     * 实际二次供温
     */
    @TableField("real_t2g")
    private BigDecimal realT2g;

    /**
     * 实际二次回温
     */
    @TableField("real_t2h")
    private BigDecimal realT2h;

    /**
     * 预测时间
     */
    @TableField("forecastTime")
    private LocalDateTime forecastTime;

    /**
     * 计算方式
     */
    @TableField("computeType")
    private Integer computeType;

    /**
     * 1.阶段 2.小时 3.天
     */
    private Integer type;

    @TableField("forecastSourceCoreId")
    private Integer forecastSourceCoreId;

    /**
     * 1、历史数据    0、预测数据
     */
    @TableField("data_status")
    private Boolean dataStatus;


    /**
     * 调度流量
     */
    @TableField("scheduleFlow")
    private BigDecimal scheduleFlow;

}
