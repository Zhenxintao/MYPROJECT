package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmSetDto;
import com.bmts.heating.commons.utils.restful.Response;
import java.util.List;

public interface PointAlarmService {
    Response setPointAlarm(List<PointAlarm> dto);

    Response removePointAlarm(List<Integer> ids);

    Response updatePointAlarm(PointAlarm dto);

    Response showPointAlarm(PointAlarmSetDto dto);

    Response showPointAlarmConfig(PointAlarmSetDto dto);
}
