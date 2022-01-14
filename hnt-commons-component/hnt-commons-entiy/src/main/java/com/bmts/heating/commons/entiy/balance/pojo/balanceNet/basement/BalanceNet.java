package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement;

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
public class BalanceNet implements Serializable {

    private static final long serialVersionUID = 1L;


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

    private BigDecimal computeTargetTemp;

    /**
     * 补偿值
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

    /**
     * 计算供热面积
     */

    private BigDecimal computeArea;

    /**
     * 实际供热面积
     */

    private BigDecimal realArea;

    /**
     * 计算目标均温
     */

    private BigDecimal computeTargetAvg;

    /**
     * 实际目标均温
     */
    private BigDecimal realTarget;

    /**
     * 计算失调度
     */

    private BigDecimal computeImbalance;

    /**
     * 实际失调度
     */

    private BigDecimal realImbalance;

    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    private String description;

    @ApiModelProperty("运行模式 1.控制下发 2.仅计算")
    private Integer runType;

    @ApiModelProperty("循环周期")
    private Integer cycle;

    @ApiModelProperty("下次调控时间")
    private LocalDateTime nextControlTime;

    @ApiModelProperty("机组数量")
    private Integer systemCount;

}
