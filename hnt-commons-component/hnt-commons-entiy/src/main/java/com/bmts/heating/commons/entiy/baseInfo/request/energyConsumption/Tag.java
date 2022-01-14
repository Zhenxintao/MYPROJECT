package com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Tag {
	@ApiModelProperty("网、源、站、控制柜(0号机组) 或系统Id")
	private Integer groupId;
	//private List<Integer> groupId;
	@ApiModelProperty("默认值1属于系统 2.控制柜 3.站 4.源 5.网")
	private Integer level;
}
