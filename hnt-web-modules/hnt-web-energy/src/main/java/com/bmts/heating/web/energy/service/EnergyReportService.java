package com.bmts.heating.web.energy.service;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;

public interface EnergyReportService {

	Response page(EnergyInfoDto dto);
}
