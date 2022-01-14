package com.bmts.heating.commons.entiy.baseInfo.response;

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
@ApiModel("设备启停记录曲线")
public class RecordDeviceUpDownCurve {

    @ApiModelProperty("设备状态 true 开启 false停止")
    private Boolean operation;

    @ApiModelProperty("设备类型 1.循环泵 2.补水泵")
    private Integer type;

    @ApiModelProperty("时间")
    private LocalDateTime createTime;

    @ApiModelProperty("周几")
    private int week;

    @ApiModelProperty("数量")
    private int count;

}
