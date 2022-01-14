package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("采集量添加类")
@Data
public class PointCollectDeleteDto {

	private Integer pointStandId;
	private List<Integer> heatSystemId;
}
