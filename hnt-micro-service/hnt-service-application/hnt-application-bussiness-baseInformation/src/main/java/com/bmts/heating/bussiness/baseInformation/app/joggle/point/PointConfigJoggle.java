package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.converter.PointConfigConverter;
import com.bmts.heating.bussiness.baseInformation.app.handler.EsHandler;
import com.bmts.heating.bussiness.baseInformation.app.service.CommonServiceAsync;
import com.bmts.heating.bussiness.baseInformation.app.utils.ListUtil;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.CraftCacheDto;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.BatchAlarmDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
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

@Api(tags = "配置点")
@RestController
@RequestMapping("/pointConfig")
@Slf4j
public class PointConfigJoggle {

    @Autowired
    private PointConfigService pointConfigService;


    @Autowired
    private RedisPointService redisPointService;
    @Autowired
    private PointConfigConverter pointConfigConverter;
    @Autowired
    private PointStandardService pointStandardService;
    @Autowired
    private EsHandler esHandler;
    @Autowired
    PointConfigMapper pointConfigMapper;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private HeatSystemService heatSystemService;
    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    @Autowired
    private CommonServiceAsync commonServiceAsync;

    @ApiOperation("根据关联Id批量新增")
    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody PointConfigAddDto dto) {
        List<Integer> result = new ArrayList<>();
        Integer pointStandardId = dto.getPointConfig().getPointStandardId();
        for (Integer e : dto.getRelevanceId()) {
            dto.getPointConfig().setRelevanceId(e);
            dto.getPointConfig().setCreateTime(LocalDateTime.now());
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pointStandardId", pointStandardId);
            queryWrapper.eq("relevanceId", e);
            queryWrapper.eq("level", dto.getLevel());
            PointConfig pointConfig = pointConfigService.getOne(queryWrapper);
            if (pointConfig != null) {
                dto.getPointConfig().setId(pointConfig.getId());
                pointConfigService.updateById(PointConfigConverter.INSTANCE.domain2dto(dto.getPointConfig()));
                result.add(e);
            } else {
                pointConfigService.save(PointConfigConverter.INSTANCE.domain2dto(dto.getPointConfig()));
                redisPointService.saveOrUpdateByrelevanceId(e, TreeLevel.HeatSystem.level());
            }
        }
        if (!CollectionUtils.isEmpty(result)) {
            redisPointService.saveOrUpdateByrelevanceId(result, TreeLevel.HeatSystem.level());
        }
        return Response.success(result);
    }

    @ApiOperation("批量删除")
    @PostMapping("/deleteBatch")
    public Response deleteBatch(@RequestBody List<PointConfig> param) {
        List<Integer> heatSystemIds = new ArrayList<>();
        if (ListUtil.isValid(param)) {
            param.forEach(e -> {
                QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pointStandardId", e.getPointStandardId());
                queryWrapper.eq("relevanceId", e.getRelevanceId());
                queryWrapper.eq("level", e.getLevel());
                if (pointConfigService.remove(queryWrapper)) {
                    heatSystemIds.add(e.getRelevanceId());
                }
            });
            redisPointService.saveOrUpdateByrelevanceId(heatSystemIds, TreeLevel.HeatSystem.level());
            // 重新加载采集点服务
            commonServiceAsync.reloadMonitor();
        } else {
            return Response.fail();
        }
        return Response.success();
    }

    @ApiOperation("单标准参量删除")
    @PostMapping("/delete")
    public Response delete(@RequestBody PointConfigDeleteDto param) {
        if (ListUtil.isValid(param.getRelevanceIds())) {
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pointStandardId", param.getPointStandId());
            queryWrapper.in("relevanceId", param.getRelevanceIds());
            queryWrapper.in("level", param.getLevels());
            if (pointConfigService.remove(queryWrapper)) {
                redisPointService.saveOrUpdateByrelevanceId(param.getRelevanceIds(), TreeLevel.HeatSystem.level());
                // 删除排行数据
                PointStandard pointStandard = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getId, param.getPointStandId()));
                redisPointService.deleteRank(pointStandard.getColumnName(), param.getRelevanceIds(), TreeLevel.HeatSystem.level());
                // 删除实时数据  测试删除实时库数据是否正常可用
                redisPointService.deleteRealData(pointStandard.getColumnName(), param.getRelevanceIds(), TreeLevel.HeatSystem.level());
                // 重新加载采集点服务
                commonServiceAsync.reloadMonitor();
                return Response.success();
            }
        } else {
            return Response.paramError();
        }
        return Response.fail();
    }

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Response page(@RequestBody PointConfigDto dto) {
        try {
            Page<PointConfigResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            //获取基础信息
            List<StationFirstNetBaseView> firstNetBases;
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
            } else {
                return Response.success();
            }
            if (dto.getPointConfigType() != null) {
                if (dto.getPointConfigType() == 1) {
                    queryWrapper.in("ps.pointConfig", PointProperties.ReadOnly.type());
                } else if (dto.getPointConfigType() == 2) {
                    queryWrapper.eq("ps.pointConfig", PointProperties.ReadAndControl.type());
                } else if (dto.getPointConfigType() == 4) {
                    queryWrapper.eq("ps.pointConfig", PointProperties.Compute.type());
                } else if (dto.getPointConfigType() == 3) {
                    queryWrapper.eq("ps.pointConfig", PointProperties.ControlOnly.type());
                }
            }
            if (dto.getType() != null) {
                queryWrapper.eq("ps.type", dto.getType());
            }
            if (StringUtils.isNotBlank(dto.getColumnName())) {
                queryWrapper.like("ps.columnName", dto.getColumnName());
            }
            queryWrapper.eq("pc.deleteFlag", false);
            Page<PointConfigResponse> result = pointConfigService.page(page, queryWrapper);

            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.in("relevanceId", relevanceIds);
            queryView.eq("level", TreeLevel.HeatSystem.level());
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(result.getRecords(), alarmList);

            this.assembleResult(firstNetBaseListFilter, result, dto.getLevel());
            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }

    private List<StationFirstNetBaseView> filterBase(List<StationFirstNetBaseView> firstNetBases, PointConfigDto dto) {

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

//		if (dto.getHeatCabinetId() != null) {
//			firstNetBaseStream = firstNetBaseStream.filter(e -> e.getHeatCabinetId() == dto.getHeatCabinetId());
//		}
//		if (dto.getHeatSystemId() != null) {
//			firstNetBaseStream = firstNetBaseStream.filter(e -> e.getHeatSystemId() == dto.getHeatSystemId());
//		}if (dto.getHeatSystemBranchId() != null) {
//			firstNetBaseStream = firstNetBaseStream.filter(e -> e.getHeatSystemBranchId() == dto.getHeatSystemBranchId());
//		}
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

    private void assembleResult(List<StationFirstNetBaseView> firstNetBases, Page<PointConfigResponse> result, int level) {
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

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody PointConfig entry) {
        entry.setUpdateTime(LocalDateTime.now());
        if (pointConfigService.updateById(entry)) {
            {
                redisPointService.saveOrUpdateByrelevanceId(entry.getRelevanceId(), TreeLevel.HeatSystem.level());
                return Response.success();
            }

        } else {
            return Response.fail();
        }
    }

    @ApiOperation("详情")
    @GetMapping("/{id}")
    public Response queryById(@PathVariable Integer id) {
        return id != null ? Response.success(pointConfigService.emptyInfo(id)) : Response.paramError();
    }

    @PostMapping("/byids")
    public Response queryByIds(@RequestBody CraftCacheDto dto) {
        LambdaQueryWrapper<PointConfigStationView> queryWrapper = Wrappers.<PointConfigStationView>lambdaQuery()
                .in(PointConfigStationView::getId, dto.getPointIds());
        return Response.success(pointConfigStationViewService.list(queryWrapper));
    }

    @ApiOperation("是否报警")
    @PutMapping("/{id}/{state}")
    public Response isAlarm(@PathVariable Integer id, @PathVariable Boolean state) {
        PointConfig pointCollectConfig = new PointConfig();
        pointCollectConfig.setId(id);
//        pointCollectConfig.setIsAlarm(state);
        return id != null ? Response.success(pointConfigService.updateById(pointCollectConfig)) : Response.paramError();
    }

    @ApiOperation("是否报警")
    @PostMapping("/batch/alarm")
    public Response isAlarm(@RequestBody BatchAlarmDto dto) {
        List<PointConfig> list = new ArrayList<>();
        dto.getIds().forEach(e -> {
            PointConfig pointCollectConfig = new PointConfig();
            pointCollectConfig.setId(e);
//            pointCollectConfig.setIsAlarm(dto.getState());
            list.add(pointCollectConfig);
        });
        return ListUtil.isValid(dto.getIds()) ? Response.success(pointConfigService.updateBatchById(list)) : Response.paramError();
    }

    @ApiOperation("加载未添加标准参量/带搜索")
    @PostMapping("/none")
    public Response loadOtherPointStandardSearch(@RequestBody PointConfigStandardQueryDto dto) {
        if (dto.getHeatSystemId() == null) {
            return Response.paramError();
        }
        try {
            Page<PointStandard> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                page = pointConfigService.loadOtherPoint(page, dto.getLevel(), dto.getHeatSystemId(), dto.getKeyWord());
            } else {
                page = pointConfigService.loadOtherPoint(page, dto.getLevel(), dto.getHeatSystemId());
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("lastCount", page.getTotal() - page.getRecords().size());
            result.put("res", page.getRecords());
            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }

    @ApiOperation("分页查询热源的配置点")
    @PostMapping("/pageSource")
    public Response pageSource(@RequestBody PointConfigDto dto) {
        try {
            Page<PointConfigResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            //获取热源下的所有系统
            QueryWrapper<Object> heatWrapper = new QueryWrapper<>();
            QueryWrapper<Object> eq = heatWrapper.eq("hc.heatSourceId", dto.getHeatSourceId());
            List<Integer> systemIds = heatSystemService.querySystemIdByHeatSourceId(eq);
            if (CollectionUtils.isEmpty(systemIds)) {
                return null;
            }
            List<SourceFirstNetBaseView> list = sourceFirstNetBaseViewService.list();
//            //过滤基础信息
//            List<StationFirstNetBaseView> firstNetBaseListFilter = this.filterBase(firstNetBases, dto);
//            List<Integer> relevanceIds = getRelevanceId(firstNetBaseListFilter, dto.getLevel());

            if (dto.getLevel() != null)
                queryWrapper.eq("pc.level", dto.getLevel());
            if (dto.getIsAlarm() != null) {
                queryWrapper.eq("pc.isAlarm", dto.getIsAlarm());
            }
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                queryWrapper.like("ps.name", dto.getKeyWord());
            }
            if (ListUtil.isValid(systemIds)) {
                queryWrapper.in("pc.relevanceId", systemIds);
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
            Page<PointConfigResponse> result = pointConfigService.page(page, queryWrapper);

            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.in("relevanceId", systemIds);
            queryView.eq("level", TreeLevel.HeatSystem.level());
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(result.getRecords(), alarmList);

            this.assembleSource(list, result, dto.getLevel());
            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }

    // 设置报警值
    private void setAlarm(List<PointConfigResponse> list, List<PointAlarmView> alarmList) {
        if (!CollectionUtils.isEmpty(list)) {
            list.parallelStream().forEach(e -> {
                PointAlarmView pointAlarmView = alarmList.stream().filter(j -> Objects.equals(j.getId(), e.getId())).findFirst().orElse(new PointAlarmView());
                if (pointAlarmView.getIsAlarm() != null) {
                    e.setIsAlarm(pointAlarmView.getIsAlarm());
                }
                if (pointAlarmView.getAccidentHigh() != null) {
                    e.setAccidentHigh(pointAlarmView.getAccidentHigh());
                }
                if (pointAlarmView.getAccidentLower() != null) {
                    e.setAccidentLower(pointAlarmView.getAccidentLower());
                }
                if (pointAlarmView.getAlarmConfigId() != null) {
                    e.setAlarmConfigId(pointAlarmView.getAlarmConfigId());
                }
                if (pointAlarmView.getAlarmValue() != null) {
                    e.setAlarmValue(pointAlarmView.getAlarmValue());
                }
                if (pointAlarmView.getRunningHigh() != null) {
                    e.setRunningHigh(pointAlarmView.getRunningHigh());
                }
                if (pointAlarmView.getRunningLower() != null) {
                    e.setRunningLower(pointAlarmView.getRunningLower());
                }
                if (pointAlarmView.getPointAlarmId() != null) {
                    e.setPointAlarmId(pointAlarmView.getPointAlarmId());
                }
                if (pointAlarmView.getGrade() != null) {
                    e.setGrade(pointAlarmView.getGrade());
                }
                if (StringUtils.isNotBlank(pointAlarmView.getAlarmDesc())) {
                    e.setAlarmDesc(pointAlarmView.getAlarmDesc());
                }
                if (StringUtils.isNotBlank(pointAlarmView.getRankJson())) {
                    e.setRankJson(pointAlarmView.getRankJson());
                }

            });

        }
    }

    private void assembleSource(List<SourceFirstNetBaseView> firstNetBases, Page<PointConfigResponse> result, int level) {
        switch (level) {
            case 1://系统
                result.getRecords().forEach(e -> {
                    SourceFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSystemId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new SourceFirstNetBaseView());
                    e.setHeatSystemName(entry.getHeatSystemName());
//                    e.setHeatTransferStationId(entry.getHeatSourceId());
//                    e.setHeatTransferStationName(entry.getHeatSourceName());
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
                    SourceFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new SourceFirstNetBaseView());
                    e.setHeatCabinetId(entry.getHeatCabinetId());
                    e.setHeatCabinetName(entry.getHeatCabinetName());
//                    e.setHeatTransferStationId(entry.getHeatSourceId());
//                    e.setHeatTransferStationName(entry.getHeatSourceName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 3://站
                result.getRecords().forEach(e -> {
                    SourceFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSourceId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new SourceFirstNetBaseView());
//                    e.setHeatTransferStationId(entry.getHeatSourceId());
//                    e.setHeatTransferStationName(entry.getHeatSourceName());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 4://源
                result.getRecords().forEach(e -> {
                    SourceFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatSourceId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new SourceFirstNetBaseView());
                    e.setHeatSourceId(entry.getHeatSourceId());
                    e.setHeatSourceName(entry.getHeatSourceName());
//                    e.setHeatNetId(entry.getHeatNetId());
//                    e.setHeatNetName(entry.getHeatNetName());
                });
                break;
            case 5://网
                result.getRecords().forEach(e -> {
                    SourceFirstNetBaseView entry = firstNetBases.stream().
                            filter(j -> j.getHeatCabinetId() == (e.getRelevanceId() != null ? e.getRelevanceId() : 0)).findFirst().orElse(new SourceFirstNetBaseView());
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

    @Autowired
    PointConfigStationViewService pointConfigStationViewService;
    @Autowired
    PointConfigSourceViewService pointConfigSourceViewService;

    @ApiOperation("根据点名称单个站/源 下所有点id")
    @PostMapping("/ids/bycolumns")
    public Response queryPointsIdByColumns(@RequestBody CraftCacheDto dto) {
        try {
            // level：1属于系统  3.站 4.源 5.网
            // 处理热力站id
            if (dto.getType() == 1) {
                // level：1属于系统  3.站 4.源 5.网
                if (dto.getLevel() == 1) {
                    // 查询所属站
                    StationFirstNetBaseView stationFirstNetBaseView = stationFirstNetBaseViewService.getOne(Wrappers.<StationFirstNetBaseView>lambdaQuery()
                            .eq(StationFirstNetBaseView::getHeatSystemId, dto.getRelevanceId()));
                    if (stationFirstNetBaseView == null) {
                        return Response.success();
                    }
                    // 查询站下面的所有点
                    List<PointConfigStationView> list = pointConfigStationViewService.list(Wrappers.<PointConfigStationView>lambdaQuery()
                            .eq(PointConfigStationView::getStationId, stationFirstNetBaseView.getHeatTransferStationId())
                            .in(PointConfigStationView::getColumnName, dto.getColumnNames()));
                    if (!CollectionUtils.isEmpty(list)) {
//                        Integer stationId = list.get(0).getStationId();
//                        List<PointConfigStationView> zeroSystem = pointConfigStationViewService.list(Wrappers.<PointConfigStationView>lambdaQuery()
//                                .eq(PointConfigStationView::getStationId, stationId)
//                                .eq(PointConfigStationView::getSystemNumber, 0)
//                                .in(PointConfigStationView::getColumnName, dto.getColumnNames()));

                        List<PointConfigStationView> collect = list.stream()
                                .filter(e -> e.getSystemNumber() == 0 || Objects.equals(e.getRelevanceId(), dto.getRelevanceId()))
                                .collect(Collectors.toList());

                        return Response.success(collect);
                    } else {
                        return Response.success();
                    }
                } else {
                    List<PointConfigStationView> list = pointConfigStationViewService.list(Wrappers.<PointConfigStationView>lambdaQuery()
                            .eq(PointConfigStationView::getStationId, dto.getRelevanceId()).in(PointConfigStationView::getColumnName, dto.getColumnNames()));
                    return Response.success(list);
                }
            }
            // 处理热源 id
            if (dto.getType() == 2) {
                if (dto.getLevel() == 1) {
                    List<PointConfigSourceView> list = pointConfigSourceViewService.list(Wrappers.<PointConfigSourceView>lambdaQuery()
                            .eq(PointConfigSourceView::getRelevanceId, dto.getRelevanceId()).in(PointConfigSourceView::getColumnName, dto.getColumnNames()));
                    if (!CollectionUtils.isEmpty(list)) {
                        Integer sourceId = list.get(0).getSourceId();
                        List<PointConfigSourceView> zeroSystem = pointConfigSourceViewService.list(Wrappers.<PointConfigSourceView>lambdaQuery()
                                .eq(PointConfigSourceView::getSourceId, sourceId)
                                .eq(PointConfigSourceView::getSystemNumber, 0)
                                .in(PointConfigSourceView::getColumnName, dto.getColumnNames()));
                        list.addAll(zeroSystem);
                    }
                    return Response.success(list);
                } else {
                    List<PointConfigSourceView> list = pointConfigSourceViewService.list(Wrappers.<PointConfigSourceView>lambdaQuery()
                            .eq(PointConfigSourceView::getSourceId, dto.getRelevanceId()).in(PointConfigSourceView::getColumnName, dto.getColumnNames()));
                    return Response.success(list);

                }
            }

        } catch (Exception e) {
            log.error("根据点名称单个站/源 下所有点id {}", e.getMessage());
            return Response.success();
        }
        return Response.success();
    }

    @ApiOperation("批量保存系统点配置")
    @PostMapping("/insertPointConfig")
    public Response insertPointConfig(@RequestBody List<PointConfig> dto) {
        try {
            if (!CollectionUtils.isEmpty(dto)) {
                if (pointConfigService.saveBatch(dto)) {
                    Set<Integer> collect = dto.stream().map(PointConfig::getRelevanceId).collect(Collectors.toSet());
                    if (!CollectionUtils.isEmpty(collect)) {
                        redisPointService.saveOrUpdateByrelevanceId(new ArrayList<>(collect), TreeLevel.HeatSystem.level());
                    }
                    // 重新加载采集点服务
                    commonServiceAsync.reloadMonitor();

                    return Response.success();
                } else {
                    return Response.fail();
                }
            }


            return Response.success();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询某系统下已配置站点信息")
    @GetMapping("/queryPointConfigExist/{id}")
    public Response queryPointConfigExist(@PathVariable Integer id) {
        try {
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("p.relevanceId", id);
            List<PointStandard> pointStandardList = pointConfigMapper.queryPointConfigExist(queryWrapper);
            return Response.success(pointStandardList);
        } catch (Exception e) {
            return Response.fail();
        }
    }


    //// 进行接口调用，重启采集线程
    //private void reloadMonitor() {
    //    // 进行接口调用，重启采集线程
    //    WebPageConfig monitorReloadUrl = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "monitorReloadUrl"));
    //    if (monitorReloadUrl != null && StringUtils.isNotBlank(monitorReloadUrl.getJsonConfig())) {
    //        String jsonConfig = monitorReloadUrl.getJsonConfig();
    //        JSONObject strJson = JSONObject.parseObject(jsonConfig);
    //        // 返回的请求路径
    //        String url = strJson.getString("reloadUrl");
    //        BaseInfoUrlConn.doGet(url);
    //    }
    //}

}
