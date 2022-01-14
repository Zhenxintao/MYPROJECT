package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatCabinetService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeatCabinetServiceImpl extends SavantServices implements HeatCabinetService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatCabinet info) {
        return backRestTemplate.post("/heatCabinet", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/heatCabinet?id=" + id, baseServer);
    }

    @Override
    public Response update(HeatCabinet info) {
        return backRestTemplate.put("/heatCabinet", info, baseServer);
    }


    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/heatCabinet?id=" + id, baseServer);
    }

    @Override
    public Response query() {
        return backRestTemplate.post("/heatCabinet/query", null, baseServer);
    }

    @Override
    public Response queryByStationId(int id) {
        return backRestTemplate.get("/heatCabinet/queryByStationId?id=" + id, baseServer);
    }

    @Override
    public Response queryByMap(int id) {
        return backRestTemplate.get("/heatCabinet/queryCs?id=" + id, baseServer);
    }

    @Override
    public Response queryBySourceId(int id) {
        return backRestTemplate.get("/heatCabinet/queryBySourceId?id=" + id, baseServer);
    }

    @Override
    public Response SourceCs(int id) {
        return backRestTemplate.get("/heatCabinet/SourceCs?id=" + id, baseServer);
    }


}
