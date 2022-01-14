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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("equipmentInfo")
@ApiModel("设备信息表")
public class EquipmentInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id ;
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @TableField("equipmentName")
    private String equipmentName;
    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    @TableField("equipmentCode")
    private String equipmentCode;
}
