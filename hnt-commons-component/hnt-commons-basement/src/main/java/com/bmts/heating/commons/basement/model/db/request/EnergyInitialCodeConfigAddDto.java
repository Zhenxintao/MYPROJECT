package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("能耗初始化添加类")
@Data
public class EnergyInitialCodeConfigAddDto {

	private List<Integer> parentIds;

	private EnergyInitialCodeConfig energyInitialConfigs;
}
