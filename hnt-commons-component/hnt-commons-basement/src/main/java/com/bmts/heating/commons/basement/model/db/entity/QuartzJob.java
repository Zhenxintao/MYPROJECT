package com.bmts.heating.commons.basement.model.db.entity;

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
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuartzJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 定时表达式
     */
    private String cron;

    /**
     * 任务执行类bean名称
     */
    @TableField("beanName")
    private String beanName;

    /**
     * 分组名称
     */
    @TableField("groupName")
    private String groupName;

    /**
     * 是否开启
     */
    private Boolean status;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 1.cron形式 2.秒 3.分 4.小时
     */
    private Integer type;
}
