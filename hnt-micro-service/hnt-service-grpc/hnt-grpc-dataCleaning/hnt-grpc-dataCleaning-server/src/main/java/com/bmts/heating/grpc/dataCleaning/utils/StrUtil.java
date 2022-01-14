//package com.bmts.heating.grpc.dataCleaning.utils;
//
//
//import java.util.*;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//public class StrUtil {
//
//    public static void main(String[] args) {
//        long starTime = System.currentTimeMillis();
//        OrFilter orFilter = new OrFilter();
//        String str = "cn=Alm_P2D_H,cn=1,cn=DTU1,ou=System1:100011";
//        orFilter.append(new EqualsFilter(str.substring(0, str.indexOf("=")), "Alm_P2D_H,cn=1,cn=DTU1,ou=System1:100011"));
//        String[] split11 = str.split(",");
//        for (int i = 0; i < split11.length - 1; i++) {
//            str = str.substring(str.indexOf(",") + 1);
////            System.out.println(str);
//            orFilter.append(new EqualsFilter(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1)));
//        }
//        System.out.println("---   耗时" + (System.currentTimeMillis() - starTime));
//        System.out.println(orFilter.encode());
//
//    }
//
//    public static void main44(String[] args) {
//
//
//        long starTime = System.currentTimeMillis();
//        List<LdapDTO> listAll = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key" + i);
//            dto.setComTsccPointAddress("address" + i);
//            listAll.add(dto);
//        }
//        System.out.println("---添加耗时" + (System.currentTimeMillis() - starTime));
//
//        List<LdapDTO> list = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key100000");
//            dto.setComTsccPointAddress("address100000");
//            list.add(dto);
//        }
//
//        // 取差集
//        long starTime22 = System.currentTimeMillis();
//        List<LdapDTO> reduce1 = listAll.stream().filter(item -> !list.contains(item)).collect(Collectors.toList());
//        System.out.println("---Lamdba 取差集 list 耗时" + (System.currentTimeMillis() - starTime22));
//        System.out.println("-----------------  " + reduce1.size());
//
//        // 取差集
//        long starTime33 = System.currentTimeMillis();
//        boolean b = listAll.removeAll(list);
//        System.out.println("---removeAll 取差集 list 耗时" + (System.currentTimeMillis() - starTime33));
//        System.out.println("-----------------  " + listAll.size());
//
//
//        long starTime44 = System.currentTimeMillis();
//        Map<String, LdapDTO> map1 = new HashMap<>();
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key" + i);
//            dto.setComTsccPointAddress("address" + i);
//            map1.put("key" + i, dto);
//        }
//        System.out.println("---map1 添加耗时" + (System.currentTimeMillis() - starTime44));
//
//        long starTime55 = System.currentTimeMillis();
//        Map<String, LdapDTO> map2 = new HashMap<>();
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key" + i);
//            dto.setComTsccPointAddress("address" + i);
//            map2.put("key" + i, dto);
//        }
//        System.out.println("---map2 添加耗时" + (System.currentTimeMillis() - starTime55));
//
//
//        long starTime66 = System.currentTimeMillis();
//        for (Iterator<String> ite = map1.keySet().iterator(); ite.hasNext(); ) {
//            String next = ite.next();
//            if (Objects.equals(next, "key100000")) {
//                ite.remove();
//            }
//
//        }
//        System.out.println("---map 删除 耗时" + (System.currentTimeMillis() - starTime66));
//        System.out.println("-----------------  " + map1.size());
//
//        long starTime77 = System.currentTimeMillis();
//        boolean b2 = map2.keySet().removeIf(key -> Objects.equals(key, "key100000"));
//        System.out.println("---map 删除 耗时" + (System.currentTimeMillis() - starTime77));
//        System.out.println("-----------------  " + map2.size());
//
//    }
//
//    public static void main33(String[] args) {
//        Map<String, Boolean> map = new HashMap<>();
//        map.put("cn=Alm_P2D_H,cn=1,cn=DTU1,ou=System1:100011,o=jincheng,o=shanxi,dc=tscc,dc=com", true);
//        map.put("cn=1,cn=DTU1,ou=System1:100011,o=jincheng,o=shanxi,dc=tscc,dc=com", true);
//        map.put("cn=DTU1,ou=System1:100011,o=jincheng,o=shanxi,dc=tscc,dc=com", true);
//        map.put("ou=System1:100011,o=jincheng,o=shanxi,dc=tscc,dc=com", true);
//
//        String str = "ou=System1:100011,o=jincheng,o=shanxi,dc=tscc,dc=com";
//        System.out.println(map.containsKey(str));
//
//    }
//
//    public static void main22(String[] args) {
//        List<LdapDTO> listAll = new ArrayList<>();
//        long starTime = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key" + i);
//            dto.setComTsccPointAddress("address" + i);
//            listAll.add(dto);
//        }
//        System.out.println("---添加耗时" + (System.currentTimeMillis() - starTime));
//
//
//        List<LdapDTO> list = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO dto = new LdapDTO();
//            dto.setDn("key" + i);
//            dto.setComTsccPointAddress("address" + i);
//            list.add(dto);
//        }
//
//
////        // 取差集
////        long starTime22 = System.currentTimeMillis();
////        List<LdapDTO> reduce1 = listAll.stream().filter(item -> !list.contains(item)).collect(Collectors.toList());
////        System.out.println("---Lamdba 取差集 耗时" + (System.currentTimeMillis() - starTime22));
////        System.out.println("-----------------  " + reduce1.size());
////
////        // 取差集
////        long starTime33 = System.currentTimeMillis();
////        boolean b = listAll.removeAll(list);
////        System.out.println("---removeAll 取差集 耗时" + (System.currentTimeMillis() - starTime33));
////        System.out.println(listAll.size());
//
//        // 取差集
//        long starTime44 = System.currentTimeMillis();
//        List<LdapDTO> reduceDto = new ArrayList<LdapDTO>();
//        HashMap<String, Boolean> map = new HashMap();
//        for (int i = 0; i < 1000000; i++) {
//            map.put(list.get(i).getDn(), true);
//        }
//        for (int i = 0; i < 1000000; i++) {
//            LdapDTO x = listAll.get(i);
//            if (!map.containsKey(x.getDn())) {
//                reduceDto.add(x);
//            }
//        }
//        System.out.println("---map 取差集 耗时" + (System.currentTimeMillis() - starTime44));
//        System.out.println(reduceDto.size());
//
//
//    }
//
//    public static void mainee(String[] args) {
//        String str = "cn=Alm_P2D_H,cn=1,cn=DTU1,ou=System1:100011";
//        String strSub1 = str.substring(str.indexOf(",") + 1);
//        System.out.println(strSub1);
//        String strSub2 = strSub1.substring(strSub1.indexOf(",") + 1);
//        System.out.println(strSub2);
//        String strSub3 = strSub2.substring(strSub2.indexOf(",") + 1);
//        System.out.println(strSub3);
//        String strSub4 = strSub3.substring(strSub3.indexOf(",") + 1);
//        System.out.println(strSub4);
//
//        int n = str.length() - str.replaceAll(",", "").length();
//        System.out.println(n);
//        String[] split = str.split(",");
//        System.out.println(split.length);
//
//        long timeSplit = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            String[] sp = str.split(",");
//        }
//        System.out.println("---split  耗时" + (System.currentTimeMillis() - timeSplit));
//
//        long timeSub = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            String strSub = str.substring(str.indexOf(",") + 1);
//        }
//        System.out.println("---substring  耗时" + (System.currentTimeMillis() - timeSub));
//
//    }
//
//    public static void mainss(String[] args) {
////        String str = "PCIp";
////        String str = "-11";
//        String str = "12.12";
//        boolean isInteger = isInteger(str);
//        boolean isNumeric1 = isNumeric1(str);
//        boolean isNumeric2 = isNumeric2(str);
//        boolean isNumeric = isNumeric(str);
//        System.out.println("--------");
//        long timeisInteger = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            boolean integer = isInteger(str);
//        }
//        System.out.println("---isInteger  耗时" + (System.currentTimeMillis() - timeisInteger));
//
//        long timeisNumeric1 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            boolean integer = isNumeric1(str);
//        }
//        System.out.println("---isNumeric1  耗时" + (System.currentTimeMillis() - timeisNumeric1));
//
//        long timeisNumeric2 = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            boolean integer = isNumeric2(str);
//        }
//        System.out.println("---isNumeric2  耗时" + (System.currentTimeMillis() - timeisNumeric2));
//
//        long timeisNumeric = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            boolean integer = isNumeric(str);
//        }
//        System.out.println("---isNumeric  耗时" + (System.currentTimeMillis() - timeisNumeric));
//
//
//    }
//
//    /**
//     * 判断是不是正整数
//     * 效率最高的
//     *
//     * @param str
//     * @return
//     */
//    public static boolean isNumeric(String str) {
//        for (int i = str.length(); --i >= 0; ) {
//            int chr = str.charAt(i);
//            if (chr < 48 || chr > 57)
//                return false;
//        }
//        return true;
//    }
//
//
//    public static boolean isInteger(String str) {
//        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        return pattern.matcher(str).matches();
//    }
//
//
//    public static boolean isNumeric1(String str) {
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
//    }
//
//
//    public final static boolean isNumeric2(String s) {
//        if (s != null && !"".equals(s.trim()))
//            return s.matches("^[0-9]*$");
//        else
//            return false;
//    }
//
//}
