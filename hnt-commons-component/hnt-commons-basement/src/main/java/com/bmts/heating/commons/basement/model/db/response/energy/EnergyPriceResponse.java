package com.bmts.heating.commons.basement.model.db.response.energy;

import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("能耗单价相应类")
public class EnergyPriceResponse extends EnergyPrice {

	@ApiModelProperty("收费类型")
	private String chargeName;

	@ApiModelProperty("供暖季")
	private String heatYear;
}
