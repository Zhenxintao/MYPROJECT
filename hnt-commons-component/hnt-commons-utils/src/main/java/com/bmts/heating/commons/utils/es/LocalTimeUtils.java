package com.bmts.heating.commons.utils.es;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author hjw
 */
public class LocalTimeUtils {

	private static final ZoneId zone = ZoneId.of("Asia/Shanghai");

	public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
		Instant instant = localDateTime.atZone(zone).toInstant();
		return instant.toEpochMilli();
	}
	/**
	 * 获取分钟
	 * param int 0 当前分钟 1 下一分钟 -1 上一分钟
	 */
	public static long getMinute(int minute) {
		LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0).plusMinutes(minute);
		return getTimestampOfDateTime(localDateTime);
	}

	/**
	 * 获取整点时间
	 * param int 0 当前小时 1 下一小时 -1 上一小时
	 */
	public static long getHour(int hour) {
		LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(hour);
		return getTimestampOfDateTime(localDateTime);
	}
	/**
	 * 获取整点时间
	 * param int 0 当前小时 1 下一小时 -1 上一小时
	 */
	public static LocalDateTime getHourTime(int hour) {
		System.out.println(LocalDateTime.now());
		return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(hour);
	}
	/**
	 * 获取整点时间
	 * param int 0 当前小时 1 下一小时 -1 上一小时
	 */
	public static LocalDateTime getHour(int hour, Boolean index) {
		return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(hour);
	}

	/**
	 * 获取天0点时间
	 * param int 0 当日 1 下一天 -1 上一天
	 */
	public static long getDay(int day) {
		LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).withHour(0).plusDays(day);
		return getTimestampOfDateTime(localDateTime);
	}

	/**
	 * 获取整点时间
	 * param int 0 当日 1 下一天 -1 上一天
	 */
	public static LocalDateTime getDay(int day,Boolean index) {
		return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).withHour(0).plusDays(day);
	}


	public static LocalDateTime getYear(int year,Boolean index){
		return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).withHour(0).plusYears(year);
	}

	/**
	 * 获取上一年的时间点
	 * @param dateTime 时间
	 * @param year int 0 当日 1 下一年 -1 上一年
	 * @return Long
	 */
	public static Long getYear(Long dateTime, int year){
		Instant instant = Instant.ofEpochMilli(dateTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return getTimestampOfDateTime(localDateTime.plusYears(year));
	}

	/**
	 * 获取上一年的时间点
	 * @param dateTime 时间
	 * @param day int 0 当日 1 下一年 -1 上一年
	 * @return Long
	 */
	public static Long getDay(Long dateTime, int day){
		Instant instant = Instant.ofEpochMilli(dateTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return getTimestampOfDateTime(localDateTime.plusDays(day));
	}

	/**
	 * 获取年-月-日
	 * @param dateTime 时间
	 * @return String
	 */
	public static String getLocalDate(Long dateTime){
		Instant instant = Instant.ofEpochMilli(dateTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * 获取年-月-日
	 * @param dateTime 时间
	 * @return String
	 */
	public static String getLocalDateTime(Long dateTime){
		Instant instant = Instant.ofEpochMilli(dateTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(dateTimeFormatter);
	}

	public static String getLocalDateTime(Long dateTime , String format){
		Instant instant = Instant.ofEpochMilli(dateTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * long转LocalDateTime
	 * @param dateTime 时间
	 * @return Long
	 */
	public static LocalDateTime longToLocalDateTime(Long dateTime){
		Instant instant = Instant.ofEpochMilli(dateTime);
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * String日期转换为Long
	 * @param formatDate("yyy-MM-dd HH:mm:ss")
	 * @param date("2013-12-31 21:08:00")
	 * @return * @throws ParseException
	 */
	public static long transferStringDateToLong(String formatDate, String date){
		try {
			SimpleDateFormat sdf= new SimpleDateFormat(formatDate, Locale.CHINA);
			Date dt = sdf.parse(date);
			return dt.getTime();
		}catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
	}
}
