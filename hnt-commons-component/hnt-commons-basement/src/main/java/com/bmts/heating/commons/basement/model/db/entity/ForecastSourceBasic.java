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
 * 负荷数据
 * </p>
 *
 * @author naming
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 负荷数据名称
     */
    private String name;

    /**
     * 开始时间
     */
    @TableField("startTime")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("endTime")
    private LocalDateTime endTime;

    /**
     * 采暖季开始室外计算温度 默认8
     */
    @TableField("outsideTemp")
    private BigDecimal outsideTemp;

    /**
     * 室外实际计算平均温度
     */
    @TableField("outsideComputeTempAvg")
    private BigDecimal outsideComputeTempAvg;

    /**
     * 室外设计温度
     */
    @TableField("outsideConfigTemp")
    private BigDecimal outsideConfigTemp;

    /**
     * 室内设计温度
     */
    @TableField("insideDesignTemp")
    private BigDecimal insideDesignTemp;

    /**
     * 散热器系数
     */
    @TableField("radiatorCoefficient")
    private BigDecimal radiatorCoefficient;

    /**
     * 是否有效
     */
    @TableField("isValid")
    private Boolean isValid;


}
