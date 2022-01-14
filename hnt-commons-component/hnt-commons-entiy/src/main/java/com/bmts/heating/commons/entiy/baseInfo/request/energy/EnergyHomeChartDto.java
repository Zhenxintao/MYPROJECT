package com.bmts.heating.commons.entiy.baseInfo.request.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("能耗Top")
@Data
public class EnergyHomeChartDto {
	//1:水  2:电  3:热
	@ApiModelProperty("1:水  2:电  3:热")
	private Integer energyType;

	//1能耗 2单耗
	@ApiModelProperty("1能耗 2单耗")
	private Integer source;

	//1:小时 2:天 3:月
	@ApiModelProperty("查询方式1:小时 2:天 3:月")
	private Integer type;

	@ApiModelProperty("获取数据条数")
	private int top = 10;

	@ApiModelProperty("asc")
	private Boolean isAsc;
}
