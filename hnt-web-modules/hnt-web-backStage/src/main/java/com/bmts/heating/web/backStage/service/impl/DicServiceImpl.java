package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.DicDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DicService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DicServiceImpl extends SavantServices implements DicService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(Dic info) {
        return backRestTemplate.post("/dic", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/dic?id=" + id, baseServer);
    }

    @Override
    public Response update(Dic info) {
        return backRestTemplate.put("/dic", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/dic?id=" + id, baseServer);
    }

    @Override
    public Response query(DicDto dto) {
        return backRestTemplate.post("/dic/query", dto, baseServer);
    }

    @Override
    public Response queryAll() {
        return backRestTemplate.post("/dic/queryAll", null, baseServer);
    }

}
