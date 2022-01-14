package com.bmts.heating.web.backStage.service.systemConfig;

import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface WebPageConfigService {
	Response save(WebPageConfig webPageConfig);

	Response update(WebPageConfig webPageConfig);

	Response delete(String configKey);

	Response searchPage(BaseDto baseDto);
}
