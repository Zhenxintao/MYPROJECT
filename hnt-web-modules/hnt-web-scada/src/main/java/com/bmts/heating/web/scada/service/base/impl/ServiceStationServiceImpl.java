package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.ServiceStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceStationServiceImpl extends SavantServices implements ServiceStationService {
    @Autowired
    private TSCCRestTemplate template;

    private final String baseServer = "bussiness_baseInfomation";
    @Override
    public Response queryServiceStationInfo(String id) {
        return template.get("/serviceInfo/queryServiceStationInfo?id="+id, baseServer,Response.class);
    }
}
