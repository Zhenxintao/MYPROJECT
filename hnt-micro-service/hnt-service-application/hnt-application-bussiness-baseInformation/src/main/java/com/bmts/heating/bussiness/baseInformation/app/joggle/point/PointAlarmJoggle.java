package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.ListUtil;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.mapper.PointAlarmMapper;
import com.bmts.heating.commons.db.service.PointAlarmService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm.PointAlarmSetDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointAlarmResponse;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "换热站点位报警配置")
@RestController
@RequestMapping("/pointAlarm")
@Slf4j
public class PointAlarmJoggle {
    @Autowired
    private PointAlarmService pointAlarmService;
    @Autowired
    private PointAlarmMapper pointAlarmMapper;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private RedisPointService redisPointService;
    @Autowired
    private PointConfigService pointConfigService;

    @ApiOperation("批量配置")
    @PostMapping("/setPointAlarm")
    public Response setPointAlarm(@RequestBody List<PointAlarm> dto) {
        if (dto.stream().count() > 0) {
            List<Integer> removeIds = dto.stream().map(s -> s.getPointConfigId()).collect(Collectors.toList());
            QueryWrapper<PointAlarm> pointAlarmQueryWrapper = new QueryWrapper<>();
            pointAlarmQueryWrapper.in("pointConfigId", removeIds);
            List<PointAlarm> pointAlarms = pointAlarmService.list(pointAlarmQueryWrapper);
            if (pointAlarms.stream().count() == 0) {
                Boolean result = pointAlarmService.saveBatch(dto);
                if (result) {
                    // 同步报警值到缓存  查询系统id
                    QueryWrapper<PointConfig> pointConfigWrapper = new QueryWrapper<>();
                    pointConfigWrapper.in("id", removeIds);
                    List<PointConfig> listPointConfig = pointConfigService.list(pointConfigWrapper);
                    Set<Integer> relevanceIds = new HashSet<>();
                    for (PointConfig pointConfig : listPointConfig) {
                        relevanceIds.add(pointConfig.getRelevanceId());
                    }
                    if (!CollectionUtils.isEmpty(relevanceIds)) {
                        redisPointService.syncByRelevanceIds(new ArrayList(relevanceIds), TreeLevel.HeatSystem.level());
                    }
                    return Response.success();
                }
            } else {
                Boolean removeResult = pointAlarmService.remove(pointAlarmQueryWrapper);
                if (removeResult) {
                    Boolean result = pointAlarmService.saveBatch(dto);
                    if (result) {
                        // 同步报警值到缓存  查询系统id
                        QueryWrapper<PointConfig> pointConfigWrapper = new QueryWrapper<>();
                        pointConfigWrapper.in("id", removeIds);
                        List<PointConfig> listPointConfig = pointConfigService.list(pointConfigWrapper);
                        Set<Integer> relevanceIds = new HashSet<>();
                        for (PointConfig pointConfig : listPointConfig) {
                            relevanceIds.add(pointConfig.getRelevanceId());
                        }
                        if (!CollectionUtils.isEmpty(relevanceIds)) {
                            redisPointService.syncByRelevanceIds(new ArrayList(relevanceIds), TreeLevel.HeatSystem.level());
                        }
                        return Response.success();
                    }
                }
            }
        }
        return Response.fail();
    }

