package com.bmts.heating.bussiness.common.service;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;

public interface LogRecordService {

    Boolean recordBPMode(BPModeDto dto, int type);

    Boolean recordCVMode(CVModeDto dto);

    Boolean recordXPMode(XPModeDto dto, int type);

    Boolean recordXSVMode(XSVModeDto dto);

}
