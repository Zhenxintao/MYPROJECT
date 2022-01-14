package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/4/28 17:04
 **/
@Data
@ApiModel("天气对比响应类")
public class WeatherTempComparison implements Serializable {
    @ApiModelProperty("当前天气")
    BigDecimal current;
    @ApiModelProperty("昨日天气")
    BigDecimal beforeDay;
    @ApiModelProperty("同比天气")
    BigDecimal beforeYear;
}
