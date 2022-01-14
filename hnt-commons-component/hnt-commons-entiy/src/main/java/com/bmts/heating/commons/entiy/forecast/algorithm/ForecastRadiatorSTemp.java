package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测散热器供暖二次网供回水温度
 * */
@Data
public class ForecastRadiatorSTemp {
    //计算温度:标准工况为t‘n;发生改变为tn
    private BigDecimal designCountTemp;
    //二次网散热器采暖供水温度：标准工况为t'sg;发生改变为t''sg
    private BigDecimal designTempG;
    //二次网散热器采暖回水温度：标准工况为t'sh;发生改变为t''sh
    private BigDecimal designTempH;
    //室内实际计算温度tn
    private BigDecimal realCountTemp;
    ///预测二次供回水温度负荷率：标准工况为nz;发生改变为n;
    private BigDecimal loadRateStandardConditions;
    //二次管网相对流量G(—)ty
    private BigDecimal relativeDischargeS;
//    //类型：1:标准工况  2:发生改变
//    private  Integer type;
}
