package com.bmts.heating.service.task.history.task.common;

import com.bmts.heating.service.task.history.config.AlgorithmConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;

import java.util.List;
import java.util.Map;

//import static jdk.nashorn.internal.objects.Global.Infinity;

@Slf4j
public class CalcRule {

	private static final Map<Integer, Algorithm> algorithmMap = AlgorithmConfig.ALGORITHM_MAP;
	private static double Infinity;


	/**
	 * 计算策略路由
	 * @param aggregation es汇聚结果集
	 * @param list
	 * @param convergeType
	 * @param dataType 1、2、3 ：最大、差值、平均
	 * @return 计算结果
	 */
	public static Object converges(ExtendedStats aggregation, List<Object> list, Integer convergeType,Integer dataType) {
		Object res;
		if (aggregation.getMax() == -Infinity || aggregation.getMax() == Infinity) {
			return null;
		}
		res = algorithmMap.get(convergeType).algorithmMethod(aggregation, list, dataType);
		return res;
	}





}
