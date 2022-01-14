package com.bmts.heating.commons.auth.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PermsResponse implements Serializable {
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
}
