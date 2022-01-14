package com.bmts.heating.web.forecast.service;


import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.utils.restful.Response;

public interface ForecastCalculateService {
     void phasesForecast(Integer id);
     ForecastSourceBasic forecastSourceBasicInfo();
}
