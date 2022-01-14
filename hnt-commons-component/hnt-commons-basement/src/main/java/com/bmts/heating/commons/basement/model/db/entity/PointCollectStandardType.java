package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author naming
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pointCollectStandardType")
public class PointCollectStandardType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统分区/机组 id
     */
    @TableField("heatSystemId")
    private Integer heatSystemId;

    /**
     * 标签名称
     */
    @TableField("pointName")
    private String pointName;

    /**
     * 唯一标识
     */
    private String code;


}
