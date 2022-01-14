package com.bmts.heating.monitor.plugins.jk.constructors.jkUtil;

import com.bmts.heating.commons.utils.msmq.PointL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JKValueTwo {
    public static String putValue(List<PointL> taskArray, OutputStream ous, InputStream ins) throws IOException {
        Map<String, Map<String, String>> map = new HashMap<>();
        for (PointL point : taskArray) {
//            String pointAddress = point.getPointAddress();
//            String[] split = pointAddress.split("\\[");
//            String WValue = split[1].replaceAll("]", "").replaceAll("w","");
//            String DValue = split[0].replaceAll("D","");
//            double i = Double.parseDouble(WValue);
//            Map<String,String> map1 = new HashMap<>();
//            for (PointL pointT:taskArray) {
//                String pointAddress1 = pointT.getPointAddress();
//                String[] split1 = pointAddress1.split("\\[");
//                String DValue1 = split1[0].replaceAll("D","");
//                String WValue1 = split1[1].replaceAll("]", "").replaceAll("w","");
//                double i1 = Double.parseDouble(WValue1);
//                if (DValue1.equals(DValue) && i<1 && i1<1){
//                    map1.put(WValue1,pointT.getValue());
//                }
//            }
//            if (i<1){
//                map.put(DValue,map1);//把所有的bool类型存放map中
//            }
//            if (i>=1&&i<16){
//                /**整数类型*/
//                int first = (int)(i*2);
//                String sixValue = JKValueTwo.getSixValue(DValue, first);
//                String s1 = "3E2A" + sixValue + "0002" + point.getValue();
//
//                byte[] byf = Conver.hexStringToBytes(s1);
//                ous.write(byf);
//                String ret = "";
//                byte[] bytes = new byte[1];
//                while (ins.read(bytes) != -1) {//接受应答数据包
//                    ret += Conver.byteArrToHex(bytes) + " ";
//                    if (ins.available() == 0) {
//                        System.out.println(pointAddress+":----->Jk下发："+ret);
//                        break;
//                    }
//                }
//            }
//            if (i>16&&i<30){
//                /** 浮点类型*/
//                int first = (int)((i-1)*2);//first
//                String sixValue = JKValueTwo.getSixValue(DValue, first);
//                String s1 = "3E2A" + sixValue + "0004" + point.getValue();
//
//                byte[] byf = Conver.hexStringToBytes(s1);
//                ous.write(byf);
//                String ret = "";
//                byte[] bytes = new byte[1];
//                while (ins.read(bytes) != -1) {//接受应答数据包
//                    ret += Conver.byteArrToHex(bytes) + " ";
//                    if (ins.available() == 0) {
//                        System.out.println(pointAddress+":----->JK下发："+ret);
//                        break;
//                    }
//                }
//            }
        }
        //先根据设备号进行数据请求，再对原数据进行对比修改
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            byte[] BoolValue = GetValues.getBool(entry.getKey());
            ous.write(BoolValue);//请求数据包
            //接受应答数据包
            String ret = "";
            byte[] bytes = new byte[1];
            StringBuffer buffer = new StringBuffer();
            while (ins.read(bytes) != -1) {
                ret += Conver.byteArrToHex(bytes) + " ";
                if (ins.available() == 0) {
                    String bools = GetValues.getBoolValue(ret);
                    char[] ch = bools.toCharArray();
                    if (bools != null && ch.length >= 4) {
                        for (int a = 0; a < 4; a++) {
                            buffer.append(FloatValueUtil.hexToBin(ch[a]));//十六进制转换成二进制
                        }
                    }
                    break;
                }
            }
            char[] chars = buffer.toString().toCharArray();//原二进制数;
            for (Map.Entry<String, String> entry1 : entry.getValue().entrySet()) {
                System.out.println("Key = " + entry1.getKey() + ", Value = " + entry1.getValue());
                String s = entry1.getKey().split(".")[1];
                int i = Integer.parseInt(s);
//                char[] ch1 = entry1.getValue().toCharArray();
//                for (char ca:ch1){
//                    buffer1.append(FloatValueUtil.hexToBin(ca));
//                }
                //char[] chars1 = buffer1.toString().toCharArray();//十六位二进制数
                chars[15 - i] = (char) Integer.parseInt(entry1.getValue());
            }

            String str = new String(chars);
            if (str == null || str.equals("") || str.length() % 16 != 0)
                System.out.println("为空或转换错误！");
            else {
                StringBuilder tmp = new StringBuilder();
                int iTmps = 0;
                for (int s = 0; s < str.length(); s += 4) {
                    iTmps = 0;
                    for (int j = 0; j < 4; j++) {
                        iTmps += Integer.parseInt(str.substring(s + j, s + j + 1)) << (4 - j - 1);
                    }
                    tmp.append(Integer.toHexString(iTmps));
                }
//                System.out.println(tmp);//修改过后的值
                /**下发请求*/
                int i = Integer.parseInt(entry.getKey()) + 1000;
                String s = Integer.toHexString(i);
                String adds = Conver.padLeft(s, 4);
                String s1 = "3E2A" + adds + "00000002" + tmp;
                byte[] byb = Conver.hexStringToBytes(s1);
                ous.write(byb);
                String rets = "";
                byte[] bys = new byte[1];
                while (ins.read(bys) != -1) {//接受应答数据包
                    ret += Conver.byteArrToHex(bys) + " ";
                    if (ins.available() == 0) {
                        System.out.println(entry.getKey() + ":----->JK下发：" + rets);
                        break;
                    }
                }
            }
        }
        return null;
    }

    public static String getSixValue(String DValue, int first) {
        int i1 = Integer.parseInt(DValue) + 1000;
        String s = Integer.toHexString(i1);//转换成十六进制
        String s2 = Integer.toHexString(first);
        String s3 = Conver.padLeft(s, 4);//不足四位前面补零
        String s4 = Conver.padLeft(s2, 4);
        return s3 + s4;
    }
}
