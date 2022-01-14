package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;

@ApiModel("全网平衡温度差距饼图信息")
@Data
@Description("全网平衡温度差距饼图信息")
public class BalanceTempGap {
    @ApiModelProperty("全网平衡Id")
    private Integer balanceNetId;
    @ApiModelProperty("系统Id")
    private Integer systemId;
    @ApiModelProperty("级别Id")
    private Integer levelId;
    @ApiModelProperty("计算目标均温")
    private BigDecimal computeTargetAvg;
    @ApiModelProperty("温度差距值")
    private float  gapValue;
}
