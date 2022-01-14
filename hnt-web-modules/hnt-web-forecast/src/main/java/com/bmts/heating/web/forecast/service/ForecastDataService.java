package com.bmts.heating.web.forecast.service;

import com.bmts.heating.commons.entiy.forecast.SearchDataDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface ForecastDataService {
    Response searchForecastData(SearchDataDto dto);
    Response searchForecastHistoryData(SearchDataDto dto);
    Response searchForecastSourceCoreList();
}
