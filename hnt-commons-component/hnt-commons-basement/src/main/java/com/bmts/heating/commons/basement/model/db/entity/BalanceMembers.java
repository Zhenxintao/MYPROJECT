package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("balanceMembers")
@ApiModel("组员信息")
public class BalanceMembers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("balanceNetId")
    @ApiModelProperty("全网平衡id")
    private Integer balanceNetId;

    /**
     * 组员id
     */
    @TableField("relevanceId")
    @ApiModelProperty("组员Id")
    private Integer relevanceId;

    /**
     * 级别
     */
    @ApiModelProperty("级别 1.系统 2.控制柜")
    private Integer level;

    /**
     * 是否参与全网平衡
     */
    @ApiModelProperty("是否参与全网平衡")
    private Boolean status;

    /**
     * 描述信息
     */
    private String description;
    /**
     * 控制方式 1.阀控 2.泵控 3.二次供回均温
     */
    @ApiModelProperty("控制方式 1.阀控 2.泵控 3.二次供回均温")
    @TableField("controlType")
    private int controlType;

    @ApiModelProperty("补偿值")
    private BigDecimal compensation;

    @ApiModelProperty("所属补偿大类")
    @TableField("balanceCompensationId")
    private int balanceCompensationId;


}
