package com.bmts.heating.commons.basement.model.db.response.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("模板Tab详情")
public class TemplateTabInfoResponse {

	@ApiModelProperty("标准点表管理类型名称")
	private String name;

	@ApiModelProperty("数量")
	private Integer count;
}
