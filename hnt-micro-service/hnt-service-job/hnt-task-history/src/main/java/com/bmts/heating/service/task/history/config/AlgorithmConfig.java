package com.bmts.heating.service.task.history.config;

import com.bmts.heating.service.task.history.task.common.Algorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class AlgorithmConfig {
	/**
	 * 策略模式,将实现ConsumeInterface接口的对象方法放入集合
	 */
	public static final Map<Integer, Algorithm> ALGORITHM_MAP = new HashMap<>();

	public AlgorithmConfig(List<Algorithm> algorithmService) {
		for (Algorithm algorithm: algorithmService) {
			ALGORITHM_MAP.put(algorithm.algorithmIndex(),algorithm);
		}
	}
}
