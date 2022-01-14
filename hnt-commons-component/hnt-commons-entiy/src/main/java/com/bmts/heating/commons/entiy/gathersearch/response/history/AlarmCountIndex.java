package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "报警首页数量实体类")
@Data
public class AlarmCountIndex {
    @ApiModelProperty("报警实时统计")
    private Integer alarmStatistics = 3000;
    @ApiModelProperty("报警统计总数")
    private Integer alarmSumCount = 12400;
    @ApiModelProperty("数据报警已确定总数")
    private Integer alarmSumDataCountY = 5500;
    @ApiModelProperty("数据报警当月已确定总数")
    private Integer alarmDataMonthY =29;
    @ApiModelProperty("数据报警未确定总数")
    private Integer alarmSumDataCountW = 1500;
    @ApiModelProperty("数据报警当月未确定总数")
    private Integer alarmDataMonthW =21;
    @ApiModelProperty("设备报警已确定总数")
    private Integer alarmFacilityCountY = 3000;
    @ApiModelProperty("设备报警当月已确定总数")
    private Integer alarmFacilityMonthY =18;
    @ApiModelProperty("设备报警未确定总数")
    private Integer alarmFacilityCountW = 2177;
    @ApiModelProperty("设备报警当月未确定总数")
    private Integer alarmFacilityMonthW =43;
    @ApiModelProperty("换热站数量")
    private Integer stationCount =41;
}
