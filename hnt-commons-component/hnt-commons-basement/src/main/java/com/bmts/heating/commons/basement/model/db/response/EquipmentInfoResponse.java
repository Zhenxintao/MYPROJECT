package com.bmts.heating.commons.basement.model.db.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentInfoResponse {

    /**
     * 设备Id
     */
    @ApiModelProperty("设备Id")
    private Integer equipmentInfoId;
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;
    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    private String equipmentCode;

    /**
     * 设备Id
     */
    @ApiModelProperty("设备与点位关联Id")
    private Integer equipmentInfoPointStandardId;

    /**
     * 参量名称
     */
    @ApiModelProperty("参量名称")
    private String name;
    /**
     * 类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间
     */
    @ApiModelProperty("1.AI模拟量 2.DI 数字量 3.TX 日期时间")
    private Integer type;
    /**
     * 数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    @TableField("dataType")
    private Integer dataType;
    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    @TableField("columnName")
    private String columnName;
    /**
     * 网测类型 0公用 1.一次测 2.二次测
     */
    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    @TableField("netFlag")
    private Integer netFlag;
    /**
     * 是否参与运算
     */
    @ApiModelProperty("是否参与运算")
    @TableField("isComputePoint")
    private Boolean isComputePoint;
    /**
     * 是否显示
     */
    @ApiModelProperty("是否显示")
    @TableField("isShow")
    private Boolean isShow;
    /**
     * 是否是主要参量
     */
    @ApiModelProperty("是否是主要参量")
    @TableField("isImportantPoint")
    private Boolean isImportantPoint;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("所属参量分类")
    @TableField("pointParameterTypeId")
    private int pointParameterTypeId;
    @ApiModelProperty("关联单位id")
    @TableField("pointUnitId")
    private int pointUnitId;
    @ApiModelProperty("fix值类型，从字典表取")
    @TableField("fixValueType")
    private int fixValueType;
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
    @ApiModelProperty("描述")
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
    @ApiModelProperty("删除标识")
    @JSONField(serialize = false)
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer pointStandardId;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;
    /**
     * 1.只读 2.可读可写 3.只写 4.计算量
     */
    @ApiModelProperty("1.只读 2.可读可写 3.只写 4.计算量")
    @TableField("pointConfig")
    private Integer pointConfig;

    /**
     * 描述性信息 例如 0.表示什么 1.表示什么
     */
    @ApiModelProperty("描述性信息 例如 0.表示什么 1.表示什么")
    @TableField("descriptionJson")
    private String descriptionJson;

    /**
     * 计算表达式
     */
    @ApiModelProperty("计算表达式")
    @TableField("expression")
    private String expression;
}
