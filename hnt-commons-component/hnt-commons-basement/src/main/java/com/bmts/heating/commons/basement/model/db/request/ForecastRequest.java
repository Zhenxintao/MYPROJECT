package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预测 数据入参
 */
@Data
public class ForecastRequest {

    // 热源预测供暖季信息表
    private ForecastSourceBasic forecastSourceBasic;

    // 预测热源常数表
    private ForecastSourceCore sourceCore;

    // 负荷预测阶段流量设定表
    private ForecastSourceHeatSeason heatSeason;

    // 预测计算需要的 温度   （weather_real_temperature 表的数据）
    private BigDecimal forecastHourAvgTemperature;

    // 时间间隔
    private Integer interval;

    //  室内实际计算温度 或  补偿值系数
    private BigDecimal commonValue;

    //  面积计算倍数
    private BigDecimal areaMultiple;


    // `type`  '1.阶段 2.小时 3.天'
    private Integer forecastType=0;

//    // 室内实际计算温度 （对于温度法 ，流量法 使用的  ）
//    private BigDecimal realCountTemp;
//
//    // 补偿系数法   的补偿值  （补偿系数法 使用的）
//    private BigDecimal compensationValue;


}
