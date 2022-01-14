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
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("heatSource")
@ApiModel("热源实体")
public class HeatSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 简拼
     */
    @ApiModelProperty("简拼")
    private String logogram;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 热源标识:1.虚拟 2.真实
     */
    @ApiModelProperty("热源标识 1.虚拟 2.真实")
    private Integer flag;

    /**
     * 热源标识： 1.电厂 2.长输热源 3.调峰热源
     */
    @ApiModelProperty("热源标识： 1.电厂 2.长输热源 3.调峰热源")
    private Integer type;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("最大输出功率(kw)")
    @TableField("maxPowerOut")
    private BigDecimal maxPowerOut;
    @ApiModelProperty("热量峰值℃")
    @TableField("hotPeak")
    private BigDecimal hotPeak;
    @ApiModelProperty("流量峰值T")
    @TableField("flowPeak")
    private BigDecimal flowPeak;
    @ApiModelProperty("供温峰值℃")
    @TableField("tempSupplyPeak")
    private BigDecimal tempSupplyPeak;
    @TableField("tempReturnPeak")
    @ApiModelProperty("回温峰值℃")
    private BigDecimal tempReturnPeak;
    /**
     * 组织结构id
     */
    @ApiModelProperty("组织结构id")
    @TableField("heatOrganizationId")
    private Integer heatOrganizationId;


//    /**
//     * 所属热网
//     */
//    @ApiModelProperty("所属热网")
//    @TableField("heatNetId")
//    private Integer heatNetId;

    
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
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 管径
     */
    @ApiModelProperty("管径（m）")
    @TableField("pipeDiameter")
    private Integer pipeDiameter;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    @TableField("heatArea")
    private BigDecimal heatArea;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("同步编号")
    @TableField("syncNumber")
    private Integer syncNumber;

    @ApiModelProperty("同步父级编号")
    @TableField("syncParentNum")
    private Integer syncParentNum;
}
