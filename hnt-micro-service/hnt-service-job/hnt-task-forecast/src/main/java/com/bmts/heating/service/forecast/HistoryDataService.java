package com.bmts.heating.service.forecast;

import com.bmts.heating.commons.entiy.forecast.history.ForecastRequest;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: HistoryDataService
 * @Description: 获取历史数据
 * @Author: pxf
 * @Date: 2021/4/28
 * @Version: 1.0
 */
@Service
public class HistoryDataService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String gatherSearch = "gather_search";

    public Response getSource(ForecastRequest forecastRequest) {
        return tsccRestTemplate.post("/forecast/baseSource", forecastRequest, gatherSearch, Response.class);
    }

}
