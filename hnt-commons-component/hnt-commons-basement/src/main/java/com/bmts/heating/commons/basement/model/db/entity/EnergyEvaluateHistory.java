package com.bmts.heating.commons.basement.model.db.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EnergyEvaluateHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联id
     */
    @TableField("relevanceId")
    private Integer relevanceId;

    private Integer qualified;
    /**
     * 热力站或热源名称
     */
    private String name;

    /**
     * 基准值
     */
    @TableField("standardValue")
    private BigDecimal standardValue;

    /**
     * 1.水 2.电 3.热
     */
    private Integer type;

    /**
     * 对应水电热具体值
     */
    private BigDecimal value;

    /**
     * 对应网和源的级别
     */
    private Integer level;

    /**
     * 评价 优良中差
     */
    private String evaluate;

    /**
     * 评价的具体日期
     */
    @TableField("evaluateTime")
    private LocalDate evaluateTime;
    @TableField("createTime")
    private LocalDateTime createTime;
}
