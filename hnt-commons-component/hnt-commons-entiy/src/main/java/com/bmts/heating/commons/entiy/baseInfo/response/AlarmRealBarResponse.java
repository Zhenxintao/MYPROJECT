package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("实时换热站报警信息")
@Data
public class AlarmRealBarResponse {
    @ApiModelProperty("换热站Id")
    private Integer stationId;

    @ApiModelProperty("换热站名称")
    private String StationName;

    @ApiModelProperty("报警数量")
    private Integer stationAlarmCount;
}
