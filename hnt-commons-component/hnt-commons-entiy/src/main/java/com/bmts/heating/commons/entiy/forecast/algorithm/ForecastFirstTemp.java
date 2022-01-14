package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测一次网供回水温度
 * */
@Data
public class ForecastFirstTemp {
    //一次网地板辐射采暖供水温度t'g
    private BigDecimal designTempG;
    //一次网地板辐射采暖回水温度t'h
    private BigDecimal designTempH;
    //预测一次供回水温度负荷率：标准工况为nz;发生改变为n;
    private BigDecimal loadRateStandardConditions;
    //一次管网相对流量G(—)rw
    private BigDecimal relativeDischargeF;
    //预测二次网散热器供水温度tsg
    private BigDecimal forecastSTempG;
    //预测二次网散热器回水温度tsh
    private BigDecimal forecastSTempH;
    //系数d
    private BigDecimal coefficient;
    private  Integer type;
}
