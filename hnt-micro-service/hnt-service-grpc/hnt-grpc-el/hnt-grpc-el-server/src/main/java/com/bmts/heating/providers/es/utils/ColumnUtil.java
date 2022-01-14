package com.bmts.heating.providers.es.utils;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ColumnUtil {

	public static void of(QueryEsDto dto, SearchSourceBuilder sourceBuilder){
		String[] includeFields = dto.getIncludeFields();
		String[] excludeFields = dto.getExcludeFields();
		//返回和排除列
		if (!CollectionUtils.isEmpty(includeFields) || !CollectionUtils.isEmpty(excludeFields)) {
			sourceBuilder.fetchSource(includeFields, excludeFields);
		}
	}
}
