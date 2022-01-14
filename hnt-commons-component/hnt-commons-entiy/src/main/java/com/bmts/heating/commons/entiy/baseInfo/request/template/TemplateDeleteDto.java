package com.bmts.heating.commons.entiy.baseInfo.request.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel("批量删除采集、控制模板参数体")
public class TemplateDeleteDto {

	@ApiModelProperty("标准参量id集合")
	private Collection<Integer> ids;

	@ApiModelProperty("模板Id")
	private Integer templateId;

}
