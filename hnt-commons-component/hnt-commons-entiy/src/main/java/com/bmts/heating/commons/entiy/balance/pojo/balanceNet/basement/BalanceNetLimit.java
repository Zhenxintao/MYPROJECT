package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class BalanceNetLimit implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 点标识名称
     */

    private String pointName;

    /**
     * 比较类型 1.小于 2.大于 后续根据需要增加
     */

    private Integer compareType;

    /**
     * 外键
     */

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
    private String pointZh;
}
