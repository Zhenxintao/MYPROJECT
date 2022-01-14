package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

@Data
/**
 * 系数d计算
 * */
public class ForecastCoefficient {
    //一次网地板辐射采暖供水温度t'g
    private BigDecimal designFTempG;
    //一次网地板辐射采暖回水温度t'h
    private BigDecimal designFTempH;
    //一次管网相对流量G(—)rw
    private BigDecimal relativeDischargeF;
    //二次管网相对流量G(—)ty
    private BigDecimal relativeDischargeS;
    //二次网散热器采暖供水温度t'sg
    private BigDecimal designSTempG;
    //二次网散热器采暖回水温度t'sh
    private BigDecimal designSTempH;
}
