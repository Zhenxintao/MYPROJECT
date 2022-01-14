package com.bmts.heating.web.forecast.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceDetail;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.entiy.forecast.history.ForecastResponse;
import com.bmts.heating.commons.entiy.forecast.response.IndexPerformanceResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ForecastCoreService {
    Response forecastCoreCompare(Integer id);
    Response forecastCorePerformance(Integer id);
    Response forecastCoreEvaluate(Integer id);
    BigDecimal forecastTemp(LocalDateTime time);
    ForecastSourceHistory sumHeatLoad(LocalDateTime start, LocalDateTime end, Integer id, Integer type);
//    ForecastResponse sumHeatUnitConsumption(LocalDateTime start, LocalDateTime end, List<Integer> ids);
    List<IndexPerformanceResponse.ForecastHeatLoad> forecastInfo(Integer id);
    List<Integer> forecastSourceDetailList(Integer id);
}
