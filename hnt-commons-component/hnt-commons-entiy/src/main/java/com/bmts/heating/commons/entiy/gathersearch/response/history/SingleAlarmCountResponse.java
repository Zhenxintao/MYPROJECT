package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单站监测监控报警统计响应体")
public class SingleAlarmCountResponse {
    @ApiModelProperty("供暖季至今统计")
    private Integer alarmSumCount = 29;
    @ApiModelProperty("当前报警数量")
    private Integer alarmRealCount = 4;
    @ApiModelProperty("已确定")
    private Integer alarmDetermined = 99;
    @ApiModelProperty("未确定")
    private Integer alarmNotDetermined  = 10;

}
