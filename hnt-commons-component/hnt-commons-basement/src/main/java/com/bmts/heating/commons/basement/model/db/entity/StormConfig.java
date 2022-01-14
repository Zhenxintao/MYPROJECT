package com.bmts.heating.commons.basement.model.db.entity;

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
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stormConfig")
public class StormConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 拓扑名称
     */
    private String name;

    /**
     * 数据源配置信息
     */
    @TableField("dataSourceConfig")
    private String dataSourceConfig;

    /**
     * 是否启用
     */
    private Boolean status;

    /**
     * 描述信息
     */
    private String description;


}
