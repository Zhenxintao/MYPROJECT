package com.bmts.heating.web.energy.controller;

import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.commons.entiy.baseInfo.response.EnergyEvaluateResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.EnergyChartResponse;
import com.bmts.heating.web.energy.pojo.EnergyInfoResponse;
import com.bmts.heating.web.energy.service.EnergyEvaluateService;
import com.bmts.heating.web.energy.service.EnergyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "能耗明细")
@RestController
@RequestMapping("energy/info")
public class EnergyInfoController {

    @Autowired
    private EnergyInfoService energyInfoService;
    @Autowired
    EnergyEvaluateService energyEvaluateService;

    @ApiOperation(value = "表格", response = EnergyInfoResponse.class)
    @PostMapping(value = "/page")
    public Response page(@RequestBody EnergyInfoDto dto) {
        return energyInfoService.page(dto);
    }

    @ApiOperation(value = "评价雷达图",response = EnergyEvaluateResponse.class)
    @PostMapping("evaluate/radar")
    public Response evaluateRadar(@RequestBody EnergyInfoDto dto) {
        return energyEvaluateService.evaluateStatistics(dto);
    }

    @ApiOperation(value = "能耗明细",response = EnergyChartResponse.class)
    @PostMapping("energy/info")
    public Response energyInfo(@RequestBody EnergyInfoDto dto) {
        return energyInfoService.energyInfo(dto);
    }

    @ApiOperation(value = "能耗对比",response = EnergyChartResponse.class)
    @PostMapping("energy/converge")
    public Response energyConverge(@RequestBody EnergyInfoDto dto) {
        return energyInfoService.energyCompared(dto);
    }

}
