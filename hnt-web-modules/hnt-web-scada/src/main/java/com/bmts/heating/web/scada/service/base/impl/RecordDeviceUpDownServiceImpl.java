package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.entiy.baseInfo.request.RecordDeviceUpDownDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.RecordDeviceUpDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RecordDeviceUpDownServiceImpl implements RecordDeviceUpDownService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response queyPage(RecordDeviceUpDownDto dto) {
        return tsccRestTemplate.post("/device/page", dto, baseServer, Response.class);
    }

    @Override
    public Response countDevice(RecordDeviceUpDownDto dto) {
        return tsccRestTemplate.post("/device/curve", dto, baseServer, Response.class);
    }
}
