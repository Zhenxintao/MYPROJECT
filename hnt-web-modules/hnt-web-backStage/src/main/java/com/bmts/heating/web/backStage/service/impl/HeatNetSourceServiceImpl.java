package com.bmts.heating.web.backStage.service.impl;


import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatNetService;
import com.bmts.heating.web.backStage.service.HeatNetSourceService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeatNetSourceServiceImpl extends SavantServices implements HeatNetSourceService {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insertNetSource(List<HeatNetSource> info) {
        return backRestTemplate.post("/heatNetSource/insertNetSource",info,baseServer);
    }

    @Override
    public Response deleteNetById(int id) {
        return backRestTemplate.delete("/heatNetSource/deleteNetById?id="+id,baseServer);
    }

    @Override
    public Response deleteSourceById(int id) {
        return backRestTemplate.delete("/heatNetSource/deleteSourceById?id="+id,baseServer);
    }

    @Override
    public Response deleteNetSourceById(int id) {
        return backRestTemplate.delete("/heatNetSource/deleteNetSourceById?id="+id,baseServer);
    }

    @Override
    public Response deleteNetSource(HeatNetSource dto) {
        return backRestTemplate.post("/heatNetSource/deleteNetSource",dto,baseServer);
    }

    @Override
    public Response queryNetSource() {
        return backRestTemplate.get("/heatNetSource/queryNetSource",baseServer);
    }

    @Override
    public Response updNetSource(HeatNetSource info) {
        return backRestTemplate.put("/heatNetSource/updNetSource",info,baseServer);
    }
}
