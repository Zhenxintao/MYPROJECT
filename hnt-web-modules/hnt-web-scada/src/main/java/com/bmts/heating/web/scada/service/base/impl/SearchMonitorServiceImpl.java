package com.bmts.heating.web.scada.service.base.impl;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationScadaDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.SearchMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchMonitorServiceImpl extends SavantServices implements SearchMonitorService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response pageStation(HeatTransferStationScadaDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/heatTransferStation/page";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }

        return tsccRestTemplate.post("/heatTransferStation/page", dto, baseServer, Response.class);
    }

    @Override
    public Response pageHeatNet(BaseDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/heatNet/query";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }

        return tsccRestTemplate.post("/heatNet/query", dto, baseServer, Response.class);
    }

    @Override
    public Response queryAll(Integer userId) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/heatOrganizationService/all";
//            backInfo = restTemplate.getForObject(url, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.get("/heatOrganizationService/all/" + userId, baseServer, Response.class);
    }

    @Override
    public Response pageHeatSource(BaseDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/heatSource/query";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/heatSource/query", dto, baseServer, Response.class);
    }

    @Override
    public Response pagePointStandard(PointStandardSearchDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/pointStandard/pageNetFlag";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/pointStandard/pageNetFlag", dto, baseServer, Response.class);
    }

    @Override
    public Response moreStandard(PointStandardSearchDto dto) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/pointStandard/query";
//            backInfo = restTemplate.postForObject(url, dto, Response.class);
//            log.info("返回远程调用结果:-------------{}", backInfo);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.post("/pointStandard/query", dto, baseServer, Response.class);
    }

    @Override
    public Response listHeaders(int type) {
//        ConnectionToken cd = null;
//        Response backInfo = null;
//        try {
//            cd = super.getToken(baseServer);
//            String url = RestfulUtils.resolutionAddress(cd.getHost(), cd.getPort());
//            url += "/defaultRealHeaders/query";
//            backInfo = restTemplate.getForObject(url, Response.class);
//        } catch (MicroException e) {
//            e.printStackTrace();
//        } finally {
//            super.backToken(baseServer, cd);
//        }
        return tsccRestTemplate.get("/defaultRealHeaders/query/" + type, baseServer, Response.class);
    }

    @Override
    public Response pageStationInfo(BaseDto dto) {
        return tsccRestTemplate.post("/heatTransferStation/pageInfo", dto, baseServer, Response.class);
    }

}
