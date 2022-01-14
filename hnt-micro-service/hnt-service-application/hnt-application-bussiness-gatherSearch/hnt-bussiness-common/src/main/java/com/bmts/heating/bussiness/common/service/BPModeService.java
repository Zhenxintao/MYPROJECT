package com.bmts.heating.bussiness.common.service;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface BPModeService {

    Response query(Integer systemId);

    Response down(BPModeDto dto);
}
