package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatOrganizationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "热源组织架构")
@RestController
@RequestMapping("/heatOrganizationService")
public class HeatOrganizationController {

    @Autowired
    private HeatOrganizationService heatOrganizationService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatOrganization info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatOrganizationService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id) {
        return heatOrganizationService.delete(id);
    }

    @ApiOperation(value = "批量删除",response = Integer[].class)
    @DeleteMapping("/batch")
    public Response deleteBatch(@RequestBody List<Integer> ids) {
        return heatOrganizationService.deleteBatch(ids);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatOrganization info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatOrganizationService.update(info);
    }

    @ApiOperation(value = "单体查询",response = HeatOrganization.class)
    @GetMapping("/{id}")
    public Response detail(@PathVariable int id) {
        return heatOrganizationService.detail(id);
    }

    @ApiOperation(value = "分页查询",response = HeatOrganization[].class)
    @PostMapping("/page")
    public Response query(@RequestBody HeatOrganizationDto dto) {
        return heatOrganizationService.page(dto);
    }

    @ApiOperation(value = "查询全部",response = HeatOrganization[].class)
    @GetMapping("/all")
    public Response queryAll(HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        return heatOrganizationService.queryAll(userId);
    }

    @ApiOperation(value = "查询组织架构层级")
    @PostMapping("/queryTree2")
    public Response queryTree2(@RequestBody QueryTreeDto dto,HttpServletRequest request){
        dto.setUserId(JwtUtils.getUserId(request));
        return heatOrganizationService.queryTree2(dto);
    }

    @ApiOperation(value = "查询组织架构层级方法")
    @PostMapping("/queryTree")
    public Response queryTree(@RequestBody QueryTreeDto dto){
        //1 单层级 2 多层级 3 单层级带站 4 多层级带站
        return heatOrganizationService.queryTree(dto);
    }

}
