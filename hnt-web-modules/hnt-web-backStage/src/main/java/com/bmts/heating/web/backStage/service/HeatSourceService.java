package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface HeatSourceService {

    Response insert(HeatSource info);

    Response delete(int id);

    Response update(HeatSource info);

    Response detail(int id);

    Response query(HeatSourceDto dto);

}
