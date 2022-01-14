package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.basement.model.db.request.ForecastWeatherCoefDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastWeatherCoefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forecastWeather")
@Api(tags = "预测天气系数")
public class ForecastWeatherCoefController {
	@Autowired
	private ForecastWeatherCoefService forecastWeatherCoefService;

	@PostMapping("/insert")
	@ApiOperation("新增")
	public Response insertForecastWeatherCoef(@RequestBody ForecastWeatherCoefDto forecastWeatherCoefdto){
		return forecastWeatherCoefService.insertForecastWeatherCoef(forecastWeatherCoefdto);
	}

	@PostMapping("find")
	@ApiOperation("查询")
	public Response findForecastWeatherCoef(@RequestBody BaseDto baseDto){
		return forecastWeatherCoefService.findForecastWeatherCoef(baseDto);
	}

	@PutMapping("update")
	@ApiOperation("修改")
	public Response updateForecastWeatherCoef(@RequestBody ForecastWeatherCoefDto forecastWeatherCoefdto){
		forecastWeatherCoefService.updateForecastWeatherCoef(forecastWeatherCoefdto);
		return Response.success();
	}

	@DeleteMapping("delete")
	@ApiOperation("删除")
	public Response deleteForecastWeatherCoef(@RequestParam int id){
		forecastWeatherCoefService.deleteForecastWeatherCoef(id);
		return Response.success();
	}
}
