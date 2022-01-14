package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AreaManagerDto {

	@ApiModelProperty("关联id")
	private Integer relevanceId;

	@ApiModelProperty("面积")
	private BigDecimal area;

	@ApiModelProperty("层级 系统:1 控制柜:2 换热站:3 热源:4 热网:5 系统分支:6")
	private Integer level;

}
