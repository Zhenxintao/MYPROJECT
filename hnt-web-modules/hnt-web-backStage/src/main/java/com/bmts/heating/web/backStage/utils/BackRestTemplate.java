package com.bmts.heating.web.backStage.utils;

import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.rest.RestUtil;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.entiy.baseInfo.request.RepairOrderImageDto;
import com.bmts.heating.commons.utils.compute.RestfulUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class BackRestTemplate extends SavantServices {

    @Autowired
    private RestTemplate restTemplate;

    private String getHost(ConnectionToken cd) throws UnknownHostException {
        return SpringContextUtil.getActiveProfile().equals("debug") ? InetAddress.getLocalHost().getHostAddress() : cd.getHost();
    }

    public Response file(String url, MultipartFile file, PointStandardImportDto dto, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;

            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            ByteArrayResource isByteResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            paramMap.add("file", isByteResource);
            paramMap.add("userName", dto.getUserName());
            paramMap.add("userId", dto.getUserId());
            paramMap.add("pointConfig", dto.getPointConfig());

            backInfo = restTemplate.postForObject(Baseurl, paramMap, Response.class);
            log.info("返回远程调用结果:-------------{}", backInfo);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }
    public Response file(String url, MultipartFile file, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;

            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            ByteArrayResource isByteResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            paramMap.add("file", isByteResource);
            backInfo = restTemplate.postForObject(Baseurl, paramMap, Response.class);
            log.info("返回远程调用结果:-------------{}", backInfo);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }
    public Response file(String url, MultipartFile file, EnergyConsumptionDto dto, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;

            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            ByteArrayResource isByteResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            paramMap.add("file", isByteResource);
            paramMap.add("id",dto.getId());
            paramMap.add("startTime", dto.getStartTime());
            paramMap.add("endTime", dto.getEndTime());
            paramMap.add("energyType",dto.getEnergyType());
            paramMap.add("isCaculate",dto.getIsCaculate());
            paramMap.add("type",dto.getType());
            paramMap.add("groupId",dto.getGroupId());

            backInfo = restTemplate.postForObject(Baseurl, paramMap, Response.class);
            log.info("返回远程调用结果:-------------{}", backInfo);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }


    public Response fileSystemLogo(String url, MultipartFile file, RepairOrderImageDto dto, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;

            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            ByteArrayResource isByteResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            paramMap.add("image", isByteResource);
            paramMap.add("userName", dto.getUserName());
            paramMap.add("userId", dto.getUserId());
            paramMap.add("systemName", dto.getSystemName());

            backInfo = restTemplate.postForObject(Baseurl, paramMap, Response.class);
            log.info("返回远程调用结果:-------------{}", backInfo);
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
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
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

    /*
    适用于参数在url中xxx/{param}\或无参数
     */
    public Response get(String url, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = restTemplate.getForObject(Baseurl, Response.class);
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

    /*
    适用于参数在url中xxx/{param}\或无参数
     */
    public Response delete(String url, String baseServer) {
        ConnectionToken cd = null;
        Response backInfo = null;
        try {
            cd = super.getToken(baseServer);
//            String Baseurl = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = RestUtil.exchange(Baseurl, HttpMethod.DELETE, Response.class);
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

    public <T> T doHttp(String url, Object param, String baseServer, Class<T> clazz, HttpMethod httpMethod) {
        ConnectionToken cd = null;
        T backInfo = null;
        try {
            cd = super.getToken(baseServer);
            String Baseurl = RestfulUtils.resolutionAddress(getHost(cd), cd.getPort());
            Baseurl += url;
            backInfo = RestUtil.exchange(Baseurl, httpMethod, clazz, param);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
        } finally {
            super.backToken(baseServer, cd);
        }
        return backInfo;
    }


}
