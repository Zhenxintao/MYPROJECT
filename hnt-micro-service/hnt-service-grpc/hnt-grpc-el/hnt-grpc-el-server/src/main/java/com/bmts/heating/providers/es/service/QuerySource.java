package com.bmts.heating.providers.es.service;


import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;
import java.util.Map;

public interface QuerySource {

	//根据索引查询分页数据
	Map<String,Object> getData(String index, QueryEsDto dto);

	List<Map<String,Object>> getBucketData(String index, QueryEsBucketDto dto, TimeRange range);

	//根据系统获取数据和统计数据
	SearchResponse getDataByHeatSystem(String index, QueryEsDto dto);

}
