package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;
import com.bmts.heating.commons.entiy.common.TreeLevel;
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
import java.util.stream.Collectors;

@Api(tags = "控制柜管理")
@RestController
@RequestMapping("/heatCabinet")
@Slf4j
public class HeatCabinetJoggle {
    @Autowired
    HeatCabinetService heatCabinetService;
    @Autowired
    HeatSystemService heatSystemService;
    @Autowired
    PointConfigMapper pointConfigMapper;

    @ApiOperation("新增")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response insert(@RequestBody HeatCabinet info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        if (heatCabinetService.save(info))
            return Response.success();
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        deletePoints(id);
        if (heatCabinetService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response update(@RequestBody HeatCabinet info) {
        Response response = Response.fail();
        HeatCabinet heatCabinet = heatCabinetService.getById(info.getId());
        if (heatCabinet == null) {
            return Response.fail();
        }
        info.setUpdateTime(LocalDateTime.now());
        if (heatCabinetService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        HeatCabinet info = heatCabinetService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询所有控制柜")
    @PostMapping("/query")
    public Response query() {
        List<HeatCabinet> list = heatCabinetService.list();
        return Response.success(list);
    }

    @ApiOperation("查询所属热力站下的控制柜")
    @GetMapping("/queryByStationId")
    public Response queryById(@RequestParam int id) {
        List<HeatCabinet> list;
        try {
            QueryWrapper<HeatCabinet> queryWrappers = new QueryWrapper<>();
            queryWrappers.eq("heatTransferStationId", id);
            list = heatCabinetService.list(queryWrappers);
            return Response.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

    @ApiOperation("查询控热力站所有控制柜和所属机组")
    @GetMapping("/queryCs")
    public Response queryByMap(@RequestParam int id) {
        QueryWrapper<HeatCabinet> queryWrappers = new QueryWrapper<>();
        queryWrappers.eq("heatTransferStationId", id);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        List<CommonTree> cts = new ArrayList<>();
        List<HeatCabinet> list = heatCabinetService.list(queryWrappers);

        for (HeatCabinet hc : list) {
            CommonTree ct1 = new CommonTree();
            ct1.setId(String.valueOf(hc.getId()));
            ct1.setLevel(1);
            ct1.setName(hc.getName());
            ct1.setPid("0");
            ct1.setProperties(null);
            cts.add(ct1);
            queryWrapper.eq("heatCabinetId", hc.getId());
            List<HeatSystem> heatSystems = heatSystemService.list(queryWrapper);
            queryWrapper.clear();
            for (HeatSystem hs : heatSystems) {
                CommonTree ct2 = new CommonTree();
                ct2.setId(String.valueOf(hs.getId()));
                if (hs.getPid() == 0 || hs.getPid() == null) {
                    ct2.setPid(String.valueOf(hs.getHeatCabinetId()));
                    ct2.setLevel(2);
                } else {
                    ct2.setPid(String.valueOf(hs.getPid()));
                    ct2.setLevel(3);
                }
                ct2.setName(hs.getName());
                ct2.setProperties(null);
                cts.add(ct2);
            }
        }
        return Response.success(cts);
    }

    @Autowired
    PointConfigService pointConfigService;

    @Autowired
    RedisPointService redisPointService;

    private void deletePoints(int cabinetId) {
        try {
            List<CabinetPointResponse> cabinetPointResponses = pointConfigService.pointsAndSystem(cabinetId);
            List<Integer> systemCollectIds = cabinetPointResponses.stream().map(CabinetPointResponse::getHeatSystemId).filter(x -> x > 0).distinct().collect(Collectors.toList());
            List<Integer> poinstCollectIds = cabinetPointResponses.stream().map(CabinetPointResponse::getPointId).filter(x -> x > 0).distinct().collect(Collectors.toList());
            //deal redis
            redisPointService.delete(systemCollectIds, TreeLevel.HeatSystem.level());
            redisPointService.deleteCache(systemCollectIds, TreeLevel.HeatSystem.level());

            if (!CollectionUtils.isEmpty(systemCollectIds)) {
                heatSystemService.removeByIds(systemCollectIds);
            }
            if (!CollectionUtils.isEmpty(poinstCollectIds)) {
                pointConfigService.removeByIds(poinstCollectIds);
            }

        } catch (Exception e) {
            log.error("delete cabinet cause exception {}", e);
        }

    }


    @ApiOperation("查询热源下的控制柜")
    @GetMapping("/queryBySourceId")
    public Response queryBySourceId(@RequestParam int id) {
        List<HeatCabinet> list = null;
        try {
            QueryWrapper<HeatCabinet> queryWrappers = new QueryWrapper<>();
            queryWrappers.eq("heatSourceId", id);
            list = heatCabinetService.list(queryWrappers);
            return Response.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

    @ApiOperation("查询热源下所有控制柜和所属机组")
    @GetMapping("/SourceCs")
    public Response SourceCs(@RequestParam int id) {
        QueryWrapper<HeatCabinet> queryWrappers = new QueryWrapper<>();
        queryWrappers.eq("heatSourceId", id);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        List<CommonTree> cts = new ArrayList<>();
        List<HeatCabinet> list = heatCabinetService.list(queryWrappers);

        for (HeatCabinet hc : list) {
            CommonTree ct1 = new CommonTree();
            ct1.setId(String.valueOf(hc.getId()));
            ct1.setLevel(1);
            ct1.setName(hc.getName());
            ct1.setPid("0");
            ct1.setProperties(null);
            cts.add(ct1);
            queryWrapper.eq("heatCabinetId", hc.getId());
            List<HeatSystem> heatSystems = heatSystemService.list(queryWrapper);
            queryWrapper.clear();
            for (HeatSystem hs : heatSystems) {
                CommonTree ct2 = new CommonTree();
                ct2.setId(String.valueOf(hs.getId()));
                if (hs.getPid() == 0 || hs.getPid() == null) {
                    ct2.setPid(String.valueOf(hs.getHeatCabinetId()));
                    ct2.setLevel(2);
                } else {
                    ct2.setPid(String.valueOf(hs.getPid()));
                    ct2.setLevel(3);
                }
                ct2.setName(hs.getName());
                ct2.setProperties(null);
                cts.add(ct2);
            }
        }
        return Response.success(cts);
    }
}
