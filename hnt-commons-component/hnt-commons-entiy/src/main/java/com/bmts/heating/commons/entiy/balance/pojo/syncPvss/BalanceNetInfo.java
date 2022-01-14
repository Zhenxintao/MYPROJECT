package com.bmts.heating.commons.entiy.balance.pojo.syncPvss;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;

/**
 * <p>
 *  全网平衡基础信息同步（PVSS）
 * </p>
 *
 * @author 甄鑫涛
 * @since 2021-09-17
 */

@Data
@ApiModel("全网平衡基础信息同步（PVSS）")
@Description("全网平衡基础信息同步（PVSS）")
public class BalanceNetInfo  {
    /**
     * 全网平衡Id
     * */
    private String syncNetBalanceId;

    /**
     * 网名称
     */
    private String name;

    /**
     * 当前计算目标均温设定值(手动设定时有效)
     */
    private BigDecimal computeTargetTemp;

    /**
     * 整网补偿值
     */
    private BigDecimal compensation;

    /**
     * 目标均温(targetlast)计算模式 0-自动计算 1-手动给定
     */
    private Integer computeType;

    /**
     * 给定与反馈值得允许误差
     */
    private BigDecimal errorValue;

    /**
     * 超时时间
     */
    private Integer timeOut;

    /**
     * 控制方式（1阀控制；2泵控制；3二次供温控制，4二次供回平均温度控制）
     */
    private Integer controlType;

    /**
     * 是否开启
     */
    private Boolean status;

//    /**
//     * 计算供热面积
//     */
//    private BigDecimal computeArea;
//
//    /**
//     * 实际供热面积
//     */
//    private BigDecimal realArea;
//
//    /**
//     * 计算目标均温
//     */
//    private BigDecimal computeTargetAvg;
//
//    /**
//     * 实际目标均温
//     */
//    private BigDecimal realTarget;
//
//    /**
//     * 计算失调度
//     */
//    private BigDecimal computeImbalance;
//
//    /**
//     * 实际失调度
//     */
//    private BigDecimal realImbalance;

//    @ApiModelProperty("创建人")
//    private String createUser;

//    /**
//     * 创建时间
//     */
//    @ApiModelProperty("创建时间")
//    private LocalDateTime createTime;

//    /**
//     * 修改人
//     */
//    @ApiModelProperty("修改人")
//    private String updateUser;
//
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    private LocalDateTime updateTime;

//    /**
//     * 描述
//     * */
//    private String description;

    /**
     * 循环周期
     */
    @ApiModelProperty("循环周期")
    private int cycle;

    @ApiModelProperty("运行模式 1.控制下发 2.仅计算")
    private Integer runType;
//
//    /**
//     * 下次调控时间
//     */
//    @ApiModelProperty("下次调控时间")
//    private LocalDateTime nextControlTime;

    /**
     * 机组数量
     */
    @ApiModelProperty("机组数量")
    private Integer systemCount;
}
