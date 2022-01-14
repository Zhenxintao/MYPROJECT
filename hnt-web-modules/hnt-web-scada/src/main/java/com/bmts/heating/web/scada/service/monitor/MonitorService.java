package com.bmts.heating.web.scada.service.monitor;

import com.bmts.heating.commons.basement.model.db.request.QueryShowPowerDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.*;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.entiy.converge.HeatStationRealDto;
import com.bmts.heating.commons.entiy.converge.HeatStationRealResponseDto;

import java.util.List;
import java.util.Map;

public interface MonitorService {


    Map<String, Object> getBigTable(MonitorDto dto);

    Map<String, Object> getBigTable(MonitorSingleDto dto);

    List<CommonTree> getOrgStationTree(MonitorDto dto);

    List<CommonTree> getOrgSystemTree(MonitorDto dto);

    Map<String, Object> getSystemRealData(QuerySystemDto dto);

    List<Map<String, Object>> selShowPower(QueryShowPowerDto dto);


    List<CommonTree> sourceSystemTree(MonitorDto dto);

    List<HeatStationRealResponseDto> getRealData(HeatStationRealDto dto);

    List<Map<String, Object>> queryHydrostaticTempDiagram(QueryHydrostaticTempDto dto);

    Map<String, Object> getBeyond(MonitorBeyondDto dto);

}
