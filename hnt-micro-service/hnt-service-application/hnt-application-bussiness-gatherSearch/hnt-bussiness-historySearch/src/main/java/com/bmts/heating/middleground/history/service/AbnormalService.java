package com.bmts.heating.middleground.history.service;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.entiy.baseInfo.request.AbnormalDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.gathersearch.response.history.AlarmCountIndex;

import java.util.List;

public interface AbnormalService {

    List<Abnormal> pageAbnormal(AbnormalDto dto);

    Integer alarmCountIndex(AbnormalDto abnormalDto);
}
