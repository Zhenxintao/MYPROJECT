package com.bmts.heating.web.backStage.service.impl.base;

import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EnergyConvergeService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyConvergeServiceImpl implements EnergyConvergeService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	private final String url =  "/energy/config/";

	@Override
	public Response page(BaseDto dto) {
		return backRestTemplate.post(url + "page",dto,baseServer);
	}

	@Override
	public Response saveOrUpdate(EnergyConfig entity) {
		return backRestTemplate.post(url + "saveOrUpdate", entity, baseServer);
	}

	@Override
	public Response delete(Integer id) {
		return backRestTemplate.delete(url + id,baseServer);
	}



}
