package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmSetDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointAlarmService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PointAlarmServiceImpl extends SavantServices implements PointAlarmService {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response setPointAlarm(List<PointAlarm> dto) {
        return backRestTemplate.post("/pointAlarm/setPointAlarm", dto, baseServer);
    }

    @Override
    public Response removePointAlarm(List<Integer> ids) {
        return backRestTemplate.post("/pointAlarm/removePointAlarm", ids, baseServer);
    }

    @Override
    public Response updatePointAlarm(PointAlarm dto) {
        return backRestTemplate.put("/pointAlarm/updatePointAlarm", dto, baseServer);
    }

    @Override
    public Response showPointAlarmConfig(PointAlarmSetDto dto) {
        dto.setSetType(2);
        return backRestTemplate.post("/pointAlarm/pointStandard", dto, baseServer);
    }

    @Override
    public Response showPointAlarm(PointAlarmSetDto dto) {
        dto.setSetType(1);
        return backRestTemplate.post("/pointAlarm/pointStandard", dto, baseServer);
    }
}
