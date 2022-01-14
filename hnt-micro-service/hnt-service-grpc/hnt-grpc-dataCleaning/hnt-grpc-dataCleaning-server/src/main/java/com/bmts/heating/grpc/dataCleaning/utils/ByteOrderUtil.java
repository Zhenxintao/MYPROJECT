package com.bmts.heating.grpc.dataCleaning.utils;

public class ByteOrderUtil {

    /**
     * 大端  转  小端   或  小端  转  大端
     * <p>
     * eg: ABCD  转为  DCBA
     *
     * @param str
     * @return
     */
    public static String bigToLittle(String str) {
        //长度对2取余，为了处理字符为奇数
        int re = str.length() % 2;
        StringBuilder sbd = new StringBuilder();
        for (int i = 2; i <= str.length(); i += 2) {
            String sub = str.substring(i - 2, i);
            sbd.insert(0, sub);
        }
        // 处理字符串长度为奇数的
        if (re == 1) {
            sbd.insert(0, "" + str.charAt(str.length() - 1));
        }
        return sbd.toString();
    }


    /**
     * 四个字节的  大端  小端的转换
     * <p>
     * 大端交换
     * eg:  ABCD   转为 BADC
     *
     * @param str
     * @return
     */
    public static String bigToSwap(String str) {
        String[] arr = stringToArray(str);
        // 进行交换
        swarp(arr, 0, 1);
        swarp(arr, 2, 3);
        return arrayToString(arr);
    }

    /**
     * 四个字节的  大端  小端的转换
     * <p>
     * 小端交换
     * eg:  ABCD   转为 CDAB
     *
     * @param str
     * @return
     */
    public static String littleToSwap(String str) {
        String[] arr = stringToArray(str);
        // 进行交换
        swarp(arr, 0, 2);
        swarp(arr, 1, 3);
        return arrayToString(arr);
    }


    /**
     * 把字符串转数组
     *
     * @param str
     * @return
     */
    public static String[] stringToArray(String str) {
        int mi = str.length() / 2;
        //长度对2取余，为了处理字符为奇数
        int re = str.length() % 2;
        //定义字符串数组的长度
        int len = mi + re;
        String[] strs = new String[len];
        int j = 0;
        for (int i = 2; i <= str.length(); i += 2) {
            String sub = str.substring(i - 2, i);
            strs[j] = sub;
            j++;
        }
        // 处理字符串长度为奇数的
        if (re == 1) {
            strs[j] = "" + str.charAt(str.length() - 1);
        }
        return strs;
    }


    /**
     * 数组转字符串
     *
     * @param arr
     * @return
     */
    public static String arrayToString(String[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (String str : arr) {
            sbd.append(str);
        }
        return sbd.toString();
    }


    /**
     * 将传入的任意数组的任意的两个位置进行交换
     *
     * @param t
     * @param i
     * @param j
     */
    public static <T> void swarp(T[] t, int i, int j) {
        T temp = t[i];
        t[i] = t[j];
        t[j] = temp;
    }
}
