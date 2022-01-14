package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.AlarmConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "报警配置管理")
@RestController
@RequestMapping("/alarmConfig")
public class AlarmConfigController {

    @Autowired
    private AlarmConfigService alarmConfigService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody AlarmConfig info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return alarmConfigService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return alarmConfigService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody AlarmConfig info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return alarmConfigService.update(info);
    }

    @ApiOperation(value = "详情",response = AlarmConfig.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return alarmConfigService.detail(id);
    }

    @ApiOperation(value = "查询",response = AlarmConfig.class,responseContainer = "Page")
    @PostMapping("/query")
    public Response query(@RequestBody AlarmConfigDto dto) {
        return alarmConfigService.query(dto);
    }

}
