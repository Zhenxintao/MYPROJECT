package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryWeatherDto;
import com.bmts.heating.commons.entiy.baseInfo.request.RealTemperatureDto;
import com.bmts.heating.commons.entiy.baseInfo.request.WeatherForecastDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestBody;

public interface WeatherService {

    Response addWeatherForecast(WeatherDay dto);

    Response addRealTemperature(WeatherHour dto);

    Response queryWeatherForecast(QueryWeatherDto queryWeatherDto);
}
