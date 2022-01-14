package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("报警实时Dto")
public class AlarmRealDto extends BaseDto {
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("站点名称")
    private String stationName;
//    @ApiModelProperty("报警类型")
//    private Integer classify;
    @ApiModelProperty("是否确认（历史）:报警信息是否确认:0为全部信息,1为未确认，2为已确认")
    private Integer isConfirm;
}
