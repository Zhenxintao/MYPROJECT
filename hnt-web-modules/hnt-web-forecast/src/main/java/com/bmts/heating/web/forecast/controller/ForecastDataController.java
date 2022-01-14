package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.entiy.forecast.SearchDataDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "负荷预测数据展示")
@RestController
@RequestMapping("forecastData")
public class ForecastDataController {
    @Autowired
    private ForecastDataService forecastDataService;

    @ApiOperation("查询负荷预测数据信息")
    @PostMapping("/searchForecastData")
    public Response searchForecastData(@RequestBody SearchDataDto dto) {
        if (dto.getDataType() == 1) {
            return forecastDataService.searchForecastData(dto);
        }
        return forecastDataService.searchForecastHistoryData(dto);
    }

    @ApiOperation("查询负荷预测全部配置信息")
    @GetMapping("/searchForecastSourceCoreList")
    public Response searchForecastSourceCoreList() {
        return forecastDataService.searchForecastSourceCoreList();
    }
}
