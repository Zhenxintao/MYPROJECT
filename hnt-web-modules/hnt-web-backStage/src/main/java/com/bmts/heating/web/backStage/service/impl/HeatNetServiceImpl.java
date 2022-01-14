package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatNetDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatNetService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HeatNetServiceImpl extends SavantServices implements HeatNetService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatNet info) {
        return backRestTemplate.post("/heatNet", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/heatNet?id=" + id, baseServer);
    }

    @Override
    public Response update(HeatNet info) {
        return backRestTemplate.put("/heatNet", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/heatNet?id=" + id, baseServer);
    }

    @Override
    public Response query(HeatNetDto dto) {
        return backRestTemplate.post("/heatNet/query", dto, baseServer);
    }


}
