package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.entiy.gathersearch.response.cache.HeatInformation;
import com.bmts.heating.commons.entiy.gathersearch.response.charts.BarChartData;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.AlarmRealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Api(tags = "首页信息")
@RequestMapping("index")
public class IndexController {

    @Autowired
    private AlarmRealService alarmRealService;

    @ApiOperation(value = "获取首页热源、站等基本信息", response = HeatInformation.class)
    @GetMapping("/heatInformation")
    public Response heatInformation() {
        HeatInformation heatInformation = new HeatInformation();
        return Response.success(heatInformation);
    }

    @ApiOperation(value = "获取实时换热站柱状图数据信息", response = BarChartData.class)
    @GetMapping("/realAlarmChart")
    public Response RealAlarmChart() {
        Response response = alarmRealService.realAlarmbarIndex();
        return Response.success(response);
    }
}
