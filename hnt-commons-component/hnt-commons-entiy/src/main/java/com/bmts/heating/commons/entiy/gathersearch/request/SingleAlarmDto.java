package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("单站监测报警实体类")
@Data
public class SingleAlarmDto {
    @ApiModelProperty("开始时间*")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间*")
    private LocalDateTime endTime;
    @ApiModelProperty("报警分类:0为全部分类信息,1.数据报警 2.设备故障")
    private Integer alarmType;
    @ApiModelProperty("换热站Id*")
    private Integer stationId;
    @ApiModelProperty("是否确认（历史）:报警信息是否确认:0为全部信息,1为已确认，2为未确认")
    private Integer isConfirm;
}
