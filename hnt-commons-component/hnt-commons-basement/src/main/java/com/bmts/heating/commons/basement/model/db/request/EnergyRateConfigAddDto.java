package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("能耗倍率添加类")
@Data
public class EnergyRateConfigAddDto {

	private List<Integer> parentIds;

	private EnergyRateConfig energyRateConfig;
}
