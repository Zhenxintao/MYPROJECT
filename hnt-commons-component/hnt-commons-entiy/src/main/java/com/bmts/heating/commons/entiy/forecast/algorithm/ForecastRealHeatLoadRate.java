package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 实际计算热负荷率η
 * */
@Data
public class ForecastRealHeatLoadRate {
    //调整系数热指标q´
    private BigDecimal coefficientHeatLoadIndex;
    //热负荷指标q
    private BigDecimal heatLoadIndex;
}
