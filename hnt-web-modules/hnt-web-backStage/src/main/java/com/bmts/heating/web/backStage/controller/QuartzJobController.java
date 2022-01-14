package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.QuartzJob;
import com.bmts.heating.commons.entiy.quartzjob.request.QueryJobDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "定时任务信息")
@RestController
@RequestMapping("quartzJob")
public class QuartzJobController {
    @Autowired
    private QuartzJobService quartzJobService;

    @ApiOperation("新增定时任务")
    @PostMapping("/insertJob")
    public Response insertJob(@RequestBody QuartzJob dto) {
        return quartzJobService.insertJob(dto);
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/removeJob")
    public Response removeJob(@RequestParam Integer id) {
        return quartzJobService.removeJob(id);
    }

    @ApiOperation("修改定时任务")
    @PutMapping("/updJob")
    public Response updJob(@RequestBody QuartzJob dto) {
        return quartzJobService.updJob(dto);
    }

    @ApiOperation("查询定时任务")
    @PostMapping("/queryJob")
    public Response queryJob(@RequestBody QueryJobDto dto) {
        return quartzJobService.queryJob(dto);
    }

}
