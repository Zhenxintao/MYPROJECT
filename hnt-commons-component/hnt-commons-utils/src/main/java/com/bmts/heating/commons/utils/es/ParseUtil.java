package com.bmts.heating.commons.utils.es;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Slf4j
public class ParseUtil {

	private static final DecimalFormat DF = new DecimalFormat("#.000");


	public static Double parseDouble(Object obj){
		double doubleValue;
		try {
			doubleValue = Double.parseDouble(obj.toString());
			if ( Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)){
				doubleValue = 0.0;
			}else{
				String format = DF.format(doubleValue);
				doubleValue = Double.parseDouble(format);
			}
		} catch (Exception e) {
			log.warn("计算数据结果：{}", obj);
			e.printStackTrace();
			return 0.0;
		}
		return doubleValue;
	}

	public static Long parseLong(Object obj){
		long longValue;
		try {
			longValue = Long.parseLong(String.valueOf(obj));
		} catch (Exception e) {
			log.warn("计算数据结果：{}", obj);
			e.printStackTrace();
			return null;
		}
		return longValue;
	}

	public static Integer parseInter(Object obj){
		Integer intValue = null;
		try {
			intValue = Integer.parseInt(String.valueOf(obj));
		} catch (Exception e) {
			log.warn("计算数据结果：{}", obj);
			e.printStackTrace();
			return null;
		}
		return intValue;
	}

	public static Float parseFloat(Object obj){
		float floatValue = Float.parseFloat(obj.toString());
		try {
			floatValue = Float.parseFloat(DF.format(obj));
		} catch (Exception e) {
			log.warn("计算数据结果：{}", obj);
			e.printStackTrace();
			return null;
		}
		return floatValue;
	}
}
