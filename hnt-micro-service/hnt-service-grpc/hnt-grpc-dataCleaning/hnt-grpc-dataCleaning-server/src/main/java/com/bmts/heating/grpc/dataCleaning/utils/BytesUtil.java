package com.bmts.heating.grpc.dataCleaning.utils;

/**
 * @ClassName: BytesUtils
 * @Description: 字节转换工具
 * @Author: pxf
 * @Date: 2020/7/30 15:09
 * @Version: 1.0
 */
public class BytesUtil {


    /**
     * int  类型 转 十六进制  字符串
     *
     * @param n
     * @return
     */
    public static String intToHex(int n, int size) {
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        a = add_zore(a, size);
        return a;
    }

    /**
     * 十六进制 字符串 位数补全
     *
     * @param str
     * @param size
     * @return
     */
    public static String add_zore(String str, int size) {
        if (str.length() < size) {
            str = "0" + str;
            str = add_zore(str, size);
            return str;
        } else {
            return str;
        }
    }

    /**
     * 十六进制转成二进制
     *
     * @param hexNum
     * @return
     */
    public static String hexToBinary(String hexNum) {
        char[] chs = {'0', '1'};
        String str = new String("0123456789ABCDEF");
        char[] charArray = hexNum.toUpperCase().toCharArray();// toCharArray()：将字符串对象中的字符转大写字母转换为一个字符数组
        int pos = charArray.length * 4;
        char[] binaryArray = new char[pos];
        for (int i = charArray.length - 1; i >= 0; i--) {
            int temp = str.indexOf(charArray[i]);
            for (int j = 0; j < 4; j++) {
                binaryArray[--pos] = chs[temp & 1];
                temp = temp >>> 1;

            }
        }
        return new String(binaryArray);
    }

    public static void main(String[] args) {
        long starTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            // 这种效率比价高
            String strBud = intToHex(150, 4);
        }
        System.out.println("StringBuilder 的方法耗时：" + (System.currentTimeMillis() - starTime));
//        System.out.println("150 的十六进制字符串是：" + strBud);

        long bigenTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            String str = String.format("%04X", 150);
        }
        System.out.println("String.format 的方法耗时：" + (System.currentTimeMillis() - bigenTime));
//        System.out.println("String.format 的  150 的十六进制字符串是：" + str);
    }
}
