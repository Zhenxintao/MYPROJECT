package com.bmts.heating.web.backStage.controller.base;

import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.base.TreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "树形结构数据管理")
@RestController
@RequestMapping("/tree")
public class TreeController {

    @Autowired
    private TreeService treeService;

    @ApiOperation(value = "获取表格-组织架构树", response = CommonTree.class)
    @PostMapping("/station")
    public Response getStationTree(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            dto.setStatus(true);
            List<CommonTree> res = treeService.getOrgStationTree(dto);
            return Response.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success();
        }
    }

    @ApiOperation(value = "获取系统-组织架构树", response = CommonTree.class)
    @PostMapping("/system")
    public Response getSystemTree(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            dto.setStatus(true);
            return treeService.getOrgSystemTree(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

}
