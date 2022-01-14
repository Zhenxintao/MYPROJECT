package com.bmts.heating.grpc.dataCleaning.utils;


import com.bmts.heating.commons.utils.msmq.PointType;

import java.math.BigInteger;
import java.util.Objects;

/**
 * @ClassName: DataTypeUtil
 * @Description: 数据类型解析
 * @Author: pxf
 * @Date: 2020/8/13 17:33
 * @Version: 1.0
 */
public class DataTypeUtil {


    /**
     * 根据不同类型进行数据字节序解析
     *
     * @param type
     * @param str
     * @return
     */
    public static String type(int type, String str, boolean status) {
        if (type == PointType.POINT_BOOL.getType()) {
            str = strToBool(str, status);
        }
        if (type == PointType.POINT_INT.getType()) {
            str = strToInt(str, status);
        }
        if (type == PointType.POINT_UINT.getType()) {
            str = strToUint(str, status);
        }
        if (type == PointType.POINT_LONG.getType()) {
            str = strToLong(str, status);
        }
        if (type == PointType.POINT_ULONG.getType()) {
            str = strToULong(str, status);
        }
        if (type == PointType.POINT_FLOAT.getType()) {
            str = strToFloat(str, status);
        }
        if (type == PointType.POINT_DOUBLE.getType()) {
            str = strToDouble(str, status);
        }
        return str;
    }

    private static String strToBool(String str, boolean status) {
        if (status) {
            // 十六进制转 bool
            if (Objects.equals(str, "1") || Objects.equals(str, "0")) {
                boolean bool = Objects.equals(str, "1");
                str = String.valueOf(bool);
            }
            if (str.length() == 4) {
                str = BytesUtil.hexToBinary(str);
            }

        }
//        else {
//            // bool  转 十六进制
////            String s = Objects.equals(str, "1") ? "true" : "false";
//            returnStr = str;
//        }

        return str;
    }

    private static String strToInt(String str, boolean status) {
        if (status) {
            // 十六进制转 int  
            int bigInt = new BigInteger(str, 16).intValue();
            str = String.valueOf(bigInt);
        } else {
            // int类型的字符串转  十六进制字符串
            int intStr = Integer.parseInt(str);
            if (intStr > 0) {
                str = BytesUtil.intToHex(intStr, 4);
            } else {
                str = Integer.toHexString(intStr);
            }
        }
        return str;
    }

    private static String strToUint(String str, boolean status) {
        if (status) {
            int uintStr = Integer.parseInt(str, 16);
            str = String.valueOf(uintStr);
        } else {
            // 正数 int类型的字符串转  十六进制字符串
            int uintStr = Integer.parseInt(str);
            str = BytesUtil.intToHex(uintStr, 4);
        }
        return str;
    }

    private static String strToLong(String str, boolean status) {
        if (status) {
            long longStr = new BigInteger(str, 16).longValue();
            str = String.valueOf(longStr);
        } else {
            // long 类型的字符串转  十六进制字符串
            long longStr = Long.parseLong(str);
            str = Long.toHexString(longStr);

        }
        return str;
    }

    private static String strToULong(String str, boolean status) {
        if (status) {
            long ulongStr = Long.parseLong(str, 16);
            str = String.valueOf(ulongStr);
        } else {
            // ulong 类型的字符串转  十六进制字符串
            long ulongStr = Long.parseLong(str);
            str = Long.toHexString(ulongStr);
        }
        return str;
    }

    private static String strToFloat(String str, boolean status) {
        if (status) {
            float floatStr = Float.intBitsToFloat(new BigInteger(str, 16).intValue());
            str = String.valueOf(floatStr);
        } else {
            // float  类型的字符串转 十六进制字符串
            float floatStr = Float.parseFloat(str);
            str = Integer.toHexString(Float.floatToIntBits(floatStr)).toUpperCase();
            str = BytesUtil.add_zore(str, 8);
        }
        return str;
    }

    private static String strToDouble(String str, boolean status) {
        if (status) {
            double doubleStr = Double.parseDouble(str);
            str = String.valueOf(doubleStr);
        } else {
            // Double  类型的字符串转 十六进制字符串
            double doubleStr = Double.parseDouble(str);
            str = Double.toHexString(doubleStr);
        }
        return str;

        // type  保留小数点位数  #.00保留两位  #.0保留一位  #保留整数
        // String toDouble = new DecimalFormat(type).format(Double.parseDouble(str));
    }

    public static void main(String[] args) {
//        String str22 = "ff08";
//        System.out.println(str22.length());
//        String str = BytesUtil.hexToBinary(str22);
//        long startTime = System.currentTimeMillis();
//        String str = BytesUtil.hexToBinary(str22);
//        for (int i = 0; i < 1000000; i++) {
//
//        }
//        System.out.println(System.currentTimeMillis() - startTime);
//        System.out.println(str);
//
//        System.out.println(str.length() + "---");
//
//        String drr = "D1[w0.1]";
//        String[] split = drr.split("\\.");
//        System.out.println(Arrays.toString(split));
//
//        System.out.println(drr.indexOf("."));

//        long strattime = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            int c = drr.charAt(drr.indexOf(".") + 1)- '0';
//        }
//        System.out.println(System.currentTimeMillis() - strattime);
//
//        long strattime11 = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            char c = drr.charAt(drr.indexOf(".") + 1);
//        }
//        System.out.println(System.currentTimeMillis() - strattime11);


//        int c = drr.charAt(drr.indexOf(".") + 1) - '0';
//        System.out.println(c);
//        System.out.println(16 - c);

//        String type = type(6, "-0.02", false);
//        System.out.println(type);
//        float f = Float.intBitsToFloat(new BigInteger(type, 16).intValue());
//        System.out.println(f);
//        String str = "D2[w0.9]";
//        String str2 = "D2[w2]";
//        long stratTime = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            String str66 = "D2[w2]";
//            str66.contains(".");
//        }
//        System.out.println("---" + (System.currentTimeMillis() - stratTime));
//        System.out.println(str2.contains("."));
//
//        long stratTime2 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            String str666 = "D2[w2]";
//            str666.indexOf(".");
//        }
//        System.out.println("---" + (System.currentTimeMillis() - stratTime2));
//        System.out.println(str2.indexOf("."));

//        String strBinary = "9600010100001008";
//        System.out.println(strBinary.length());
//        System.out.println(strBinary.charAt(0));
//        System.out.println(strBinary.charAt(1));
//        System.out.println(strBinary.charAt(15));
//        System.out.println(strBinary.charAt(16));

        String strHex = "0.04";
        strToFloat(strHex, false);
        System.out.println(strToFloat(strHex, false));

        String hexStr = "3D23D70A";
        System.out.println(Float.intBitsToFloat(new BigInteger(hexStr, 16).intValue()));

        String hexStrRR = "02030200";
        System.out.println(Float.intBitsToFloat(new BigInteger(hexStrRR, 16).intValue()));

        String strHexHH = "9.624934E-38";
        System.out.println(strToFloat(strHexHH, false));
    }
}
