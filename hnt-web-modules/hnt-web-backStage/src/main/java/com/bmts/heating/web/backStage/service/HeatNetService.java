package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatNetDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface HeatNetService {

    Response insert(HeatNet info);

    Response delete(int id);

    Response update(HeatNet info);

    Response detail(int id);

    Response query(HeatNetDto dto);

}
