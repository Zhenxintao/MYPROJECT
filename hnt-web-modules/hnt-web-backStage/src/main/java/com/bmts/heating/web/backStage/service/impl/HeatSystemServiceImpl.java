package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSystemDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatSystemService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeatSystemServiceImpl extends SavantServices implements HeatSystemService {

    @Autowired
    private BackRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatSystem info) {
        return tsccRestTemplate.post("/heatSystem", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return tsccRestTemplate.delete("/heatSystem?id=" + id, baseServer);
    }

    @Override
    public Response update(HeatSystem info) {
        return tsccRestTemplate.put("/heatSystem", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return tsccRestTemplate.get("/heatSystem?id=" + id, baseServer);
    }

    @Override
    public Response query(HeatSystemDto dto) {
        return tsccRestTemplate.post("/heatSystem/query", dto, baseServer);
    }

    @Override
    public Response querySystem(HeatSystemDto dto) {
        return tsccRestTemplate.post("/heatSystem/querySystem", dto, baseServer);
    }
}
