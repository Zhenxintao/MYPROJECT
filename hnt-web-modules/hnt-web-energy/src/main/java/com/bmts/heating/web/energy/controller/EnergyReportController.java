package com.bmts.heating.web.energy.controller;

import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.energyByTd.EnergySystemReportFormsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "能耗报表")
@RestController
@RequestMapping("energy/repo")
public class EnergyReportController {

    @Autowired
    private EnergySystemReportFormsService energySystemReportFormsService;

    @ApiOperation(value = "历史能耗数据查询", response = HistoryEnergyDataResponse.class)
    @PostMapping(value = "/page")
    public Response page(@RequestBody QueryBaseHistoryDto dto) {
        return energySystemReportFormsService.page(dto);
    }
//默认传参示例
//    {
//        "aggregateDataType": "single",
//            "aggregateTimeType": "day",
//            "currentPage": 1,
//            "endTime": 1634744669000,
//            "heatType": "station",
//            "historyType": "energy_hour",
//            "relevanceIds": [],
//        "size": 200,
//            "sortType": true,
//            "startTime": 1633881600000
//    }
//    单个站传参示例
//{
//    "aggregateDataType": "single",
//        "aggregateTimeType": "day",
//        "currentPage": 1,
//        "endTime": 1634744669000,
//        "heatType": "station",
//        "historyType": "energy_hour",
//        "relevanceIds": [2284],
//    "size": 200,
//        "sortType": true,
//        "startTime": 1633881600000
//}
    @ApiOperation(value = "汇聚能耗查询", response = HistoryEnergyDataResponse.class)
    @PostMapping("/converge")
    public Response converge(@RequestBody QueryAggregateHistoryDto dto) {
        return energySystemReportFormsService.converge(dto);
    }

}
