package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointControlConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointControlConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointControlConfigAddDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface PointControlConfigService {

    Response addBatch(PointControlConfigAddDto info);

    Response deleteBatch(List<PointControlConfig> pointControlConfigs);

    Response delete( PointCollectDeleteDto param);

    Response page(PointControlConfigDto dto);

    Response update(PointControlConfig info);

    Response detail(Integer id);

    Response loadOtherPointStandardSearch( PointConfigStandardQueryDto dto);
}
