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
 * @ClassName: LogOperationControl
 * @Description: 控制下发日志表
 * @Author: pxf
 * @Date: 2020/11/25 9:55
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_operation_control")
@ApiModel("设备配置表")
public class LogOperationControl implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    @TableField("userName")
    private String userName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    @TableField("operationTime")
    private LocalDateTime operationTime;

    /**
     * 操作者IP
     */
    @ApiModelProperty("操作者IP")
    @TableField("ip")
    private String ip;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @TableField("deviceName")
    private String deviceName;

    /**
     * 操作类型 ：1、阀门动作   2、变频动作
     */
    @ApiModelProperty("操作类型 ：1、阀门动作   2、变频动作")
    @TableField("type")
    private Integer type;

    /**
     * 操作模式
     */
    @ApiModelProperty("操作模式")
    @TableField("mode")
    private String mode;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    @TableField("company")
    private String company;

    /**
     * 站点名称
     */
    @ApiModelProperty("站点名称")
    @TableField("stationName")
    private String stationName;

    /**
     * 控制柜名称
     */
    @ApiModelProperty("控制柜名称")
    @TableField("cabinetName")
    private String cabinetName;

    /**
     * 系统或机组名称
     */
    @ApiModelProperty("系统或机组名称")
    @TableField("systemName")
    private String systemName;

    /**
     * 原来的数据值描述
     */
    @ApiModelProperty("原来的数据值描述")
    @TableField("oldValue")
    private String oldValue;


    /**
     * 下发数据值描述
     */
    @ApiModelProperty("下发数据值描述")
    @TableField("newValue")
    private String newValue;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

}
