package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointParameterType;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointParameterTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "参量分类管理")
@RestController
@RequestMapping("/pointParameterType")
public class PointParameterTypeController {

    @Autowired
    private PointParameterTypeService pointParameterTypeService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody PointParameterType info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        if (StringUtils.isBlank(info.getName())) {
            return Response.fail("名称不能为空！");
        }
        return pointParameterTypeService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return pointParameterTypeService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody PointParameterType info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return pointParameterTypeService.update(info);
    }

    @ApiOperation(value = "详情",response = PointParameterType.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return pointParameterTypeService.detail(id);
    }

    @ApiOperation(value = "查询",response = PointParameterType[].class)
    @PostMapping("/query")
    public Response query(@RequestBody BaseDto dto) {
        return pointParameterTypeService.query(dto);
    }


    @ApiOperation(value = "查询树型数据",response = CommonTree.class)
    @GetMapping("/tree")
    public Response tree() {
        return pointParameterTypeService.tree();
    }

}
