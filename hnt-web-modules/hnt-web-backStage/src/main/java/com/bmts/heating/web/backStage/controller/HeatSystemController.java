package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSystemDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatSystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "系统管理")
@RestController
@RequestMapping("/heatSystem")
@Slf4j
public class HeatSystemController {

    @Autowired
    private HeatSystemService heatSystemService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatSystem info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatSystemService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return heatSystemService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatSystem info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatSystemService.update(info);
    }

    @ApiOperation(value = "详情",response = HeatSystem.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return heatSystemService.detail(id);
    }

    @ApiOperation(value = "查询(所属机柜下的所有系统)",response = HeatSystem[].class)
    @PostMapping("/query")
    public Response query(@RequestBody HeatSystemDto dto) {
        return heatSystemService.query(dto);
    }

    @ApiOperation(value = "查询网、源、站、控制柜、系统关联的基本信息)",response = HeatSystem[].class)
    @PostMapping("/querySystem")
    public Response querySystem(@RequestBody HeatSystemDto dto) {
        return heatSystemService.querySystem(dto);
    }
}
