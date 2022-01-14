//package com.bmts.heating.commons.basement.services.ldap.impl;
//
//
//import com.bmts.heating.commons.basement.config.LdapInfo;
//import com.bmts.heating.commons.basement.model.ldap.pojo.base.PointConstructionBO;
//import com.bmts.heating.commons.basement.model.ldap.pojo.vo.PointConstructionVo;
//import com.bmts.heating.commons.basement.services.ldap.ILdapService;
//import com.bmts.heating.commons.basement.services.ldap.PointLCommon;
//import com.bmts.heating.commons.basement.utils.LdapFieldUtil;
//import com.bmts.heating.commons.utils.msmq.PointL;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ldap.core.ContextMapper;
//import org.springframework.ldap.core.DirContextAdapter;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.naming.NamingException;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.BasicAttribute;
//import javax.naming.directory.BasicAttributes;
//import javax.naming.directory.SearchControls;
//import java.util.*;
//
//
//@Service
//public class LdapService implements ILdapService {
//
//
//    @Autowired
//    private LdapTemplate ldapTemplate;
//
//    @Autowired
//    private LdapInfo ldapInfo;
//
//
//    @Override
//    public List<PointConstructionBO> search(PointConstructionVo vo) {
//        List<PointConstructionBO> list = new ArrayList<>();
//        return list;
//    }
//
//    @Override
//    public List<PointConstructionBO> searchList(List<String> list) {
//        List<PointConstructionBO> listBO = new ArrayList<>();
//        return listBO;
//    }
//
//    @Override
//    public boolean addLdap(Class<?> type, Object obj) {
//        return add(type, obj);
//    }
//
//
//    @Override
//    public boolean updateLdap(Class<?> type, Object obj) {
//        Map<String, String> map = LdapFieldUtil.getFields(type, obj);
//
//        // 属性
//        Attributes attrs = new BasicAttributes();
//        for (HashMap.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = map.get(key);
//            String dn = null;
//            try {
//                if (Objects.equals(key, "objectClass")) {
//                    // 基类设置
//                    BasicAttribute ocattr = new BasicAttribute("objectClass");
//                    ocattr.add("top");
//                    ocattr.add(value);
//                    attrs.put(ocattr);
//                } else if (Objects.equals(key, "dn")) {
//                    dn = "ou=" + map.get("ou") + "," + value;
//                } else {
//                    attrs.put(key, value);
//                }
//                ldapTemplate.bind(dn, null, attrs);
//                return true;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public boolean delLdap(String dn) {
//        return false;
//    }
//
//    @Override
//    public List<PointL> query() {
//        return searchAll();
//    }
//
//    private boolean add(Class<?> type, Object obj) {
//        boolean result = false;
//        Map<String, String> map = LdapFieldUtil.getFields(type, obj);
//        // 属性
//        Attributes attrs = new BasicAttributes();
//        for (HashMap.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = map.get(key);
//            String dn = null;
//            try {
//                if (Objects.equals(key, "objectClass")) {
//                    // 基类设置
//                    BasicAttribute ocattr = new BasicAttribute("objectClass");
//                    ocattr.add("top");
//                    ocattr.add(value);
//                    attrs.put(ocattr);
//                } else if (Objects.equals(key, "dn")) {
//                    dn = "ou=" + map.get("ou") + "," + value;
//                } else {
//                    attrs.put(key, value);
//                }
//                ldapTemplate.bind(dn, null, attrs);
//                result = true;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return false;
//            }
//        }
//        return result;
//    }
//
//
//    private List<PointL> searchAll() {
//        List<PointL> list = new ArrayList<>();
//        SearchControls controls = new SearchControls();
//        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//        ldapTemplate.search("", "objectClass=*", controls, new ContextMapper<DirContextAdapter>() {
//            @Override
//            public DirContextAdapter mapFromContext(Object o) throws NamingException {
//                DirContextAdapter context = (DirContextAdapter) o;
//                PointLCommon.setListDto(context, list);
//                return null;
//            }
//        });
//        return list;
//    }
//}
