package com.bmts.heating.monitor.plugins.jk.constructors.jkUtil;

public class FloatValueUtil {
    public static String hexToBin(char c) {
        // TODO Auto-generated method stub
        switch (c) {
            case '0':
                return "0000";
            case '1':
                return "0001";
            case '2':
                return "0010";
            case '3':
                return "0011";
            case '4':
                return "0100";
            case '5':
                return "0101";
            case '6':
                return "0110";
            case '7':
                return "0111";
            case '8':
                return "1000";
            case '9':
                return "1001";
            case 'A':
            case 'a':
                return "1010";
            case 'B':
            case 'b':
                return "1011";
            case 'C':
            case 'c':
                return "1100";
            case 'D':
            case 'd':
                return "1101";
            case 'E':
            case 'e':
                return "1110";
            case 'F':
            case 'f':
                return "1111";

        }
        return null;
    }
    public static String indexStr(String str, int first, int last) {
        if (first > str.length())
            return null;
        if (last > str.length()) {
            return str.substring(first, str.length());
        } else {
            return str.substring(first, last);
        }
    }
}

