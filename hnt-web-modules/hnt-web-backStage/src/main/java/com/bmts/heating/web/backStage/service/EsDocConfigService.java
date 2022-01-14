package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface EsDocConfigService {

    Response page(BaseDto dto);

    Response detail(int id);

    Response update(EsDocConfig info);

}
