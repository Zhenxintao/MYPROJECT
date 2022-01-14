package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceCore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预测名称
     */
    private String name;

    /**
     * 单位面积热指标（w/m2） qf
     */
    @TableField("areaHeatingIndex")
    private BigDecimal areaHeatingIndex;

    /**
     * 预计热耗
     */
    @TableField("predictHeatRate")
    private BigDecimal predictHeatRate;

    /**
     * 实际热耗qs（GJ/m2）
     */
    @TableField("actualHeatRate")
    private BigDecimal actualHeatRate;

    /**
     * 供热面积(万/㎡)
     */
    @TableField("heatArea")
    private BigDecimal heatArea;

    /**
     * 室内温度 (tn)
     */
    @TableField("insideTemp")
    private BigDecimal insideTemp;

    /**
     * 一次网严寒季供水温度 (t´g)
     */
    @TableField("firstNetTg")
    private BigDecimal firstNetTg;

    /**
     * 计算方式：1.流量法 2.温度法
     */
    @TableField("computeType")
    private Integer computeType;

    /**
     * 一次网严寒季循环流量 G´rw（t/h）  给定或计算值：
     */
    @TableField("firstNetCycleFlow")
    private BigDecimal firstNetCycleFlow;

    /**
     * 一次网严寒季回水温度t´h（℃） 给定值或计算
     */
    @TableField("firstNetTh")
    private BigDecimal firstNetTh;

    /**
     * 是否有效
     */
    @TableField("isValid")
    private Boolean isValid;

    /**
     * 类型：1.一网一源 2.一网多源
     */
    private Integer type;

    /**
     * 散热器 严寒季供水温度
     */
    @TableField("radiatorTg")
    private BigDecimal radiatorTg;

    /**
     * 散热器 严寒季回水温度
     */
    @TableField("radiatorTh")
    private BigDecimal radiatorTh;

    /**
     * 地板辐射 严寒季供水温度
     */
    @TableField("floorTg")
    private BigDecimal floorTg;

    /**
     * 地板辐射 严寒季回水温度
     */
    @TableField("floorTh")
    private BigDecimal floorTh;

    /**
     * 散热间距:1.300 2.250 3.200 4.150
     */
    @TableField("radiatingSpace")
    private Integer radiatingSpace;

    /**
     * 自动预测时间，例如：08:00
     */
    @TableField("forecastTime")
    private String forecastTime;

    /**
     * 补偿值设置json形式
     */
    private String compensation;

    /**
     * 调度 json形式
     */
    private String dispatch;

    /**
     * 温度预测方式设置json形式(开始时间、结束时间、温度值、温度预测类型：1.温度改变 2.流量改变)
     */
    @TableField("tempSetting")
    private String tempSetting;
    /**
     * 耗热量期望值
     */
    @TableField("desiredValue")
    private BigDecimal desiredValue;

}
