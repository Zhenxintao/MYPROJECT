package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointStandardAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointAlarmService;
import com.bmts.heating.web.backStage.service.point.PointStandardAlarmService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PointStandardAlarmServiceImpl extends SavantServices implements PointStandardAlarmService {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response setPointStandardAlarm(List<PointStandardAlarm> dto) {
        return backRestTemplate.post("/pointStandardAlarm/setPointStandardAlarm", dto, baseServer);
    }

    @Override
    public Response removePointStandardAlarm(List<Integer> ids) {
        return backRestTemplate.post("/pointStandardAlarm/removePointStandardAlarm", ids, baseServer);
    }

    @Override
    public Response updatePointStandardAlarm(PointStandardAlarm dto) {
        return backRestTemplate.put("/pointStandardAlarm/updatePointStandardAlarm", dto, baseServer);
    }

    @Override
    public Response showPointStandardAlarm(PointStandardAlarmDto dto) {
        return backRestTemplate.post("/pointStandardAlarm/page", dto, baseServer);
    }

    @Override
    public Response showPointStandardAlarmConfig(PointStandardDto dto) {
        return backRestTemplate.post("/pointStandardAlarm/pointStandard", dto, baseServer);
    }
}
