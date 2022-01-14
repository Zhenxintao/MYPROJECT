package com.bmts.heating.commons.basement.model.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("balanceNetLimit")
public class BalanceNetLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 点标识名称
     */
    @TableField("pointName")
    private String pointName;

    /**
     * 比较类型 1.小于 2.大于 后续根据需要增加
     */
    @TableField("compareType")
    private Integer compareType;

    /**
     * 外键
     */
    @TableField("balanceNetId")
    private Integer balanceNetId;

    /**
     * 扩展字段
     */
    private String extention;

    /**
     * 设定值
     */
    private BigDecimal value;

    /**
     * 中文名称
     */
    @TableField("pointZh")
    private String pointZh;


}
