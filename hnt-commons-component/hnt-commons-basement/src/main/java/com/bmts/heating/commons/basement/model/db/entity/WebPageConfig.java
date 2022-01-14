package com.bmts.heating.commons.basement.model.db.entity;

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
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WebPageConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置key唯一，可为页面标识，可为某种业务标识
     */
    @TableField("configKey")
    private String configKey;

    /**
     * 具体配置，前端存储，查询时原样返回
     */
    @TableField("jsonConfig")
    private String jsonConfig;

    /**
     * 可空用户id
     */
    @TableField("userId")
    private Integer userId;

    /**
     * 配置描述
     */
    @TableField("description")
    private String description;


}
