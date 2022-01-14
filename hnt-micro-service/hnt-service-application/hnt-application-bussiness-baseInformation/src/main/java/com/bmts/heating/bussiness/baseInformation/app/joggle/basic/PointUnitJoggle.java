package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.db.service.PointUnitService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.common.response.PointUnitType;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "单位表管理")
@RestController
@RequestMapping("/pointUnit")
@Slf4j
public class PointUnitJoggle {
    @Autowired
    PointUnitService pointUnitService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody PointUnit info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        PointUnit one = pointUnitService.getOne(Wrappers.<PointUnit>lambdaQuery().eq(PointUnit::getUnitValue, info.getUnitValue()));
        if (one != null) {
            return Response.fail("该单位已经存在，不能重复添加！");
        }
        if (pointUnitService.save(info))
            return Response.success();
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        if (pointUnitService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody PointUnit info) {
        Response response = Response.fail();
        PointUnit pointUnit = pointUnitService.getById(info.getId());
        if (pointUnit == null)
            return Response.fail();
        info.setUpdateTime(LocalDateTime.now());

        PointUnit one = pointUnitService.getOne(Wrappers.<PointUnit>lambdaQuery().eq(PointUnit::getUnitValue, info.getUnitValue()));
        if (one != null && !Objects.equals(one.getId(), info.getId())) {
            return Response.fail("该单位已经存在，请更改单位！");
        }
        if (pointUnitService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        PointUnit info = pointUnitService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response queryByMap(@RequestBody BaseDto dto) {
        try {
            QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord()))
                queryWrapper.like("unitName", dto.getKeyWord());
            Page<PointUnit> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            // List<PointUnit> list = pointUnitService.list();
            return Response.success(pointUnitService.page(page, queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

    @ApiOperation("查询单位分类")
    @GetMapping("/queryType")
    public List<String> queryUnitGroup() {
        List<String> collect = pointUnitService.list()
                .stream()
                .map(x -> x.getUnitName())
                .distinct()
                .collect(Collectors.toList());
        return collect;
    }
}
