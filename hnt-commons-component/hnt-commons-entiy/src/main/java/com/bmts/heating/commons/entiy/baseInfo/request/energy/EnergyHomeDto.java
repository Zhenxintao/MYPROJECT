package com.bmts.heating.commons.entiy.baseInfo.request.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("能耗首页站查询类")
@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyHomeDto extends BaseDto {

	@ApiModelProperty("热源Id")
	private Integer heatSourceIds;

	@ApiModelProperty("查询方式1:小时 2:天 3:自定义")
	private Integer queryType;

	@ApiModelProperty("组织架构Id")
	private Integer orgId;

	@ApiModelProperty("开始时间 queryType为3时的必填项")
	private Long start;

	@ApiModelProperty("结束时间 queryType为3时的必填项")
	private Long end;
}
