package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatNetSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "热网、热源关系管理")
@RestController
@RequestMapping("/heatNetSource")
public class HeatNetSourceController {
    @Autowired
    private HeatNetSourceService heatNetSourceService;

    @ApiOperation("新增网源关系信息")
    @PostMapping("insertNetSource")
    public Response insertNetSource(@RequestBody List<HeatNetSource> info) {
        return heatNetSourceService.insertNetSource(info);
    }

    @ApiOperation("热网基础信息删除后，删除该网关系信息")
    @DeleteMapping("deleteNetById")
    public Response deleteNetById(@RequestParam int id) {
       return heatNetSourceService.deleteNetById(id);
    }

    @ApiOperation("热源基础信息删除后，删除该源关系信息")
    @DeleteMapping("deleteSourceById")
    public Response deleteSourceById(@RequestParam int id) {
        return heatNetSourceService.deleteSourceById(id);
    }

    @ApiOperation("根据Id删除网源关系信息")
    @DeleteMapping("deleteNetSourceById")
    public Response deleteNetSourceById(@RequestParam int id) {
        return heatNetSourceService.deleteNetSourceById(id);
    }

    @ApiOperation("删除网源关系信息")
    @PostMapping("deleteNetSourceInfo")
    public Response deleteNetSourceInfo(@RequestBody HeatNetSource dto) {
        return heatNetSourceService.deleteNetSource(dto);
    }

    @ApiOperation("网源信息查询")
    @GetMapping("queryNetSource")
    public Response queryNetSource() {
       return heatNetSourceService.queryNetSource();
    }

    @ApiOperation("修改网源关系信息")
    @PutMapping("updNetSource")
    public Response updNetSource(@RequestBody HeatNetSource info) {
        return heatNetSourceService.updNetSource(info);
    }
}
