package com.bmts.heating.web.energy.service.config.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigAddDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigParamQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.config.EnergyCollectConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnergyCollectConfigServiceImpl extends SavantServices implements EnergyCollectConfigService{

	private final String baseServer = "bussiness_baseInfomation";
	private final String url = "/energy/collect/";
	@Autowired
	private TSCCRestTemplate backRestTemplate;

	/**
	 * 分页查询
	 * @param dto 源、站 id
	 * @return response
	 */
	@Override
	public Response page(EnergyCollectConfigQueryDto dto){
		return backRestTemplate.post(url+"page",dto,baseServer, Response.class);
	}


	@Override
	public Map<String, List<EnergyPointResponse>> queryAll(EnergyCollectConfigParamQueryDto dto){
		EnergyPointResponse[] energyPointResponses = backRestTemplate.post(url, dto, baseServer, EnergyPointResponse[].class);
		List<EnergyPointResponse> energyPointResponseList = Arrays.asList(energyPointResponses);
		return transform(energyPointResponseList);
	}
	private Map<String, List<EnergyPointResponse>> transform(List<EnergyPointResponse> energyPointResponseList){
		return energyPointResponseList.stream().collect(Collectors.groupingBy(e ->
			e.getCabinetName() + "-" + e.getSystemName()
		));
	}

	@Override
	public Response saveOrUpdate(EnergyCollectConfigAddDto addRequest){
		return backRestTemplate.put(url+"saveOrUpdate", addRequest, baseServer, Response.class);
	}

	@Override
	public Response info(Integer id){
		return backRestTemplate.get(url + "info/" + id,baseServer,Response.class);
	}

	@Override
	public Response delete(Integer id){
		return backRestTemplate.delete(url + id,baseServer,Response.class);
	}

	@Override
	public Response heatSourceList(){
		return backRestTemplate.get(url + "heatSource",baseServer,Response.class);
	}


}
