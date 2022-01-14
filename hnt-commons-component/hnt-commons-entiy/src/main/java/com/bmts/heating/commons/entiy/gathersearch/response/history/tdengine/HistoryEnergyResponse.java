package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 能耗历史数据--返回数据响应实体
 */
@Data
public class HistoryEnergyResponse {
    @ApiModelProperty("查询Id标识(系统id或站id)")
    private Integer relevanceId;
    @ApiModelProperty("时间戳")
    private long timeStrap;
    @ApiModelProperty("折算面积")
    private BigDecimal area;
    @ApiModelProperty("水耗")
    private BigDecimal waterConsumption;
    @ApiModelProperty("电耗")
    private BigDecimal electricityConsumption;
    @ApiModelProperty("热耗")
    private BigDecimal heatConsumption;
    @ApiModelProperty("水单耗")
    private BigDecimal waterUnitConsumption;
    @ApiModelProperty("电单耗")
    private BigDecimal electricityUnitConsumption;
    @ApiModelProperty("热单耗")
    private BigDecimal heatUnitConsumption;
    @ApiModelProperty("折标热单耗")
    private BigDecimal niggerHeadHeatUnitConsumption;
    @ApiModelProperty("折标煤")
    private BigDecimal niggerHeadCoal;
    @ApiModelProperty("热力站或者热源名称")
    private String name;
}
