package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatSourceService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeatSourceServiceImpl extends SavantServices implements HeatSourceService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatSource info) {
        return backRestTemplate.post("/heatSource", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/heatSource?id=" + id, baseServer);
    }

    @Override
    public Response update(HeatSource info) {
        return backRestTemplate.put("/heatSource", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/heatSource?id=" + id, baseServer);
    }

    @Override
    public Response query(HeatSourceDto dto) {
        return backRestTemplate.post("/heatSource/query", dto, baseServer);
    }


}
