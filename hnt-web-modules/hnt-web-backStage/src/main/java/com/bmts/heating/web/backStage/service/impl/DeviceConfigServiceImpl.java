package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.DeviceConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DeviceConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceConfigServiceImpl extends SavantServices implements DeviceConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(DeviceConfig info) {
        return backRestTemplate.post("/deviceConfig", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/deviceConfig?id=" + id, baseServer);
    }

    @Override
    public Response update(DeviceConfig info) {
        return backRestTemplate.put("/deviceConfig", info, baseServer);
    }


    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/deviceConfig?id=" + id, baseServer);
    }

    @Override
    public Response query() {
        return backRestTemplate.get("/deviceConfig/query", baseServer);
    }


}
