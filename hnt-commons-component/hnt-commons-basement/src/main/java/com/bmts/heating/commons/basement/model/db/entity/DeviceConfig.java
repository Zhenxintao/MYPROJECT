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

/**
 * @ClassName: DeviceConfig
 * @Description: 设备  id 配置表
 * @Author: pxf
 * @Date: 2020/11/25 9:55
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("deviceConfig")
@ApiModel("设备配置表")
public class DeviceConfig implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 节点名称或者描述
     */
    @ApiModelProperty("节点名称")
    @TableField("name")
    private String name;

    /**
     * 实例唯一编码
     */
    @ApiModelProperty("实例唯一编码")
    @TableField("nodeCode")
    private String nodeCode;

    /**
     * 设备类型 类型 1.PVSS   2.JK
     */
    @ApiModelProperty("设备类型 类型 1.PVSS   2.JK")
    @TableField("type")
    private Integer type;

}
