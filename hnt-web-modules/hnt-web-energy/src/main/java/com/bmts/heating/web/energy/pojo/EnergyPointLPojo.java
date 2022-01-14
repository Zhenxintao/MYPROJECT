package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EnergyPointLPojo {

	@ApiModelProperty("控制柜Id")
	private Integer cabinetId;

	@ApiModelProperty("控制柜名称")
	private String cabinetName;

	@ApiModelProperty("系统Id")
	private Integer systemId;

	@ApiModelProperty("系统名称")
	private String systemName;

	@ApiModelProperty("能源参量")
	private List<EnergyPointResponse> children;
}
