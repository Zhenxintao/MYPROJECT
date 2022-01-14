package com.bmts.heating.commons.entiy.forecast.history;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ForecastResponse {

    // 热源id 或 热力站id
    private Integer relevanceId=0;
    //实际热量
    private BigDecimal realHeatTotal=new BigDecimal(0);

    //实际热负荷
    private BigDecimal realHeatLoad=new BigDecimal(0);

    //实际热指标
    private BigDecimal heatIndex=new BigDecimal(0);

    //实际流量
    private BigDecimal realFlow=new BigDecimal(0);

    //一次供温
    private BigDecimal heatSourceTg=new BigDecimal(0);

    //一次回温
    private BigDecimal heatSourceTh=new BigDecimal(0);
}
