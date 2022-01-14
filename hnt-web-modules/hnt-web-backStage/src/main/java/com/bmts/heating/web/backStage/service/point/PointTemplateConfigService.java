package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointTemplateConfigDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface PointTemplateConfigService {


    Response insert(PointTemplateConfig info);

    Response update(PointTemplateConfig info);

    Response delete(int id);

    Response detail(int id);

    Response query(PointTemplateConfigDto dto);

    Response freeze(FreezeDto dto);
}
