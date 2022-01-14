package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author ${author}
 * @since 2020-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pointUnit")
@ApiModel("单位表")
public class PointUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单位对应中文名称
     */
    @ApiModelProperty("单位对应中文名称")
    @TableField("unitName")
    private String unitName;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    @TableField("unitValue")
    private String unitValue;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("")
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
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

}
