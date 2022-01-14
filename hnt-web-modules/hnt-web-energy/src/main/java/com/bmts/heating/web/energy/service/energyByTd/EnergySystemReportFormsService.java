package com.bmts.heating.web.energy.service.energyByTd;

import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;

public interface EnergySystemReportFormsService {
    Response page(QueryBaseHistoryDto dto);

    Response converge(QueryAggregateHistoryDto dto);
}
