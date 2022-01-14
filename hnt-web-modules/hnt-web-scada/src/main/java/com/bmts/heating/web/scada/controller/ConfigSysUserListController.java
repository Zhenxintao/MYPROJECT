package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.entiy.baseInfo.request.ConfigSysUserListDto;
import com.bmts.heating.commons.entiy.baseInfo.response.ConfigSysUserListResponse;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.ConfigSysUserListService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "自定义列表管理")
@Slf4j
@RestController
@RequestMapping("configSysUserList")
public class ConfigSysUserListController {

    @Autowired
    private ConfigSysUserListService configSysUserListService;

    @ApiOperation(value = "查询自定义列表", response = ConfigSysUserListResponse.class)
    @PostMapping("/query")
    public Response query(HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null && userId != 0) {
            return configSysUserListService.listConfigSysUser(userId);
        }
        log.info("用户信息userId---{}", userId);
        return Response.fail();
    }

    @ApiOperation("添加自定义列表")
    @PostMapping("/add")
    public Response insert(@RequestBody ConfigSysUserListDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null && userId != 0) {
            dto.setUserId(userId);
        } else {
            return Response.fail("没有查询到该用户信息！无法添加！");
        }
        return configSysUserListService.addConfig(dto);
    }

    @ApiOperation("编辑自定义列表")
    @PutMapping
    public Response update(@RequestBody ConfigSysUserListDto updateInfo, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId == null || userId == 0) {
            return Response.fail("还没登陆，没有操作权限！");
        }
        if (updateInfo.getId() == 0) {
            return Response.fail("该列表不存在！");
        }
        Response detail = configSysUserListService.getDetail(updateInfo.getId());
        if (detail.getData() == null) {
            return Response.fail("该列表不存在！");
        }
        Gson gson = new Gson();
        ConfigSysUserListResponse configSysUser = gson.fromJson(gson.toJson(detail.getData()), ConfigSysUserListResponse.class);
        if (userId != configSysUser.getUserId()) {
            return Response.fail("该列表没有操作权限！");
        }
        updateInfo.setUserId(userId);
        return configSysUserListService.updateConfig(updateInfo);
    }

    @ApiOperation("删除列表")
    @DeleteMapping
    public Response delete(@RequestParam int id, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId == null || userId == 0) {
            return Response.fail("还没登陆，没有操作权限！");
        }
        Response detail = configSysUserListService.getDetail(id);
        if (detail.getData() == null) {
            return Response.fail("该列表不存在！");
        }
        Gson gson = new Gson();
        ConfigSysUserListResponse configSysUser = gson.fromJson(gson.toJson(detail.getData()), ConfigSysUserListResponse.class);
        if (userId != configSysUser.getUserId()) {
            return Response.fail("该列表没有操作权限！");
        }
        return configSysUserListService.delConfig(id);
    }


    @ApiOperation(value = "详情", response = ConfigSysUserListResponse.class)
    @GetMapping
    public Response detail(@RequestParam int id, HttpServletRequest request) {
        return configSysUserListService.getDetail(id);
    }
}
