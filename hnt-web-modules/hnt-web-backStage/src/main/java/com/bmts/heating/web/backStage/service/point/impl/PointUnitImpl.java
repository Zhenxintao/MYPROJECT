package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointUnitService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointUnitImpl implements PointUnitService {

    @Autowired
    private BackRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response insert(PointUnit info) {
        return tsccRestTemplate.post("/pointUnit", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return tsccRestTemplate.delete("/pointUnit?id=" + id, baseServer);
    }

    @Override
    public Response update(PointUnit info) {
        return tsccRestTemplate.put("/pointUnit", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return tsccRestTemplate.get("/pointUnit?id=" + id, baseServer);
    }

    @Override
    public Response queryByMap(BaseDto dto) {
        return tsccRestTemplate.post("/pointUnit/query", dto, baseServer);
    }

}
