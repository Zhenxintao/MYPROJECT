package com.bmts.heating.commons.basement.utils;

public class LdapStrUtil {

    /**
     * 判断是不是正整数
     * 效率最高的
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }


}
