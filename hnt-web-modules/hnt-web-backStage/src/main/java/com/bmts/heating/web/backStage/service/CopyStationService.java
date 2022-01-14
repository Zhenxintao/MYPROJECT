package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyNewDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface CopyStationService {

    Response toExist(CopyDto info);

    Response querySource(int heatStationId);

    Response copyNew(CopyNewDto dto);


}
