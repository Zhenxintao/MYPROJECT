package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.AlarmConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmConfigServiceImpl extends SavantServices implements AlarmConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(AlarmConfig info) {
        return backRestTemplate.post("/alarmConfig", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/alarmConfig?id=" + id, baseServer);
    }

    @Override
    public Response update(AlarmConfig info) {
        return backRestTemplate.put("/alarmConfig", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/alarmConfig?id=" + id, baseServer);
    }

    @Override
    public Response query(AlarmConfigDto dto) {
        return backRestTemplate.post("/alarmConfig/query", dto, baseServer);
    }

}
