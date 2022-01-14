package com.bmts.heating.commons.basement.model.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("balanceTargetHistory")
public class BalanceTargetHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("balanceNetId")
    private Integer balanceNetId;

    /**
     * 计算目标均温
     */
    @TableField("targetTemp")
    private BigDecimal targetTemp;

    /**
     * 计算失调度
     */
    private BigDecimal imbalance;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 实际目标均温
     */
    @TableField("realtargetTemp")
    private BigDecimal realtargetTemp;

    /**
     * 实际失调度
     */
    @TableField("realImbalance")
    private BigDecimal realImbalance;


}
