package com.bmts.heating.commons.utils.cron;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author naming
 * @description
 * @date 2021/1/28 17:11
 **/

public class CronUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("0 m H * * ? ");
    private static final SimpleDateFormat sdfWeek = new SimpleDateFormat("0 m H ? * w");
    static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    public static String nextNodeTime(String corn) {
        try {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(corn);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nextTimePoint = new Date();
            return sdf.format(cronSequenceGenerator.next(nextTimePoint));
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDateWeekByPattern(Date date, Integer week) {

        String formatTimeStr = new SimpleDateFormat("0 m H ? * "+week).format(date);

        return formatTimeStr;
    }

    public static String formatDateWeekByPattern(String date, Integer week) throws ParseException {

        return formatDateWeekByPattern(simpleDateFormat.parse(date), week);
    }

    public static String formatDateByPattern(String date) throws ParseException {
        return formatDateByPattern(simpleDateFormat.parse(date));
    }

    public static String formatDateByPattern(Date date) {
        String formatTimeStr = null;

        if (Objects.nonNull(date)) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    public static void main(String[] args) throws ParseException {
//        Date date = new Date();
//        System.out.println(date);
//        date.setTime(System.currentTimeMillis());
//        System.out.println(formatDateByPattern("2017/02/22 23:00:00"));
//        System.out.println(nextNodeTime(formatDateByPattern("23:00")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date parse = simpleDateFormat.parse("24:04");
        System.out.println(parse);
    }


}
