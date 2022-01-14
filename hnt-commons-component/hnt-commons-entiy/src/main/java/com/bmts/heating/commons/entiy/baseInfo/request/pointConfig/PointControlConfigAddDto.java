package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointControlConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel("控制量添加类")
public class PointControlConfigAddDto {

	@ApiModelProperty("选中的机组Id")
	Collection<Integer> heatSystemId;

	@ApiModelProperty("采集量")
	PointControlConfig pointControlConfig;
}
