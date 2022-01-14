package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("组织结构查询类")
@Data
public class HeatOrganizationDto extends BaseDto {
	@ApiModelProperty("父级Id")
	private int pid;
}




