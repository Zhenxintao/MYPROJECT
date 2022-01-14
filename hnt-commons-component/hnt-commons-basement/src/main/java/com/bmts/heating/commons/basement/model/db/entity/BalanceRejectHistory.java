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
@TableName("balanceRejectHistory")
public class BalanceRejectHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("balanceNetId")
    private Integer balanceNetId;

    /**
     * 关联id
     */
    @TableField("relevanceId")
    private Integer relevanceId;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 供水温度
     */
    @TableField("tempTg")
    private BigDecimal tempTg;

    /**
     * 回水温度
     */
    @TableField("tempTh")
    private BigDecimal tempTh;

    /**
     * 剔除时间
     */
    @TableField("rejectTime")
    private LocalDateTime rejectTime;

    /**
     * 剔除原因
     */
    private String reason;


}
