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
 * @ClassName: HeatDeviceConfig
 * @Description: 关联设备配置表
 * @Author: pxf
 * @Date: 2020/11/25 9:55
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("heatDeviceConfig")
@ApiModel("关联设备表")
public class HeatDeviceConfig implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联 设备表 id
     */
    @ApiModelProperty("设备表 id ")
    @TableField("deviceConfigId")
    private Integer deviceConfigId;

    /**
     * 关联id ： 所属换热站id 或热源id
     */
    @ApiModelProperty("关联id ： 所属换热站id 或热源id")
    @TableField("relevanceId")
    private Integer relevanceId;

    /**
     * 类型：1、所属换热站    2、所属热源
     */
    @ApiModelProperty("类型：1、所属换热站    2、所属热源")
    @TableField("type")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(" 修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

}
