package com.bmts.heating.web.auth.controller;

import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单")
public class MenuController {
    @Autowired
    AuthrityService authrityService;

    @GetMapping(path = "/menus")
    public Response queryMenuBySystem(int id, HttpServletRequest request) {
        List<SysPermission> sysPermissions = authrityService.queryPerms(JwtUtils.getUserId(request));
        SysPermission sysPermission = sysPermissions.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (sysPermission == null) return Response.success();
        List<SysPermission> collect = sysPermissions.stream().filter(x ->
                x.getTreePath().contains(sysPermission.getTreePath())
        ).collect(Collectors.toList());
        return Response.success(collect);
    }
    @GetMapping(path = "/systems")
    public Response queryRoot(HttpServletRequest request) {
        List<SysPermission> sysPermissions = authrityService.queryPerms(JwtUtils.getUserId(request));
//        List<SysPermission> collect = sysPermissions.stream().filter(x->x.getTreeLeave()==0).collect(Collectors.toList());
        return Response.success(sysPermissions);
    }
}
