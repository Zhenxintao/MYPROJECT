package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 温度预测方式设置json形式(开始时间、结束时间、温度值、温度预测类型：1.温度改变 2.流量改变)
 */
@Data
@ApiModel("温度预测方式设置json形式(开始时间、结束时间、温度值、温度预测类型：1.温度改变 2.流量改变)")
public class TempSetting {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("设置温度值")
    private BigDecimal tempValue;
    @ApiModelProperty("负荷预测温度设定类型:1.温度改变 2.流量改变")
    private Integer tempType;
}
