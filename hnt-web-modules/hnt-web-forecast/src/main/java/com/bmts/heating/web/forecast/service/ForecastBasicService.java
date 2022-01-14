package com.bmts.heating.web.forecast.service;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.forecast.ForecastAreaHotIndexDto;
import com.bmts.heating.commons.entiy.forecast.InsertForecastConfigDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface ForecastBasicService {
     Response queryCommonHeatSeason();
     Response queryNowSourceBasic();
     Response queryForecastSourceBasic(BaseDto dto);
     Response insertForecastSourceBasic(ForecastSourceBasic dto);
     Response updForecastSourceBasic(ForecastSourceBasic dto);
     Response removeForecastSourceBasic(Integer id);
     Response insertForecastConfig(InsertForecastConfigDto dto);
     Response queryForecastConfig(BaseDto dto);
     Response updForecastConfig(InsertForecastConfigDto dto);
     Response removeForecastConfig(Integer id);
     Response forecastdetailconfig(InsertForecastConfigDto dto,Integer type);
     Response updForecastHeatSeason(ForecastSourceHeatSeason dto);
     Response insertForecastSourceHeatSeason(ForecastSourceHeatSeason dto);
     Response removeForecastSourceHeatSeason(Integer id);
     Response forecastAreaIndex(ForecastAreaHotIndexDto dto);
     Response forecastFirstNetFlow(Integer id, float circulationValue,float flowValue);
}
