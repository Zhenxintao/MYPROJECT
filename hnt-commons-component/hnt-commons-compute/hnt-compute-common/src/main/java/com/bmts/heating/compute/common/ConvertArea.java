package com.bmts.heating.compute.common;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ConvertArea {

	/**
	 * 折算面积算法类
	 * @param areaMap key:小时 val:面积
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 折算面积
	 */
	public static BigDecimal calc(Map<LocalDateTime, BigDecimal> areaMap, LocalDateTime start, LocalDateTime end) {
		if (areaMap.size() == 0){
			throw new RuntimeException("areaMap is empty");
		}else if(areaMap.size() == 1){
			return areaMap.get(start);
		}
		long allHours = unitl(start, end, DateNum.HOUR);
		Set<LocalDateTime> keySet = areaMap.keySet();
		BigDecimal res = BigDecimal.ZERO;
		List<LocalDateTime> collect = keySet.stream().sorted().collect(Collectors.toList());
		if (!collect.get(0).equals(start)){
			throw new RuntimeException("map开始时间不等于参数开始时间");
		}
		for (int i = 0; i < collect.size(); i++) {
			long hourNum;
			if (i == collect.size()-1){
				hourNum = unitl(collect.get(i), end, DateNum.HOUR);
			}else{
				hourNum = unitl(collect.get(i), collect.get(i + 1), DateNum.HOUR);
			}
			res = res.add(areaMap.get(collect.get(i)).multiply(BigDecimal.valueOf(hourNum)));
		}
		return res.divide(BigDecimal.valueOf(allHours),2, RoundingMode.CEILING);
	}

	private static long unitl(LocalDateTime start, LocalDateTime end, DateNum dateNum) {
		LocalDateTime tempDateTime = LocalDateTime.from(start);
		switch (dateNum) {
			case DAY:
				return tempDateTime.until(end, ChronoUnit.DAYS);
			case HOUR:
				return tempDateTime.until(end, ChronoUnit.HOURS);
			case MINTUE:
				return tempDateTime.until(end, ChronoUnit.MINUTES);
			case SECOND:
				return tempDateTime.until(end, ChronoUnit.SECONDS);
			default:
				throw new RuntimeException("dateNum is error" + dateNum.name());
		}

	}


}
