package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@ApiModel("采集量添加类")
@Data
public class PointCollectConfigAddDto {

	@ApiModelProperty("选中的机组Id")
	Collection<Integer> heatSystemId;

	@ApiModelProperty("采集量")
	PointCollectConfig pointCollectConfig;

	@ApiModelProperty("数据类型")
	int dataType;
}
