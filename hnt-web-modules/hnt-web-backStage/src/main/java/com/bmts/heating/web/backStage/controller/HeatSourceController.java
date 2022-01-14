package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "热源管理")
@RestController
@RequestMapping("/heatSource")
public class HeatSourceController {

    @Autowired
    private HeatSourceService heatSourceService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatSource info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatSourceService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return heatSourceService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatSource info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatSourceService.update(info);
    }

    @ApiOperation(value = "详情",response = HeatSourceResponse.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return heatSourceService.detail(id);
    }

    @ApiOperation(value = "查询",response = HeatSourceResponse[].class)
    @PostMapping("/query")
    public Response query(@RequestBody HeatSourceDto dto) {
        return heatSourceService.query(dto);
    }

}
