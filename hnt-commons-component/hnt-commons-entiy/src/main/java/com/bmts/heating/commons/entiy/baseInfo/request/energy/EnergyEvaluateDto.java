package com.bmts.heating.commons.entiy.baseInfo.request.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("能耗评价配置类")
@Data
public class EnergyEvaluateDto extends BaseDto {

	@ApiModelProperty("能耗类型 1:水 2:电 3:热 ")
	private int energyType;
}
