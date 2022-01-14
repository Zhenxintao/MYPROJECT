package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 地板表面平均温度t''pj
 * */
@Data
public class ForecastFloorAvgTemp {
    //室内设计计算温度t‘n
    private BigDecimal designCountTemp;
    // 散热量q'd（W/㎡）
    private BigDecimal heatDissipatingCapacity;
}
