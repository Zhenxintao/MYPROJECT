package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeasonDetail;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.CommonHeatSeasonDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CommonHeatSeasonService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonHeatSeasonServiceImpl extends SavantServices implements CommonHeatSeasonService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(CommonHeatSeason info) {
        return backRestTemplate.post("/commonHeatSeason", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/commonHeatSeason?id=" + id, baseServer);
    }

    @Override
    public Response update(CommonHeatSeason info) {
        return backRestTemplate.put("/commonHeatSeason", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/commonHeatSeason?id=" + id, baseServer);
    }

    @Override
    public Response query(CommonHeatSeasonDto dto) {
        return backRestTemplate.post("/commonHeatSeason/query", dto, baseServer);
    }

    @Override
    public Response queryDetail(int id) {
        return backRestTemplate.get("/commonHeatSeason/queryDetail?id=" + id, baseServer);
    }

    @Override
    public Response insertDetail(CommonHeatSeasonDetail com) {
        return backRestTemplate.post("/commonHeatSeason/insertDetail", com, baseServer);
    }

    @Override
    public Response updateDetail(CommonHeatSeasonDetail com) {
        return backRestTemplate.post("/commonHeatSeason/updateDetail", com, baseServer);
    }

    @Override
    public Response deleteDetail(int id) {
        return backRestTemplate.get("/commonHeatSeason/deleteDetail?id=" + id, baseServer);
    }

    @Override
    public Response deleteAllDetail(int id) {
        return backRestTemplate.get("/commonHeatSeason/deleteAllDetail?id=" + id, baseServer);
    }

    @Override
    public Response heatSeasonDayNumber() {
        return backRestTemplate.get("/commonHeatSeason/heatSeasonDayNumber", baseServer);
    }

    @Override
    public Response queryHeatSeasonDetailById(int id) {
        return backRestTemplate.get("/commonHeatSeason/queryHeatSeasonDetailById?id=" + id, baseServer);
    }


}
