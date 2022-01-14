package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author naming
 * @since 2020-08-05
 */
@Data
@ApiModel("菜单、权限")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父ID
     */
    @ApiModelProperty("根节点标识为0")
    private Integer pid;

    /**
     * 资源类型（0：菜单，1：按钮，）
     */
    @ApiModelProperty("资源类型（0：菜单，1：按钮）")
    private Integer type;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源标识（或者叫权限字符串）
     */
    private String code;

    /**
     * 图标
     */
    private String icon;

    /**
     * api路径
     */
    private String uri;

    /**
     * 序号
     */
    private Integer seq;


    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;
    @TableField(exist = false)
    private String checked;
    private Integer treeLeave;
    @TableField(exist = false)
    private List<SysPermission> children = new ArrayList<>();
    @TableField(value = "treePath")
    private String treePath;
}
