package com.bmts.heating.service.task.history.task.common;

import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小时统计计算定义
 */
@Component
public interface Algorithm {
	/**传入数据汇集计算方式
	 * */
	Object algorithmMethod(ExtendedStats aggregation, List<Object> list, Integer dataType);
	/**计算方法标识符
	 * */
	Integer algorithmIndex();
}
