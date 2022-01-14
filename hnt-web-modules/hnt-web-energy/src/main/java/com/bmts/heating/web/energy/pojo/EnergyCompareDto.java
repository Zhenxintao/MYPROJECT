package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EnergyCompareDto {

	//1:热源 2:热网 3:热力站
	@ApiModelProperty("1:热源 2:热网 3:热力站")
	private Integer type = 3;

	//相关id
	@ApiModelProperty("相关id")
	private Integer relevanceId;

	//1:水 2:电 3:热
	@ApiModelProperty("1:水 2:电 3:热")
	private Integer energyType = 3;

	@ApiModelProperty("对比类型 1:同比 2:环比")
	private Integer compare = 1;

	@ApiModelProperty("周期")
	private Integer cycle = 5;

	//时间
	private Long startTime = LocalTimeUtils.getDay(-1);

	private Long endTime = LocalTimeUtils.getDay(0);
}
