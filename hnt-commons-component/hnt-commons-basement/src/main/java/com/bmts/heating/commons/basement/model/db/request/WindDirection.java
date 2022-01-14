package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WindDirection {
	@ApiModelProperty("风向")
	private String windDirection;
	@ApiModelProperty("风向系数")
	private String windDirectionCoefficient;

}
