package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointCollectConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PointCollectConfigServiceImpl extends SavantServices implements PointCollectConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response addBatch(PointCollectConfigAddDto info) {
        return backRestTemplate.post("/pointCollectConfig/addBatch", info, baseServer);
    }

    @Override
    public Response deleteBatch(List<PointCollectConfig> param) {
        return backRestTemplate.post("/pointCollectConfig/deleteBatch", param, baseServer);
    }

    @Override
    public Response delete( PointCollectDeleteDto param) {
        return backRestTemplate.post("/pointCollectConfig/delete", param, baseServer);
    }

    @Override
    public Response page(PointCollectConfigDto dto) {
        return backRestTemplate.post("/pointCollectConfig/page", dto, baseServer);
    }

    @Override
    public Response update(PointCollectConfig info) {
        return backRestTemplate.put("/pointCollectConfig", info, baseServer);
    }

    @Override
    public Response detail(Integer id) {
        return backRestTemplate.get("/pointCollectConfig/" + id, baseServer);
    }

    @Override
    public Response loadOtherPointStandardSearch(PointConfigStandardQueryDto dto) {
        return backRestTemplate.post("/pointCollectConfig/none", dto, baseServer);
    }


}
