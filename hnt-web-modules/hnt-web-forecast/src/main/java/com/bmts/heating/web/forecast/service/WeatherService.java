package com.bmts.heating.web.forecast.service;

import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryWeatherDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface WeatherService {

    Response addWeatherForecast(WeatherDay dto);

    Response addRealTemperature(WeatherHour dto);

    Response queryWeatherForecast(QueryWeatherDto queryWeatherDto);
}
