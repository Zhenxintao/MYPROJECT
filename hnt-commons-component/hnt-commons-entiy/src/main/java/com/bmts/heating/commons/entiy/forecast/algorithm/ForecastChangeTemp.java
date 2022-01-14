package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 温度改变后一次、二次供回水温度的调整
 * */
@Data
public class ForecastChangeTemp {
    //室内设计计算温度t‘n
    private BigDecimal designCountTemp;
    //室内实际计算温度tn
    private BigDecimal realCountTemp;
    //设计室外计算温度t'w
    private BigDecimal designOutCountTemp;
    //供水温度
    private BigDecimal designTempG;
    //回水温度
    private BigDecimal designTempH;
}
