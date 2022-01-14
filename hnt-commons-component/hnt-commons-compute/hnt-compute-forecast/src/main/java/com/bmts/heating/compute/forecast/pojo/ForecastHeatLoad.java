package com.bmts.heating.compute.forecast.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测热负荷 字段实体类
 */
@Data
public class ForecastHeatLoad {

    // 热负荷指标q”
    private BigDecimal heatLoadIndex;

    // 热负荷率
    private BigDecimal loadRate;


}
