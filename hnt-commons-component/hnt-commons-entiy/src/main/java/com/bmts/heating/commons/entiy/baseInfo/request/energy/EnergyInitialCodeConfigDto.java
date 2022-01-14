package com.bmts.heating.commons.entiy.baseInfo.request.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("能耗初始化查询类")
@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyInitialCodeConfigDto extends BaseDto {

	@ApiModelProperty("热源:4 热力站：3")
	private Integer type;

	@ApiModelProperty("供暖季id")
	private Integer commonSeasonId;

	@ApiModelProperty("关联Id")
	private Integer relevanceId;


}
