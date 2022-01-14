package com.bmts.heating.web.backStage.service.impl.systemConfig;

import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.RepairOrderImageDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SystemConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CommonHeatSeasonService;
import com.bmts.heating.web.backStage.service.systemConfig.SystemConfigService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class SystemConfigServiceImpl extends SavantServices implements SystemConfigService {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response saveDataAlarmConfig(WebPageConfig dto) {
        return backRestTemplate.post("/systemConfig/saveDataAlarmConfig", dto, baseServer);
    }

    @Override
    public Response searchDataAlarmConfig(SystemConfigDto dto) {
        return backRestTemplate.post("/systemConfig/searchDataAlarmConfig", dto, baseServer);
    }

    @Override
    public Response searchDataStatus(Integer id) {
        return backRestTemplate.get("/systemConfig/searchDataStatus?id="+id, baseServer);
    }

    @Override
    public Response tableRunConfigStatus() {
        return backRestTemplate.get("/systemConfig/tableRunConfigStatus", baseServer);
    }

    @Override
    public Response uploadImage(MultipartFile image, RepairOrderImageDto dto) {
        return backRestTemplate.fileSystemLogo("/systemConfig/uploadImage",image, dto, baseServer);
    }

    @Override
    public Response queryImage() {
        return backRestTemplate.get("/systemConfig/queryImage", baseServer);
    }
}
