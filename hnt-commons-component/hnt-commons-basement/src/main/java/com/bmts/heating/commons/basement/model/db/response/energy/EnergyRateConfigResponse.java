package com.bmts.heating.commons.basement.model.db.response.energy;

import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("能耗倍率响应类")
public class EnergyRateConfigResponse extends EnergyRateConfig {

	@ApiModelProperty("热力站Id、热源Id")
	private Integer parentId;

	@ApiModelProperty("热力站、热源名称")
	private String name;

	@ApiModelProperty("收费类型")
	private String chargeType;
}
