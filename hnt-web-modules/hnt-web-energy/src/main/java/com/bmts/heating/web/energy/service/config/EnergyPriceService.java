package com.bmts.heating.web.energy.service.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.entiy.energy.EnergyPricePageDto;

public interface EnergyPriceService {


	Response page(EnergyPricePageDto dto);

	Response saveOrUpdate(EnergyPrice price);

	Response delete(Integer id);
}
