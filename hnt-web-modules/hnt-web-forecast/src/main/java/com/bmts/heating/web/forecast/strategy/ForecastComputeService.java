package com.bmts.heating.web.forecast.strategy;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.basement.model.db.request.ForecastRequest;

public interface ForecastComputeService {

    // 预测一次供回水温度
    ForecastSourceHistory forecastTgTh(ForecastRequest forecastRequest);

    // 预测用热量
    ForecastSourceHistory forecastUseHeat(ForecastRequest forecastRequest);


}
