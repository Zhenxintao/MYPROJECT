package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointConfigServiceImpl implements PointConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    private final String baseUrl = "/pointConfig/";

    @Override
    public Response addBatch(PointConfigAddDto info) {
        return backRestTemplate.post(baseUrl + "addBatch", info, baseServer);
    }

    @Override
    public Response deleteBatch(List<PointConfig> pointControlConfigs) {
        return backRestTemplate.post(baseUrl + "deleteBatch", pointControlConfigs, baseServer);
    }

    @Override
    public Response delete(PointConfigDeleteDto param) {
        return backRestTemplate.post(baseUrl + "delete", param, baseServer);
    }

    @Override
    public Response page(PointConfigDto dto) {
        return backRestTemplate.post(baseUrl + "page", dto, baseServer);
    }

    @Override
    public Response update(PointConfig info) {
        return backRestTemplate.put(baseUrl, info, baseServer);
    }

    @Override
    public Response detail(Integer id) {
        return backRestTemplate.get(baseUrl + id, baseServer);
    }

    @Override
    public Response loadOtherPointStandardSearch(PointConfigStandardQueryDto dto) {
        return backRestTemplate.post(baseUrl + "none", dto, baseServer);
    }

    @Override
    public Response pageSource(PointConfigDto dto) {
        return backRestTemplate.post(baseUrl + "pageSource", dto, baseServer);
    }

    @Override
    public Response insertPointConfig(List<PointConfig> dto) {
        return backRestTemplate.post(baseUrl + "insertPointConfig", dto, baseServer);
    }

    @Override
    public Response queryPointConfigExist(Integer id) {
        return backRestTemplate.get(baseUrl + "/queryPointConfigExist/"+id, baseServer);
    }
}
