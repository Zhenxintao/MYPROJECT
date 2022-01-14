package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String gatherSearch = "gather_search";

    /**
     * 读取控制管理
     */
    @Override
    public Response bpMode(Integer systemId) {
        return tsccRestTemplate.get("/read/bpMode/" + systemId, gatherSearch, Response.class);
    }

    @Override
    public Response cvMode(Integer systemId) {
        return tsccRestTemplate.get("/read/cvMode/" + systemId, gatherSearch, Response.class);
    }

    @Override
    public Response xpMode(Integer systemId) {
        return tsccRestTemplate.get("/read/xpMode/" + systemId, gatherSearch, Response.class);
    }

    @Override
    public Response xsvMode(Integer systemId) {
        return tsccRestTemplate.get("/read/xsvMode/" + systemId, gatherSearch, Response.class);
    }

    /**
     * 下发控制管理
     */
    @Override
    public Response bpModeIssue(BPModeDto param) {
        return tsccRestTemplate.post("/issue/bpMode", param, gatherSearch, Response.class);
    }

    @Override
    public Response cvModeIssue(CVModeDto param) {
        return tsccRestTemplate.post("/issue/cvMode", param, gatherSearch, Response.class);
    }

    @Override
    public Response xpModeIssue(XPModeDto param) {
        return tsccRestTemplate.post("/issue/xpMode", param, gatherSearch, Response.class);
    }

    @Override
    public Response xsvModeIssue(XSVModeDto param) {
        return tsccRestTemplate.post("/issue/xsvMode", param, gatherSearch, Response.class);
    }
}
