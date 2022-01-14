package com.bmts.heating.web.backStage.service.point;


import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointStandardAlarmDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface PointStandardAlarmService {
    Response setPointStandardAlarm(List<PointStandardAlarm> dto);

    Response removePointStandardAlarm(List<Integer> ids);

    Response updatePointStandardAlarm(PointStandardAlarm dto);

    Response showPointStandardAlarm(PointStandardAlarmDto dto);

    Response showPointStandardAlarmConfig(PointStandardDto dto);
}
