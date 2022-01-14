package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSystemDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface HeatSystemService {

    Response insert(HeatSystem info);

    Response delete(int id);

    Response update(HeatSystem info);

    Response detail(int id);

    Response query(HeatSystemDto dto);

    Response querySystem(HeatSystemDto dto);
}
