package com.bmts.heating.web.auth.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStation;
import com.bmts.heating.commons.db.mapper.HeatOrganizationMapper;
import com.bmts.heating.commons.db.service.auth.*;
import com.bmts.heating.commons.auth.entity.DataPerm;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.auth.base.dto.RoleDto;
import com.bmts.heating.web.auth.base.dto.RolePermissionsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthScrollBarUI;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRolePermissionService sysRolePermissionService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysDataPermService sysDataPermService;
    @Autowired
    SysDataRelevanceService sysDataRelevanceService;

    @PostMapping("insert")
    @ApiOperation("添加")
    public Response insert(@Valid @RequestBody RolePermissionsDto rolePermissionsDto, HttpServletRequest request) {
        Response response = new Response();
        SysRole sysRole = new SysRole();
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setRoleCode(rolePermissionsDto.getRoleCode());
        sysRole.setRoleDescription(rolePermissionsDto.getRoleDes());
        sysRole.setRoleName(rolePermissionsDto.getRoleName());
        sysRole.setCreateUser(JwtUtils.getUserId(request));
        if (!sysRoleService.save(sysRole))
            return Response.fail();
        if (!setPerms(rolePermissionsDto, sysRole.getId()))
            return Response.fail();
        if (!setDataPerm(rolePermissionsDto, sysRole.getId(), true))
            return Response.fail();
        if (!setSysDataRelevance(rolePermissionsDto,sysRole.getId()))
            return Response.fail();
        return response;
    }

    private boolean setDataPerm(RolePermissionsDto rolePermissionsDto, int roleId, boolean add) {
        if (CollectionUtils.isNotEmpty(rolePermissionsDto.getOrgs())) {
            for (DataPerm org : rolePermissionsDto.getOrgs()) {
                org.setNodeType(org.getId().contains("org") ? "org" : "station");
            }
            String jsonValue = JSONObject.toJSONString(rolePermissionsDto.getOrgs());
            SysDataPerm sysDataPerm = new SysDataPerm();

            if (!add) {
                if (sysDataPermService.getOne(Wrappers.<SysDataPerm>lambdaQuery().eq(SysDataPerm::getRoleId, roleId)) != null) {
                    UpdateWrapper<SysDataPerm> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("roleId", roleId);
                    updateWrapper.set("checked", jsonValue);
                    return sysDataPermService.update(updateWrapper);
                }
            }
            sysDataPerm.setChecked(jsonValue);
            sysDataPerm.setRoleId(roleId);
            return sysDataPermService.save(sysDataPerm);

        }
        return true;
    }

    private boolean setPerms(RolePermissionsDto rolePermissionsDto, Integer roleId) {
        if (rolePermissionsDto.getPerms().size() > 0) {
            try {
                sysRolePermissionService.remove(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId,
                        roleId));
                List<SysRolePermission> sysRolePermissionList = new ArrayList<>();
                rolePermissionsDto.getPerms().forEach(x -> {
                    SysRolePermission sysRolePermission = new SysRolePermission();
                    sysRolePermission.setRoleId(roleId);
                    sysRolePermission.setPermissionId(x);
                    sysRolePermissionList.add(sysRolePermission);
                });
                if (!sysRolePermissionService.saveBatch(sysRolePermissionList))
                    return false;
            } catch (Exception e) {
                log.error("set perms cause execption {}", e);
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * 配置角色热源权限
     * */
    private Boolean setSysDataRelevance(RolePermissionsDto rolePermissionsDto, Integer roleId) {
        if (rolePermissionsDto.getRelevanceIds().size() > 0) {
            try {
                sysDataRelevanceService.remove(Wrappers.<SysDataRelevance>lambdaQuery().eq(SysDataRelevance::getRoleId, roleId).eq(SysDataRelevance::getLevel, TreeLevel.HeatSource.level()));
                SysDataRelevance sysDataRelevance = new SysDataRelevance();
                sysDataRelevance.setRelevanceIds(rolePermissionsDto.getRelevanceIds().toString());
                sysDataRelevance.setLevel(TreeLevel.HeatSource.level());
                sysDataRelevance.setRoleId(roleId);
                return sysDataRelevanceService.save(sysDataRelevance);
            } catch (Exception e) {
                log.error("角色配置热源权限失败 {}", e.getMessage());
                return false;
            }
        }
        return true;
    }

    @PostMapping("update")
    @ApiOperation("修改")
    public Response upt(@Valid @RequestBody RolePermissionsDto rolePermissionsDto) {
        Response response = new Response();
        SysRole sysRole = sysRoleService.getById(rolePermissionsDto.getId());
        if (sysRole == null)
            return Response.fail();
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setRoleCode(rolePermissionsDto.getRoleCode());
        sysRole.setRoleDescription(rolePermissionsDto.getRoleDes());
        sysRole.setRoleName(rolePermissionsDto.getRoleName());
        if (!sysRoleService.updateById(sysRole))
            return Response.fail();
        if (!setPerms(rolePermissionsDto, sysRole.getId()))
            return Response.fail();
        if (!setDataPerm(rolePermissionsDto, sysRole.getId(), false))
            return Response.fail();
        if (!setSysDataRelevance(rolePermissionsDto,sysRole.getId()))
            return Response.fail();
        return response;
    }

    @GetMapping("delete")
    @ApiOperation("删除")
    public Response del(@RequestParam("id") int id) {
        Response response = new Response();
        if (!sysRoleService.removeById(id))
            return Response.fail();
        sysRolePermissionService.remove(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, id));
        sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, id));
        return response;
    }

    @PostMapping("query")
    @ApiOperation("查询角色")
    public Response queryRole(@Valid @RequestBody RoleDto roleDto, HttpServletRequest request) {
        Response response = new Response();
        IPage<SysRole> page = new Page<>(roleDto.getCurrentPage(), roleDto.getPagesize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        int userId = JwtUtils.getUserId(request);
        if (userId > 0)
            queryWrapper.eq("create_user", userId);
        if (StringUtils.isNotBlank(roleDto.getName())) {
            queryWrapper.like("role_name", roleDto.getName());
        }
        response.setData(sysRoleService.page(page, queryWrapper));
        return response;
    }

    @PostMapping("queryAll")
    @ApiOperation("加载所有角色")
    public Response queryAll(HttpServletRequest request) {
        int userId = JwtUtils.getUserId(request);
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (userId > 0)
            queryWrapper.eq("create_user", userId);
        Response response = new Response();
        response.setData(sysRoleService.list(queryWrapper));
        return response;
    }


    @Autowired
    HeatOrganizationMapper heatOrganizationMapper;
    @Autowired
    SysUserOrganizationService sysUserOrganizationService;

    @PostMapping("queryDataPermsTree")
    @ApiOperation("查询树形结构")
    public Response queryTree(HttpServletRequest request) {
        int userId = JwtUtils.getUserId(request);
        Response response = new Response();

        List<OrgAndStation> orgAndStations = heatOrganizationMapper.queryOrgAndStation(new QueryWrapper());
        Map<Integer, List<OrgAndStation>> collect;
        List<CommonTree> commonTrees = new ArrayList<>();

        if (userId <= 0) collect = orgAndStations.stream().collect(Collectors.groupingBy(x -> x.getId()));
        else {
            //非超级管理员 过滤数据
            List<Integer> orgs = sysUserOrganizationService.list(Wrappers.<SysUserOrganization>lambdaQuery().eq(SysUserOrganization::getUserId, userId))
                    .stream().map(x -> x.getHeatOrganizationId()).distinct()
                    .collect(Collectors.toList());
            List<String> codes = orgAndStations.stream().filter(x -> orgs.contains(x.getId())).map(x -> x.getCode()).collect(Collectors.toList());
            List<OrgAndStation> result = orgAndStations.stream().filter(x -> codes.stream().anyMatch(code -> x.getCode().contains(code))).distinct().collect(Collectors.toList());

            collect = result.stream().collect(Collectors.groupingBy(x -> x.getId()));
        }
        collect.forEach((k, v) -> {
            OrgAndStation orgAndStation = v.get(0);
            commonTrees.add(buildCommonTree(k, orgAndStation.getLevel(), orgAndStation.getName(), "org_" + orgAndStation.getPid().toString(), "org"));
            v.forEach(x -> {
                if (orgAndStation.getStationId() != 0) {
                    commonTrees.add(
                            buildCommonTree(x.getStationId(), -1, x.getStationName(), "org_" + k.toString(), "station")
                    );
                }
            });
        });
        response.setData(commonTrees);
        return response;
    }

    @GetMapping("queryDataPerms")
    @ApiOperation("查询选中项")
    public Response queryCheckedTree(int roleId) {
        Response response = new Response();
        SysDataPerm one = sysDataPermService.getOne(Wrappers.<SysDataPerm>lambdaQuery().eq(SysDataPerm::getRoleId, roleId));
        if (one != null && StringUtils.isNotBlank(one.getChecked())) {
            List<DataPerm> dataPerms = JSONObject.parseArray(one.getChecked(), DataPerm.class);
            List<String> collect = dataPerms.stream().map(x -> x.getId()).collect(Collectors.toList());
            response.setData(collect);
        }
        return response;
    }

    private CommonTree buildCommonTree(int id, int level, String name, String Pid, String prefix) {
        CommonTree build = CommonTree.builder()
                .id(prefix.concat("_") + id)
                .level(level)
                .name(name)
                .pid(Pid == null ? null : (Pid + ""))
                .build();
        return build;
    }
}
