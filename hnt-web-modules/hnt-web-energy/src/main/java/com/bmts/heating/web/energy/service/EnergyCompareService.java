package com.bmts.heating.web.energy.service;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.EnergyCompareDto;

public interface EnergyCompareService {

	Response page(EnergyCompareDto dto);

	Response charts(EnergyCompareDto dto);
}
