package com.bmts.heating.web.backStage.service.point;

import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface PointCollectConfigService {

    Response addBatch(PointCollectConfigAddDto info);

    Response deleteBatch(List<PointCollectConfig> param);

    Response delete( PointCollectDeleteDto param);

    Response page(PointCollectConfigDto dto);

    Response update(PointCollectConfig info);

    Response detail(Integer id);

    Response loadOtherPointStandardSearch( PointConfigStandardQueryDto dto);

}
