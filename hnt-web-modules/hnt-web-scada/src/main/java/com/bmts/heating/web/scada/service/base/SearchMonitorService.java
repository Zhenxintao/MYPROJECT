package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationScadaDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface SearchMonitorService {

    Response pageStation(HeatTransferStationScadaDto dto);

    Response pageHeatNet(BaseDto dto);

    Response queryAll(Integer userId);

    Response pageHeatSource(BaseDto dto);

    Response pagePointStandard(PointStandardSearchDto dto);

    Response moreStandard(PointStandardSearchDto dto);

    Response listHeaders(int type);

    Response pageStationInfo(BaseDto dto);


}
