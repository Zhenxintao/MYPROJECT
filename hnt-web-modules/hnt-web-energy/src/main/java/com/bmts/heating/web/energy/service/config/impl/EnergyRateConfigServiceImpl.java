package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.config.EnergyRateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyRateConfigServiceImpl implements EnergyRateConfigService {


	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/rate/";
	@Autowired
	private TSCCRestTemplate backRestTemplate;

	@Override
	public Response page(EnergyInitialCodeConfigDto dto) {
		return backRestTemplate.post(url + "page", dto, baseServer, Response.class);
	}

	@Override
	public Response saveOrUpdate(List<EnergyRateConfig> list){
		return backRestTemplate.put(url, list, baseServer, Response.class);
	}

	@Override
	public Response delete(Integer id){
		return backRestTemplate.delete(url + id,baseServer,Response.class);
	}
}
