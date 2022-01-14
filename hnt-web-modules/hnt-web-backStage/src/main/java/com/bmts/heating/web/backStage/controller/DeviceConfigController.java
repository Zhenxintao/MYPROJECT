package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.DeviceConfig;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DeviceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "设备配置管理")
@RestController
@RequestMapping("/deviceConfig")
public class DeviceConfigController {

    @Autowired
    private DeviceConfigService deviceConfigService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody DeviceConfig info) {
        return deviceConfigService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return deviceConfigService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody DeviceConfig info) {
        return deviceConfigService.update(info);
    }

    @ApiOperation(value = "详情",response = DeviceConfig.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return deviceConfigService.detail(id);
    }

    @ApiOperation(value = "查询",response = CommonTree[].class)
    @GetMapping("/query")
    public Response query() {
        return deviceConfigService.query();
    }

}
