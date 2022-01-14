package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyCollectConfigResponse extends CabinetEntityBaseInfo{

	private Integer id;

	@ApiModelProperty("目标计算量pointId")
	private Integer pointTargetId;

	@ApiModelProperty("目标计算量名称")
	private String pointTargetName;

	@ApiModelProperty("jep表达式")
	private String expression;

	@ApiModelProperty("是否参与计算")
	private Boolean isConverge;

	@ApiModelProperty("计算方式1:默认 2:分区占比")
	private Integer computeType;

}
