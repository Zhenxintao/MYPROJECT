package com.bmts.heating.commons.entiy.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("能耗采集配置类")
@Data
public class EnergyCollectConfigQueryDto extends BaseDto {

	@ApiModelProperty("站id")
	private Integer stationId;

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;
}
