package com.bmts.heating.web.config.utils;

import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.rest.RestUtil;
import com.bmts.heating.commons.utils.compute.RestfulUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.bmts.heating.commons.container.rest.RestUtil.exchange;

@Slf4j
@Component
public class TSCCRestTemplate extends SavantServices {

    @Autowired
    private RestTemplate restTemplate;

    private String getHost(ConnectionToken cd) throws UnknownHostException {
        return SpringContextUtil.getActiveProfile().equals("debug") ? InetAddress.getLocalHost().getHostAddress() : cd.getHost();
    }

    public <T> T get(String url, Object param, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = restTemplate.getForObject(Baseurl, clazz, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public Response get(String url, Object param, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = restTemplate.getForObject(Baseurl, Response.class, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public Response post(String url, Object param, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
//            backInfo = restTemplate.postForObject(Baseurl, param, Response.class);
            backInfo = RestUtil.exchange(Baseurl, HttpMethod.POST, Response.class, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public Response delete(String url, Object param, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = RestUtil.exchange(Baseurl, HttpMethod.DELETE, Response.class, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }
    public Response put(String url, Object param, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = RestUtil.exchange(Baseurl, HttpMethod.PUT, Response.class, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }
    /*
    适用于参数在url中xxx/{param}\或无参数
     */
    public <T> T get(String url, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = restTemplate.getForObject(Baseurl, clazz);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T get(String url, Class<T> clazz) {
        T backInfo = null;
        try {
            String Baseurl = "http://";
            Baseurl += url;
            backInfo = restTemplate.getForObject(Baseurl, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backInfo;
    }

    public <T> T post(String url, Object param, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.POST, clazz, param);
            backInfo = restTemplate.postForObject(Baseurl, param, clazz);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T postBalance(String baseIp, String url, Object param, Class<T> clazz) {
        T backInfo = null;
        String Baseurl = baseIp;
        Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.POST, clazz, param);
        backInfo = restTemplate.postForObject(Baseurl, param, clazz);
        return backInfo;
    }

    public <T> T post(String ip, String url, Object param, Class<T> clazz) {
        T backInfo = null;
        String Baseurl = "http://" + ip;
        Baseurl += url;
//            backInfo = RestUtil.exchange(Baseurl, HttpMethod.POST, clazz, param);
        backInfo = restTemplate.postForObject(Baseurl, param, clazz);
        return backInfo;
    }

    public <T> T delete(String url, Object param, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = exchange(Baseurl, HttpMethod.DELETE, clazz, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    /*
    适用于参数在url中xxx/{param}\或无参数
     */
    public <T> T delete(String url, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = exchange(Baseurl, HttpMethod.DELETE, clazz);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T put(String url, Object param, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = exchange(Baseurl, HttpMethod.PUT, clazz, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T put(String url, String baseServer, Class<T> clazz) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = exchange(Baseurl, HttpMethod.PUT, clazz);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T doHttp(String url, Object param, String baseServer, Class<T> clazz, HttpMethod httpMethod) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = exchange(Baseurl, httpMethod, clazz, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }

    public <T> T doHttpTemp(String url, Object param, Class<T> clazz, HttpMethod httpMethod) {
        T backInfo = null;
        backInfo = exchange(url, httpMethod, clazz, param);
        return backInfo;
    }

}
