package com.bmts.heating.commons.entiy.baseInfo.request.template;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateCollect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;


@ApiModel("采集量模板添加类")
@Data
public class TemplateCollectAddDto {

	@ApiModelProperty("模板Id")
	private Integer templateControlId;

	@ApiModelProperty("采集量模板集合")
	private Collection<TemplateCollect> templateCollects;
}
