package com.bmts.heating.commons.basement.model.db.entity;

import com.alibaba.fastjson.annotation.JSONField;
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

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("字典")
public class Dic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    private String name;

    /**
     * 唯一标识
     */
    @ApiModelProperty("唯一标识")
    private String code;
    /**
     * 系统默认
     */
    @ApiModelProperty("系统默认")
    @TableField("systemDefault")
    private Boolean systemDefault;
    /**
     * 是否有效
     */
    @ApiModelProperty("是否有效")
    @TableField("isEnabled")
    private Boolean isEnabled;

    /**
     * 父Id
     */
    @ApiModelProperty("父Id")
    private Integer pid;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private Integer level;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

    private String description;
    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @JSONField(serialize = false)
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

}
