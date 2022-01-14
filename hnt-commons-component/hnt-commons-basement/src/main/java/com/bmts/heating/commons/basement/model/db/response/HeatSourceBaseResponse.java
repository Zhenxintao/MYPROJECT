package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HeatSourceBaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;

	@ApiModelProperty("热源名称")
	private String heatSourceName;

	@ApiModelProperty("控制柜Id")
	private Integer cabinetId;

	@ApiModelProperty("控制柜名称")
	private String cabinetName;

	@ApiModelProperty("系统Id")
	private Integer heatSystemId;

	@ApiModelProperty("系统名称")
	private String heatSystemName;

}
