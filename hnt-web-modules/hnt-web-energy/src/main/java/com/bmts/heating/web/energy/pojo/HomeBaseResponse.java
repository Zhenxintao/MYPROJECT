package com.bmts.heating.web.energy.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("首页基础信息")
@Data
public class HomeBaseResponse {

	@ApiModelProperty("室外温度")
	private BigDecimal temp;

	@ApiModelProperty("温度变化")
	private Integer tempIndex;

	@ApiModelProperty("折标热能耗")
	private BigDecimal heat;

	@ApiModelProperty("温度变化")
	private Integer heatIndex;

	@ApiModelProperty("电能耗")
	private BigDecimal ele;

	@ApiModelProperty("温度变化")
	private Integer eleIndex;

	@ApiModelProperty("水能耗")
	private BigDecimal water;

	@ApiModelProperty("温度变化")
	private Integer waterIndex;

	@ApiModelProperty("标识")
	private String index;
}
