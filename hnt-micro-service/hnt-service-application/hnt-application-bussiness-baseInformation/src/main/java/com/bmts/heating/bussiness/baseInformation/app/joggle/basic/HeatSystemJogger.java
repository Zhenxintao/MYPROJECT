package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSystemDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "系统管理")
@RestController
@RequestMapping("/heatSystem")
@Slf4j
public class HeatSystemJogger {
    @Autowired
    HeatSystemService heatSystemService;

    @Autowired
    private PointConfigService pointConfigService;

    @Autowired
    private AreaManagerJoggle areaManagerJoggle;

    @ApiOperation("新增")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response insert(@RequestBody HeatSystem info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        if (heatSystemService.save(info)) {
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatSystem.level());
            dto.setRelevanceId(info.getId());
            dto.setArea(info.getHeatArea());
            return areaManagerJoggle.update(dto);
//            return Response.success();
        }
        return response;
    }

    @Autowired
    RedisPointService redisCollectPointService;

    @ApiOperation("删除")
    @DeleteMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        if (heatSystemService.removeById(id)) {
            pointConfigService.remove(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getRelevanceId, id).eq(PointConfig::getLevel,TreeLevel.HeatSystem.level()));
            List<Integer> param =new ArrayList<>(1);
            param.add(id);
            redisCollectPointService.delete(param, TreeLevel.HeatSystem.level());
            redisCollectPointService.deleteCache(param, TreeLevel.HeatSystem.level());
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response update(@RequestBody HeatSystem info) {
        Response response = Response.fail();
        HeatSystem heatSystem = heatSystemService.getById(info.getId());
        if (heatSystem == null) {
            return Response.fail();
        }
        info.setUpdateTime(LocalDateTime.now());
        if (heatSystemService.updateById(info)){
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatSystem.level());
            dto.setRelevanceId(info.getId());
            dto.setArea(info.getHeatArea());
            return areaManagerJoggle.update(dto);
        }

        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        HeatSystem info = heatSystemService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询(所属机柜下的所有系统)")
    @PostMapping("/query")
    public Response query(@RequestBody HeatSystemDto dto) {
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        Page<HeatSystem> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        queryWrapper.eq("heatCabinetId", dto.getHeatCabinetId());
        return Response.success(heatSystemService.page(page, queryWrapper));
    }

    @ApiOperation("查询网、源、站、控制柜、系统关联的基本信息)")
    @PostMapping("/querySystem")
    public Response querySystem(@RequestBody HeatSystemDto dto) {
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.deleteFlag", 0);
        return Response.success(heatSystemService.querySystem(queryWrapper));
    }
}
