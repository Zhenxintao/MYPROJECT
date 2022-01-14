package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 当室内温度由室内设计计算温度t´n变为tn下的严寒季热负荷指标q(W/㎡)
 * */
@Data
public class ForecastTempHeatLoadIndex {
    //室内实际计算温度tn
    private BigDecimal designRealCountTemp;
    //室内设计计算温度t‘n
    private BigDecimal designCountTemp;
    //设计室外计算温度t'w
    private BigDecimal designOutCountTemp;
    //单位面积设计热指标
    private BigDecimal areaHeatIndex;
}
