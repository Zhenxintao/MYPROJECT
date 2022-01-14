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
@TableName("balanceStepConfig")
public class BalanceStepConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 高限(℃)-当该值为0时代表不在所有区间之外的计算方法或步幅(如5%)
     */
    private BigDecimal limits;

    /**
     * 步长([%|Hz])-当该值为0时代表使用计算公式计算,当该值为-1时代表死区范围内不做调整
     */
    private BigDecimal step;

    /**
     * 1：阀门；2：泵
     */
    @TableField("workType")
    private Integer workType;


}
