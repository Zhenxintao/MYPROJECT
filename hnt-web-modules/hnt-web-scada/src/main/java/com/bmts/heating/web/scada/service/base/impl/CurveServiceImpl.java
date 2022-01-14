package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.entiy.gathersearch.request.CurveDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.CurveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


@Service
public class CurveServiceImpl implements CurveService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String gatherSearch = "gather_search";

    @Override
    public Response curve(CurveDto param) {
        return tsccRestTemplate.post("/history/curve", param, gatherSearch, Response.class);
    }

    @Override
    public Response dataCurve(CurveDto param) {
        return tsccRestTemplate.doHttp("/history/dataCurve", param, gatherSearch, Response.class,HttpMethod.POST);
    }

    @Override
    public  Response dayCurveContrast(CurveDto dto) {
        return tsccRestTemplate.doHttp("/history/dayCurveContrast", dto, gatherSearch, Response.class, HttpMethod.POST);
    }

    @Override
    public Response energyCurve(CurveDto param) {
        return tsccRestTemplate.doHttp("/history/energyCurve", param, gatherSearch, Response.class, HttpMethod.POST);
    }
}
