package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyPointResponse extends CabinetEntityBaseInfo {

	private Integer id;

	@ApiModelProperty("系统参量名称")
	private String pointStandardName;

	@ApiModelProperty("参量标识")
	private String columnName;

	@ApiModelProperty("点Id")
	private Integer pointId;

	@ApiModelProperty("常量值")
	private Double val;
}
