package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.request.PointStandardAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.multipart.MultipartFile;

public interface PointStandardService {

    Response insert(PointStandardAddDto info);

    Response delete(int id);

    Response update(PointStandardAddDto info);

    Response detail(int id);

    Response query(PointStandardDto dto);

    Response importExcel(MultipartFile file, PointStandardImportDto dto);

    Response list(PointStandardDto dto);
    Response pointStandardFullList();
    Response pointStandardList();
    Response insertPointStandard(PointStandardChangeDto pointStandardChangeDto);
    Response deletePointStandard(PointStandardChangeDto pointStandardChangeDto);

	Response updatePointStandardTd(int id);

    Response initialTdPointStandard(int level);
}
