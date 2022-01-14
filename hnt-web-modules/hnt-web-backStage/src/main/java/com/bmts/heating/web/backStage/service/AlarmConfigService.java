package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface AlarmConfigService {

    Response insert(AlarmConfig info);

    Response delete(int id);

    Response update(AlarmConfig info);

    Response detail(int id);

    Response query(AlarmConfigDto dto);

}
