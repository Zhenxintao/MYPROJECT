package com.bmts.heating.stream.flink.utils;


import java.util.Objects;

/**
 * @ClassName: HandleTypeUtil
 * @Description: 数据类型解析
 * @Author: pxf
 * @Date: 2020/8/13 17:33
 * @Version: 1.0
 */
public class MonitorHandleUtil {


    /**
     * 根据不同类型进行数据解析
     *
     * @param type
     * @param str
     * @return
     */
    public static String type(int type, String str) {

        switch (type) {
            case 1:
                return strToBool(str);
            case 2:
                return strToInt(str);
            case 3:
                return strToUint(str);
            case 4:
                return strToLong(str);
            case 5:
                return strToULong(str);
            case 6:
                return strToFloat(str);
            case 7:
                return strToDouble(str);
            default:
                return strToDouble(str);
        }


    }

    private static String strToBool(String str) {
        return String.valueOf(Objects.equals(str, "true") ? 1 : 0);
    }

    private static String strToInt(String str) {
        return str;
    }

    private static String strToUint(String str) {
        return str;
    }

    private static String strToLong(String str) {
        // long 类型的字符串转
//        long longStr = Long.parseLong(str);
//        return String.valueOf(longStr);
        return str;
    }

    private static String strToULong(String str) {
        return str;
    }

    private static String strToFloat(String str) {
        // float  类型的字符串转
        float floatStr = Float.parseFloat(str);
        return String.format("%.2f", floatStr);
    }

    private static String strToDouble(String str) {
        // Double  类型的字符串转
        double doubleStr = Double.parseDouble(str);
        return String.format("%.2f", doubleStr);
    }


}
