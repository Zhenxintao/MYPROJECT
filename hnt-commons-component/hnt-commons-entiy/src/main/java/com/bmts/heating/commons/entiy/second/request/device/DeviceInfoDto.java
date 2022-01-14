package com.bmts.heating.commons.entiy.second.request.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description 设备信息
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("设备信息")
public class DeviceInfoDto {

    @ApiModelProperty("设备code")
    private String deviceCode;
    @ApiModelProperty("设备类型： 1.单元阀门 2.户阀 3.室温")
    private Integer deviceType;
    @ApiModelProperty("13位时间戳")
    private long timestamp;
    @ApiModelProperty("供温")
    private BigDecimal tg;
    @ApiModelProperty("回温")
    private BigDecimal th;
    @ApiModelProperty("供压")
    private BigDecimal pg;
    @ApiModelProperty("回压")
    private BigDecimal ph;
    @ApiModelProperty("设定开度")
    private BigDecimal acture_opening;
    @ApiModelProperty("给定开度")
    private BigDecimal given_opening;
    @ApiModelProperty("室温")
    private BigDecimal temperature;

}
