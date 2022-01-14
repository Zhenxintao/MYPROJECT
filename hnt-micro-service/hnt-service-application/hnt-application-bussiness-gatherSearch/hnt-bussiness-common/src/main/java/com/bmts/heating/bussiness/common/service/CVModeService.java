package com.bmts.heating.bussiness.common.service;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface CVModeService {

    Response query(Integer systemId);

    Response down(CVModeDto dto);
}
