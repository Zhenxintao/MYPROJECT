package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EsDocConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: EsDocConfigJoggle
 * @Description: ES 配置信息
 * @Author: pxf
 * @Date: 2021/1/15 15:13
 * @Version: 1.0
 */

@Api(tags = "ES配置信息")
@RestController
@RequestMapping("/esdoc")
@Slf4j
public class EsDocConfigController {

    @Autowired
    private EsDocConfigService esDocConfigService;

    @ApiOperation(value = "分页查询",response = EsDocConfigResponse[].class)
    @PostMapping("/page")
    public Response query(@RequestBody BaseDto dto) {
        return esDocConfigService.page(dto);
    }

    @ApiOperation(value = "详情",response = EsDocConfig.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return esDocConfigService.detail(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody EsDocConfig info) {
        return esDocConfigService.update(info);
    }

}
