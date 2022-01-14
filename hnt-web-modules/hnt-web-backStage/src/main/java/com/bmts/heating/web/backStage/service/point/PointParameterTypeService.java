package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointParameterType;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface PointParameterTypeService {

    Response insert(PointParameterType info);

    Response delete(int id);

    Response update(PointParameterType info);

    Response detail(int id);

    Response query(BaseDto dto);

    Response tree();

}
