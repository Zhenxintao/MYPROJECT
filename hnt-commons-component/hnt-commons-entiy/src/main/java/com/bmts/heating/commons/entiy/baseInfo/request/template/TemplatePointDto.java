package com.bmts.heating.commons.entiy.baseInfo.request.template;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("点模板查询类")
@Data
public class TemplatePointDto extends BaseDto {

	@ApiModelProperty("模板Id")
	private Integer tid;

	@ApiModelProperty("类型:AI、DI、AO、DO、TX、CO对应的Id")
	private Integer type;

	@ApiModelProperty("1.只读 2.可读可写 3.只写 默认加载全部")
	private Integer pointConfig;
}
