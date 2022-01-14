package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointStandardAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardAlarmResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointAlarmService;
import com.bmts.heating.web.backStage.service.point.PointStandardAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "通用点位报警配置")
@RestController
@RequestMapping("/pointStandardAlarm")
public class PointStandardAlarmController {
    @Autowired
    private PointStandardAlarmService pointStandardAlarmService;

    @ApiOperation(value = "批量配置",response = PointStandardAlarm.class)
    @PostMapping("/setPointStandardAlarm")
    public Response setPointStandardAlarm(@RequestBody List<PointStandardAlarm> dto)
    {
        return pointStandardAlarmService.setPointStandardAlarm(dto);
    }

    @ApiOperation("删除")
    @PostMapping("/removePointStandardAlarm")
    public  Response removePointStandardAlarm(@RequestBody  List<Integer> ids)
    {
        return pointStandardAlarmService.removePointStandardAlarm(ids);
    }

    @ApiOperation(value = "修改",response = PointStandardAlarm.class)
    @PutMapping("/updatePointStandardAlarm")
    public Response updatePointStandardAlarm(@RequestBody PointStandardAlarm dto)
    {
        return pointStandardAlarmService.updatePointStandardAlarm(dto);
    }

    @ApiOperation(value = "查询展示",response = PointStandardAlarmResponse.class)
    @PostMapping("/page")
    public Response showPointStandardAlarm(@RequestBody PointStandardAlarmDto dto)
    {
        return pointStandardAlarmService.showPointStandardAlarm(dto);
    }
    @ApiOperation(value = "未配置查询展示",response = PointStandardAlarmResponse.class)
    @PostMapping("/showPointStandardAlarmConfig")
    public Response showPointStandardAlarmConfig(@RequestBody PointStandardDto dto)
    {
        return pointStandardAlarmService.showPointStandardAlarmConfig(dto);
    }

}
