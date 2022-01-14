package com.bmts.heating.service.task.history.task.common;

import com.bmts.heating.commons.utils.es.ParseUtil;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlgorithmAvg implements Algorithm{
	@Override
	public Object algorithmMethod(ExtendedStats aggregation, List<Object> list, Integer dataType) {
		return ParseUtil.parseDouble(aggregation.getAvg());
	}

	@Override
	public Integer algorithmIndex() {
		return 3;
	}
}
