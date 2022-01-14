package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfig;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfigChild;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ApiModel("保存类")
@Data
public class EnergyCollectConfigDto {

	@ApiModelProperty("目标量")
	private EnergyCollectConfig energyCollectConfig;

	@ApiModelProperty("参与目标计算量")
	private List<EnergyCollectConfigChild> energyCollectConfigChildren;

	public Boolean isNotValid(){
		boolean b = true;
		if (this.energyCollectConfig != null) {
			b = CollectionUtils.isEmpty(energyCollectConfigChildren);
		}
		return b;
	}
}
