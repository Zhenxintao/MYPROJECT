package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeasonDetail;
import com.bmts.heating.commons.entiy.baseInfo.request.CommonHeatSeasonDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommonHeatSeasonService {

    Response insert(CommonHeatSeason info);

    Response delete(int id);

    Response update(CommonHeatSeason info);

    Response detail(int id);

    Response query(CommonHeatSeasonDto dto);

    Response queryDetail(int id);

    Response insertDetail(CommonHeatSeasonDetail com);

    Response updateDetail(CommonHeatSeasonDetail com);

    Response deleteDetail(int id);

    Response deleteAllDetail(int id);

    Response heatSeasonDayNumber();

    Response queryHeatSeasonDetailById(int id);
}
