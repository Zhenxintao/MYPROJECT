package com.bmts.heating.web.energy.service;

import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface EnergyInfoService {

	Response page(EnergyInfoDto dto);

	Response evaluateRadar(EnergyInfoDto dto);

	Response energyInfo(EnergyInfoDto dto);

	Response energyCompared(EnergyInfoDto dto);
}
