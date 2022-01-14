package com.bmts.heating.web.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.auth.utils.PermissTreeUtil;
import com.bmts.heating.commons.basement.model.db.entity.SysDataRelevance;
import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import com.bmts.heating.commons.basement.model.db.entity.SysRole;
import com.bmts.heating.commons.basement.model.db.entity.SysRolePermission;
import com.bmts.heating.commons.db.mapper.auth.SysPermissionMapper;
import com.bmts.heating.commons.db.service.auth.SysDataRelevanceService;
import com.bmts.heating.commons.db.service.auth.SysPermissionService;
import com.bmts.heating.commons.db.service.auth.SysRolePermissionService;
import com.bmts.heating.commons.db.service.auth.SysRoleService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("configRelevance")
@Api(tags = "权限管理(热源)")
public class ConfigRelevanceController {
    @Autowired
    SysDataRelevanceService sysDataRelevanceService;

    @PostMapping("setRoleRelevance")
    @ApiOperation("给角色分配权限热源权限")
    public Response setRoleRelevance(@RequestBody SysDataRelevance dto) {
        if (dto == null) {
            return Response.fail();
        }
//        if (dto.getRelevanceIds().isEmpty()) {
//            return Response.fail("未选中热源信息");
//        }
        dto.setLevel(TreeLevel.HeatSource.level());
        Boolean result = sysDataRelevanceService.saveOrUpdate(dto);
        return result ? Response.success() : Response.fail();
    }

    @GetMapping("queryRoleRelevanceInfo")
    @ApiOperation("查询角色配置热源权限信息")
    public Response queryRoleRelevanceInfo(@RequestParam Integer id) {
        if (id <= 0) {
            return Response.fail();
        }
        List<Integer> relevanceIds = new ArrayList<>();
        QueryWrapper<SysDataRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleId", id).eq("level", TreeLevel.HeatSource.level());
        SysDataRelevance sysDataRelevance = sysDataRelevanceService.getOne(queryWrapper);
        if (sysDataRelevance!=null) {
            relevanceIds = JSONObject.parseArray(sysDataRelevance.getRelevanceIds(), Integer.class);
        }
        return Response.success(relevanceIds);
    }


}
