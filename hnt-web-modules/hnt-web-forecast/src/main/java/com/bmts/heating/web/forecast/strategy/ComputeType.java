package com.bmts.heating.web.forecast.strategy;

import com.bmts.heating.web.forecast.service.impl.CompensationFactorServiceImpl;
import com.bmts.heating.web.forecast.service.impl.FlowServiceImpl;
import com.bmts.heating.web.forecast.service.impl.StandardConditionServiceImpl;
import com.bmts.heating.web.forecast.service.impl.TemperatureServiceImpl;

/**
 * @ClassName: ComputeType
 * @Description: 计算 枚举
 * @Author: pxf
 * @Date: 2021/4/14 17:23
 * @Version: 1.0
 */
public enum ComputeType {

    // 1   标准工况 计算方式
    STANDARD_CONDITION(1, new StandardConditionServiceImpl()),
    // 2   温度法 计算方式
    TEMPERATURE_COMPUTE(2, new TemperatureServiceImpl()),
    // 3   流量法 计算方式
    FLOW_COMPUTE(3, new FlowServiceImpl()),
    // 4   补偿系数法 计算方式
    COMPENSATION_FACTOR(4, new CompensationFactorServiceImpl()),

    ;

    // 计算方式
    private Integer computeType;

    // 这是一个接口
    private ForecastComputeService computeService;

    ComputeType(Integer computeType, ForecastComputeService computeService) {
        this.computeType = computeType;
        this.computeService = computeService;
    }

    public Integer type() {
        return computeType;
    }

    public ForecastComputeService getCompute() {
        return computeService;
    }

    public static ComputeType getByType(Integer type) {
        for (ComputeType computeEnum : ComputeType.values()) {
            if (computeEnum.computeType == type) {
                return computeEnum;
            }
        }
        return null;
    }

}
