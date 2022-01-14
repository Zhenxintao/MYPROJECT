package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EsDocConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EsDocConfigServiceImpl extends SavantServices implements EsDocConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response page(BaseDto dto) {
        return backRestTemplate.post("/esdoc/page", dto, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/esdoc?id=" + id, baseServer);
    }

    @Override
    public Response update(EsDocConfig info) {
        return backRestTemplate.put("/esdoc", info, baseServer);
    }

}
