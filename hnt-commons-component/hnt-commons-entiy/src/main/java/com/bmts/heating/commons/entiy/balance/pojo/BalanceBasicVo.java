package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/1/29 12:16
 **/
@ApiModel("全网平衡基础信息")
@Data
@Description("全网平衡基础信息")
public class BalanceBasicVo {
    @ApiModelProperty("全网平衡Id")
    private Integer Id;
    @ApiModelProperty("下次调控时间")
    private LocalDateTime nextTime;
    @ApiModelProperty("全网平衡名称")
    private String netBalanceName;
    @ApiModelProperty("前计算目标均温设定值(手动设定时有效)")
    private BigDecimal computeTargetTemp;
    @ApiModelProperty("前给定与反馈值得允许误差")
    private BigDecimal errorValue;
    @ApiModelProperty("超时时间")
    private Integer timeOut;
    @ApiModelProperty("补偿值")
    private BigDecimal compenSation;
    @ApiModelProperty("目标均温(targetlast)计算模式 0-自动计算 1-手动给定")
    private Integer computeType;
    @ApiModelProperty("是否开启")
    private Boolean status;
    @ApiModelProperty("计算供热面积")
    private BigDecimal computeArea;
    @ApiModelProperty("实际供热面积")
    private BigDecimal realArea;
    @ApiModelProperty("计算目标均温")
    private BigDecimal computeTargetAvg;
    @ApiModelProperty("实际目标均温")
    private BigDecimal realTarget;
    @ApiModelProperty("计算失调度")
    private BigDecimal computeImbalance;
    @ApiModelProperty("实际失调度")
    private BigDecimal realImbalance;
    @ApiModelProperty("机组数量")
    private Integer systemCount;
    @ApiModelProperty("循环周期")
    private Integer cycle;
}
