package com.bmts.heating.web.backStage.service.base;

import com.bmts.heating.commons.entiy.baseInfo.request.HeatAreaChangeDto;
import com.bmts.heating.commons.entiy.energy.AreaChangeHistoryDto;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface AreaManagerService {

	Response update(AreaManagerDto dto);

	Response list( Integer id);

	Response getAreaChangeHistoryList(AreaChangeHistoryDto dto);

}
