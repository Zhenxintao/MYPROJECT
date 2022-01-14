package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 最大流量的调整Grw
 * */
@Data
public class ForecastMaxFlow {
    //某时刻室外温度下的热负荷指标q''(w/㎡)
    private BigDecimal heatLoadIndex;
    //面积
    private BigDecimal area;
    //一次网地板辐射采暖供水温度t'g
    private BigDecimal designTempG;
    //一次网地板辐射采暖回水温度t'h
    private BigDecimal designTempH;
}
