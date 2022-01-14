package com.bmts.heating.providers.es.service.impl;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.providers.es.service.QuerySource;
import com.bmts.heating.providers.es.utils.ColumnUtil;
import com.bmts.heating.providers.es.utils.PageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		//条件
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (dto.getHeatSystemId()!=null && dto.getHeatSystemId().length>0) {
			boolQueryBuilder.must(QueryBuilders.termsQuery("relevanceId", Arrays.asList(dto.getHeatSystemId())));
		}
		// 范围查询 from:相当于闭区间; gt:相当于开区间(>) gte:相当于闭区间 (>=) lt:开区间(<) lte:闭区间 (<=)
		if (dto.getStart() != 0 && dto.getEnd() != 0) {
			boolQueryBuilder.filter(QueryBuilders.rangeQuery("timeStrap").gt(dto.getStart()).lte(dto.getEnd()));
		}
		sourceBuilder.query(boolQueryBuilder);
		PageUtil.of(sourceBuilder, dto);//分页处理
		ColumnUtil.of(dto, sourceBuilder);//指定、排除查询列
		SearchRequest rq = new SearchRequest();
		//索引
		rq.indices(index);
		//各种组合条件
		rq.source(sourceBuilder);
		SearchResponse rp;
		try {
			//请求
			rp = client.search(rq, RequestOptions.DEFAULT);
		}catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		//解析返回
		if (rp.status() != RestStatus.OK || rp.getHits().getTotalHits().value <= 0) {
			log.warn("查询结果---{}",Collections.emptyList());
		}
		HashMap<String, Long> totalMap = new HashMap<>();
		totalMap.put("total",rp.getHits().getTotalHits().value);
		Map<String,Object> map = new HashMap<>();
		System.out.println(Arrays.toString(rp.getHits().getHits()));
		map.put("stream" , Arrays.stream(rp.getHits().getHits()).map(SearchHit::getSourceAsString));
		Gson gson = new Gson();
		String s = gson.toJson(totalMap);
		map.put("total",s);
		//获取source
		return map;
	}


	/**
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


	/**
	 * 聚合查询
	 */
	@Override
	public List<Map<String,Object>> getBucketData(String index, QueryEsBucketDto dto, TimeRange range) {
		List<Map<String,Object>> listMap = new ArrayList<>();
		String aggregation;
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			//根据 relevanceId进行分组
			//特别注意，如果不设置size,默认查出最多10条
			TermsAggregationBuilder field = AggregationBuilders.terms("energy");
			if (dto.getIndex() == 1) {
				aggregation = "relevanceId";
				//特别注意，如果不设置size,默认查出最多10条
				field = field.field(aggregation).size(2000);
			}else{
				aggregation = "level";
				field = field.field(aggregation).size(2000);
			}
			AggregationBuilder area = AggregationBuilders.avg("area").field("area");
			for (String columnName : dto.getIncludeFields()) {
				AggregationBuilder sumValue = AggregationBuilders.sum(columnName).field(columnName+".consumption");
				field.subAggregation(sumValue);
			}
			field.subAggregation(area);
			searchSourceBuilder.aggregation(field);
			//where条件
			QueryBuilder relevanceIds = QueryBuilders.termsQuery("relevanceId",  Arrays.asList(dto.getHeatSystemId()));
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(relevanceIds);
			RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("timeStrap")
					.from(range.getStart())
					.to(range.getEnd());
			boolQueryBuilder.must(timeRange);
			searchSourceBuilder.query(boolQueryBuilder);

			SearchRequest searchRequest = new SearchRequest(index).source(searchSourceBuilder);
			SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

			ParsedLongTerms termsCode = response.getAggregations().get("energy");
			List<? extends Terms.Bucket> buckets = termsCode.getBuckets();
			buckets.forEach(coed -> {
				Map<String, Object> map = new HashMap<>();
				String key =  coed.getKey() + "";
				map.put(aggregation,key);
				Aggregations aggregations = coed.getAggregations();

				for (String columnName : dto.getIncludeFields()) {
					ParsedSum val = aggregations.get(columnName);
					ParsedAvg avgArea = aggregations.get("area");
					double sysArea = avgArea.value();
					map.put(columnName,ParseUtil.parseDouble(val.value()));
					Double divide = this.divide(val.value(), sysArea);
					map.put(columnName + "unitStandard",divide);
					if (!Double.isInfinite(sysArea)){
						map.put("area", ParseUtil.parseDouble(sysArea));
					}
				}
				map.put("index",range.getIndex());
				listMap.add(map);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}

	/**
	 * 计算折标热单耗
	 * @param dividend 被除数
	 * @param divisor 除数
	 * @return Double
	 */
	private Double divide(double dividend ,double divisor){
		if (!Double.isInfinite(dividend) && !Double.isInfinite(divisor)){
			Double aDouble = ParseUtil.parseDouble(dividend)/ParseUtil.parseDouble(divisor);
			return ParseUtil.parseDouble(aDouble);
		}
		return 0.0;
	}



}
