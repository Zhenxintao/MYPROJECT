package com.bmts.heating.web.scada.service.patrol.impl;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.patrol.PatrolStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatrolStationServiceImpl implements PatrolStationService {

	@Autowired
	private TSCCRestTemplate template;

	private final String gatherSearch = "gather_search";

	@Override
	public Response statPatrol() {
		return template.get("/patrol/stat", gatherSearch, Response.class);
	}
}
