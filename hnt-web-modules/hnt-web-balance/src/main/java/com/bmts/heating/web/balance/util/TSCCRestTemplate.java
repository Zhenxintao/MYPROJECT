//package com.bmts.heating.web.balance.util;
//
//import com.bmts.heating.commons.container.performance.config.ConnectionToken;
//import com.bmts.heating.commons.container.performance.config.SavantServices;
//import com.bmts.heating.commons.container.rest.RestUtil;
//import com.bmts.heating.commons.utils.compute.RestfulUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Slf4j
//@Component
//public class TSCCRestTemplate extends SavantServices {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public <T> T get(String url, Object param, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = restTemplate.getForObject(Baseurl, clazz, param);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    /*
//    适用于参数在url中xxx/{param}\或无参数
//     */
//    public <T> T get(String url, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = restTemplate.getForObject(Baseurl, clazz);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    public <T> T post(String url, Object param, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
////            backInfo = RestUtil.exchange(Baseurl, HttpMethod.POST, clazz, param);
//            backInfo = restTemplate.postForObject(Baseurl, param, clazz);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    public <T> T delete(String url, Object param, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.DELETE, clazz, param);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    /*
//    适用于参数在url中xxx/{param}\或无参数
//     */
//    public <T> T delete(String url, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.DELETE, clazz);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    public <T> T put(String url, Object param, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.PUT, clazz, param);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    public <T> T put(String url, String baseServer, Class<T> clazz) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.PUT, clazz);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//    public <T> T doHttp(String url, Object param, String baseServer, Class<T> clazz, HttpMethod httpMethod) {
//        ConnectionToken cd = null;
//        T backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, httpMethod, clazz, param);
//        } catch (Exception e) {
//            super.clearToken(baseServer, e);
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
//        return backInfo;
//    }
//
//
//}
