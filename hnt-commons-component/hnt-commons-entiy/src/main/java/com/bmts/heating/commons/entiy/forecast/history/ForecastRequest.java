package com.bmts.heating.commons.entiy.forecast.history;

import lombok.Data;

import java.util.List;

@Data
public class ForecastRequest {

	//热源关联Id
	private List<Integer> relevanceId;

	//数据类型 1: 小时 2:天
	private Integer type = 1;

	//开始时间
	private Long startTime;

	//结束时间
	private Long endTime;

}
