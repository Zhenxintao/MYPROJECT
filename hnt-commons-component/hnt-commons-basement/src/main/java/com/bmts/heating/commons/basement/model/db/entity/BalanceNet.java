package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("balanceNet")
public class BalanceNet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer Id;

    /**
     * 名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 当前计算目标均温设定值(手动设定时有效)
     */
    @TableField("computeTargetTemp")
    private BigDecimal computeTargetTemp;

    /**
     * 补偿值
     */
    private BigDecimal compensation;

    /**
     * 目标均温(targetlast)计算模式 0-自动计算 1-手动给定
     */
    @TableField("computeType")
    private Integer computeType;

    /**
     * 给定与反馈值得允许误差
     */
    @TableField("errorValue")
    private BigDecimal errorValue;

    /**
     * 超时时间
     */
    @TableField("timeOut")
    private Integer timeOut;

    /**
     * 控制方式（1阀控制；2泵控制；3二次供温控制，4二次供回平均温度控制）
     */
    @TableField("controlType")
    private Integer controlType;

    /**
     * 是否开启
     */
    private Boolean status;

    /**
     * 计算供热面积
     */
    @TableField("computeArea")
    private BigDecimal computeArea;

    /**
     * 实际供热面积
     */
    @TableField("realArea")
    private BigDecimal realArea;

    /**
     * 计算目标均温
     */
    @TableField("computeTargetAvg")
    private BigDecimal computeTargetAvg;

    /**
     * 实际目标均温
     */
    @TableField("realTarget")
    private BigDecimal realTarget;

    /**
     * 计算失调度
     */
    @TableField("computeImbalance")
    private BigDecimal computeImbalance;

    /**
     * 实际失调度
     */
    @TableField("realImbalance")
    private BigDecimal realImbalance;

    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

    private String description;

    /**
     * 循环周期
     */
    @ApiModelProperty("循环周期")
    @TableField("cycle")
    private int cycle;

    @ApiModelProperty("运行模式 1.控制下发 2.仅计算")
    @TableField("runType")
    private Integer runType;

    /**
     * 下次调控时间
     */
    @ApiModelProperty("下次调控时间")
    @TableField("nextControlTime")
    private LocalDateTime nextControlTime;

    /**
     * 机组数量
     */
    @ApiModelProperty("机组数量")
    @TableField("systemCount")
    private Integer systemCount;

    /**
     * 全网平衡Id（PVSS同步）
     */
    @ApiModelProperty("全网平衡Id（PVSS同步）")
    @TableField("syncNetBalanceId")
    private String syncNetBalanceId;
}
