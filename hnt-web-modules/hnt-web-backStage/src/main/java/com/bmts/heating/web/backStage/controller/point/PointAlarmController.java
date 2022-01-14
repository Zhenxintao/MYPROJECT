package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmSetDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointAlarmResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointAlarmSetResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "换热站及热源点位报警配置")
@RestController
@RequestMapping("/pointAlarm")
public class PointAlarmController {
    @Autowired
    private PointAlarmService pointAlarmService;

    @ApiOperation(value = "批量配置",response = PointAlarm.class)
    @PostMapping("/setPointAlarm")
    public Response setPointAlarm(@RequestBody List<PointAlarm> dto)
    {
        return pointAlarmService.setPointAlarm(dto);
    }

    @ApiOperation("删除")
    @PostMapping("/removePointAlarm")
    public  Response removePointAlarm(@RequestBody  List<Integer> ids)
    {
        return pointAlarmService.removePointAlarm(ids);
    }

    @ApiOperation(value ="修改",response = PointAlarm.class)
    @PutMapping("/updatePointAlarm")
    public Response updatePointAlarm(@RequestBody PointAlarm dto)
    {
        return pointAlarmService.updatePointAlarm(dto);
    }

    @ApiOperation(value = "查询展示",response = PointAlarmResponse.class)
    @PostMapping("/page")
    public Response showPointAlarm(@RequestBody PointAlarmSetDto dto)
    {
        return pointAlarmService.showPointAlarm(dto);
    }

    @ApiOperation(value = "未配置查询展示",response = PointAlarmSetResponse.class)
    @PostMapping("/showPointAlarmConfig")
    public Response showPointAlarmConfig(@RequestBody PointAlarmSetDto dto)
    {
        return pointAlarmService.showPointAlarmConfig(dto);
    }
}
