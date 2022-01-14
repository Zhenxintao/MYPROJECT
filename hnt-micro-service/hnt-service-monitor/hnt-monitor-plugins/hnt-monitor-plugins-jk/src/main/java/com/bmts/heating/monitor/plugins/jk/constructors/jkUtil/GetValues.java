package com.bmts.heating.monitor.plugins.jk.constructors.jkUtil;

import java.io.IOException;

public class GetValues {
    /**
     * addre:只针对D1000[w0.0-w29]样式
     * @param addre
     * @return
     * @throws IOException
     */
    public static byte[] getBool(String addre) throws IOException {
        String addres = addre.replaceAll("[a-zA-Z]","").replaceAll("\\]","");
        String[] addr = addres.split("\\[");
        int addref = Integer.parseInt(addr[0]);//设备号
        String s1 = Integer.toHexString(addref);//转换成十六进制
        String adds = Conver.padLeft(s1, 4);//四位的十六进制，不够补零
        //字符数组转字符串
        String s = "3E2A" + adds + "00000002";
        byte[] byb = Conver.hexStringToBytes(s);
        return byb;
    }

    public static byte[] getInt(String addre) {
        String addres = addre.replaceAll("[a-zA-Z]","").replaceAll("\\]","");
        String[] addr = addres.split("\\[");
        int addref = Integer.parseInt(addr[0]);//设备号
        String s = Integer.toHexString(addref);//补齐四位
        int pof = Integer.parseInt(addr[1]);//点位置
        int first = pof*2;
        int last = first+2;
        String s2 = Integer.toHexString(first);
        String ss =Integer.toHexString(last);
        byte[] byi = GetValues.inputValues(s,s2,ss);
        return byi;
    }

    public static byte[] getFloat(String addre) {
        String addres = addre.replaceAll("[a-zA-Z]","").replaceAll("\\]","");
        String[] addr = addres.split("\\[");
        String addref = Integer.toHexString(Integer.parseInt(addr[0]));//设备号,十六进制
        String pof = addr[1];//点位置
        int pofs =Integer.parseInt(pof);
        int first = (pofs-1)*2;//first
        int last = first+4;
        String s = Integer.toHexString(first);
        String s1 = Integer.toHexString(last);
        byte[] byf = GetValues.inputValues(addref,s,s1);
        return byf;
    }
    public static byte[] inputValues(String addre, String first, String last){
        String bsf = Conver.padLeft(addre, 4);//四位的十六进制，不够补零
        String fir = Conver.padLeft(first, 4);
        String la = Conver.padLeft(last, 4);
        String s = "3E2A" + bsf + fir + la;
        //字符数组转字符串
        byte[] byf = Conver.hexStringToBytes(s);
        return byf;
    }
    public static String getBoolValue(String ret) {
        String rep = ret.replaceAll(" ", "");
        if (rep.length() <= 12) {
            System.out.println("A:设备号通讯不正常！");
            return null;
        }
        String bools = FloatValueUtil.indexStr(rep, 12, 16);//bools十六进制字符串
        char[] ch = bools.toCharArray();
        if (bools != null && ch.length == 4) {
            return bools;
        }
        return null;
    }
}