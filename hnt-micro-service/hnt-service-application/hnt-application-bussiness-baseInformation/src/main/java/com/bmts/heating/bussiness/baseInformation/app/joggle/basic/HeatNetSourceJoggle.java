package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.db.mapper.HeatNetSourceMapper;
import com.bmts.heating.commons.db.service.HeatNetSourceService;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatNetSourceResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(tags = "热网及热源多关系管理")
@RestController
@RequestMapping("/heatNetSource")
@Slf4j
public class HeatNetSourceJoggle {
    @Autowired
    HeatNetSourceService heatNetSourceService;
    @Autowired
    HeatNetSourceMapper heatNetSourceMapper;

    @ApiOperation("新增网源关系信息")
    @PostMapping("insertNetSource")
    public Response insertNetSource(@RequestBody List<HeatNetSource> info) {
        List<HeatNetSourceResponse> heatNetSourceResponseList = querySourceInfo(0);
        List<HeatNetSource> list = new ArrayList<>();
        List<HeatNetSourceResponse> existList = new ArrayList<>();
        for (HeatNetSource h : info) {
            if (Objects.equals(h.getHeatNetId(), h.getHeatSourceId()) || h.getMatchupType() == null) {
                continue;
            }
            HeatNetSourceResponse netSource = heatNetSourceResponseList.stream()
                    .filter(s -> Objects.equals(s.getHeatNetId(), h.getHeatNetId()) && Objects.equals(s.getHeatSourceId(), h.getHeatSourceId()))
                    .findFirst().orElse(null);
            if (netSource == null) {
                list.add(h);
            } else {
                existList.add(netSource);
            }
        }
        if (list.stream().count() == 0) {
            return Response.success(existList);
        } else {
            if (heatNetSourceService.saveBatch(list))
                return Response.success(existList);
            return Response.fail();
        }
    }

    @ApiOperation("热网基础信息删除后，删除该网关系信息")
    @DeleteMapping("deleteNetById")
    public Response deleteNetById(@RequestParam int id) {
        Boolean result = heatNetSourceService.remove(Wrappers.<HeatNetSource>lambdaQuery().eq(HeatNetSource::getHeatNetId, id));
        if (result)
            return Response.success();
        return Response.fail();
    }

    @ApiOperation("热源基础信息删除后，删除该源关系信息")
    @DeleteMapping("deleteSourceById")
    public Response deleteSourceById(@RequestParam int id) {
        Boolean result = heatNetSourceService.remove(Wrappers.<HeatNetSource>lambdaQuery().eq(HeatNetSource::getHeatSourceId, id));
        if (result)
            return Response.success();
        return Response.fail();
    }

    @ApiOperation("根据Id删除网源关系信息")
    @DeleteMapping("deleteNetSourceById")
    public Response deleteNetSourceById(@RequestParam int id) {
        Boolean result = heatNetSourceService.removeById(id);
        if (result)
            return Response.success();
        return Response.fail();
    }

    @ApiOperation("删除网源关系信息")
    @PostMapping("deleteNetSourceInfo")
    public Response deleteNetSourceInfo(@RequestBody HeatNetSource dto) {
        Boolean result = heatNetSourceService.remove(Wrappers.<HeatNetSource>lambdaQuery().eq(HeatNetSource::getHeatNetId, dto.getHeatNetId()).eq(HeatNetSource::getHeatSourceId, dto.getHeatSourceId()));
        if (result)
            return Response.success();
        return Response.fail();
    }

    @ApiOperation("网源信息查询")
    @GetMapping("queryNetSource")
    public Response queryNetSource() {
        QueryWrapper<HeatNetSource> queryWrapper = new QueryWrapper<>();
        return Response.success(heatNetSourceMapper.queryHeatNetSourceInfo(queryWrapper));
    }

    @ApiOperation("修改网源关系信息")
    @PutMapping("updNetSource")
    public Response updNetSource(@RequestBody HeatNetSource info) {
        if (heatNetSourceService.updateById(info))
            return Response.success();
        return Response.fail();
    }

    @ApiOperation("查询热源信息")
    @GetMapping("querySourceInfo")
    public List<HeatNetSourceResponse> querySourceInfo(Integer id) {
        QueryWrapper<HeatNetSource> queryWrapper = new QueryWrapper<>();
        if (id != 0) {

            queryWrapper.eq("h.heatNetId", id);
        }
        return heatNetSourceMapper.queryHeatNetSourceInfo(queryWrapper);
    }
}
