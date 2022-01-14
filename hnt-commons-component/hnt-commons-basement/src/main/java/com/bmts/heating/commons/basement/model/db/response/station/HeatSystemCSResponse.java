package com.bmts.heating.commons.basement.model.db.response.station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("站-控制柜-系统名称实体")
@Data
public class HeatSystemCSResponse {

	@ApiModelProperty("控制柜名称")
	private String cabinetName;

	@ApiModelProperty("控制柜Id")
	private int cabinetId;

	@ApiModelProperty("热力站id")
	private int stationId;

	@ApiModelProperty("热力站名称")
	private String stationName;

	@ApiModelProperty("系统id")
	private int heatSystemId;

	@ApiModelProperty("系统名称")
	private String heatSystemName;

}
