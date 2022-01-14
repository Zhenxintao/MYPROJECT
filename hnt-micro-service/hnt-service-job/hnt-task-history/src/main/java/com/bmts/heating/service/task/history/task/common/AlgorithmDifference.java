package com.bmts.heating.service.task.history.task.common;

import com.bmts.heating.commons.utils.es.ParseUtil;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AlgorithmDifference implements Algorithm{

	/**
	 * 差值计算逻辑具体实现
	 * @param aggregation es汇聚查询结果集
	 * @param list
	 * @param dataType
	 * @return 计算结果
	 */
	@Override
	public Object algorithmMethod(ExtendedStats aggregation, List<Object> list, Integer dataType) {
		if (CollectionUtils.isEmpty(list)){
			return null;
		}else {
			double start = ParseUtil.parseDouble(list.get(0));
			double end = ParseUtil.parseDouble(list.get(list.size()-1));
			if(start > end){
				return aggregation.getMax()+end-start;
			}
		}
		return aggregation.getMax() - aggregation.getMin();
	}

	@Override
	public Integer algorithmIndex() {
		return 1;
	}
}
