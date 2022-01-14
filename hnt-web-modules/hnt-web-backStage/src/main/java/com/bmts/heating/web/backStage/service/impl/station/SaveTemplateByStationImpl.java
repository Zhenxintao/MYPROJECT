package com.bmts.heating.web.backStage.service.impl.station;

import com.bmts.heating.commons.entiy.baseInfo.request.station.TemplateStationDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.station.SaveTemplateByStationService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;


@Service
public class SaveTemplateByStationImpl implements SaveTemplateByStationService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	public Response queryHeatSystemInCache(Integer id) {
		return backRestTemplate.get("/StationTemplate/cache/" + id, baseServer);
	}

	public Response queryHeatSystem(Integer id) {
		return backRestTemplate.get("/StationTemplate/" + id, baseServer);
	}

	public Response insert(@RequestBody Collection<TemplateStationDto> dtoList) {
		return backRestTemplate.post("/StationTemplate", dtoList, baseServer);
	}

}




