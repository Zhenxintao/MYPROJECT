package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 在室外某时刻室外温度下，在热负荷调整系数β下的实际计算热负荷指标q´
 * */
@Data
public class ForecastCoefficientHeatLoadIndex {
    //热指标q”
    private BigDecimal heatLoadIndex;
    //调整系数
    private BigDecimal coefficient;
}
