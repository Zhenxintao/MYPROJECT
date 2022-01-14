package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface EnergyConvergeService {

	Response page(BaseDto dto);

	Response saveOrUpdate(EnergyConfig entity);

	Response delete(Integer id);
}
