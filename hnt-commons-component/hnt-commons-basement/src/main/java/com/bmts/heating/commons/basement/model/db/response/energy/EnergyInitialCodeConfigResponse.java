package com.bmts.heating.commons.basement.model.db.response.energy;

import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("能耗初始化相应类")
public class EnergyInitialCodeConfigResponse extends EnergyInitialCodeConfig {

	@ApiModelProperty("热力站Id、热源Id")
	private Integer parentId;

	@ApiModelProperty("热力站、热源名称")
	private String name;

	@ApiModelProperty("供暖季")
	private String heatYear;

}
