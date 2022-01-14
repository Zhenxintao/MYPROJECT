package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/1/25 17:57
 **/
@Data
@ApiModel("水电热曲线")
public class WaterPowerHeatCurveResponse {
    @ApiModelProperty("水")
    BigDecimal water;
    @ApiModelProperty("电")
    BigDecimal power;
    @ApiModelProperty("热")
    BigDecimal heat;
    @ApiModelProperty("时间")
    LocalDateTime time;
    @ApiModelProperty("室外温度")
    BigDecimal tempOut;
}
