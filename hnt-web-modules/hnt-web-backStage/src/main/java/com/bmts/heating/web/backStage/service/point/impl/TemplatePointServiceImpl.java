package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplatePoint;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplatePointAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplatePointDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.TemplatePointService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplatePointServiceImpl implements TemplatePointService {

	@Autowired
	private BackRestTemplate tsccRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	private final String baseUrl = "/templatePointJoogle/";

	@Override
	public Response page(TemplatePointDto dto) {
		return tsccRestTemplate.post(baseUrl + "page", dto, baseServer);
	}


	@Override
	public Response loadOtherPointStandard(int id) {
		return tsccRestTemplate.get(baseUrl + "none/" + id, baseServer);
	}

	@Override
	public Response loadOtherPointStandardSearch(TemplateLoadStandardDto dto) {
		return tsccRestTemplate.post(baseUrl + "none", dto, baseServer);
	}

	@Override
	public Response addPointStandard(TemplatePointAddDto dto) {
		return tsccRestTemplate.post(baseUrl + "addBatch", dto, baseServer);
	}

	@Override
	public Response deletePointStandard(TemplateDeleteDto dto) {
		return tsccRestTemplate.post(baseUrl + "deleteBatch", dto, baseServer);
	}

	@Override
	public Response info(String id) {
		return tsccRestTemplate.get(baseUrl + id, baseServer);
	}

	@Override
	public Response update(TemplatePoint entry) {
		return tsccRestTemplate.put(baseUrl , entry, baseServer);
	}

	@Override
	public Response delete(int id) {
		return tsccRestTemplate.delete(baseUrl + id, baseServer);
	}


}
