package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("alarmConfig")
@ApiModel("报警配置")
public class AlarmConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报警类型名称
     */
    @ApiModelProperty("报警类型名称")
    private String name;

    /**
     * 唯一标识,用于关联代码
     */
    @ApiModelProperty("唯一标识,用于关联代码")
    private String code;

    /**
     * 报警级别
     */
    @ApiModelProperty("报警级别")
    private Integer level;

    /**
     * 报警类别(站点、单元阀、户阀、室温)
     */
    @ApiModelProperty("报警类别(站点、单元阀、户阀、室温)")
    @TableField("Type")
    private Integer Type;

    /**
     * 设定报警值(例如回温报警值)
     */
    @ApiModelProperty("设定报警值(例如回温报警值)")
    @TableField("SetValue")
    private Integer SetValue;

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
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;
}
