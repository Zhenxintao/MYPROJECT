package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("balanceNetControlLog")
public class BalanceNetControlLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联Id
     */
    @TableField("relevanceId")
    private Integer relevanceId;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 点名称
     */
    @TableField("pointName")
    private String pointName;

    /**
     * 点地址
     */
    @TableField("pointAddress")
    private String pointAddress;

    /**
     * 下发是否成功
     */
    private Boolean status;

    /**
     * 失败说明
     */
    private String msg;

    @TableField("balanceNetId")
    private Integer balanceNetId;
    /**
     * 下发值
     */
    @TableField("controlValue")
    private BigDecimal controlValue;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

}
