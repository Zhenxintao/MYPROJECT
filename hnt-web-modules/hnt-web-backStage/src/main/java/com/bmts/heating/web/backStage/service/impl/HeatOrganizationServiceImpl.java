package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatOrganizationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatOrganizationService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeatOrganizationServiceImpl extends SavantServices implements HeatOrganizationService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatOrganization info) {
        return backRestTemplate.post("/heatOrganizationService", info, baseServer);
    }


    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/heatOrganizationService/" + id, baseServer);
    }

    @Override
    public Response deleteBatch(List<Integer> ids) {
        return backRestTemplate.delete("/heatOrganizationService/batch", ids, baseServer);
    }

    @Override
    public Response update(HeatOrganization info) {
        return backRestTemplate.put("/heatOrganizationService", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/heatOrganizationService/" + id, baseServer);
    }

    @Override
    public Response page(HeatOrganizationDto dto) {
        return backRestTemplate.post("/heatOrganizationService/page", dto, baseServer);
    }


    @Override
    public Response queryAll(Integer userId) {
        return backRestTemplate.get("/heatOrganizationService/all/" + userId, baseServer);
    }

    @Override
    public Response queryTree2(QueryTreeDto dto) {
        return backRestTemplate.post("/heatOrganizationService/queryTree2",dto,baseServer);
    }

    @Override
    public Response queryTree(QueryTreeDto dto) {
        return backRestTemplate.post("/heatOrganizationService/queryTree",dto,baseServer);
    }

}
