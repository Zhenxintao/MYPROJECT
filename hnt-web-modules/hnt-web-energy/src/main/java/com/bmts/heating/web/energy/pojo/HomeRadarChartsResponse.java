package com.bmts.heating.web.energy.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("首页雷达图")
@Data
public class HomeRadarChartsResponse {

	@ApiModelProperty("折标热能耗")
	private BigDecimal heat;

	@ApiModelProperty("电能耗")
	private BigDecimal ele;

	@ApiModelProperty("水能耗")
	private BigDecimal water;

	@ApiModelProperty("热单耗")
	private BigDecimal unitHeat;

	@ApiModelProperty("电单耗")
	private BigDecimal unitEle;

	@ApiModelProperty("水单耗")
	private BigDecimal unitWater;
}
