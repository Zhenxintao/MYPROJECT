package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.mapper.PointStandardAlarmMapper;
import com.bmts.heating.commons.db.service.PointStandardAlarmService;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointStandardAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardAlarmResponse;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "通用点位报警配置")
@RestController
@RequestMapping("/pointStandardAlarm")
@Slf4j
public class PointStandardAlarmJoggle {
    @Autowired
    private PointStandardAlarmService pointStandardAlarmService;
    @Autowired
    private PointStandardAlarmMapper pointStandardAlarmMapper;

//    @Autowired
//    PointStandardService pointStandardService;

    @Autowired
    private RedisPointService redisPointService;

    @ApiOperation("批量配置")
    @PostMapping("/setPointStandardAlarm")
    public Response setPointAlarm(@RequestBody List<PointStandardAlarm> dto) {
        if (dto.stream().count() > 0) {
            List<Integer> removeIds = dto.stream().map(s -> s.getPointStandardId()).collect(Collectors.toList());
            QueryWrapper<PointStandardAlarm> pointAlarmQueryWrapper = new QueryWrapper<>();
            pointAlarmQueryWrapper.in("pointStandardId", removeIds);
            List<PointStandardAlarm> pointStandardAlarms = pointStandardAlarmService.list(pointAlarmQueryWrapper);
            if (pointStandardAlarms.stream().count() == 0) {
                Boolean result = pointStandardAlarmService.saveBatch(dto);
                if (result) {
                    QueryWrapper<PointStandardAlarm> queryWrapper = new QueryWrapper<>();
                    queryWrapper.in("psa.pointStandardId", removeIds);
                    List<Integer> relevanceIds = pointStandardAlarmService.listRelevanceId(queryWrapper);
                    // 同步报警值到缓存  查询系统id
                    if (!CollectionUtils.isEmpty(relevanceIds)) {
                        redisPointService.syncByRelevanceIds(relevanceIds, TreeLevel.HeatSystem.level());
                    }
                    return Response.success();
                }
            } else {
                Boolean removeResult = pointStandardAlarmService.remove(pointAlarmQueryWrapper);
                if (removeResult) {
                    Boolean result = pointStandardAlarmService.saveBatch(dto);
                    if (result) {
                        QueryWrapper<PointStandardAlarm> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("psa.pointStandardId", removeIds);
                        List<Integer> relevanceIds = pointStandardAlarmService.listRelevanceId(queryWrapper);
                        // 同步报警值到缓存  查询系统id
                        if (!CollectionUtils.isEmpty(relevanceIds)) {
                            redisPointService.syncByRelevanceIds(relevanceIds, TreeLevel.HeatSystem.level());
                        }
                        return Response.success();
                    }
                }
            }
        }
        return Response.fail();
    }

