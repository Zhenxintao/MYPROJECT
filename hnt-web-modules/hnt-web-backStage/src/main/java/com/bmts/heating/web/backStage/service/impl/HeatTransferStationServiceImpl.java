package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatTransferStationService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeatTransferStationServiceImpl extends SavantServices implements HeatTransferStationService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(HeatTransferStation info) {
        return backRestTemplate.post("/heatTransferStation", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/heatTransferStation?id=" + id, baseServer);
    }

    @Override
    public Response update(HeatTransferStation info) {
        return backRestTemplate.put("/heatTransferStation", info, baseServer);
    }

    @Override
    public Response detail(Integer id) {
        return backRestTemplate.get("/heatTransferStation/" + id, baseServer);
    }

    @Override
    public Response page(HeatTransferStationDto dto) {
        return backRestTemplate.post("/heatTransferStation/page", dto, baseServer);
    }

    @Override
    public Response getInfo(int id) {
        return backRestTemplate.get("/heatTransferStation?id=" + id, baseServer);
    }

    @Override
    public Response freeze(FreezeDto dto) {
        return backRestTemplate.post("/heatTransferStation/freeze", dto, baseServer);
    }

    @Override
    public Response repeat(HeatTransferStation info) {
        return backRestTemplate.post("/heatTransferStation/repeat", info, baseServer);
    }

    @Override
    public Response updateById(List<String> list) {
        return backRestTemplate.put("/heatTransferStation/sort",list,baseServer);
    }


}
