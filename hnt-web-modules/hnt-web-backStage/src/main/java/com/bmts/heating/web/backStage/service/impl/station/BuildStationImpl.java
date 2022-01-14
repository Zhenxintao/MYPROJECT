package com.bmts.heating.web.backStage.service.impl.station;

import com.bmts.heating.commons.entiy.baseInfo.request.station.BuildStationDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.station.BuildStationService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildStationImpl implements BuildStationService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	public Response configStation(BuildStationDto station) {
		return backRestTemplate.post("/configStation", station, baseServer);
	}

	public Response queryTemplateTree() {
		return backRestTemplate.post("/configStation/queryTeplateTree", null, baseServer);
	}
}





















