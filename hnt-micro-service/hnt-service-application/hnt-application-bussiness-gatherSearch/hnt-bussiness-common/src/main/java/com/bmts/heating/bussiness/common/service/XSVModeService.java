package com.bmts.heating.bussiness.common.service;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface XSVModeService {

    Response query(Integer systemId);

    Response down(XSVModeDto dto);
}
