package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("能耗首页达标率查询类")
public class HomeRateDto {

	@ApiModelProperty("1:水 2:电 3:热")
	private EnergyType type;

	@ApiModelProperty("1:7日 2:月 3:上一供暖季同期")
	private Integer compare;

	@ApiModelProperty("站、源、网")
	private EvaluateTarget target;
}
