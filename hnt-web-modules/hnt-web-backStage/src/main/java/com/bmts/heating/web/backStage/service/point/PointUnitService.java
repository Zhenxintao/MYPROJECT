package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface PointUnitService {

    Response insert(PointUnit info);

    Response delete(int id);

    Response update(PointUnit info);

    Response detail(int id);

    Response queryByMap(BaseDto dto);

}
