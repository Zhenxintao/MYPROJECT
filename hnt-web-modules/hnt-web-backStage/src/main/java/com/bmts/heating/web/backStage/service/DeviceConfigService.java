package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.DeviceConfig;
import com.bmts.heating.commons.utils.restful.Response;

public interface DeviceConfigService {

    Response insert(DeviceConfig info);

    Response delete(int id);

    Response update(DeviceConfig info);

    Response detail(int id);

    Response query();

}
