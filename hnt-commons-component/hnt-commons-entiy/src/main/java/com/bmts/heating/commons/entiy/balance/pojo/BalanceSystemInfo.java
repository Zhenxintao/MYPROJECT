package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;

/**
 * @author zxt
 * @description
 * @date 2021/02/02 15:46
 **/
@ApiModel("全网平衡机组基础信息")
@Data
@Description("全网平衡机组基础信息")
public class BalanceSystemInfo {
    @ApiModelProperty("全网平衡Id")
    private Integer balanceNetId;
    @ApiModelProperty("系统名称")
    private String systemName;
    @ApiModelProperty("机组Id")
    private Integer relevanceId;
    @ApiModelProperty("级别")
    private Integer level;
    @ApiModelProperty("系统供热面积")
    private BigDecimal heatArea;
    @ApiModelProperty("控制方式")
    private Integer controlType;
    @ApiModelProperty("是否参与全网平衡")
    private  Boolean status;
    @ApiModelProperty("补偿值")
    private BigDecimal compensation;
    @ApiModelProperty("系统散热方式")
    private Integer heatingType;
    @ApiModelProperty("大类补偿值")
    private BigDecimal compensationValue;
    @ApiModelProperty("下发点名称")
    private String pointName;
}
