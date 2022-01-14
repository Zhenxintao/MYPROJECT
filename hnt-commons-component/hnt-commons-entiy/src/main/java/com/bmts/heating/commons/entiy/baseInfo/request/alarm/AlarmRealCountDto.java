package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmRealCountDto {
    @ApiModelProperty("报警分类:0为全部分类信息,1.数据报警 2.设备故障")
    private Integer alarmType;
    @ApiModelProperty("换热站Id:0为全部信息")
    private Integer stationId;
    @ApiModelProperty("报警信息是否确认:0为全部信息,1为已确认，2为未确认")
    private Integer isConfirm;
}
