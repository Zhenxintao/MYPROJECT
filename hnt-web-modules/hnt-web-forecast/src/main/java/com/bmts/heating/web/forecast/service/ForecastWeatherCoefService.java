package com.bmts.heating.web.forecast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.ForecastWeatherCoef;
import com.bmts.heating.commons.basement.model.db.request.ForecastWeatherCoefDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface ForecastWeatherCoefService extends IService<ForecastWeatherCoef> {

    Response insertForecastWeatherCoef(ForecastWeatherCoefDto forecastWeatherCoefdto);

    Response findForecastWeatherCoef(BaseDto baseDto);

    Response updateForecastWeatherCoef(ForecastWeatherCoefDto forecastWeatherCoefdto);

    Response deleteForecastWeatherCoef(int id);
}
