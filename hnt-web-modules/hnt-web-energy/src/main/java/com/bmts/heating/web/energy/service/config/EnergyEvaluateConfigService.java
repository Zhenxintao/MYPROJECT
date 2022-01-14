package com.bmts.heating.web.energy.service.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyEvaluateDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface EnergyEvaluateConfigService {

	Response page(EnergyEvaluateDto dto);

	Response saveOrUpdate(EnergyEvaluateConfig energyEvaluateConfig);

	Response remove(int id);

}
