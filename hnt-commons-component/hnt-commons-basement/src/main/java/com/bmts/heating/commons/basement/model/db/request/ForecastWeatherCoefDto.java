package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForecastWeatherCoefDto{

	private int id;
	@ApiModelProperty("风力")
	private List<WindPower> windPower;


	@ApiModelProperty("风向")
	private List<WindDirection> windDirection;


	@ApiModelProperty("天气情况")
	private List<Weather> weather;


}
