package com.bmts.heating.commons.entiy.forecast.response;

import com.bmts.heating.commons.entiy.forecast.Compensation;
import com.bmts.heating.commons.entiy.forecast.Dispatch;
import com.bmts.heating.commons.entiy.forecast.ForecastHeatSourceDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ForecastPhasesInfoResponse {

    /**
     * 采暖季开始室外计算温度 默认8
     */
    private BigDecimal outsideTemp;

    /**
     * 室外实际计算平均温度
     */
    private BigDecimal outsideComputeTempAvg;

    /**
     * 室外设计温度
     */
    private BigDecimal outsideConfigTemp;

    /**
     * 室内设计温度
     */
    private BigDecimal insideDesignTemp;

    /**
     * 散热器系数
     */
    private BigDecimal radiatorCoefficient;
    
    private Integer id;

    /**
     * 预测名称
     */
    private String name;

    /**
     * 单位面积热指标（w/m2） qf
     */
    private BigDecimal areaHeatingIndex;

    /**
     * 预计热耗
     */
    private BigDecimal predictHeatRate;

    /**
     * 实际热耗qs（GJ/m2）
     */
    private BigDecimal actualHeatRate;

    /**
     * 供热面积(万/㎡)
     */
    private BigDecimal heatArea;

    /**
     * 室内温度 (tn)
     */
    private BigDecimal insideTemp;

    /**
     * 一次网严寒季供水温度 (t´g)
     */
    private BigDecimal firstNetTg;

    /**
     * 计算方式：1.流量法 2.温度法
     */
    private Integer computeType;

    /**
     * 一次网严寒季循环流量 G´rw（t/h）  给定或计算值：
     */
    private BigDecimal firstNetCycleFlow;

    /**
     * 一次网严寒季回水温度t´h（℃） 给定值或计算
     */
    private BigDecimal firstNetTh;

    /**
     * 是否有效
     */
    private Boolean isValid;

    /**
     * 类型：1.一网一源 2.一网多源
     */
    private Integer type;

    /**
     * 散热器 严寒季供水温度
     */
    private BigDecimal radiatorTg;

    /**
     * 散热器 严寒季回水温度
     */
    private BigDecimal radiatorTh;

    /**
     * 地板辐射 严寒季供水温度
     */
    private BigDecimal floorTg;

    /**
     * 地板辐射 严寒季回水温度
     */
    private BigDecimal floorTh;

    /**
     * 散热间距:1.300 2.250 3.200 4.150
     */
    private Integer radiatingSpace;

    /**
     * 选择热源信息
     */
//    private List<ForecastHeatSourceDto> forecastHeatSourceDtoList;
    /**
     * 补偿值信息
     */
    private String compensationList;
    /**
     * 调度信息
     */
    private String dispatchList;
}
