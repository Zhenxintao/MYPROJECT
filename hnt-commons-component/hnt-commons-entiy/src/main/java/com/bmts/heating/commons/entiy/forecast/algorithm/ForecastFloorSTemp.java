package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测地板辐射供暖二次网供回水温度
 * */
@Data
public class ForecastFloorSTemp {
    //计算温度:标准工况为t‘n;发生改变为tn
    private BigDecimal designCountTemp;
    //二次网地板辐射采暖供水温度：标准工况为t'dg;发生改变为t''dg
    private BigDecimal designTempG;
    //二次网地板辐射采暖回水温度：标准工况为t'dh;发生改变为t''sh
    private BigDecimal designTempH;
    //预测二次供回水温度负荷率：标准工况为nz;发生改变为n;
    private BigDecimal loadRateStandardConditions;
    //二次管网相对流量G(—)ty
    private BigDecimal relativeDischargeS;
    // 采暖季设计室外平均温度℃ t‘pj
    private BigDecimal designOutdoorAvgTemp;
    // 地板表面平均温度℃ t‘’pj
    private BigDecimal floorAvgTemp;
    //类型：1:标准工况  2:发生改变
//    private  Integer type;
}
