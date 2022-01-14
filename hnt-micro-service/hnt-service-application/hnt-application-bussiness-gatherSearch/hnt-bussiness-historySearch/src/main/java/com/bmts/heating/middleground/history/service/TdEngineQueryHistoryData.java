package com.bmts.heating.middleground.history.service;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryAggregateTdDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTdDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryBaseDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;

import java.util.List;
import java.util.Map;

public interface TdEngineQueryHistoryData {

	List<Map<String, String>> queryHistoryData(QueryTdDto dto);

	List<Map<String, String>> queryHistoryEnergyData(QueryAggregateTdDto dto,Integer level);
	/**
	 * Td基础历史数据查询
	 * */
	HistoryBaseDataResponse  queryHistoryBase(QueryBaseHistoryDto dto);
	/**
	 * Td基础历史能耗数据查询
	 * */
	HistoryEnergyDataResponse queryHistoryEnergy(QueryBaseHistoryDto dto);
	/**
	 * Td聚合历史数据查询
	 * */
	HistoryBaseDataResponse queryHistoryAggregate(QueryAggregateHistoryDto dto);
	/**
	 * Td聚合历史能耗数据查询
	 * */
	HistoryEnergyDataResponse queryHistoryAggregateEnergy(QueryAggregateHistoryDto dto);
}
