package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pxf
 * @description 设备启停记录
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("设备启停记录")
public class RecordDeviceUpDownDto extends BaseDto {
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("设备类型 1.循环泵 2.补水泵")
    private Integer type;
    @ApiModelProperty("热力站id")
    private Integer heatTransferStationId;
}
