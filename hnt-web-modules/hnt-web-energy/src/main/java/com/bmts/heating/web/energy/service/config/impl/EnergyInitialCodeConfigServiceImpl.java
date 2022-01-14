package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.config.EnergyInitialCodeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class EnergyInitialCodeConfigServiceImpl implements EnergyInitialCodeConfigService {

	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/initial/";
	@Autowired
	private TSCCRestTemplate backRestTemplate;

	public Response page(EnergyInitialCodeConfigDto dto) {
		return backRestTemplate.post(url + "page", dto, baseServer, Response.class);
	}

	public Response saveOrUpdate(@RequestBody List<EnergyInitialCodeConfig> list){
		return backRestTemplate.put(url, list, baseServer, Response.class);
	}

	public Response delete(@PathVariable Integer id){
		return backRestTemplate.delete(url + id,baseServer,Response.class);
	}
}
