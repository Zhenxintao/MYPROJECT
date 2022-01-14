package com.bmts.heating.commons.entiy.forecast.algorithm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 一次网的相对流量(当室内温度由室内设计计算温度t´n变为tn下的流量的调整)
 * */
@Data
public class ForecastRelativeFlowRate {
    //一次管网阶段循环流量
    private BigDecimal  netStageFlow;
    //最大流量的调整Grw
    private BigDecimal  maxFlow;
}
