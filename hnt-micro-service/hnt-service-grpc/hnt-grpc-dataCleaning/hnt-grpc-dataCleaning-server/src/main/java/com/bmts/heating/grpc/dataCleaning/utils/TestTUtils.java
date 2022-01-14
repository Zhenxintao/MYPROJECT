package com.bmts.heating.grpc.dataCleaning.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestTUtils {
    public static void main(String[] args) {
        String str = "o=jincheng";
        String str1 = "ou=System1:100001,o=jincheng";
        String str2 = "cn=DTU1,ou=System1:100001,o=jincheng";
        String str3 = "cn=Alm_POW,cn=DTU1,ou=System1:100001,o=jincheng";
        Student st0 = new Student();
        st0.setId(0);
        st0.setDn("o=shanxi");
        Student st1 = new Student();
        st1.setId(1);
        st1.setDn("o=jincheng,o=shanxi");
        Student st2 = new Student();
        st2.setId(2);
        st2.setDn("o=taiyuan,o=shanxi");
        Student st3 = new Student();
        st3.setId(3);
        st3.setDn("ou=System1:100001,o=jincheng,o=shanxi");
        Student st4 = new Student();
        st4.setId(4);
        st4.setDn("ou=System1:100001,o=taiyuan,o=shanxi");
        Student st5 = new Student();
        st5.setId(5);
        st5.setDn("cn=DTU1,ou=System1:100001,o=jincheng,o=shanxi");
        Student st6 = new Student();
        st6.setId(6);
        st6.setDn("cn=DTU1,ou=System1:100001,o=taiyuan,o=shanxi");
        Student st7 = new Student();
        st7.setId(7);
        st7.setDn("cn=Alm_POW,cn=DTU1,ou=System1:100001,o=jincheng,o=shanxi");
        Student st8 = new Student();
        st8.setId(8);
        st8.setDn("cn=Alm_POW,cn=DTU1,ou=System1:100001,o=taiyuan,o=shanxi");

        List<Student> slist = new ArrayList<>();
        slist.add(st0);
        slist.add(st1);
        slist.add(st2);
        slist.add(st3);
        slist.add(st4);
        slist.add(st5);
        slist.add(st6);
        slist.add(st7);
        slist.add(st8);
//        List<Student> listNew = buildTree(slist, "o=shanxi");
//        System.out.println(listNew);

//        System.out.println(str2.contains("ou=System1:100001,o=jincheng"));
//        System.out.println(str2.contains("ou=System1:100002,o=jincheng"));
//        System.out.println(str2.contains("o=jincheng1"));

//        System.out.println("monitor10_PVSS-0:10000".contains("JK"));
//        System.out.println("monitor10_PVSSJ-0:10000".contains("JK"));
//        System.out.println("monitor10_PVSSJ-0:10000".contains("PVSSS"));
//        System.out.println("monitor10_PVSSJ-0:10000".contains("PVSS"));

        String strrrr = "\"2458\"";
        String strrrrrrrr = "\"\"";
        System.out.println(strrrrrrrr);
        System.out.println(strrrrrrrr.contains("\""));
        System.out.println(strrrr);
        System.out.println(Objects.equals("\"\"", strrrr));
        System.out.println(strrrr.contains("\""));

    }


    private static List<Student> buildTree(List<Student> list, String str) {
        List<Student> treeList = new ArrayList<>();
        list.forEach(stud -> {
            String dn = stud.getDn();
            if (Objects.equals(str, dn.substring(dn.indexOf(",") + 1))) {
                stud.setStuList(buildTree(list, stud.getDn()));
                treeList.add(stud);
            }
        });
        return treeList;
    }
}

@Data
class Student {
    private int id;
    private String dn;
    private List<Student> stuList;
}