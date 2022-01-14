package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 当室内温度由室内设计计算温度t´n变为tn下的二网供回水温度-一次网严寒季供回水温度的调整
 * */
@Data
public class ForecastChangeFirstTemp {
    //室内设计计算温度t‘n
    private BigDecimal designCountTemp;
    //室内实际计算温度tn
    private BigDecimal realCountTemp;
    //设计室外计算温度t'w
    private BigDecimal designOutCountTemp;
    //一次网设计供水温度
    private BigDecimal designFirstTempG;
    //一次网设计回水温度
    private BigDecimal designFirstTempH;
    //二次网设计供水温度
    private BigDecimal designSecondTempG;
    //二次网设计回水温度
    private BigDecimal designSecondTempH;
    //二次网改变供水温度
    private BigDecimal designSecondTempGChange;
    //二次网改变回水温度
    private BigDecimal designSecondTempHChange;
}
