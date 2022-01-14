package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 在室外某时刻室外温度下的热负荷指标q”
 * */
@Data
public class ForecastHeatLoadIndex {
    //热指标q
    private BigDecimal heatLoadIndex;
    //热负荷率η´
    private BigDecimal heatLoadRate;
}
