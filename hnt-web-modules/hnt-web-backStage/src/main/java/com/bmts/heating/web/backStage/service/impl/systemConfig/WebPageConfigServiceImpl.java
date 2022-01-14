package com.bmts.heating.web.backStage.service.impl.systemConfig;

import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.systemConfig.WebPageConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebPageConfigServiceImpl extends SavantServices implements WebPageConfigService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	@Override
	public Response save(WebPageConfig webPageConfig) {
		return backRestTemplate.post("/webPageConfig/save",webPageConfig,baseServer);
	}

	@Override
	public Response update(WebPageConfig webPageConfig) {
		return backRestTemplate.put("/webPageConfig/update",webPageConfig,baseServer);
	}

	@Override
	public Response delete(String configKey) {
		return backRestTemplate.delete("/webPageConfig/delete?configKey="+configKey,baseServer);
	}

	@Override
	public Response searchPage(BaseDto baseDto) {
		return backRestTemplate.post("/webPageConfig/searchPage",baseDto,baseServer);
	}
}
