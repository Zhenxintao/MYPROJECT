package com.bmts.heating.web.scada.controller.alarm;

import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.BatchAlarmDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.AlarmConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "报警配置")
@RestController
@RequestMapping("alarmConfig")
public class AlarmConfigController {

    @Autowired
    private AlarmConfigService alarmConfigService;

    @ApiOperation(value = "分页查询", response = PointConfigResponse.class)
    @PostMapping("/page")
    public Response page(@RequestBody PointCollectConfigDto dto) {
        return alarmConfigService.page(dto);
    }

    @ApiOperation("修改报警阈值")
    @PutMapping()
    public Response update(@RequestBody PointCollectConfig entity) {
        return alarmConfigService.update(entity);
    }

    @ApiOperation("是否报警")
    @PutMapping("/{id}/{state}")
    public Response isAlarm(@PathVariable Integer id, @PathVariable Boolean state) {
        return alarmConfigService.isAlarm(id, state);
    }

    @ApiOperation(value = "详情", response = PointConfigResponse.class)
    @GetMapping("/{id}")
    public Response info(@PathVariable Integer id) {
        return alarmConfigService.info(id);
    }

    @ApiOperation("批量启停报警")
    @PostMapping("/batch/alarm")
    public Response batchIsAlarm(@RequestBody BatchAlarmDto dto) {
        return alarmConfigService.batchIsAlarm(dto);
    }


}
