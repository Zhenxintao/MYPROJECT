package com.bmts.heating.commons.basement.model.db.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
@TableName("pointStandard")
@ApiModel("标准点表")
public class PointStandard implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer id;

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

    /**
     * 层级：1：热力站   2：热源
     */
    @TableField("level")
    private Integer level;

    @ApiModelProperty("是否同步到td库 false")
    @TableField("tdColumn")
    private Boolean tdColumn;
}
