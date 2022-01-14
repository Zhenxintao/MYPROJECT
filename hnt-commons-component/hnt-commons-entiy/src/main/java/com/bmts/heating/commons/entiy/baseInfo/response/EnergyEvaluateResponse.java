package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/5/7 17:28
 **/
@Data
@ApiModel("能耗评价数据")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyEvaluateResponse {
    @ApiModelProperty("室外温度")
    private BigDecimal outsideTemp;
    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;
    @ApiModelProperty("总耗")
    private BigDecimal heatTotalCost;
    @ApiModelProperty("单耗")
    private BigDecimal singalCost;
    @ApiModelProperty("折标总耗")
    private BigDecimal niggerHeadTotalCost;
    @ApiModelProperty("折标单耗")
    private BigDecimal niggerHeadSingalCost;
    @ApiModelProperty("达标数据")
    Object reachStandardResponseList;
}
