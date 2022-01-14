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
 * @ClassName: RecordDeviceUpDown
 * @Description: 设备启停记录表
 * @Author: pxf
 * @Date: 2020/12/11 15:55
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_deviceUpDown")
@ApiModel("设备启停记录表")
public class RecordDeviceUpDown implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 热力站id
     */
    @ApiModelProperty("热力站id")
    @TableField("heatTransferStationId")
    private Integer heatTransferStationId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @TableField("content")
    private String content;


    /**
     * 设备状态  true 开启 false停止
     */
    @ApiModelProperty("设备状态 true 开启 false停止")
    @TableField("operation")
    private Boolean operation;


    /**
     * 设备类型 1.循环泵 2.补水泵
     */
    @ApiModelProperty("设备类型 1.循环泵 2.补水泵")
    @TableField("type")
    private Integer type;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;


}
