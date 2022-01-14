package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WindPower {
	@ApiModelProperty("风力")
	private String windPower;

	@ApiModelProperty("风力系数")
	private String windPowerCoefficient;

}
