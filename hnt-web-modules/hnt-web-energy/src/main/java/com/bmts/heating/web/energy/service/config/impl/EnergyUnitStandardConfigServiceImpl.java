package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyUnitStandardConfigServiceImpl implements EnergyUnitStandardConfigService {

	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/unitStandard/";

	@Autowired
	private TSCCRestTemplate backRestTemplate;

	@Override
	public EnergyUnitStandardConfig info(){
		return backRestTemplate.get(url + "info", baseServer,EnergyUnitStandardConfig.class);
	}

	@Override
	public Response saveOrUpdate(EnergyUnitStandardConfig one){
		return backRestTemplate.put(url,one,baseServer,Response.class);
	}
}
