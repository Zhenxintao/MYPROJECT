package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.WebScreenConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebScreenConfigServiceImpl extends SavantServices implements WebScreenConfigService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	@Override
	public Response queryWebScreenConfig() {
		return backRestTemplate.get("/screen/query",baseServer);
	}

	@Override
	public Response queryPage(WebScreenConfigDto webScreenConfigDto) {
		return backRestTemplate.post("/screen/page",webScreenConfigDto,baseServer);
	}

	@Override
	public Response insertWebScreenConfig(WebScreenConfig webScreenConfig) {
		return backRestTemplate.post("/screen/insert",webScreenConfig,baseServer);
	}

	@Override
	public Response removeWebScreenConfig(int id) {
		return backRestTemplate.delete("/screen/delete?id="+id,baseServer);
	}

	@Override
	public Response updateWebScreenConfig(WebScreenConfig webScreenConfig) {
		return backRestTemplate.put("/screen/update",webScreenConfig,baseServer);
	}

	@Override
	public Response queryDetail(int id) {
		return backRestTemplate.get("/screen/detail?id="+id,baseServer);
	}

	@Override
	public Response queryPageKey(WebScreenConfig webScreenConfig) {
		return backRestTemplate.post("/screen/queryPageKey",webScreenConfig,baseServer);
	}
}
