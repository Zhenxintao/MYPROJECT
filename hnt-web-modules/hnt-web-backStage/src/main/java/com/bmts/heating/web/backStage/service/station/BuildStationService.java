package com.bmts.heating.web.backStage.service.station;

import com.bmts.heating.commons.entiy.baseInfo.request.station.BuildStationDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestBody;

public interface BuildStationService {

	Response configStation(@RequestBody BuildStationDto station);

	Response queryTemplateTree();
}





















