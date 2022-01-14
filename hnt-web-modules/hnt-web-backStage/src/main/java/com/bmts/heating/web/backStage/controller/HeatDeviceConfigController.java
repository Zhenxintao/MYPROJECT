package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatDeviceConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigDetail;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigListDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatDeviceConfigResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "关联设备配置管理")
@RestController
@RequestMapping("/heatDeviceConfig")
public class HeatDeviceConfigController {


    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatDeviceConfigListDto dto) {
        return backRestTemplate.doHttp("/heatDeviceConfig", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestBody List<Integer> ids) {
        return backRestTemplate.doHttp("/heatDeviceConfig", ids, baseServer, Response.class, HttpMethod.DELETE);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatDeviceConfig update) {
        return backRestTemplate.doHttp("/heatDeviceConfig", update, baseServer, Response.class, HttpMethod.PUT);
    }


    @ApiOperation(value = "查询列表", response = HeatDeviceConfigResponse.class)
    @PostMapping("/page")
    public Response page(@RequestBody HeatDeviceConfigDto dto) {
        return backRestTemplate.doHttp("/heatDeviceConfig/page", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation(value = "详情", response = HeatDeviceConfigResponse.class)
    @PostMapping("/detail")
    public Response detail(@RequestBody HeatDeviceConfigDetail detail) {
        return backRestTemplate.doHttp("/heatDeviceConfig/detail", detail, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation("查询热源")
    @PostMapping("/sourcePage")
    public Response sourcePage(@RequestBody BaseDto dto) {
        return backRestTemplate.doHttp("/heatDeviceConfig/sourcePage", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation("查询热力站")
    @PostMapping("/heatStationPage")
    public Response heatStationPage(@RequestBody BaseDto dto) {
        return backRestTemplate.doHttp("/heatDeviceConfig/heatStationPage", dto, baseServer, Response.class, HttpMethod.POST);
    }

}
