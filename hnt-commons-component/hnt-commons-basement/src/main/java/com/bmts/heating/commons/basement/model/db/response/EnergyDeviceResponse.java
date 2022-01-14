package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EnergyDeviceResponse extends EnergyDevice {

	@ApiModelProperty("热系统名称")
	private String heatSysName;

	@ApiModelProperty("热源/热力站名称")
	private String heatName;

}
