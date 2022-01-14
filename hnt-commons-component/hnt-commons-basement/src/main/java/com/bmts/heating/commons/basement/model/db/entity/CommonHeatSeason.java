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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("commonHeatSeason")
@ApiModel("供暖季")
public class CommonHeatSeason implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供暖开始时间
     */
    @ApiModelProperty("供暖开始时间")
    @TableField("heatStartTime")
    private LocalDateTime heatStartTime;
    /**
     * 供暖结束时间
     */
    @ApiModelProperty("供暖结束时间")
    @TableField("heatEndTime")
    private LocalDateTime heatEndTime;
    /**
     * 供热年度
     */
    @ApiModelProperty("供热年度")
    @TableField("heatYear")
    private String heatYear;
//    /**
//     * 电价
//     */
//    @ApiModelProperty("电价")
//    @TableField("electricityPrice")
//    private BigDecimal electricityPrice;
//    /**
//     * 基本单价
//     */
//    @ApiModelProperty("基本单价")
//    @TableField("basePrice")
//    private BigDecimal basePrice;
//    /**
//     * 供热单价
//     */
//    @ApiModelProperty("供热单价")
//    @TableField("heatPrice")
//    private BigDecimal heatPrice;
//    /**
//     * 面积单价
//     */
//    @ApiModelProperty("面积单价")
//    @TableField("areaPrice")
//    private BigDecimal areaPrice;
    /**
     * 所属机构
     */
    @ApiModelProperty("所属机构")
    @TableField("heatOrganizationId")
    private Integer heatOrganizationId;
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
    @TableField("description")
    private String description;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;
    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;
    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;


}
