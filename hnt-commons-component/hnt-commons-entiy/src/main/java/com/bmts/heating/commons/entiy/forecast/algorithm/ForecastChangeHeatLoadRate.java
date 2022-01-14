package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 在室外某时刻室外温度下的热负荷率η´
 * */
@Data
public class ForecastChangeHeatLoadRate {
    //室内实际计算温度tn
    private BigDecimal designRealCountTemp;
    //设计室外计算温度t'w
    private BigDecimal designOutCountTemp;
    //采暖季预测室外温度tw
    private BigDecimal designForecastTemp;
}
