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
@TableName("log_operation_balance")
@ApiModel("全网平衡操作记录表")
public class LogOperationBalance implements Serializable {

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
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 操作者IP
     */
    @ApiModelProperty("ip地址")
    @TableField("ipAddress")
    private String ipAddress;

    /**
     * 设备名称
     */
    @ApiModelProperty("操作模块")
    @TableField("module")
    private String module;


    @ApiModelProperty("操作按钮")
    @TableField("button")
    private String button;

    /**
     * 操作模式
     */
    @ApiModelProperty("操作描述")
    @TableField("description")
    private String description;

}
