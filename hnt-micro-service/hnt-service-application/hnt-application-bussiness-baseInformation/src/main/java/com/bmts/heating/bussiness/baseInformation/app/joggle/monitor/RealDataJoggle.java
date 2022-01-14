package com.bmts.heating.bussiness.baseInformation.app.joggle.monitor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.request.QueryShowPowerDto;
import com.bmts.heating.commons.basement.model.db.response.ShowPowerResponse;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.QueryHydrostaticTempDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@Api(tags = "实时数据查询")
@RestController
@RequestMapping("/realData")
@Slf4j
public class RealDataJoggle {

    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

    @ApiOperation("热源及换热站实时数据")
    @PostMapping("/selShowPower")
    public List<Map<String, Object>> selShowPower(@RequestBody QueryShowPowerDto dto) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            if (dto.getTagNames().size() <= 0) {
                return mapList;
            }
            Map<Integer, String[]> map = new HashMap<>();
            List<ShowPowerResponse> showPowerResponseList = new ArrayList<>();
            List<PointCache> pointCaches;
            if (dto.getType() == -1) {
                QueryWrapper<StationFirstNetBaseView> queryWrapperStation = new QueryWrapper<>();
                queryWrapperStation.eq("status", true);
                List<StationFirstNetBaseView> stationFirstNetBaseViewList = stationFirstNetBaseViewService.list(queryWrapperStation);
                for (StationFirstNetBaseView systemInfo : stationFirstNetBaseViewList) {
                    map.put(systemInfo.getHeatSystemId(), dto.getTagNames().toArray(new String[dto.getTagNames().size()]));
                    showPowerResponseList.add(new ShowPowerResponse() {{
                        setHeatSystemId(systemInfo.getHeatSystemId());
                        setStationBranchArrayNumber(systemInfo.getNumber());
                        setStationName(systemInfo.getHeatTransferStationName());
                        setStationBranchName(systemInfo.getHeatSystemName());
                        setArea(systemInfo.getHeatSystemArea());
                        setStationLatitude(systemInfo.getLatitude());
                        setStationLongitude(systemInfo.getLongitude());
                    }});
                }
            } else if (dto.getType() == -2) {
                QueryWrapper<SourceFirstNetBaseView> queryWrapperSource = new QueryWrapper<>();
                List<SourceFirstNetBaseView> sourceFirstNetBaseViewList = sourceFirstNetBaseViewService.list(queryWrapperSource);
                for (SourceFirstNetBaseView systemInfo : sourceFirstNetBaseViewList) {
                    map.put(systemInfo.getHeatSystemId(), dto.getTagNames().toArray(new String[dto.getTagNames().size()]));
                    showPowerResponseList.add(new ShowPowerResponse() {{
                        setHeatSystemId(systemInfo.getHeatSystemId());
                        setStationBranchArrayNumber(String.valueOf(systemInfo.getNumber()));
                        setStationName(systemInfo.getHeatSourceName());
                        setStationBranchName(systemInfo.getHeatSystemName());
                        setArea(systemInfo.getHeatSourceArea());
                        setStationLatitude(systemInfo.getLatitude());
                        setStationLongitude(systemInfo.getLongitude());
                    }});
                }
            } else if (dto.getType() == -3) {
                QueryWrapper<StationFirstNetBaseView> queryWrapperStation = new QueryWrapper<>();
                queryWrapperStation.eq("status", true);
                List<ShowPowerResponse> stationSystemIds = stationFirstNetBaseViewService.list(queryWrapperStation).stream().map(s -> {
                    ShowPowerResponse showPowerResponse = new ShowPowerResponse() {{
                        setHeatSystemId(s.getHeatSystemId());
                        setStationBranchArrayNumber(String.valueOf(s.getNumber()));
                        setStationName(s.getHeatTransferStationName());
                        setStationBranchName(s.getHeatSystemName());
                        setArea(s.getHeatSystemArea());
                        setStationLatitude(s.getLatitude());
                        setStationLongitude(s.getLongitude());
                    }};
                    return showPowerResponse;
                }).collect(Collectors.toList());
                QueryWrapper<SourceFirstNetBaseView> queryWrapperSource = new QueryWrapper<>();
                List<ShowPowerResponse> sourceSystemIds = sourceFirstNetBaseViewService.list(queryWrapperSource).stream().map(s -> {
                    ShowPowerResponse showPowerResponse = new ShowPowerResponse() {{
                        setHeatSystemId(s.getHeatSystemId());
                        setStationBranchArrayNumber(String.valueOf(s.getNumber()));
                        setStationName(s.getHeatSourceName());
                        setStationBranchName(s.getHeatSystemName());
                        setArea(s.getHeatSourceArea());
                        setStationLatitude(s.getLatitude());
                        setStationLongitude(s.getLongitude());
                    }};
                    return showPowerResponse;
                }).collect(Collectors.toList());
                List<ShowPowerResponse> heatSystemIds = new ArrayList<>();
                heatSystemIds.addAll(stationSystemIds);
                heatSystemIds.addAll(sourceSystemIds);
                for (ShowPowerResponse heatSystemId : heatSystemIds) {
                    map.put(heatSystemId.getHeatSystemId(), dto.getTagNames().toArray(new String[dto.getTagNames().size()]));
                }
            } else if (dto.getType() == 1) {
                QueryWrapper<StationFirstNetBaseView> queryWrapperStation = new QueryWrapper<>();
                queryWrapperStation.eq("status", true);
                queryWrapperStation.in("heatSystemId", dto.getRelevanceIds());
                List<StationFirstNetBaseView> stationFirstNetBaseViewList = stationFirstNetBaseViewService.list(queryWrapperStation);
                for (StationFirstNetBaseView stationSystem : stationFirstNetBaseViewList) {
                    map.put(stationSystem.getHeatSystemId(), dto.getTagNames().toArray(new String[dto.getTagNames().size()]));
                    showPowerResponseList.add(new ShowPowerResponse() {{
                        setHeatSystemId(stationSystem.getHeatSystemId());
                        setStationBranchArrayNumber(stationSystem.getNumber());
                        setStationName(stationSystem.getHeatTransferStationName());
                        setStationBranchName(stationSystem.getHeatSystemName());
                        setArea(stationSystem.getHeatSystemArea());
                        setStationLatitude(stationSystem.getLatitude());
                        setStationLongitude(stationSystem.getLongitude());
                    }});
                }
            } else {
                QueryWrapper<SourceFirstNetBaseView> queryWrapperSource = new QueryWrapper<>();
                queryWrapperSource.in("heatSystemId", dto.getRelevanceIds());
                List<SourceFirstNetBaseView> sourceFirstNetBaseViewList = sourceFirstNetBaseViewService.list(queryWrapperSource);
                for (SourceFirstNetBaseView sourceSystem : sourceFirstNetBaseViewList) {
                    map.put(sourceSystem.getHeatSystemId(), dto.getTagNames().toArray(new String[dto.getTagNames().size()]));
                    showPowerResponseList.add(new ShowPowerResponse() {{
                        setHeatSystemId(sourceSystem.getHeatSystemId());
                        setStationBranchArrayNumber(String.valueOf(sourceSystem.getNumber()));
                        setStationName(sourceSystem.getHeatSourceName());
                        setStationBranchName(sourceSystem.getHeatSystemName());
                        setArea(sourceSystem.getHeatSourceArea());
                        setStationLatitude(sourceSystem.getLatitude());
                        setStationLongitude(sourceSystem.getLongitude());
                    }});
                }

            }
            pointCaches = redisCacheService.queryRealOnlyValue(map, TreeLevel.HeatSystem.level());
            log.info("实时数据查询-------查询缓存库耗时: {} ms", System.currentTimeMillis() - start);
            if (pointCaches.size() > 0) {
                Map<Integer, List<PointCache>> groupList = pointCaches.stream().collect(Collectors.groupingBy(s -> s.getRelevanceId()));
                groupList.forEach((key, value) -> {
                    Map<String, Object> mapResponse = new HashMap<>();
                    ShowPowerResponse showPowerResponse = showPowerResponseList.stream().filter(s -> Objects.equals(s.getHeatSystemId().toString(), key.toString())).findFirst().orElse(null);
                    if (showPowerResponse != null) {
                        //基础数据信息
                        mapResponse.put("relevanceId", key);
                        mapResponse.put("StationBranchNumber", showPowerResponse.getStationBranchArrayNumber());
                        mapResponse.put("StationName", showPowerResponse.getStationName());
                        mapResponse.put("StationBranchName", showPowerResponse.getStationBranchName());
                        mapResponse.put("Area", showPowerResponse.getArea());
                        //经纬度
                        mapResponse.put("StationLongitude", showPowerResponse.getStationLongitude());
                        mapResponse.put("StationLatitude", showPowerResponse.getStationLatitude());
                        //点位信息值
                        for (PointCache pointCache : value) {
                            mapResponse.put(pointCache.getPointName(), pointCache.getValue());
                        }
                        mapList.add(mapResponse);
                    }
                });
            } else {
                for (ShowPowerResponse showPowerResponse : showPowerResponseList) {
                    Map<String, Object> mapResponse = new HashMap<>();
                    mapResponse.put("relevanceId", showPowerResponse.getHeatSystemId());
                    mapResponse.put("StationBranchNumber", showPowerResponse.getStationBranchArrayNumber());
                    mapResponse.put("StationName", showPowerResponse.getStationName());
                    mapResponse.put("StationBranchName", showPowerResponse.getStationBranchName());
                    mapResponse.put("Area", showPowerResponse.getArea());
                    //经纬度
                    mapResponse.put("StationLongitude", showPowerResponse.getStationLongitude());
                    mapResponse.put("StationLatitude", showPowerResponse.getStationLatitude());
                    mapList.add(mapResponse);
                }
            }
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @ApiOperation("水压温度图")
    @PostMapping("/queryHydrostaticTempDiagram")
    public List<Map<String, Object>> queryHydrostaticTempDiagram(@RequestBody QueryHydrostaticTempDto dto) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            QueryWrapper<StationFirstNetBaseView> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("number",0).in("heatSystemId", dto.getRelevanceIds()).orderByAsc("heatStationSort");
            List<StationFirstNetBaseView> heatSystemIds = stationFirstNetBaseViewService.list(queryWrapper);
            Map map = new HashMap();
            for (StationFirstNetBaseView heatSystemId : heatSystemIds) {
                map.put(heatSystemId.getHeatSystemId(), dto.getPointsName().toArray(new String[dto.getPointsName().size()]));
            }
            List<PointCache> pointCaches = redisCacheService.queryRealOnlyValue(map, TreeLevel.HeatSystem.level());
            Map<Integer, List<PointCache>> groupList = pointCaches.stream().collect(Collectors.groupingBy(s -> s.getRelevanceId()));
            groupList.forEach((key, value) -> {
                Map<String, Object> mapResponse = new HashMap<>();
                StationFirstNetBaseView showPowerResponse = heatSystemIds.stream().filter(s -> Objects.equals(s.getHeatSystemId().toString(), key.toString())).findFirst().orElse(null);
                if (showPowerResponse != null) {
                    //基础数据信息
                    mapResponse.put("relevanceId", key);
                    mapResponse.put("stationName",showPowerResponse.getHeatTransferStationName());
                    mapResponse.put("systemName",showPowerResponse.getHeatSystemName());
                    mapResponse.put("altitude", showPowerResponse.getAltitude());
                    //点位信息值
                    for (PointCache pointCache : value) {
                        mapResponse.put(pointCache.getPointName(), pointCache.getValue());
                    }
                    mapList.add(mapResponse);
                }
            });
            return mapList;
        } catch (Exception e) {
            log.error("水压温度图接口内部错误！{}", e.getMessage());
            return mapList;
        }
    }
}
