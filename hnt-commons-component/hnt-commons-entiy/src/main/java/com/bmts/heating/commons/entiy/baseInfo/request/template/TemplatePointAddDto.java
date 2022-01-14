package com.bmts.heating.commons.entiy.baseInfo.request.template;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplatePoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@ApiModel("模板点添加类")
@Data
public class TemplatePointAddDto {

	@ApiModelProperty("模板Id")
	private Integer templateId;

	@ApiModelProperty("模板点集合")
	private Collection<TemplatePoint> templatePoints;
}
