package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询单级/多级组织结构树")
public class QueryTreeDto {
	@ApiModelProperty("层级 1组织 2 公司 3 所")
	private Integer level;
	@ApiModelProperty("查询类型 1 单层组织结构  2多层组织结构  ")//3单层组织结构+站  4多层组织结构+站
	private Integer type;

	@ApiModelProperty("是否带系统 1带站系统  2带站不带系统 0不带站不带系统")
	private Integer flag;

	@ApiModelProperty
	private Integer userId;
}
