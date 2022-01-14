package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/2/5 16:32
 **/
@Data
public class BalanceMembersBase {

    private Integer id;


    @ApiModelProperty("全网平衡id")
    private Integer balanceNetId;

    /**
     * 组员id
     */

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

    private int controlType;

    @ApiModelProperty("补偿值")
    private BigDecimal compensation;

    @ApiModelProperty("所属补偿大类")
    private int balanceCompensationId;

    @ApiModelProperty(value = "分类名称")
    private String name ;

    @ApiModelProperty(value = "值")
    private String compensationValue;
}
