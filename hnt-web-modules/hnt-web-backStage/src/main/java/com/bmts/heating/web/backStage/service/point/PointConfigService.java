package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PointConfigService {

    Response addBatch(PointConfigAddDto info);

    Response deleteBatch(List<PointConfig> param);

    Response delete(PointConfigDeleteDto param);

    Response page(PointConfigDto dto);

    Response update(PointConfig info);

    Response detail(Integer id);

    Response loadOtherPointStandardSearch(PointConfigStandardQueryDto dto);

    Response pageSource(PointConfigDto dto);

    Response insertPointConfig(List<PointConfig> dto);

    Response queryPointConfigExist(Integer id);
}
