package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointControlConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.PointControlConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointControlConfigAddDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointControlConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PointControlConfigServiceImpl extends SavantServices implements PointControlConfigService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response addBatch(PointControlConfigAddDto info) {
        return backRestTemplate.post("/pointControlConfig/addBatch", info, baseServer);
    }

    @Override
    public Response deleteBatch(List<PointControlConfig> pointControlConfigs) {
        return backRestTemplate.post("/pointControlConfig/deleteBatch", pointControlConfigs, baseServer);
    }

    @Override
    public Response delete( PointCollectDeleteDto param) {
        return backRestTemplate.post("/pointControlConfig/delete", param, baseServer);
    }

    @Override
    public Response page(PointControlConfigDto dto) {
        return backRestTemplate.post("/pointControlConfig/page", dto, baseServer);
    }

    @Override
    public Response update(PointControlConfig info) {
        return backRestTemplate.put("/pointControlConfig", info, baseServer);
    }

    @Override
    public Response detail(Integer id) {
        return backRestTemplate.get("/pointControlConfig/" + id, baseServer);
    }

    @Override
    public Response loadOtherPointStandardSearch(PointConfigStandardQueryDto dto) {
        return backRestTemplate.post("/pointControlConfig/none", dto, baseServer);
    }


}
