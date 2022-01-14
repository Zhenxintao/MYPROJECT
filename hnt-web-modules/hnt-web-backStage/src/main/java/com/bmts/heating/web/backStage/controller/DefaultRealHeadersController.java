package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.DefaultRealHeadersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "默认表头管理")
@RestController
@RequestMapping("/defaultRealHeaders")
public class DefaultRealHeadersController {

    @Autowired
    private DefaultRealHeadersService defaultRealHeadersService;


    @ApiOperation(value = "查询",response = DefaultRealHeadersDto[].class)
    @GetMapping("/query/{type}")
    public Response query(@PathVariable int type) {
        return defaultRealHeadersService.query(type);
    }

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public Response add(@RequestBody DefaultRealHeaders param) {
        return defaultRealHeadersService.add(param);
    }

    @ApiOperation(value = "批量-添加",response = DefaultRealHeaders[].class)
    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody List<DefaultRealHeaders> param) {
        return defaultRealHeadersService.addBatch(param);
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody DefaultRealHeaders param) {
        return defaultRealHeadersService.update(param);
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return defaultRealHeadersService.delete(id);
    }


}
