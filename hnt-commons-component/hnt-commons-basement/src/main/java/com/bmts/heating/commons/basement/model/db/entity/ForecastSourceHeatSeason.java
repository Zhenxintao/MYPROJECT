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
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceHeatSeason implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 一次网阶段流量G（t/h）
     */
    @TableField("firstNetStageFlow")
    private BigDecimal firstNetStageFlow;

    /**
     * 一次网相对流量 (计算得来)
     */
    @TableField("firstNetRelativeFlow")
    private BigDecimal firstNetRelativeFlow;

    /**
     * 二次网相对流量
     */
    @TableField("secondRelativeFlow")
    private BigDecimal secondRelativeFlow;

    @TableField("forcastSourceCoreId")
    private Integer forcastSourceCoreId;

    /**
     * 采暖季阶段名称
     */
    @TableField("stageName")
    private String stageName;


}
