package com.bmts.heating.web.backStage.service.station;

import com.bmts.heating.commons.entiy.baseInfo.request.station.TemplateStationDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.Collection;


public interface SaveTemplateByStationService {

	Response queryHeatSystemInCache(Integer id);

	Response queryHeatSystem(Integer id);

	Response insert(Collection<TemplateStationDto> dtoList);

}




