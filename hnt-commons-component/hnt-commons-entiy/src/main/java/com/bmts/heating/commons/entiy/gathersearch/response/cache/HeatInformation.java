package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("获取首页热源、站等基本信息")
@Data
public class HeatInformation {
    @ApiModelProperty("热源数量")
    private Integer heatSouceCount = 8;
    @ApiModelProperty("热力站数量")
    private Integer heatStationCount = 322;
    @ApiModelProperty("热力站系统数量")
    private Integer heatStationNarrayCount = 580;
    @ApiModelProperty("供暖季总天数")
    private Integer heatSeasonCount =188;
    @ApiModelProperty("已供天数")
    private Integer heatSeasonCountY = 160;
    @ApiModelProperty("未供天数")
    private Integer heatSeasonCountW =28;
    @ApiModelProperty("在网面积")
    private Integer inNetArea = 292920;
    @ApiModelProperty("供暖面积")
    private Integer heatArea =222022;
    @ApiModelProperty("每日增加面积")
    private Integer dailyIncreaseArea = 23;
}
