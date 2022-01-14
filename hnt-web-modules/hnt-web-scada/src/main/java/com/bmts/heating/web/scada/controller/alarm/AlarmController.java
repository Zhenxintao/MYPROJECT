package com.bmts.heating.web.scada.controller.alarm;

import com.bmts.heating.commons.basement.model.db.entity.AlarmHistory;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.AlarmConfirmAllDto;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.AlarmRealDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleAlarmDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.AlarmCountIndex;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "报警实时")
@RequestMapping("alarm")

public class AlarmController extends BaseController {
    @PostMapping
    @ApiOperation(value = "实时列表", response = AlarmReal.class)
    public Response page(@RequestBody AlarmRealDto dto, HttpServletRequest request) {
        dto.setUserId(JwtUtils.getUserId(request));

        return template.doHttp("/alarmReal", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @PutMapping("/confirm/{id}")
    @ApiOperation("确认")
    public Response confirm(@PathVariable Integer id, HttpServletRequest request) {
        AlarmRealDto dto = new AlarmRealDto();
        dto.setUserId(JwtUtils.getUserId(request));
        return template.doHttp("/alarmReal/" + id, dto, baseServer, Response.class, HttpMethod.PUT);
    }

    @PostMapping("/history")
    @ApiOperation(value = "历史列表", response = AlarmHistory.class)
    public Response historyPage(@RequestBody AlarmRealDto dto, HttpServletRequest request) {
        dto.setUserId(JwtUtils.getUserId(request));
        return template.doHttp("/alarmReal/queryHisAlarm", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @GetMapping("/his/{id}")
    @ApiOperation(value = "历史详情", response = AlarmHistory.class)
    public Response his(@PathVariable Integer id) {
        return template.doHttp("/alarmReal/his/" + id, null, baseServer, Response.class, HttpMethod.GET);
    }

    @GetMapping("/real/{id}")
    @ApiOperation(value = "报警详情", response = AlarmReal.class)
    public Response real(@PathVariable Integer id) {
        return template.doHttp("/alarmReal/real/" + id, null, baseServer, Response.class, HttpMethod.GET);
    }

    @GetMapping("/queryAlarmIndex")
    @ApiOperation(value = "查询报警首页数据信息", response = AlarmCountIndex.class)
    public Response queryAlarmIndex() {
        return template.doHttp("/alarmReal/queryAlarmIndexCount", null, baseServer, Response.class, HttpMethod.GET);
    }

    @PutMapping("/confirmAll")
    @ApiOperation("批量确认")
    public Response confirmAll(@RequestBody AlarmConfirmAllDto dto, HttpServletRequest request) {
        dto.setUserId(JwtUtils.getUserId(request));
        dto.setUserName(JwtUtils.getUserName(request));
        return template.doHttp("/alarmReal/confirmAll", dto, baseServer, Response.class, HttpMethod.POST);
    }

    @PostMapping("/singleStationAlarmCount")
    @ApiOperation("单站监测报警个数")
    public Response singleStationAlarmCount(@RequestBody SingleAlarmDto dto)
    {
        return  template.doHttp("/alarmReal/singleStationAlarmCount",dto,baseServer,Response.class, HttpMethod.POST);
    }

    @PostMapping("/alarmTimeZonesCount")
    @ApiOperation("报警时间区间个数")
    public Response alarmTimeZonesCount(@RequestBody SingleAlarmDto dto)
    {
        return  template.doHttp("/alarmReal/alarmTimeZonesCount",dto,baseServer,Response.class, HttpMethod.POST);
    }
}
