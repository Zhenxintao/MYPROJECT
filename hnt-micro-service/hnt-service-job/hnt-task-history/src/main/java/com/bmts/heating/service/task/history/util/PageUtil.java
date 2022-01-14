package com.bmts.heating.service.task.history.util;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PageUtil {

	public static void of(SearchSourceBuilder sourceBuilder , QueryEsDto pageDto){
		if (pageDto.getCurrentPage() == 0) {
			PageUtil.of(sourceBuilder,pageDto,0);
			return;
		}
		int timeOut = 5;
		int start = (pageDto.getCurrentPage()-1)*pageDto.getSize();
		int end = pageDto.getSize();
		if(pageDto.getCurrentPage()*end >= 10000){
			sourceBuilder.trackTotalHits(true);//数据查询大于10000时开启条目数查询
		}
		//分页
		sourceBuilder.from(start);//页数
		sourceBuilder.size(end);//页面条数
		//超时
		sourceBuilder.timeout(new TimeValue(timeOut, TimeUnit.SECONDS));
		PageUtil.of(sourceBuilder,pageDto,0);
	}

	private static void of(SearchSourceBuilder sourceBuilder , QueryEsDto pageDto, int i){
		if(pageDto.getSize() <= 0){
			sourceBuilder.size(10000);//页面条数
		}
		if (pageDto.getField().isEmpty()) {
			pageDto.setField("timeStrap");
			pageDto.setSortType(false);
		}
		Map<String, Boolean> sortFieldsToAsc = new HashMap<>();
		sortFieldsToAsc.put(pageDto.getField(),pageDto.getSortType());
		//排序
		if (!sortFieldsToAsc.isEmpty()) {
			sortFieldsToAsc.forEach((k, v) -> sourceBuilder.sort(new FieldSortBuilder(k).order(v ? SortOrder.ASC : SortOrder.DESC)));
		} else {
			sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
		}
	}
}
