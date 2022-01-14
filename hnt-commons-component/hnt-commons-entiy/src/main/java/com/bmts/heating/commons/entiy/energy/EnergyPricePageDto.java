package com.bmts.heating.commons.entiy.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("单价配置表格Dto")
public class EnergyPricePageDto  extends BaseDto {

	@ApiModelProperty("供暖季Id")
	private Integer commonSeasonId;
}
