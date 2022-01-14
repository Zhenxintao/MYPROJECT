package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.utils.restful.Response;

public interface HeatCabinetService {

    Response insert(HeatCabinet info);

    Response delete(int id);

    Response update(HeatCabinet info);

    Response detail(int id);

    Response query();

    Response queryByStationId(int id);

    Response queryByMap(int id);

    Response queryBySourceId(int id);

    Response SourceCs(int id);

}
