package com.bmts.heating.service.task.history.service;


import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import org.elasticsearch.action.search.SearchResponse;

import java.util.Map;

public interface QuerySource {

	Map<String,Object> getData(String index, QueryEsDto dto);

	SearchResponse getDataByHeatSystem(String index, QueryEsDto dto);

}
