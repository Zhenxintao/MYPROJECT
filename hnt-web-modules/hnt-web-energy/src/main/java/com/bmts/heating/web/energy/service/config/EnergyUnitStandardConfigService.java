package com.bmts.heating.web.energy.service.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.utils.restful.Response;

public interface EnergyUnitStandardConfigService {

	EnergyUnitStandardConfig info();

	Response saveOrUpdate(EnergyUnitStandardConfig one);

}
