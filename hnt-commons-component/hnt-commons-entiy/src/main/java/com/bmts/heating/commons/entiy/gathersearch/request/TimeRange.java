package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

@Data
public class TimeRange {
	private Long start;
	private Long end;
	/**
	 * 区分同比环比
	 */
	private String index = "default";
}
