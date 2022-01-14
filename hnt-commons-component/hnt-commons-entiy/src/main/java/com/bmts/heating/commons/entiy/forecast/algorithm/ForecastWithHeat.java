package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测用热量信息（阶段、天）
 * */
@Data
public class ForecastWithHeat {
//    //室内实际计算温度tn
//    private BigDecimal realCountTemp;
//    //预测气温
//    private BigDecimal forecastTemp;
//    //设计室外计算温度t'w
//    private BigDecimal designOutCountTemp;
    //面积F
    private BigDecimal area;
    //配置热负荷指标q
    private BigDecimal realCountHeatLoadIndex;
    //区间个数（共多少个小时）
    private Integer hrNumber;
}
