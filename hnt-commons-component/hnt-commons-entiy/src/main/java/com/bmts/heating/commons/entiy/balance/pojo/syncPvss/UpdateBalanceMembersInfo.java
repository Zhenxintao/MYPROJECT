package com.bmts.heating.commons.entiy.balance.pojo.syncPvss;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.util.List;

/**
 * <p>
 *  修改全网平衡系统对应网信息同步（PVSS）
 * </p>
 *
 * @author 甄鑫涛
 * @since 2021-09-17
 */
@Data
@ApiModel("修改全网平衡系统对应网信息同步（PVSS）")
@Description("修改全网平衡系统对应网信息同步（PVSS）")
public class UpdateBalanceMembersInfo {

//    private Integer id;

    @ApiModelProperty("未修改的全网平衡id")
    private String originalSyncNetBalanceId;

    /**
     * 组员ids
     */
    @ApiModelProperty("组员Id")
    private List<Integer> relevanceIds;

    @ApiModelProperty("进行修改的全网平衡id")
    private String proceedSyncNetBalanceId;
//    /**
//     * 级别
//     */
//    @ApiModelProperty("级别 1.系统 2.控制柜")
//    private Integer level;

//    /**
//     * 是否参与全网平衡
//     */
//    @ApiModelProperty("是否参与全网平衡")
//    private Boolean status;

//    /**
//     * 描述信息
//     */
//    private String description;
//    /**
//     * 控制方式 1.阀控 2.泵控 3.二次供回均温
//     */
//    @ApiModelProperty("控制方式 1.阀控 2.泵控 3.二次供回均温")
//    private int controlType;

//    @ApiModelProperty("补偿值")
//    private BigDecimal compensation;

//    @ApiModelProperty("所属补偿大类")
//    private int balanceCompensationId;


}
