package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface IssueService {

    Response bpMode(Integer systemId);

    Response cvMode(Integer systemId);

    Response xpMode(Integer systemId);

    Response xsvMode(Integer systemId);


    Response bpModeIssue(BPModeDto param);

    Response cvModeIssue(CVModeDto param);

    Response xpModeIssue(XPModeDto param);

    Response xsvModeIssue(XSVModeDto param);
}
