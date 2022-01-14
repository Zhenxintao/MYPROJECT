package com.bmts.heating.web.backStage.service.systemConfig;


import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.RepairOrderImageDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SystemConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface SystemConfigService {
    Response saveDataAlarmConfig(WebPageConfig dto);
    Response searchDataAlarmConfig(SystemConfigDto dto);
    Response searchDataStatus(Integer id);
    Response tableRunConfigStatus();
    Response uploadImage(MultipartFile image, RepairOrderImageDto dto);
    Response queryImage();
}
