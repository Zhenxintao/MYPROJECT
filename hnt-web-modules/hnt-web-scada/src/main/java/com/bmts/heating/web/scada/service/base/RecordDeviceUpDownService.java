package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.baseInfo.request.RecordDeviceUpDownDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface RecordDeviceUpDownService {

    Response queyPage(RecordDeviceUpDownDto dto);

    Response countDevice(RecordDeviceUpDownDto dto);

}
