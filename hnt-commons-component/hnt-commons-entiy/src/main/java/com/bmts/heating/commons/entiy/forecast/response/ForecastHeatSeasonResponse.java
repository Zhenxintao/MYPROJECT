package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ForecastHeatSeasonResponse {

    @ApiModelProperty("forecastSourceCoreId")
    private Integer forcastSourceCoreId;

    // 预测 总计 热指标
    @ApiModelProperty("预测 总计 热指标")
    private BigDecimal forecastThermalIndex;

    // 实际 总计 热指标
    @ApiModelProperty("实际 总计 热指标")
    private BigDecimal realThermalIndex;

    private List<SeasonCurve> listSeasonCurve;


}
