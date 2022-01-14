package com.bmts.heating.commons.entiy.second.request.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description 设备故障
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("设备故障")
public class DeviceFaultDto {

    @ApiModelProperty("设备code")
    private String deviceCode;
    @ApiModelProperty("设备类型： 1.单元阀门 2.户阀 3.室温")
    private Integer deviceType;
    @ApiModelProperty("13位时间戳")
    private long timestamp;
    @ApiModelProperty("故障码")
    private String code;
    @ApiModelProperty("描述")
    private String desc;
    @ApiModelProperty("fault true代表故障，false代表故障恢复")
    private Boolean fault;
}
