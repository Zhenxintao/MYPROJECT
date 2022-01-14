package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryWeatherDto;
import com.bmts.heating.commons.entiy.baseInfo.request.RealTemperatureDto;
import com.bmts.heating.commons.entiy.baseInfo.request.WeatherForecastDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response addWeatherForecast(WeatherDay dto) {
        return tsccRestTemplate.doHttp("/weather/forecast", dto, baseServer, Response.class,HttpMethod.POST);
    }

    @Override
    public Response addRealTemperature(WeatherHour dto) {
        return tsccRestTemplate.doHttp("/weather/real",dto,baseServer,Response.class, HttpMethod.POST);
    }

    @Override
    public Response queryWeatherForecast(QueryWeatherDto queryWeatherDto) {
        if (queryWeatherDto.getType()==3) {
            return tsccRestTemplate.post("/weather/queryWeatherForecast", queryWeatherDto, baseServer, Response.class);
        }
        else {
            return tsccRestTemplate.post("/weather/queryRealTemperature", queryWeatherDto, baseServer, Response.class);
        }
    }
}
