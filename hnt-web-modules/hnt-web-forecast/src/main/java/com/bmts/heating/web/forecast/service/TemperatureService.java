package com.bmts.heating.web.forecast.service;


import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.basement.model.db.request.ForecastRequest;

/**
 * @ClassName: TemperatureService
 * @Description: 温度法 计算
 * @Author: pxf
 * @Date: 2021/4/8 14:16
 * @Version: 1.0
 */
public interface TemperatureService {

    // 预测一次供回水温度
    ForecastSourceHistory forecastTgTh(ForecastRequest forecastRequest);

    // 预测用热量
    ForecastSourceHistory forecastUseHeat(ForecastRequest forecastRequest);
}
