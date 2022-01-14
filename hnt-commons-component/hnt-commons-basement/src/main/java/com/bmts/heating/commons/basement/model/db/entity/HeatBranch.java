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
@TableName("heatBranch")
@ApiModel("控制设备分支实体")
public class HeatBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 机组编号
     */
    @ApiModelProperty("机组编号")
    private Integer number;

    /**
     * 热表类型
     */
    @ApiModelProperty("热表类型")
    @TableField("hotListType")
    private Integer hotListType;

    /**
     * 热表口径
     */
    @ApiModelProperty("热表口径")
    @TableField("hotListCaliber")
    private Integer hotListCaliber;

    /**
     * 热表编号
     */
    @ApiModelProperty("热表编号")
    @TableField("hotListNumber")
    private Integer hotListNumber;

    /**
     * 官网口径
     */
    @ApiModelProperty("官网口径")
    @TableField("webSitCaliber")
    private Integer webSitCaliber;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    @TableField("heatArea")
    private BigDecimal heatArea;

    /**
     * 父id，用于子分支
     */
    @ApiModelProperty("父id，用于子分支")
    private Integer pid;

    /**
     * 唯一编码
     */
    @ApiModelProperty("唯一编码")
    private String code;

    /**
     * 所属机柜id
     */
    @ApiModelProperty("所属机柜id")
    @TableField("heatCabinetId")
    private Integer heatCabinetId;

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
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
