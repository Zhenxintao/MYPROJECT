package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CabinetEntityBaseInfo {

	@ApiModelProperty("控制柜Id")
	private Integer cabinetId;

	@ApiModelProperty("控制柜名称")
	private String cabinetName;

	@ApiModelProperty("系统Id")
	private Integer systemId;

	@ApiModelProperty("系统名称")
	private String systemName;
}
