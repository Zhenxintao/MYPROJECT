package com.bmts.heating.web.forecast.controller;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.forecast.ForecastAreaHotIndexDto;
import com.bmts.heating.commons.entiy.forecast.InsertForecastConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastBasicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;


@Api(tags = "负荷预测基础数据信息")
@RestController
@RequestMapping("forecastBasic")
public class ForecastBasicController {
    @Autowired
    private ForecastBasicService forecastBasicService;

    //查询后台当前供暖季信息
    @ApiOperation("查询后台当前供暖季信息")
    @GetMapping("/queryCommonHeatSeason")
    public Response queryCommonHeatSeason() {
        return forecastBasicService.queryCommonHeatSeason();
    }

    @ApiOperation(value = "查询当前预测配置供暖季信息",response = ForecastSourceBasic.class)
    @GetMapping("/queryNowSourceBasic")
    public Response queryNowSourceBasic() {
        return forecastBasicService.queryNowSourceBasic();
    }

    //查询负荷预测供暖季基础数据信息
    @ApiOperation("查询负荷预测供暖季基础数据信息")
    @PostMapping("/queryForecastSourceBasic")
    public Response queryForecastSourceBasic(@RequestBody BaseDto dto) {
        return forecastBasicService.queryForecastSourceBasic(dto);
    }

    //新增负荷预测供暖季基础数据信息
    @ApiOperation("新增负荷预测供暖季基础数据信息")
    @PostMapping("/insertForecastSourceBasic")
    public Response insertForecastSourceBasic(@RequestBody ForecastSourceBasic dto) {
        return forecastBasicService.insertForecastSourceBasic(dto);
    }

    //修改负荷预测供暖季基础数据信息
    @ApiOperation("修改负荷预测供暖季基础数据信息")
    @PutMapping("/updForecastSourceBasic")
    public Response updForecastSourceBasic(@RequestBody ForecastSourceBasic dto) {
        return forecastBasicService.updForecastSourceBasic(dto);
    }

    //删除负荷预测供暖季基础数据信息
    @ApiOperation("删除负荷预测供暖季基础数据信息")
    @DeleteMapping("/removeForecastSourceBasic")
    public Response removeForecastSourceBasic(@RequestParam Integer id) {
        return forecastBasicService.removeForecastSourceBasic(id);
    }

    @ApiOperation(value = "新增负荷预测配置数据信息",response = InsertForecastConfigDto.class)
    @PostMapping("/insertForecastConfig")
    public Response insertForecastConfig(@RequestBody InsertForecastConfigDto dto) {
        return forecastBasicService.insertForecastConfig(dto);
    }

    @ApiOperation("查询负荷预测配置数据信息")
    @PostMapping("/queryForecastConfig")
    public Response queryForecastConfig(@RequestBody BaseDto dto) {
        return forecastBasicService.queryForecastConfig(dto);
    }

    @ApiOperation("修改负荷预测配置数据信息")
    @PutMapping("/updForecastConfig")
    public Response updForecastConfig(@RequestBody InsertForecastConfigDto dto) {
        return forecastBasicService.updForecastConfig(dto);
    }
    @ApiOperation("删除负荷预测配置数据信息")
    @DeleteMapping("/removeForecastConfig")
    public Response removeForecastConfig(@RequestParam  Integer id) {
        return forecastBasicService.removeForecastConfig(id);
    }

    @ApiOperation("修改供暖季详情数据信息")
    @PutMapping("/updForecastHeatSeason")
    public Response updForecastHeatSeason(@RequestBody ForecastSourceHeatSeason dto) {
        return forecastBasicService.updForecastHeatSeason(dto);
    }

    @ApiOperation("新增负荷预测供暖季详情信息")
    @PostMapping("/insertForecastSourceHeatSeason")
    public Response insertForecastSourceHeatSeason(@RequestBody ForecastSourceHeatSeason dto) {
        return forecastBasicService.insertForecastSourceHeatSeason(dto);
    }

    @ApiOperation("删除负荷预测供暖季详情信息")
    @DeleteMapping("/removeForecastSourceHeatSeason")
    public Response removeForecastSourceHeatSeason(@RequestParam  Integer id) {
        return forecastBasicService.removeForecastSourceHeatSeason(id);
    }

    @ApiOperation("调取面积热指标算法")
    @PostMapping("/forecastAreaIndex")
    public Response forecastAreaIndex(@RequestBody ForecastAreaHotIndexDto dto) {
       return forecastBasicService.forecastAreaIndex(dto);
    }

    @ApiOperation("调取一次网相对流量算法")
    @GetMapping("/forecastFirstNetFlow")
    public Response forecastFirstNetFlow(@RequestParam Integer id, @RequestParam float circulationValue,@RequestParam float flowValue)
    {
        return forecastBasicService.forecastFirstNetFlow(id,circulationValue,flowValue);
    }
}
