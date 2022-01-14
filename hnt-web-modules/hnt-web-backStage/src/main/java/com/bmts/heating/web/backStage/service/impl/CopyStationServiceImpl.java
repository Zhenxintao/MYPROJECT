package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyNewDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CopyStationService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CopyStationServiceImpl extends SavantServices implements CopyStationService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response toExist(CopyDto info) {
        return backRestTemplate.post("/copy/exist", info, baseServer);
    }

    @Override
    public Response querySource(int heatStationId) {
        return backRestTemplate.get("/copy?heatStationId=" + heatStationId, baseServer);
    }

    @Override
    public Response copyNew(CopyNewDto dto) {
        return backRestTemplate.post("/copy/copyNew", dto, baseServer);
    }
}
