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
@TableName("equipmentInfoPointStandard")
@ApiModel("设备信息与标准点表关联表")
public class EquipmentInfoPointStandard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id ;
    /**
     * 设备信息表Id
     */
    @ApiModelProperty("设备信息表Id")
    @TableField("equipmentInfoId")
    private Integer equipmentInfoId;
    /**
     * 设备编号
     */
    @ApiModelProperty("标准点表Id")
    @TableField("pointStandardId")
    private Integer pointStandardId;
}
