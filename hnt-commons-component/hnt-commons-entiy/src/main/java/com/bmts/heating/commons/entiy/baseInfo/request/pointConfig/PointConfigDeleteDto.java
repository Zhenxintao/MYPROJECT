package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("点添加类")
@Data
public class PointConfigDeleteDto {

	@ApiModelProperty("标准参量id")
	private Integer pointStandId;

	@ApiModelProperty("关联Id")
	private List<Integer> relevanceIds;

	@ApiModelProperty("层级")
	private List<Integer> levels;
}
