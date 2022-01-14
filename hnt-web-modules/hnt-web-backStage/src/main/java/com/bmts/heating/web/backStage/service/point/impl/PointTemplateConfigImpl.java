package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointTemplateConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointTemplateConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointTemplateConfigImpl extends SavantServices implements PointTemplateConfigService {

    @Autowired
    private BackRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(PointTemplateConfig info) {
        return tsccRestTemplate.post("/pointTemplateConfig", info, baseServer);
    }


    @Override
    public Response update(PointTemplateConfig info) {
        return tsccRestTemplate.put("/pointTemplateConfig", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return tsccRestTemplate.delete("/pointTemplateConfig?id=" + id, baseServer);
    }

    @Override
    public Response detail(int id) {
        return tsccRestTemplate.get("/pointTemplateConfig?id=" + id, baseServer);
    }

    @Override
    public Response query(PointTemplateConfigDto dto) {
        return tsccRestTemplate.post("/pointTemplateConfig/query", dto, baseServer);
    }

    @Override
    public Response freeze(FreezeDto dto) {
        return tsccRestTemplate.post("/pointTemplateConfig/freeze", dto, baseServer);
    }
}
