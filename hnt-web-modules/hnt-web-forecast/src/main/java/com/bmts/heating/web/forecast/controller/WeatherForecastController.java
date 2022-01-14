package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryWeatherDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "天气预报管理")
@RestController
@RequestMapping("/weather")
public class WeatherForecastController {

    @Autowired
    private WeatherService weatherService;


    @ApiOperation("新增天气预报")
    @PostMapping("/forecast")
    public Response forecast(@RequestBody WeatherDay dto) {
        return weatherService.addWeatherForecast(dto);
    }

    @ApiOperation("新增小时预报")
    @PostMapping("/real")
    public Response insertReal(@RequestBody WeatherHour dto) {
        return weatherService.addRealTemperature(dto);
    }

    @ApiOperation(value = "查询天气预报信息")
    @PostMapping("/queryWeatherForecast")
    public Response queryWeatherForecast(@RequestBody QueryWeatherDto dto) {
        return weatherService.queryWeatherForecast(dto);
    }

}
