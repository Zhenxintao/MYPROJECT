package com.bmts.heating.service.task.history.service.impl;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.service.task.history.service.QuerySource;
import com.bmts.heating.service.task.history.util.ColumnUtil;
import com.bmts.heating.service.task.history.util.PageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class QuerySourceImpl implements QuerySource {

	@Autowired
	private RestHighLevelClient client;

	/**
	 *根据时间分页查询
	 */
	@Override
	public Map<String, Object> getData(String index, QueryEsDto dto) {
		if (index == null) {
			return null;
		}
		try {
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			//条件
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			if (dto.getHeatSystemId()!=null && dto.getHeatSystemId().length>0) {
				boolQueryBuilder.must(QueryBuilders.termsQuery("relevanceId", Arrays.asList(dto.getHeatSystemId())));
			}
			// 模糊查询
			// 范围查询 from:相当于闭区间; gt:相当于开区间(>) gte:相当于闭区间 (>=) lt:开区间(<) lte:闭区间 (<=)
			if (dto.getStart() != 0 && dto.getEnd() != 0) {
				boolQueryBuilder.filter(QueryBuilders.rangeQuery("timeStrap").gte(dto.getStart()).lte(dto.getEnd()));
			}
			sourceBuilder.query(boolQueryBuilder);
			///*分页处理*/
			PageUtil.of(sourceBuilder, dto);
			/*指定、排除查询列*/
			ColumnUtil.of(dto, sourceBuilder);

			SearchRequest rq = new SearchRequest();
			//索引
			rq.indices(index);
			//各种组合条件
			rq.source(sourceBuilder);
			//请求
			SearchResponse rp = client.search(rq, RequestOptions.DEFAULT);
			//解析返回
			if (rp.status() != RestStatus.OK || rp.getHits().getTotalHits().value <= 0) {
				log.warn("查询结果---{}",Collections.emptyList());
			}
			HashMap<String, Long> totalMap = new HashMap<>();
			totalMap.put("total",rp.getHits().getTotalHits().value);
			Map<String,Object> map = new HashMap<>();
			map.put("stream" , Arrays.stream(rp.getHits().getHits()).map(SearchHit::getSourceAsString));
			Gson gson = new Gson();
			String s = gson.toJson(totalMap);
			map.put("total",s);
			//获取source
			return map;//Arrays.stream(rp.getHits().getHits()).map(SearchHit::getSourceAsString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/*
	 *根据时间、系统获取数据
	 */
	@Override
	public SearchResponse getDataByHeatSystem(String index, QueryEsDto dto) {
		if (index == null) {
			return null;
		}
		SearchRequest rq = new SearchRequest();
		//索引
		rq.indices(index);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		//条件
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (dto.getHeatSystemId()!=null && dto.getHeatSystemId().length>0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery("relevanceId", Arrays.asList(dto.getHeatSystemId())));
		}
		// 范围查询
		if (dto.getStart() != 0 && dto.getEnd() != 0) {
			boolQueryBuilder.filter(QueryBuilders.rangeQuery("timeStrap").gte(dto.getStart()).lte(dto.getEnd()));
		}
		sourceBuilder.query(boolQueryBuilder);
		//入参
		if (dto.getIncludeFields() != null) {
			for (int i = 0;i<dto.getIncludeFields().length;i++){
				sourceBuilder.aggregation(AggregationBuilders.extendedStats(dto.getIncludeFields()[i]).field(dto.getIncludeFields()[i]));
			}
		}
		// 入参
		ColumnUtil.of(dto, sourceBuilder);
		rq.source(sourceBuilder);
		SearchResponse search = null;
		// client执行
		try {
			search = client.search(rq, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.warn("IOException:-------{}",e.getMessage());
		}
		return search;
	}
}
