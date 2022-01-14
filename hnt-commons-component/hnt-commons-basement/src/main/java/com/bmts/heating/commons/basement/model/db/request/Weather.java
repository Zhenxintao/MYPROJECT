package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Weather {
	@ApiModelProperty("天气情况")
	private String weather;

	@ApiModelProperty("天气系数")
	private String weatherCoefficient;
}
