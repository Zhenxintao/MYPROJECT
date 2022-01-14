//package com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil;
//
//import com.bmts.heating.commons.utils.msmq.PointL;
//
//import javax.naming.Context;
//import javax.naming.NamingEnumeration;
//import javax.naming.NamingException;
//import javax.naming.directory.*;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//public class LdapConn {
//    public List<PointL> getPvss(String LDAP_URL,String adminName,String adminPassword,String searchBase,String searchFilter) {
//        /**
//         * searchBase：搜索目标节点（cn=PVSS1,cn=DeviceSet,o=beijing,cn=admin,dc=tscc,dc=com）
//         * searchFilter：搜索条件（cn=*）
//         */
//        Hashtable env = new Hashtable();
//        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.PROVIDER_URL, LDAP_URL);
//        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, adminName);
//        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
//        InitialDirContext dc = null;
//        try {
//            dc = new InitialDirContext(env);// 初始化上下文
//            System.out.println("认证成功");
//        } catch (javax.naming.AuthenticationException e) {
//            System.out.println("认证失败");
//        } catch (Exception e) {
//            System.out.println("认证出错：" + e);
//        }
//        SearchControls searchCtls = new SearchControls();// 创建搜索控件
//        searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);// 设置搜索范围
//        //searchCtls.setCountLimit(3000);//最大搜索条目
//        int totalResults = 0;
//        try {
//            NamingEnumeration answer = dc.search(searchBase, searchFilter, searchCtls);
//            List<PointL>list = new ArrayList<>();
//            while (answer.hasMoreElements()) {
//                SearchResult sr = (SearchResult) answer.nextElement();
//                Attributes Attrs = sr.getAttributes();
//                if (Attrs != null) {
//                    try {
//                        for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
//                            Attribute Attr = (Attribute) ne.nextElement();
//                            PointL point = new PointL();
//                            switch (Attr.getID()){
//                                case ("comTsccTagComment"):
//                                    //暂无对应字段
//                                    continue;
//                                case ("comTsccTagType"):
//                                    point.setType(Integer.parseInt(Attr.getAll().next().toString()));
//                                    continue;
//                                case ("comTsccTagReadonly"):
//                                    //暂无对应字段
//                                    continue;
//                                case ("comTsccTagAddress"):
//                                    point.setPointAddress(Attr.getAll().next().toString());
//                                    continue;
//                                case ("objectClass"):
//                                    //暂无对应字段
//                                    continue;
//                                case ("cn"):
////                                    point.setPointId(Attr.getAll().next().toString());
//                                    break;
//                            }
//                            list.add(point);
//                        }
//                    } catch (NamingException e) {
//                        System.err.println("Throw Exception : " + e);
//                    }
//                }
//                totalResults++;
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Throw Exception : " + e);
//        }
//        return null;
//    }
//}
