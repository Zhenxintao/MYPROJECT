package com.bmts.heating.web.auth.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.auth.utils.PermissTreeUtil;
import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import com.bmts.heating.commons.basement.model.db.entity.SysRole;
import com.bmts.heating.commons.basement.model.db.entity.SysRolePermission;
import com.bmts.heating.commons.db.mapper.auth.SysPermissionMapper;
import com.bmts.heating.commons.db.service.auth.SysPermissionService;
import com.bmts.heating.commons.db.service.auth.SysRolePermissionService;
import com.bmts.heating.commons.db.service.auth.SysRoleService;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.auth.base.dto.RolePermDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("permission")
@Api(tags = "权限管理")
public class PermissionController {
    @Autowired
    SysPermissionService sysPermissionService;
    @Autowired
    SysRolePermissionService sysRolePermissionService;
    @Autowired
    SysPermissionMapper sysPermissionMapper;
    @Autowired
    SysRoleService sysRoleService;

    @PostMapping("setRolePermissions")
    @ApiOperation("给角色分配权限")
    public Response setRolePermissions(@Valid @RequestBody RolePermDto rolePermDto) {
        SysRole sysRole = new SysRole();
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setRoleName(rolePermDto.getRoleName());
        if (!sysRoleService.save(sysRole))
            return Response.fail("保存失败");
        try {
            sysRolePermissionService.remove(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId,
                    rolePermDto.getRoleId()));
            List<SysRolePermission> sysRolePermissionList = new ArrayList<>();
            rolePermDto.getPerms().forEach(x -> {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setRoleId(rolePermDto.getRoleId());
                sysRolePermission.setPermissionId(x);
                sysRolePermissionList.add(sysRolePermission);
            });
            if (sysRolePermissionList.stream().count() > 0 && !sysRolePermissionService.saveBatch(sysRolePermissionList))
                return Response.fail();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
        return Response.success();
    }

    @GetMapping("locadPersByRole")
    @ApiOperation("加载角色权限信息")
    public Response locadPersByRole(@RequestParam("roleId") int id) {
        Response result = new Response();
//        result.setData(PermissTreeUtil.getTree(sysPermissionMapper.selectPermsByRole(id)));
        result.setData(sysPermissionMapper.selectPermsByRole(id).stream().map(SysPermission::getId).collect(Collectors.toList()));
        return result;
    }

    @PostMapping
    @ApiOperation("添加")
    public Response insert(@Valid @RequestBody SysPermission sysPermission) {
        Response response = new Response();
        SysPermission info = null;

        if (!sysPermission.getPid().equals(0)) {
            info = sysPermissionService.getById(sysPermission.getPid());
            sysPermission.setTreeLeave(info.getTreeLeave() + 1);
        }
        if (!sysPermissionService.save(sysPermission))
            return Response.fail();
        else {
            sysPermission.setTreePath(info == null ? "perm" : info.getTreePath().concat(":" + sysPermission.getId()));
            sysPermissionService.updateById(sysPermission);
        }

        return response;
    }

    @PutMapping
    @ApiOperation("修改")
    public Response upt(@Valid @RequestBody SysPermission sysPermission) {
        Response response = new Response();
        if (!sysPermission.getPid().equals(0)) {
            SysPermission info = sysPermissionService.getById(sysPermission.getPid());
            sysPermission.setTreeLeave(info.getTreeLeave() + 1);
        }
        if (!sysPermissionService.updateById(sysPermission))
            return Response.fail();
        return response;
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Response del(@Valid @RequestBody List<SysPermission> permissions) {
        Response response = new Response();
        List<Integer> ids = permissions.stream().map(SysPermission::getId).collect(Collectors.toList());
        if (!sysPermissionService.removeByIds(ids))
            return Response.fail();
        return response;
    }

    @PostMapping("query")
    @ApiOperation("查询")
    public Response select() {
        Response response = new Response();
        List<SysPermission> dbList =
                sysPermissionService.list();
//        response.setData(dbList);
        response.setData(PermissTreeUtil.getTree(dbList));
        return response;
    }

    @Autowired
    AuthrityService authrityService;

    @PostMapping("queryList")
    @ApiOperation("查询")
    public Response query(HttpServletRequest request) {
        Response response = new Response();
        int userId = JwtUtils.getUserId(request);
        if (userId > 0) {
            List<SysPermission> sysPermissions = authrityService.queryPerms(userId);
            return Response.success(sysPermissions);
        } else {
            List<SysPermission> dbList =
                    sysPermissionService.list();
            return Response.success(dbList);
        }
    }


}
