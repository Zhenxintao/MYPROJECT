package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyHeatSourceDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface CopySourceService {

    Response toExist(CopyDto info);

    Response copyNew(CopyHeatSourceDto dto);

    Response querySource(int sourceId);


}
