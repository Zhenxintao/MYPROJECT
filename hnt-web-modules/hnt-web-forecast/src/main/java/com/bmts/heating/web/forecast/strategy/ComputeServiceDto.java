package com.bmts.heating.web.forecast.strategy;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 负荷预测 业务 实体类
 */
@Data
public class ComputeServiceDto {

    // 计算业务service  接口
    private ForecastComputeService forecastComputeService;

    // 计算方式
    private Integer computeType;

    //  室内实际计算温度 或  补偿值系数
    private BigDecimal commonValue;

    // 时间间隔
    private Integer interval;


}
