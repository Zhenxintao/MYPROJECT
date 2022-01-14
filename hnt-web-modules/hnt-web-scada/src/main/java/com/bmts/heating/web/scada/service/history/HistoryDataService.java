package com.bmts.heating.web.scada.service.history;

import com.bmts.heating.commons.entiy.gathersearch.request.ProductReportDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryHistoryDataDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleStationRepoDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface HistoryDataService {

	Response queryHistoryData(QueryHistoryDataDto dto);
	Response querySourceAllInfo(Integer id,Integer userId);
}
