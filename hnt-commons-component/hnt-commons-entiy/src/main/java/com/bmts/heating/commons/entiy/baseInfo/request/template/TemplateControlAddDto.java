package com.bmts.heating.commons.entiy.baseInfo.request.template;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateControl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@ApiModel("控制量模板添加类")
@Data
public class TemplateControlAddDto {

	@ApiModelProperty("模板Id")
	private Integer templateControlId;

	@ApiModelProperty("控制量模板集合")
	private Collection<TemplateControl> templateControls;
}
