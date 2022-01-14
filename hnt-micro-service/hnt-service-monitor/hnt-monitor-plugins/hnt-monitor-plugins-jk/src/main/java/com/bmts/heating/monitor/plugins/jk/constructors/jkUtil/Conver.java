package com.bmts.heating.monitor.plugins.jk.constructors.jkUtil;

public class Conver {
    private static final char[] DIGITS_HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };;

    private static String hexStr =  "0123456789ABCDEF";
    //将字节数组转换为16进制字符串2
    public static String byteArrToHex(byte[] btArr) {

        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = DIGITS_HEX[bt>>>4 & 0xf];
            strArr[i++] = DIGITS_HEX[bt & 0xf];
        }
        return new String(strArr);
    }
    //字符串直接拆分成字节流
    public static byte[] hexStringToBytes(String hexString1) {
        if (hexString1 == null || hexString1.equals("")) {
            return null;
        }
        // toUpperCase将字符串中的所有字符转换为大写
        String hexString = hexString1.replaceAll(" ","");
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        // toCharArray将此字符串转换为一个新的字符数组。
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    //charToByte返回在指定字符的第一个发生的字符串中的索引，即返回匹配字符
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    public static String toHex(String str) {
        byte[] data = str.getBytes();
        int outLength = data.length;
        char[] out = new char[outLength << 1];
        for (int i = 0, j = 0; i < outLength; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return new String(out);
    }
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
    //    四位的十六进制，不够的左边补零
    public static String padLeft(String str,int len){
        String pad="000000";
        return len>str.length()&&len<=16&&len>=0?pad.substring(0,len-str.length())+str:str;
    }
}
