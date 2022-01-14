package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("负荷预测配置信息相应类")
public class ForecastWebPageConfig {

    /**
     * 负荷预测 计算的最高温度
     */
    private BigDecimal forecastTempHigh;

    /**
     * 负荷预测 计算的最低温度
     */
    private BigDecimal forecastTempLow;

    /**
     * 负荷预测面积倍数
     */
    private BigDecimal forecastAreaMultiple;



}
