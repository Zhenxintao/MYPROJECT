package com.bmts.heating.commons.entiy.baseInfo.request.template;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("template_加载标准参量")
@Data
public class TemplateLoadStandardDto extends BaseDto {

	@ApiModelProperty("模板Id")
	private Integer templateId;

}
