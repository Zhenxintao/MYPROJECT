package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.commons.entiy.energy.EnergyPricePageDto;
import com.bmts.heating.web.energy.service.config.EnergyPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class EnergyPriceServiceImpl implements EnergyPriceService {

	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/price/";
	@Autowired
	private TSCCRestTemplate backRestTemplate;

	@Override
	public Response page(EnergyPricePageDto dto){
		return backRestTemplate.post(url + "page", dto, baseServer,Response.class);
	}

	@Override
	public Response saveOrUpdate(EnergyPrice price){
		return backRestTemplate.put(url,price,baseServer,Response.class);
	}

	@Override
	public Response delete(@PathVariable Integer id){
		return backRestTemplate.delete(url + id,baseServer,Response.class);
	}

}
