package com.bmts.heating.service.task.history.task.common;

import com.bmts.heating.commons.utils.es.ParseUtil;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlgorithmMax implements Algorithm{

	/**
	 * 最大值计算逻辑具体实现
	 * @param aggregation es汇聚查询结果集
	 * @param list
	 * @param dataType
	 * @return 计算结果
	 */
	@Override
	public Object algorithmMethod(ExtendedStats aggregation, List<Object> list, Integer dataType) {
		return ParseUtil.parseDouble(aggregation.getMax());
	}

	@Override
	public Integer algorithmIndex() {
		return 2;
	}
}
