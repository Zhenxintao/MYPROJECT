package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Craft implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工艺图配置信息
     */
    @TableField("content")
    private String content;

    /**
     * 关联id
     */
    @TableField("relevanceId")
    private Integer relevanceId;

    /**
     * 1.热力站 2.热源
     */
    @TableField("type")
    private Integer type;

    /**
     * 层级：1属于系统 2.控制柜 3.站 4.源 5.网
     */
    @TableField("level")
    private Integer level;

    /**
     * 模板id
     */
    @TableField("templateId")
    private Integer templateId;

}
