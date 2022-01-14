package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("计算量相关参量添加")
@Data
public class ComputeConfigAddDto {

	@ApiModelProperty("计算量Id")
	private Integer compoteId;

	@ApiModelProperty("计算量所需采集量Id")
	private Integer[] pointStandardId;

	public int verify(){
		return (this.compoteId != null && this.compoteId != 0) ? (pointStandardId.length > 0 ? 2 : 1) : 0;
	}
}
