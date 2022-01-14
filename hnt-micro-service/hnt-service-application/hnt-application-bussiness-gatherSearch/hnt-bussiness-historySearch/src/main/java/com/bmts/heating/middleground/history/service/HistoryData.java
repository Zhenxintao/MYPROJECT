package com.bmts.heating.middleground.history.service;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HistoryData {

	List<Map<String, String>> findHistoryData(QueryEsDto dto);

	List<Map<String, Object>> findHistoryEnergyData(QueryEsDto dto);

	List<Map<String, String>> bucketEnergyData(QueryEsBucketDto dto);
}
