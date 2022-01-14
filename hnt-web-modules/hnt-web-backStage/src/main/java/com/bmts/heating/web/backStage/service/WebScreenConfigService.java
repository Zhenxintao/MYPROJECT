package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface WebScreenConfigService {

	Response queryWebScreenConfig();

	Response queryPage(WebScreenConfigDto webScreenConfigDto);

	Response insertWebScreenConfig(WebScreenConfig webScreenConfig);

	Response removeWebScreenConfig(int id);

	Response updateWebScreenConfig(WebScreenConfig webScreenConfig);

	Response queryDetail(int id);

	Response queryPageKey(WebScreenConfig webScreenConfig);
}