    @ApiOperation("删除")
    @PostMapping("/removePointAlarm")
    public Response removePointAlarm(@RequestBody List<Integer> ids) {
        if (ids.stream().count() > 0) {
            QueryWrapper<PointAlarm> pointAlarmQueryWrapper = new QueryWrapper<>();
            pointAlarmQueryWrapper.in("id", ids);
            List<PointAlarm> pointAlarms = pointAlarmService.list(pointAlarmQueryWrapper);
            List<Integer> removeIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pointAlarms)) {
                removeIds = pointAlarms.stream().map(s -> s.getPointConfigId()).collect(Collectors.toList());
            }
            Boolean result = pointAlarmService.removeByIds(ids);
            if (result) {
                if (!CollectionUtils.isEmpty(removeIds)) {
                    // 同步报警值到缓存  查询系统id
                    QueryWrapper<PointConfig> pointConfigWrapper = new QueryWrapper<>();
                    pointConfigWrapper.in("id", removeIds);
                    List<PointConfig> listPointConfig = pointConfigService.list(pointConfigWrapper);
                    Set<Integer> relevanceIds = new HashSet<>();
                    for (PointConfig pointConfig : listPointConfig) {
                        relevanceIds.add(pointConfig.getRelevanceId());
                    }
                    if (!CollectionUtils.isEmpty(relevanceIds)) {
                        redisPointService.syncByRelevanceIds(new ArrayList(relevanceIds), TreeLevel.HeatSystem.level());
                    }
                }
                return Response.success();
            }
        }
        return Response.fail();
    }

    @ApiOperation("修改")
    @PutMapping("/updatePointAlarm")
    public Response updatePointAlarm(@RequestBody PointAlarm dto) {
        if (dto.getId() > 0) {
            PointAlarm one = pointAlarmService.getOne(Wrappers.<PointAlarm>lambdaQuery().eq(PointAlarm::getId, dto.getId()));
            if (one == null) {
                return Response.fail("没有查询到信息！");
            }
            UpdateWrapper<PointAlarm> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", dto.getId());
            if(dto.getAccidentLower() != null){
                updateWrapper.set("accidentLower", dto.getAccidentLower());
            }
            if(dto.getAccidentHigh() != null){
                updateWrapper.set("accidentHigh", dto.getAccidentHigh());
            }
            if(dto.getRunningLower() != null){
                updateWrapper.set("runningLower", dto.getRunningLower());
            }
            if(dto.getRunningHigh() != null){
                updateWrapper.set("runningHigh", dto.getRunningHigh());
            }
            if(dto.getIsAlarm() != null){
                updateWrapper.set("isAlarm", dto.getIsAlarm());
            }
            if(dto.getAlarmValue() != null){
                updateWrapper.set("alarmValue", dto.getAlarmValue());
            }
            // 配置报警级别
            if(dto.getGrade() != null){
                updateWrapper.set("grade", dto.getGrade());
            }
            // 配置报警描述
            if(StringUtils.isNotBlank(dto.getAlarmDesc())){
                updateWrapper.set("alarmDesc", dto.getAlarmDesc());
            }
            // 配置报警配置级别
            if(StringUtils.isNotBlank(dto.getRankJson())){
                updateWrapper.set("rankJson", dto.getRankJson());
            }
            updateWrapper.set("updateTime", LocalDateTime.now());
            Boolean result = pointAlarmService.update(updateWrapper);
            if (result) {
                // 同步报警值到缓存  查询系统id
                PointConfig loadPonitConfig = pointConfigService.getOne(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getId, one.getPointConfigId()));
                if (loadPonitConfig == null) {
                    return Response.fail("没有查询到配置的点信息！");
                }
                redisPointService.syncByRelevanceId(loadPonitConfig.getRelevanceId(), TreeLevel.HeatSystem.level());

                return Response.success();
            }
        }
        return Response.fail();
    }

    @ApiOperation("查询展示")
    @PostMapping("/page")
    public Response showPointAlarm(@RequestBody PointAlarmDto dto) {

        QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
        WrapperSortUtils.sortWrapper(queryWrapper, dto);
        //获取基础信息
        List<StationFirstNetBaseView> firstNetBases = null;
        firstNetBases = stationFirstNetBaseViewService.list();

        if (!ListUtil.isValid(firstNetBases)) {
            return null;
        }
        //过滤基础信息
        List<StationFirstNetBaseView> firstNetBaseListFilter = filterBase(firstNetBases, dto);
        List<Integer> relevanceIds = getRelevanceId(firstNetBaseListFilter, dto.getLevel());

        if (dto.getLevel() != null)
            queryWrapper.eq("pc.level", dto.getLevel());
        if (dto.getIsAlarm() != null) {
            queryWrapper.eq("pc.isAlarm", dto.getIsAlarm());
        }
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            queryWrapper.like("ps.name", dto.getKeyWord());
        }
        if (ListUtil.isValid(relevanceIds)) {
            queryWrapper.in("pc.relevanceId", relevanceIds);
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
        queryWrapper.eq("pc.deleteFlag", false);
        Page<PointAlarmResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        Page<PointAlarmResponse> result = pointAlarmMapper.page(page, queryWrapper);
        this.assembleResult(firstNetBaseListFilter, result, dto.getLevel());
        return Response.success(result);
    }

    @ApiOperation("查询标准点表数据")
    @PostMapping("/pointStandard")
    public Response query(@RequestBody PointAlarmSetDto dto) {
        Response response = Response.fail();
        try {
            Page<PointStandardResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            if (dto.getHeatType() == 1) {
                List<SourceFirstNetBaseView> views = sourceFirstNetBaseViewService.list(Wrappers.<SourceFirstNetBaseView>lambdaQuery().eq(SourceFirstNetBaseView::getHeatSourceId, dto.getHeatId()));
                List<Integer> ids = views.stream().map(s -> s.getHeatSystemId()).collect(Collectors.toList());
                queryWrapper.in("pc.relevanceId", ids);
            } else {
                List<StationFirstNetBaseView> view = stationFirstNetBaseViewService.list(Wrappers.<StationFirstNetBaseView>lambdaQuery().eq(StationFirstNetBaseView::getHeatTransferStationId, dto.getHeatId()));
                queryWrapper.in("pc.relevanceId", view.stream().map(s -> s.getHeatSystemId()).collect(Collectors.toList()));
            }
            if (StringUtils.isBlank(dto.getSortName())) {
                queryWrapper.orderByAsc("ps.sort");
            }
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
            if (dto.getSetType() == 2) {
                queryWrapper.isNull("pa.pointConfigId");
                return Response.success(pointAlarmMapper.pagePoint(page, queryWrapper));
            } else {
                return Response.success(pointAlarmMapper.page(page, queryWrapper));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }

    private List<StationFirstNetBaseView> filterBase(List<StationFirstNetBaseView> firstNetBases, PointAlarmDto dto) {

        Stream<StationFirstNetBaseView> firstNetBaseStream = firstNetBases.stream();
        //过滤基础信息
        if (dto.getHeatCabinetId() != null) {
            firstNetBaseStream = firstNetBaseStream.filter(e -> Objects.equals(e.getHeatCabinetId(), dto.getHeatCabinetId()));
        }
        if (dto.getHeatSystemId() != null) {
            firstNetBaseStream = firstNetBaseStream.filter(e -> Objects.equals(e.getHeatSystemId(), dto.getHeatSystemId()));
        }
        if (dto.getHeatStationId() != null) {
            firstNetBaseStream = firstNetBaseStream.filter(e -> Objects.equals(e.getHeatTransferStationId(), dto.getHeatStationId()));
        }
        if (dto.getHeatSourceId() != null) {
            firstNetBaseStream = firstNetBaseStream.filter(e -> Objects.equals(e.getHeatSourceId(), dto.getHeatSourceId()));
        }
        return firstNetBaseStream.collect(Collectors.toList());
    }

    private List<Integer> getRelevanceId(List<StationFirstNetBaseView> firstNetBases, int level) {
        Stream<StationFirstNetBaseView> firstNetBaseStream = firstNetBases.stream();
        List<Integer> collect = null;
        //根据层级获取关联Id
        switch (level) {
            case 1:
                collect = firstNetBaseStream.map(StationFirstNetBaseView::getHeatSystemId).collect(Collectors.toList());
                break;
            case 2:
                collect = firstNetBaseStream.map(StationFirstNetBaseView::getHeatCabinetId).collect(Collectors.toList());
                break;
            case 3:
                collect = firstNetBaseStream.map(StationFirstNetBaseView::getHeatTransferStationId).collect(Collectors.toList());
                break;
            case 4:
                collect = firstNetBaseStream.map(StationFirstNetBaseView::getHeatSourceId).collect(Collectors.toList());
                break;
//            case 5:
//                collect = firstNetBaseStream.map(StationFirstNetBaseView::getHeatNetId).collect(Collectors.toList());
//                break;
            case 6://系统分支
//				collect= firstNetBaseStream.map(FirstNetBase::getHeatSystemBranchId).collect(Collectors.toList()); break;
            default:
                break;
        }
        return collect;
    }

    private void assembleResult(List<StationFirstNetBaseView> firstNetBases, Page<PointAlarmResponse> result, int level) {
        switch (level) {
            case 1://系统
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSystemId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatSystemName(entry.getHeatSystemName());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                    e.setHeatCabinetId(entry.getHeatCabinetId());
                    e.setHeatCabinetName(entry.getHeatCabinetName());
                });
                break;
            case 2://柜
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatCabinetId(entry.getHeatCabinetId());
                    e.setHeatCabinetName(entry.getHeatCabinetName());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 3://站
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatTransferStationId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 4://源
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSourceId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 5://网
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 6://系统分支
//				collect= firstNetBaseStream.map(FirstNetBase::getHeatSystemBranchId).collect(Collectors.toList()); break;
            default:
                break;
        }

    }

    private void assembleResultConfig(List<StationFirstNetBaseView> firstNetBases, Page<PointConfigResponse> result, int level) {
        switch (level) {
            case 1://系统
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSystemId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatSystemName(entry.getHeatSystemName());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                    e.setHeatCabinetId(entry.getHeatCabinetId());
                    e.setHeatCabinetName(entry.getHeatCabinetName());
                });
                break;
            case 2://柜
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatCabinetId(entry.getHeatCabinetId());
                    e.setHeatCabinetName(entry.getHeatCabinetName());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 3://站
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatTransferStationId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatTransferStationId(entry.getHeatTransferStationId());
                    e.setHeatTransferStationName(entry.getHeatTransferStationName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 4://源
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSourceId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 5://网
                result.getRecords().forEach(e -> {
                    StationFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new StationFirstNetBaseView());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 6://系统分支
//				collect= firstNetBaseStream.map(FirstNetBase::getHeatSystemBranchId).collect(Collectors.toList()); break;
            default:
                break;
        }

    }


}
