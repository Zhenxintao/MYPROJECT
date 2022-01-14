package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.BatchAlarmDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.AlarmConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlarmConfigServiceImpl implements AlarmConfigService {

    @Autowired
    private TSCCRestTemplate template;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response page(PointCollectConfigDto dto) {
//        return template.post("/pointCollectConfig/page", dto, baseServer, Response.class);

        return template.post("/pointConfig/page", dto, baseServer, Response.class);

    }

    @Override
    public Response update(PointCollectConfig entity) {
//        return template.put("/pointCollectConfig", entity, baseServer, Response.class);
        return template.put("/pointConfig", entity, baseServer, Response.class);
    }


    @Override
    public Response isAlarm(Integer id, Boolean state) {
//        return template.put("/pointCollectConfig/" + id + "/" + state, baseServer, Response.class);
        return template.put("/pointConfig/" + id + "/" + state, baseServer, Response.class);
    }

    @Override
    public Response info(Integer id) {
//        return template.get("/pointCollectConfig/" + id, baseServer, Response.class);
        return template.get("/pointConfig/" + id, baseServer, Response.class);
    }

    @Override
    public Response batchIsAlarm(BatchAlarmDto dto) {
//        return template.post("/pointCollectConfig/batch/alarm", dto, baseServer, Response.class);
        return template.post("/pointConfig/batch/alarm", dto, baseServer, Response.class);
    }

}
