package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.test.CompensationServiceTest;
import com.bmts.heating.web.forecast.service.test.FlowServiceTest;
import com.bmts.heating.web.forecast.service.test.StandardConditionServiceTest;
import com.bmts.heating.web.forecast.service.test.TemperatureServiceTest;
import com.bmts.heating.web.forecast.service.test.dto.CompensationDto;
import com.bmts.heating.web.forecast.service.test.dto.FlowDto;
import com.bmts.heating.web.forecast.service.test.dto.StandardConditionDto;
import com.bmts.heating.web.forecast.service.test.dto.TemperatureDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试计算接口")
@RestController
@RequestMapping("compute")
public class ComputeTestController {

    @Autowired
    private CompensationServiceTest compensationServiceTest;

    @Autowired
    private FlowServiceTest flowServiceTest;

    @Autowired
    private StandardConditionServiceTest standardConditionServiceTest;

    @Autowired
    private TemperatureServiceTest temperatureServiceTest;


//    @ApiOperation("汉字变量")
//    @PostMapping("/test")
//    @PassToken
//    public Response testHan(@RequestBody HanZiDto testDto) {
//        System.out.println(testDto.toString());
//        System.out.println(testDto.get天气预报的室外温度());
//        return Response.success();
//
//    }

    @ApiOperation("标准工况法计算")
    @PostMapping("/standard")
    public Response searchForecastData(@RequestBody StandardConditionDto testDto) {
        return standardConditionServiceTest.forecastTgTh(testDto);

    }

    @ApiOperation("温度法计算")
    @PostMapping("/temp")
    public Response tempData(@RequestBody TemperatureDto testDto) {
        return temperatureServiceTest.forecastTgTh(testDto);

    }

    @ApiOperation("流量法计算")
    @PostMapping("/flow")
    public Response flowData(@RequestBody FlowDto testDto) {
        return flowServiceTest.forecastTgTh(testDto);
    }

    @ApiOperation("补偿系数法计算")
    @PostMapping("/compensation")
    public Response compensationData(@RequestBody CompensationDto testDto) {
        return compensationServiceTest.forecastTgTh(testDto);
    }

}
