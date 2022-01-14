package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface HeatTransferStationService {

    Response insert(HeatTransferStation info);

    Response delete(int id);

    Response update(HeatTransferStation info);

    Response detail(Integer id);

    Response page(HeatTransferStationDto dto);

    Response getInfo(int id);

    Response freeze(FreezeDto dto);

    Response repeat(HeatTransferStation info);

	Response updateById(List<String> list);
}
