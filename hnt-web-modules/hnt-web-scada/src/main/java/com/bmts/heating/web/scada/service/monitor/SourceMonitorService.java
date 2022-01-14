package com.bmts.heating.web.scada.service.monitor;

import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceTableDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatSourceRealDataResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointConfigResponse;

import java.util.List;
import java.util.Map;

public interface SourceMonitorService {

    Map<String, Object> table(HeatSourceTableDto dto, Integer userId);

    List<FirstNetBase> sourceFirstNetBase(Integer userId);

    Map<String, Object> getPointDataBySource(List<FirstNetBase> sList, String[] points);

    List<PointConfigResponse> queryPointConfigExist(Integer id);

    List<HeatSourceRealDataResponse> getRealData(HeatSourceTableDto dto);
}
