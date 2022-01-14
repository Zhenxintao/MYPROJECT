package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplatePoint;
import com.bmts.heating.commons.entiy.baseInfo.request.template.*;
import com.bmts.heating.commons.utils.restful.Response;

public interface TemplatePointService {

	Response page(TemplatePointDto dto);

	Response loadOtherPointStandard(int id);

	Response loadOtherPointStandardSearch(TemplateLoadStandardDto dto);

	Response addPointStandard(TemplatePointAddDto dto);

	Response deletePointStandard(TemplateDeleteDto dto);

	Response info(String id);

	Response update(TemplatePoint entry);

	Response delete(int id);
}
