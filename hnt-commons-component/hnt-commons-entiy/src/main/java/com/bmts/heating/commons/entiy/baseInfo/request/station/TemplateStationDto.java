package com.bmts.heating.commons.entiy.baseInfo.request.station;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("站存模板Dto")
public class TemplateStationDto {

	@ApiModelProperty("模板名称")
	private String templateName;

	@ApiModelProperty("关联id")
	private int relevanceId;

	@ApiModelProperty("模板描述")
	private String desc;

	@ApiModelProperty("模板类型 0: 控制量、采集量; 1:采集量; 2:控制量")
	private int type;

	@ApiModelProperty("用户id")
	private Integer userId;

	@ApiModelProperty("用户名称")
	private String userName;

	@ApiModelProperty("层级")
	private int level;
}
