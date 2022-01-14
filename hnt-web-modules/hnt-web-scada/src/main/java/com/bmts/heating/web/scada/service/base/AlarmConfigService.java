package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.BatchAlarmDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface AlarmConfigService {

	Response page(PointCollectConfigDto dto);

	Response update(PointCollectConfig entity);

	Response isAlarm(Integer id,Boolean state);

	Response info(Integer id);

	Response batchIsAlarm(BatchAlarmDto dto);
}
