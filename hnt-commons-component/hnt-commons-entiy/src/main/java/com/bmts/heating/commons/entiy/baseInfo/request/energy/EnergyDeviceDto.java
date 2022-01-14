package com.bmts.heating.commons.entiy.baseInfo.request.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("能耗设备")
public class EnergyDeviceDto extends BaseDto {

	@ApiModelProperty("系统id")
	private int relevanceId;

	@ApiModelProperty("层级：1：热力站   2：热源")
	private int level;

	@ApiModelProperty("类型 1、水表 2、电表 3、热表")
	private int type;

	@ApiModelProperty("状态：0、未使用，1、已使用")
	private Integer status;

}
