package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("能耗导入类")
@Data
public class EnergyConsumptionDto extends BaseDto {

	@ApiModelProperty("是否参与计算  1 参与 2 不参与")
	private Integer isCaculate;

	@ApiModelProperty("类型 4 热源 3 热力站")
	private Integer type;


	@ApiModelProperty("网、源、站、控制柜(0号机组) 或系统Id")
	private Integer groupId;

	@ApiModelProperty("默认值1属于系统 2.控制柜 3.站 4.源 5.网")
	private Integer level;

	@ApiModelProperty("id")
	private Integer id;

	@ApiModelProperty("能耗类型 1 电 2 水 3 热")
	private Integer energyType;

	@ApiModelProperty("开始时间")
	private String startTime;

	@ApiModelProperty("结束时间")
	private String endTime;

	private double value;
}
