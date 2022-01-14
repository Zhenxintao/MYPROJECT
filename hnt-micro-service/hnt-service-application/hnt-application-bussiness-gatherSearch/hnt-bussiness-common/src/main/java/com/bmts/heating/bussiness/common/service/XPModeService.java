package com.bmts.heating.bussiness.common.service;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface XPModeService {

    Response query(Integer systemId);

    Response down(XPModeDto dto);
}
