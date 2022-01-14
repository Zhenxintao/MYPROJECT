package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DefaultRealHeadersService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DefaultRealHeadersServiceImpl extends SavantServices implements DefaultRealHeadersService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response query(int type) {
        return backRestTemplate.get("/defaultRealHeaders/query/" + type, baseServer);
    }

    @Override
    public Response add(DefaultRealHeaders param) {
        return backRestTemplate.post("/defaultRealHeaders/add", param, baseServer);
    }

    @Override
    public Response addBatch(List<DefaultRealHeaders> param) {
        return backRestTemplate.post("/defaultRealHeaders/addBatch", param, baseServer);
    }

    @Override
    public Response update(DefaultRealHeaders param) {
        return backRestTemplate.put("/defaultRealHeaders", param, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/defaultRealHeaders?id=" + id, baseServer);
    }


}
