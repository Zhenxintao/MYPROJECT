package com.bmts.heating.web.backStage.service.webpoint;

import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointTypeSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.SelectPointConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface WebPointSearchService {
//    Response queryUnitType(@PathVariable String pointUnitName);
    Response queryType();
    Response selectPointConfig(@RequestBody SelectPointConfigDto selectPointConfigDto);
    Response searchPointConfigStatus(@PathVariable String pageKey);
    Response queryPointType(@RequestBody PointTypeSearchDto pointTypeSearchDto);
}
