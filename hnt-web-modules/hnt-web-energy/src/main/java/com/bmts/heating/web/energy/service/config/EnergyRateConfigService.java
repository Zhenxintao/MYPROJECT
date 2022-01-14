package com.bmts.heating.web.energy.service.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface EnergyRateConfigService {

	Response page(EnergyInitialCodeConfigDto dto);

	Response saveOrUpdate(List<EnergyRateConfig> list);

	Response delete(Integer id);
}