    @ApiOperation("删除")
    @PostMapping("/removePointStandardAlarm")
    public Response removePointAlarm(@RequestBody List<Integer> ids) {
        if (ids.stream().count() > 0) {
            QueryWrapper<PointStandardAlarm> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", ids);
            List<PointStandardAlarm> listStandardAlarm = pointStandardAlarmService.list(queryWrapper);
            List<Integer> removeIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(listStandardAlarm)) {
                removeIds = listStandardAlarm.stream().map(s -> s.getPointStandardId()).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(removeIds)) {
                // 同步报警值到缓存  查询系统id
                QueryWrapper<PointStandardAlarm> queryAlarmWrapper = new QueryWrapper<>();
                queryAlarmWrapper.in("psa.pointStandardId", removeIds);
                List<Integer> relevanceIds = pointStandardAlarmService.listRelevanceId(queryAlarmWrapper);
                // 同步报警值到缓存  查询系统id
                if (!CollectionUtils.isEmpty(relevanceIds)) {
                    redisPointService.syncByRelevanceIds(relevanceIds, TreeLevel.HeatSystem.level());
                }
            }

            Boolean result = pointStandardAlarmService.removeByIds(ids);
            if (result)
                return Response.success();
        }
        return Response.fail();
    }

    @ApiOperation("修改")
    @PutMapping("/updatePointStandardAlarm")
    public Response updatePointAlarm(@RequestBody PointStandardAlarm dto) {
        if (dto.getId() > 0) {
            PointStandardAlarm one = pointStandardAlarmService.getOne(Wrappers.<PointStandardAlarm>lambdaQuery().eq(PointStandardAlarm::getId, dto.getId()));
            if (one == null) {
                return Response.fail("没有查询到信息！");
            }
            UpdateWrapper<PointStandardAlarm> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", dto.getId());
            if (dto.getAccidentLower() != null) {
                updateWrapper.set("accidentLower", dto.getAccidentLower());
            }
            if (dto.getAccidentHigh() != null) {
                updateWrapper.set("accidentHigh", dto.getAccidentHigh());
            }
            if (dto.getRunningLower() != null) {
                updateWrapper.set("runningLower", dto.getRunningLower());
            }
            if (dto.getRunningHigh() != null) {
                updateWrapper.set("runningHigh", dto.getRunningHigh());
            }
            if (dto.getIsAlarm() != null) {
                updateWrapper.set("isAlarm", dto.getIsAlarm());
            }
            if (dto.getAlarmValue() != null) {
                updateWrapper.set("alarmValue", dto.getAlarmValue());
            }
            // 配置报警级别
            if (dto.getGrade() != null) {
                updateWrapper.set("grade", dto.getGrade());
            }
            // 配置报警描述
            if (StringUtils.isNotBlank(dto.getAlarmDesc())) {
                updateWrapper.set("alarmDesc", dto.getAlarmDesc());
            }
            // 配置报警配置级别
            if (StringUtils.isNotBlank(dto.getRankJson())) {
                updateWrapper.set("rankJson", dto.getRankJson());
            }
            updateWrapper.set("updateTime", LocalDateTime.now());
            Boolean result = pointStandardAlarmService.update(updateWrapper);
            if (result) {
                QueryWrapper<PointStandardAlarm> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("psa.pointStandardId", one.getPointStandardId());
                List<Integer> relevanceIds = pointStandardAlarmService.listRelevanceId(queryWrapper);
                // 同步报警值到缓存  查询系统id
                if (!CollectionUtils.isEmpty(relevanceIds)) {
                    redisPointService.syncByRelevanceIds(relevanceIds, TreeLevel.HeatSystem.level());
                }
                return Response.success();
            }
        }
        return Response.fail();
    }

    @ApiOperation("查询展示")
    @PostMapping("/page")
    public Response showPointStandardAlarm(@RequestBody PointStandardAlarmDto dto) {
        Page<PointStandardAlarmResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        QueryWrapper<PointStandardAlarm> queryWrapper = new QueryWrapper<>();

        //WrapperSortUtils.sortWrapper(queryWrapper, dto);
        
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            queryWrapper.like("ps.name", dto.getKeyWord());
        }
        if (dto.getPointConfigType() != null) {
            if (dto.getPointConfigType() == 1) {
                queryWrapper.in("ps.pointConfig", PointProperties.ReadOnly.type());
            } else {
                queryWrapper.eq("ps.pointConfig", PointProperties.ReadAndControl.type());
            }
        }
        if (dto.getType() != null) {
            queryWrapper.eq("ps.type", dto.getType());
        }
        if (StringUtils.isNotBlank(dto.getColumnName())) {
            queryWrapper.like("ps.columnName", dto.getColumnName());
        }
        Page<PointStandardAlarmResponse> result = pointStandardAlarmMapper.page(page, queryWrapper);
        return Response.success(result);
    }


    @ApiOperation("查询标准点表数据")
    @PostMapping("/pointStandard")
    public Response query(@RequestBody PointStandardDto dto) {
        Response response = Response.fail();
        try {
            Page<PointStandardResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("psa.pointStandardId");

            //WrapperSortUtils.sortWrapper(queryWrapper, dto);
            //if (StringUtils.isBlank(dto.getSortName())) {
            //    queryWrapper.orderByAsc("sort");
            //}

            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                queryWrapper.like("ps.name", dto.getKeyWord());
            }
            if (dto.getPointConfig() != null && dto.getPointConfig() != 0) {
                queryWrapper.eq("ps.pointConfig", dto.getPointConfig());
            }
            if (StringUtils.isNotBlank(String.valueOf(dto.getType())) && dto.getType() > 0) {
                queryWrapper.eq("ps.type", dto.getType());
            }
            if (dto.getNetFlag() != null) {
                queryWrapper.eq("ps.netFlag", dto.getNetFlag());
            }
            return Response.success(pointStandardAlarmMapper.pagePointStandard(page, queryWrapper));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }


}
