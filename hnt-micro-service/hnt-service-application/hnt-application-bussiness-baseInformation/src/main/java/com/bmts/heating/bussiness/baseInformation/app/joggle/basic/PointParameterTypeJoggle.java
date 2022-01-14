package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointParameterType;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.enums.ParameterType;
import com.bmts.heating.commons.db.service.PointParameterTypeService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "参量分类管理")
@RestController
@RequestMapping("/pointParameterType")
@Slf4j
public class PointParameterTypeJoggle {
    @Autowired
    PointParameterTypeService pointParameterTypeService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody PointParameterType info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        PointParameterType one = pointParameterTypeService.getOne(Wrappers.<PointParameterType>lambdaQuery().eq(PointParameterType::getName, info.getName())
                .eq(PointParameterType::getType, info.getType()));
        if (one != null) {
            return Response.fail("参量名称重复！");
        }
        if (pointParameterTypeService.save(info)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<PointParameterType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        if (pointParameterTypeService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody PointParameterType info) {
        Response response = Response.fail();
        PointParameterType pointParameterType = pointParameterTypeService.getById(info.getId());
        if (pointParameterType == null) {
            return Response.fail();
        }
        PointParameterType one = pointParameterTypeService.getOne(Wrappers.<PointParameterType>lambdaQuery().eq(PointParameterType::getName, info.getName()));
        if (one != null && !Objects.equals(one.getId(), info.getId())) {
            return Response.fail("该参量名称已经存在，请重新修改名称！");
        }
        info.setUpdateTime(LocalDateTime.now());
        if (pointParameterTypeService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        PointParameterType info = pointParameterTypeService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response queryByMap(@RequestBody BaseDto dto) {
        try {
            QueryWrapper<PointParameterType> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord()))
                queryWrapper.like("name", dto.getKeyWord());
            Page<PointParameterType> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
//            List<PointParameterType> list = pointParameterTypeService.list();
            return Response.success(pointParameterTypeService.page(page, queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }


    @ApiOperation("查询树型数据")
    @GetMapping("/tree")
    public Response tree() {
        List<PointParameterType> list = pointParameterTypeService.list();
        List<CommonTree> listTree = new ArrayList<>();
        Map<Integer, List<PointParameterType>> collect = list.stream().collect(Collectors.groupingBy(PointParameterType::getType));
        for (Integer type : collect.keySet()) {
            String pid = String.valueOf(type);
            CommonTree commonTree = new CommonTree();
            commonTree.setId(pid);
            commonTree.setLevel(0);
            if (type == ParameterType.PARAMETER_RUN.getType()) {
                commonTree.setName(ParameterType.PARAMETER_RUN.getName());
            }
            if (type == ParameterType.PARAMETER_COMPUTE.getType()) {
                commonTree.setName(ParameterType.PARAMETER_COMPUTE.getName());
            }
            listTree.add(commonTree);

            List<PointParameterType> pointParameterTypes = collect.get(type);
            for (PointParameterType parameterType : pointParameterTypes) {
                CommonTree sonTree = new CommonTree();
                sonTree.setId(String.valueOf(parameterType.getId()));
                sonTree.setPid(pid);
                sonTree.setName(parameterType.getName());
                sonTree.setLevel(1);
                listTree.add(sonTree);
            }
        }
        return Response.success(listTree);
    }

}
