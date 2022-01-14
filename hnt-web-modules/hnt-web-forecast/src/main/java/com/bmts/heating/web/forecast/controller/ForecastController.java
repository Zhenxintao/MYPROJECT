package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.db.service.ForecastSourceCoreService;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.handler.ForeCastHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "负荷预测管理")
@RestController
@RequestMapping("forecast")
public class ForecastController {
    
    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;

    @Autowired
    private ForeCastHandler foreCastHandler;

    @ApiOperation("手动预测")
    @PostMapping("/handForecast")
    public Response searchForecastData(@RequestBody List<Integer> types) {
        return foreCastHandler.handForecast(types);

    }

}
