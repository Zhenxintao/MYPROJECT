package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.ConfigSysUserListDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.ConfigSysUserListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ConfigSysUserListServiceImpl extends SavantServices implements ConfigSysUserListService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response listConfigSysUser(int userId) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/configSysUserList/query";
//            backInfo = restTemplate.postForObject(url, userId, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/configSysUserList/query", userId, baseServer, Response.class);
    }

    @Override
    public Response addConfig(ConfigSysUserListDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/configSysUserList/add";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/configSysUserList/add", dto, baseServer, Response.class);
    }

    @Override
    public Response updateConfig(ConfigSysUserListDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/configSysUserList/update";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/configSysUserList/update", dto, baseServer, Response.class);
    }

    @Override
    public Response delConfig(int id) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/configSysUserList/del";
//            backInfo = restTemplate.postForObject(url, id, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/configSysUserList/del", id, baseServer, Response.class);
    }


    @Override
    public Response getDetail(int id) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/configSysUserList?id=" + id;
//            backInfo = restTemplate.getForObject(url, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.get("/configSysUserList?id=" + id, baseServer, Response.class);
    }


}
