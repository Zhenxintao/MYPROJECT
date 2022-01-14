package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyHeatSourceDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CopySourceService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CopySourceServiceImpl extends SavantServices implements CopySourceService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response toExist(CopyDto info) {
        return backRestTemplate.post("/copySource/exist", info, baseServer);
    }

    @Override
    public Response copyNew(CopyHeatSourceDto dto) {
        return backRestTemplate.post("/copySource/copyNew", dto, baseServer);
    }

    @Override
    public Response querySource(int sourceId) {
        return backRestTemplate.get("/copySource?sourceId=" + sourceId, baseServer);
    }

}
