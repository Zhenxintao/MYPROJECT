package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyEvaluateDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.config.EnergyEvaluateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyEvaluateConfigServiceImpl implements EnergyEvaluateConfigService {

	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/evaluate/";

	@Autowired
	private TSCCRestTemplate backRestTemplate;

	public Response page(EnergyEvaluateDto dto){
		return backRestTemplate.post(url + "page", dto,baseServer, Response.class);
	}

	public Response saveOrUpdate(EnergyEvaluateConfig energyEvaluateConfig){
		return backRestTemplate.put(url, energyEvaluateConfig, baseServer, Response.class);
	}

	public Response remove(int id){
		return backRestTemplate.delete(url+id, baseServer, Response.class);
	}
}
