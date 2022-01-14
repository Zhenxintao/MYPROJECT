package com.bmts.heating.monitor.plugins.jk.constructors.jkUtil;

public class JKValue {
//    public static String putJKValue(List<PointL> taskArray, OutputStream ous, InputStream ins) throws IOException {
//        for (PointL point:taskArray){
//            String pointAddress = point.getPointAddress();
//            String pointValue = point.getValue();
//            String[] split = pointAddress.split("\\[");
//            String DValue = split[0];
//            String WValue = split[1].replaceAll("]", "").replaceAll("w","");
//            int i = Integer.parseInt(WValue);
//
//            if (i<1){
//                byte[] BoolValue = GetValues.getBool(pointAddress);
//                ous.write(BoolValue);//请求数据包
//                //接受应答数据包
//                String ret = "";
//                byte[] bytes = new byte[1];
//                while (ins.read(bytes) != -1) {
//                    ret += Conver.byteArrToHex(bytes) + " ";
//                    if (ins.available() == 0) {
//                    //    System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                        String bools = GetValues.getBoolValue(ret);
//                        /** 修改开关类型*/
//                        StringBuffer buffer = new StringBuffer();
//                        char[] ch = bools.toCharArray();
//                        if (bools != null && ch.length>=4){
//                            for (int a=0;a<4;a++){
//                                buffer.append(FloatValueUtil.hexToBin(ch[a]));//十六进制转换成二进制
//                            }
//                        }
//                        StringBuffer bufferOld = new StringBuffer();
//                        char[] chOld = pointValue.toCharArray();
//                        if (pointValue != null && chOld.length>=4){
//                            for (int a=0;a<4;a++){
//                                bufferOld.append(FloatValueUtil.hexToBin(chOld[a]));//十六进制转换成二进制
//                            }
//                        }
//                        String[] split1 = WValue.split(".");
//                        int intValue = Integer.parseInt(split1[1]);
//
//                        //buffer.replace(15-intValue,15-intValue,bufferOld.substring(15-intValue,15-intValue));//StringBuffer直接替换
//                        char[] cha = buffer.toString().toCharArray();
//                        char[] chaOld = bufferOld.toString().toCharArray();
//                        cha[15-intValue]=chaOld[15-intValue];
//                        String str = new String(ch);
//                        if (str == null || str.equals("") || str.length() % 16 != 0)
//                            System.out.println("为空或转换错误！");
//                        else {
//                            StringBuffer tmp = new StringBuffer();
//                            int iTmp = 0;
//                            for (int s = 0; s < str.length(); s += 4) {
//                                iTmp = 0;
//                                for (int j = 0; j < 4; j++) {
//                                    iTmp += Integer.parseInt(str.substring(s + j, s + j + 1)) << (4 - j - 1);
//                                }
//                                tmp.append(Integer.toHexString(iTmp));
//                            }
//                            System.out.println(tmp);//修改过后的值
//                            /**
//                             * 下发请求
//                             */
//                            String s = DValue.replaceAll("[a-zA-Z]", "");
//                            String adds = Conver.padLeft(s, 4);
////                            String[] str1 = new String[10];
////                            str1[0] = "3E";str1[1] = "2A";
////                            str1[2] = adds.substring(0, 2);
////                            str1[3] = adds.substring(2, 4);
////                            str1[4] = "00";str1[5] = "00";
////                            str1[6] = "00";str1[7] = "02";
////                            str1[8] = tmp.substring(0,2);
////                            str1[9] = tmp.substring(2,4);
////                            String aa = Arrays.toString(str1).replaceAll("[\\[\\]]", "").replaceAll(",", "");
//                            String sadd = "3E2A" + adds + "00000002" + tmp;
//                            byte[] byb = Conver.hexStringToBytes(sadd);
//                            ous.write(byb);
//                        }
//                        break;
//                    } else {
//                        System.out.println("wo.设备号通讯不正常或服务器判断单元地址有错误！");
//                    }
//                }
//            }
//            if (i>=1&&i<17){//整数类型
//                /**
//                 * 整数类型
//                 */
//                String addres = pointAddress.replaceAll("[a-zA-Z]","").replaceAll("\\]","");
//                String[] addr = addres.split("\\[");
//                String addref = addr[0];//设备号
//                String pof = addr[1];//点位置
//                int pois =Integer.parseInt(pof);
//                int first = pois*2;
//                int last = first+2;
//
//                String bsf = Conver.padLeft(addref, 4);//四位的十六进制，不够补零
//                String fir = Conver.padLeft(String.valueOf(first), 4);
//                String la = Conver.padLeft(String.valueOf(last), 4);
//
//                //字符数组转字符串
////                String[] str = new String[8];
////                str[0] = "3E";str[1] = "2A";
////                str[2] = bsf.substring(0, 2);
////                str[3] = bsf.substring(2, 4);
////                str[4] = fir.substring(0,2);
////                str[5] = fir.substring(2,4);
////                str[6] = la.substring(0,2);
////                str[7] = la.substring(2,4);
////                str[8] = pointValue.substring(0,2);
////                str[9] = pointValue.substring(2,4);
////
////                String aa = Arrays.toString(str).replaceAll("[\\[\\]]", "").replaceAll(",", "");
//                String saddt = "3E2A" + bsf + fir + la + pointValue;
//                byte[] byf = Conver.hexStringToBytes(saddt);
//                ous.write(byf);
//
//            }
//            if (i>16&&i<30){
//                /**
//                 * 浮点类型
//                 */
//                String addres = pointAddress.replaceAll("[a-zA-Z]","").replaceAll("\\]","");
//                String[] addr = addres.split("\\[");
//                String addref = addr[0];//设备号
//                String pof = addr[1];//点位置
//                int pofs =Integer.parseInt(pof);
//                int first = (pofs-1)*2;//first
//                int last = first+4;
//
//                String bsf = Conver.padLeft(addref, 4);//四位的十六进制，不够补零
//                String fir = Conver.padLeft(String.valueOf(first), 4);
//                String la = Conver.padLeft(String.valueOf(last), 4);
//
//                //字符数组转字符串
////                String[] str = new String[12];
////                str[0] = "3E";str[1] = "2A";
////                str[2] = bsf.substring(0, 2);
////                str[3] = bsf.substring(2, 4);
////                str[4] = fir.substring(0,2);
////                str[5] = fir.substring(2,4);
////                str[6] = la.substring(0,2);
////                str[7] = la.substring(2,4);
////                str[8] = pointValue.substring(0,2);
////                str[9] = pointValue.substring(2,4);
////                str[10] = pointValue.substring(4,6);
////                str[11] = pointValue.substring(6,8);
////
////                String aa = Arrays.toString(str).replaceAll("[\\[\\]]", "").replaceAll(",", "");
//                String saddf = "3E2A" + bsf + fir + la + pointValue;
//                byte[] byf = Conver.hexStringToBytes(saddf);
//                ous.write(byf);
//
//            }
//
//        }
//        return null;
//    }
}
