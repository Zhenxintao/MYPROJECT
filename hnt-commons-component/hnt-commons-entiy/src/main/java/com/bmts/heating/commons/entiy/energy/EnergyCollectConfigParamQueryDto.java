package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("能耗相关参量配置查询类")
@Data
public class EnergyCollectConfigParamQueryDto {

	@ApiModelProperty("站id")
	private Integer stationId;

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;

	@ApiModelProperty("类型")
	private Integer computeType;
}

