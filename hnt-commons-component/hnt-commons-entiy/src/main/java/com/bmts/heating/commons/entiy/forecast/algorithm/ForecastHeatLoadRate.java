package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 在室外某时刻室外温度下标准工况热负荷率ηz
 * */
@Data
public class ForecastHeatLoadRate {
    /**
     * 室内设计计算温度t‘n
     * */
    private BigDecimal designCountTemp;
    /**
     * 室内设计计算温度t‘w
     * */
    private BigDecimal designOutCountTemp;
    /**
     * 采暖季预测室外温度tw
     * */
    private BigDecimal designForecastTemp;
}
