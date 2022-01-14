package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmts.heating.commons.basement.model.db.request.Weather;
import com.bmts.heating.commons.basement.model.db.request.WindDirection;
import com.bmts.heating.commons.basement.model.db.request.WindPower;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("forecast_weather_coef")
@Accessors(chain = true)
public class ForecastWeatherCoef implements Serializable {

	private static final long  serialVersionUID = 1L;

	@TableId(value = "Id", type = IdType.AUTO)
	private int id;

	@TableField("wind_power")
	@ApiModelProperty("风力")
	private String windPower;

	@TableField("wind_direction")
	@ApiModelProperty("风向")
	private String windDirection;

	@TableField("weather")
	@ApiModelProperty("天气情况")
	private String weather;

	@TableField(exist = false)
	@ApiModelProperty("风力系数")
	private String windPowerCoefficient;

	@TableField(exist = false)
	@ApiModelProperty("风向系数")
	private String windDirectionCoefficient;

	@TableField(exist = false)
	@ApiModelProperty("天气系数")
	private String weatherCoefficient;
	@TableField(exist = false)
	private List<WindDirection> windDirections;
	@TableField(exist = false)
	private List<WindPower> windPowers;
	@TableField(exist = false)
	private List<Weather> weathers;
}
