package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测用热量信息（阶段、天）
 * */
@Data
public class ForecastWithHeatHour {
    //面积F
    private BigDecimal area;
    //实际计算热负荷指标q'
    private BigDecimal realCountHeatLoadIndex;
}
