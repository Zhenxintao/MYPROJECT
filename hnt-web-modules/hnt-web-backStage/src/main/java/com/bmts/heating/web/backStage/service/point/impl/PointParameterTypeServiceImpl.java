package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointParameterType;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointParameterTypeService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointParameterTypeServiceImpl extends SavantServices implements PointParameterTypeService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(PointParameterType info) {
        return backRestTemplate.post("/pointParameterType", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/pointParameterType?id=" + id, baseServer);
    }

    @Override
    public Response update(PointParameterType info) {
        return backRestTemplate.put("/pointParameterType", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/pointParameterType?id=" + id, baseServer);
    }

    @Override
    public Response query(BaseDto dto) {
        return backRestTemplate.post("/pointParameterType/query", dto, baseServer);
    }

    @Override
    public Response tree() {
        return backRestTemplate.get("/pointParameterType/tree", baseServer);
    }


}
