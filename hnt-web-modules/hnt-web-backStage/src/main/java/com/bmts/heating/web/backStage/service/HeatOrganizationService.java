package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatOrganizationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface HeatOrganizationService {

    Response insert(HeatOrganization info);

    Response delete(int id);

    Response deleteBatch(List<Integer> ids);

    Response update(HeatOrganization info);

    Response detail(int id);

    Response page(HeatOrganizationDto dto);

    Response queryAll(Integer userId);

	Response queryTree2(QueryTreeDto dto);

	Response queryTree(QueryTreeDto dto);
}
