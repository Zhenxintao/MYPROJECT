package com.bmts.heating.web.backStage.service.point.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.request.PointStandardAddDto;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointStandardService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PointStandardServiceImpl extends SavantServices implements PointStandardService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insert(PointStandardAddDto info) {
        return backRestTemplate.post("/pointStandard", info, baseServer);
    }

    @Override
    public Response delete(int id) {
        return backRestTemplate.delete("/pointStandard?id=" + id, baseServer);
    }

    @Override
    public Response update(PointStandardAddDto info) {
        return backRestTemplate.put("/pointStandard", info, baseServer);
    }

    @Override
    public Response detail(int id) {
        return backRestTemplate.get("/pointStandard?id=" + id, baseServer);
    }

    @Override
    public Response query(PointStandardDto dto) {
        return backRestTemplate.post("/pointStandard/query", dto, baseServer);
    }

    @Override
    public Response importExcel(MultipartFile file, PointStandardImportDto dto) {
        return backRestTemplate.file("/pointStandard/import", file, dto, baseServer);
    }

    @Override
    public Response list(PointStandardDto dto) {
        return backRestTemplate.post("/pointStandard/list", dto, baseServer);
    }

    @Override
    public Response pointStandardFullList() {
        return  backRestTemplate.get("/pointStandardFull/pointStandardFullList", baseServer);
    }

    @Override
    public Response pointStandardList() {
        return  backRestTemplate.get("/pointStandardFull/pointStandardList", baseServer);
    }

    @Override
    public Response insertPointStandard(PointStandardChangeDto pointStandardChangeDto) {
        return backRestTemplate.post("/pointStandardFull/insertPointStandard", pointStandardChangeDto, baseServer);
    }

    @Override
    public Response deletePointStandard(PointStandardChangeDto pointStandardChangeDto) {
        return backRestTemplate.post("/pointStandardFull/deletePointStandard", pointStandardChangeDto, baseServer);
    }


    @Override
    public Response updatePointStandardTd(int id) {
        return backRestTemplate.get("/pointStandard/updateById?id="+id,baseServer);
    }

    @Override
    public Response initialTdPointStandard(int level) {
        return backRestTemplate.get("/pointStandard/initialTd?level="+level,baseServer);
    }


}
