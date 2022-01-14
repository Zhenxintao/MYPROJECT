package com.bmts.heating.middleground.history.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatAreaChangeHistory;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.db.service.HeatAreaChangeHistoryService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryAreaChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatAreaChangeResponse;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.QueryBaseHistoryResponse;
import com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.commons.utils.tdengine.InstrumentInfo;
import com.bmts.heating.commons.utils.tdengine.TdAggregateTableIndex;
import com.bmts.heating.commons.utils.tdengine.TdTableIndex;
import com.bmts.heating.compute.common.ConvertArea;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TdEngineHistoryDataImpl implements TdEngineHistoryData {

    @Autowired
    private HeatAreaChangeHistoryService heatAreaChangeHistoryService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private HistoryTdGrpcClient client;
    public static String[] BASE_INFO = {"relevanceId", "timeStrap"};
    public static String[] STATION_FIELDS = {"HM_HT", "WM_FT", "Ep"};
    public static String[] SOURCE_FIELDS = {"HeatSourceTotalHeat_MtrG", "HeatSourceFTSupply", "HeatSourceEp"};

    @Override
    public List<Map<String, String>> findHistoryData(QueryEsDto dto) {
        QueryBaseHistoryResponse response = client.queryTdEngineData(configQueryTdDto(dto));
        JSONArray array = response.getJsonData();
        List<Map<String, String>> list = new ArrayList<>();
        if (array == null || array.size() < 1) {
            return list;
        }
        try {
            queryHistoryTd(list, array, dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通信失败");
        } finally {
            ManagedChannel managedChannel = (ManagedChannel) response.getManagedChannel().get("managedChannel");
            managedChannel.shutdown();
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (dto.getIncludeTotal() != null && dto.getIncludeTotal()) {
            Map<String, String> map = new HashMap<>();
            map.put("total", String.valueOf(response.getTotal()));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findHistoryEnergyData(QueryEsDto dto) {
        try {
            QueryBaseHistoryResponse response = client.queryTdEngineData(configQueryTdDto(dto));
            JSONArray array = response.getJsonData();
            List<Map<String, Object>> list = new ArrayList<>();
            if (array == null || array.size() < 1) {
                return null;
            }
            try {
                queryEnergyHistoryTd(list, array, dto);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("通信失败");
            } finally {
                ManagedChannel managedChannel = (ManagedChannel) response.getManagedChannel().get("managedChannel");
                managedChannel.shutdown();
            }
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            if (dto.getIncludeTotal() != null && dto.getIncludeTotal()) {
                Map<String, Object> map = new HashMap<>();
                map.put("total", Integer.valueOf(response.getTotal()));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, String>> bucketEnergyData(QueryEsBucketDto dto) {
        try {
            List<Map<String, String>> list = queryAggregateData(dto);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            } else {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通信失败");
            return null;
        }

    }

    private void queryHistoryTd(List<Map<String, String>> maps, JSONArray array, QueryEsDto dto) {
        try {
            if (!Strings.isBlank(dto.getField()) && !Objects.equals(dto.getField(), "timeStrap")) {
                array = InstrumentInfo.sortJsonArray(array, dto.getField().toLowerCase(), dto.getSortType());
            }
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jsonObj = (JSONObject) array.get(i);
//                    jsonObj.put("timeStrap", LocalTimeUtils.transferStringDateToLong("yyyy-MM-dd HH:mm:ss", jsonObj.get("timeStrap").toString()));
                    List<String> pointList = new ArrayList<>(Arrays.asList(dto.getIncludeFields()));
                    pointList.removeAll(Arrays.asList(BASE_INFO));
                    for (String field : pointList) {
                        jsonObj.put(field, jsonObj.get(field.toLowerCase()));
                        jsonObj.remove(field.toLowerCase());
                    }
                    Map<String, String> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, String>>() {
                    });
                    maps.add(map);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void queryEnergyHistoryTd(List<Map<String, Object>> maps, JSONArray array, QueryEsDto dto) {
        Integer heatType;
        if (dto.getIncludeFields() != null && dto.getIncludeFields().length > 0) {
//            heatType = getHeatType(Arrays.asList(dto.getIncludeFields()));
            heatType = getHeatType(Arrays.asList(dto.getHeatSystemId()));
        } else {
            heatType = 1;
        }
        if (!Strings.isBlank(dto.getField()) && !Objects.equals(dto.getField(), "timeStrap")) {
            array = InstrumentInfo.sortJsonArray(array, dto.getField().toLowerCase(), dto.getSortType());
        }
        if (array != null && array.size() > 0) {
            QueryAreaChangeDto queryAreaChangeDto = new QueryAreaChangeDto();
            queryAreaChangeDto.setLevel(1);
            queryAreaChangeDto.setStartTime(LocalTimeUtils.longToLocalDateTime(dto.getStart()));
            queryAreaChangeDto.setEndTime(LocalTimeUtils.longToLocalDateTime(dto.getEnd()));
            queryAreaChangeDto.setRelevanceIds(dto.getHeatSystemId());
            List<HeatAreaChangeResponse> fullHeatAreaList = checkAreaInfo(dto.getHeatSystemId(), 1);
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = queryHeatAreaChange(queryAreaChangeDto, fullHeatAreaList);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject) array.get(i);
                HeatAreaChangeResponse areaChangeResponse = heatAreaChangeResponseList.stream().filter(s -> Objects.equals(s.getRelevanceId().toString(), jsonObj.get("relevanceId").toString())).findFirst().orElse(null);
                BigDecimal area;
                if (areaChangeResponse != null) {
                    area = areaChangeResponse.getAreaValue();
                } else {
                    area = new BigDecimal(0.00);
                }
                isHeatTypeEnergy(jsonObj, heatType, dto, area.doubleValue());
                Map<String, Object> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, Object>>() {
                });
                maps.add(map);
            }
        }
    }

    private void isHeatTypeEnergy(JSONObject jsonObj, Integer heatType, QueryEsDto dto, double area) {
        if (dto.getSourceType() == HistorySourceType.ENERGY_CONVERGE) {
            if (heatType == 1) {
                removeStationNaNObj(jsonObj);
                jsonObj.put("area", area);
//                jsonObj.put("timeStrap", getTimeStrapLong(jsonObj.get("timeStrap").toString()));
                jsonObj.put("WM_FT", resultListMap(jsonObj.get("wm_ft"), divide(new BigDecimal(jsonObj.get("wm_ft").toString()).doubleValue(), area)));
                jsonObj.put("Ep", resultListMap(jsonObj.get("ep"), divide(new BigDecimal(jsonObj.get("ep").toString()).doubleValue(), area)));
                jsonObj.put("HM_HT", resultListMap(jsonObj.get("hm_ht"), divide(new BigDecimal(jsonObj.get("hm_ht").toString()).doubleValue(), area)));
                jsonObj.remove("wm_ft");
                jsonObj.remove("ep");
                jsonObj.remove("hm_ht");
                jsonObj.remove("createtime");
                jsonObj.remove("level");
            } else {
                removeSourceNaNObj(jsonObj);
                jsonObj.put("area", area);
//                jsonObj.put("timeStrap", getTimeStrapLong(jsonObj.get("timeStrap").toString()));
                jsonObj.put("HeatSourceFTSupply", resultListMap(jsonObj.get("heatsourceftsupply"), divide(new BigDecimal(jsonObj.get("heatsourceftsupply").toString()).doubleValue(), area)));
                jsonObj.put("HeatSourceEp", resultListMap(jsonObj.get("heatsourceep"), divide(new BigDecimal(jsonObj.get("heatsourceep").toString()).doubleValue(), area)));
                jsonObj.put("HeatSourceTotalHeat_MtrG", resultListMap(jsonObj.get("heatsourcetotalheat_mtrg"), divide(new BigDecimal(jsonObj.get("heatsourcetotalheat_mtrg").toString()).doubleValue(), area)));
                jsonObj.remove("heatsourceftsupply");
                jsonObj.remove("heatsourceep");
                jsonObj.remove("heatsourcetotalheat_mtrg");
                jsonObj.remove("createtime");
                jsonObj.remove("level");
            }
        } else {
            jsonObj.put("area", area);
//            jsonObj.put("timeStrap", getTimeStrapLong(jsonObj.get("timeStrap").toString()));
            for (String field : dto.getIncludeFields()) {
                jsonObj.put(field, jsonObj.get(field.toLowerCase()));
                jsonObj.remove(field.toLowerCase());
            }
            jsonObj.remove("createtime");
            jsonObj.remove("level");
        }
    }

    private Map<String, Object> resultListMap(Object consumptionValue, Object unitConsumptionValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("consumption", consumptionValue);
        map.put("unitConsumption", unitConsumptionValue);
        return map;
    }

    private void removeStationNaNObj(JSONObject jsonObj) {
        if (jsonObj.get("wm_ft") == null) {
            log.error("TD历史查询能耗数据,wm_ft点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("wm_ft", 0);
        }
        if (jsonObj.get("ep") == null) {
            log.error("TD历史查询能耗数据,ep点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("ep", 0);
        }
        if (jsonObj.get("hm_ht") == null) {
            log.error("TD历史查询能耗数据,hm_ht点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("hm_ht", 0);
        }
    }

    private void removeSourceNaNObj(JSONObject jsonObj) {
        if (jsonObj.get("heatsourceftsupply") == null) {
            log.error("TD历史查询能耗数据,heatsourceftsupply点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("heatsourceftsupply", 0);
        }
        if (jsonObj.get("heatsourceep") == null) {
            log.error("TD历史查询能耗数据,heatsourceep点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("heatsourceep", 0);
        }
        if (jsonObj.get("heatsourcetotalheat_mtrg") == null) {
            log.error("TD历史查询能耗数据,heatsourcetotalheat_mtrg点位值为NaN————系统Id为{},查询返回时间为{}",jsonObj.get("relevanceId").toString(),jsonObj.get("timeStrap").toString());
            jsonObj.put("heatsourcetotalheat_mtrg", 0);
        }
    }

    private long getTimeStrapLong(String date) {
        return LocalTimeUtils.transferStringDateToLong("yyyy-MM-dd HH:mm:ss", date);
    }

    private Integer getHeatType(List<Integer> ids) {
        //        Integer count = tdPointConfigService.list(Wrappers.<TdPointConfig>lambdaQuery().eq(TdPointConfig::getHeatType, 2).in(TdPointConfig::getPointColumnName, includeFields)).size();
       QueryWrapper<SourceFirstNetBaseView> queryWrapper = new QueryWrapper<>();
       queryWrapper.in("heatSystemId",ids);
        List<SourceFirstNetBaseView> list = sourceFirstNetBaseViewService.list(queryWrapper);
//        includeFields.stream().filter(s->Objects.equals(s,"WM_FT")||Objects.equals(s,"Ep")||Objects.equals(s,"HM_HT")).collect(Collectors.toList()).size();
        Integer heatType = list.size() > 0 ? 2 : 1;
        return heatType;
    }

    private QueryTdDto configQueryTdDto(QueryEsDto dto) {
        try {
            QueryTdDto queryTdDto = new QueryTdDto();
            queryTdDto.setStartTime(dto.getStart());
            queryTdDto.setEndTime(dto.getEnd());
            queryTdDto.setGroupId(Arrays.asList(dto.getHeatSystemId()));
            Integer heatType;
            if (dto.getIncludeFields() != null && dto.getIncludeFields().length > 0) {
                List<String> pointList = new ArrayList<>(Arrays.asList(dto.getIncludeFields()));
                List<String> removeBase = (Arrays.asList(BASE_INFO));
                pointList.removeAll(removeBase);
//                heatType = getHeatType(pointList);
                heatType = getHeatType(Arrays.asList(dto.getHeatSystemId()));
                if (dto.getSourceType() == HistorySourceType.ENERGY_CONVERGE) {
                    if (heatType == 1) {
                        List<String> removeStationInfo = Arrays.asList(STATION_FIELDS);
                        pointList.removeAll(removeStationInfo);
                    } else {
                        List<String> removeSourceInfo = Arrays.asList(SOURCE_FIELDS);
                        pointList.removeAll(removeSourceInfo);
                    }
                }
                queryTdDto.setPoints(pointList);
            } else {
                heatType = 1;
            }
            tdHistoryEnumQueryTypeTd(dto.getSourceType(), queryTdDto, heatType);
            tdHistoryEnumOriginalTd(dto.getDocument(), queryTdDto);
            tdHistorySortTypeTd(dto.getSortType(), queryTdDto);
            tdHistoryPageTd(dto.getCurrentPage(), dto.getSize(), queryTdDto);
            return queryTdDto;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }

    private List<Map<String, String>> queryAggregateData(QueryEsBucketDto dto) {
        List<Map<String, String>> mapList = new ArrayList<>();
//        Integer heatType = getHeatType(Arrays.asList(dto.getIncludeFields()));
        Integer heatType = getHeatType(Arrays.asList(dto.getHeatSystemId()));
        Integer level;
        final List<Integer> searchIds;
        final List<HeatAreaChangeResponse> fullHeatAreaList;
        if (heatType == 1) {
            level = 1;
            fullHeatAreaList = checkAreaInfo(dto.getHeatSystemId(), level);
            searchIds = Arrays.asList(dto.getHeatSystemId());
        } else {
            level = 4;
            fullHeatAreaList = checkAreaInfo(dto.getHeatSystemId(), level);
            searchIds = fullHeatAreaList.stream().map(s -> s.getRelevanceId()).collect(Collectors.toList());
        }
        dto.getTimeRanges().forEach(timeRange -> {
            QueryPointsOuterClass.Response response = client.queryTdAggregate(configQueryAggregateTdDto(timeRange.getStart(), timeRange.getEnd(), dto, heatType));
            JSONArray array = JSON.parseArray(response.getJsonData());
            QueryAreaChangeDto queryAreaChangeDto = new QueryAreaChangeDto();
            queryAreaChangeDto.setLevel(level);
            queryAreaChangeDto.setStartTime(LocalTimeUtils.longToLocalDateTime(timeRange.getStart()));
            queryAreaChangeDto.setEndTime(LocalTimeUtils.longToLocalDateTime(timeRange.getEnd()));
            queryAreaChangeDto.setRelevanceIds(searchIds.toArray(new Integer[0]));
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = queryHeatAreaChange(queryAreaChangeDto, fullHeatAreaList);
            if (dto.getIndex() == 1) {
                singleBuckListInfo(array, mapList, heatAreaChangeResponseList, dto.getIncludeFields(), timeRange.getIndex());
            } else {
                double area;
                if (heatAreaChangeResponseList != null) {
                    area = heatAreaChangeResponseList.stream().mapToDouble(s -> s.getAreaValue().doubleValue()).sum();
                } else {
                    area = 0;
                }
                groupBuckListInfo(array, mapList, area, dto.getIncludeFields(), timeRange.getIndex());
            }
        });
        return mapList;
    }

    public void singleBuckListInfo(JSONArray array, List<Map<String, String>> mapList, List<HeatAreaChangeResponse> heatAreaChangeResponseList, String[] includeFields, String index) {
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject) array.get(i);
                HeatAreaChangeResponse areaChangeResponse = heatAreaChangeResponseList.stream().filter(s -> Objects.equals(s.getRelevanceId().toString(), jsonObj.get("relevanceId").toString())).findFirst().orElse(null);
                BigDecimal area;
                if (areaChangeResponse != null) {
                    area = areaChangeResponse.getAreaValue();
                } else {
                    area = new BigDecimal(0);
                }
                jsonObj.put("area", area);
                jsonObj.put("index", index);
//                jsonObj.put("timeStrap", getTimeStrapLong(jsonObj.get("ts").toString()));
                jsonObj.put("timeStrap", jsonObj.get("ts"));
                for (String field : includeFields) {
                    double value = new BigDecimal(jsonObj.get(field.toLowerCase()).toString()).doubleValue();
                    jsonObj.put(field, value);
                    jsonObj.put(field + "unitStandard", divide(value, area.doubleValue()));
                    jsonObj.remove(jsonObj.get(field.toLowerCase()));
                }
                Map<String, String> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, String>>() {
                });
                mapList.add(map);
            }
        }
    }

    public void groupBuckListInfo(JSONArray array, List<Map<String, String>> mapList, double area, String[] includeFields, String index) {
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject) array.get(i);
                jsonObj.put("area", area);
                jsonObj.put("index", index);
//                jsonObj.put("timeStrap", getTimeStrapLong(jsonObj.get("ts").toString()));
                jsonObj.put("timeStrap", jsonObj.get("ts"));
                for (String field : includeFields) {
                    double value = new BigDecimal(jsonObj.get(field.toLowerCase()).toString()).doubleValue();
                    jsonObj.put(field, value);
                    jsonObj.put(field + "unitStandard", divide(value, area));
                    jsonObj.remove(field.toLowerCase());
                }
                jsonObj.put("level", 1);
                Map<String, String> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, String>>() {
                });
                mapList.add(map);
            }
        }
    }

    public List<HeatAreaChangeResponse> queryHeatAreaChange(QueryAreaChangeDto dto, List<HeatAreaChangeResponse> fullHeatAreaList) {
        try {
            if (dto.getRelevanceIds().length <= 0) {
                return null;
            }
            QueryWrapper<HeatAreaChangeHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("createTime", dto.getStartTime());
            queryWrapper.le("createTime", dto.getEndTime());
            queryWrapper.eq("level", dto.getLevel());
            queryWrapper.orderByAsc("createTime");
            queryWrapper.in("relevanceId", dto.getRelevanceIds());
            List<HeatAreaChangeHistory> heatAreaChangeHistoryList = heatAreaChangeHistoryService.list(queryWrapper);
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = new ArrayList<>();
            if (heatAreaChangeHistoryList.size() > 0) {
                for (Integer id : dto.getRelevanceIds()) {
                    Map<LocalDateTime, BigDecimal> areaMap = new HashMap<>();
                    List<HeatAreaChangeHistory> groupAreaChangeList = heatAreaChangeHistoryList.stream().filter(s -> s.getRelevanceId() == id).collect(Collectors.toList());
                    HeatAreaChangeHistory history = groupAreaChangeList.stream().findFirst().orElse(null);
                    if (history.getCreateTime().isEqual(dto.getStartTime())) {
                        areaMap.put(dto.getStartTime(), history.getNewValue());
                    } else if (history.getCreateTime().isBefore(dto.getStartTime())) {
                        areaMap.put(dto.getStartTime(), history.getNewValue());
                    } else {
                        areaMap.put(dto.getStartTime(), history.getOldValue());
                    }
                    for (HeatAreaChangeHistory h : groupAreaChangeList.stream().filter(s -> s.getId() != history.getId()).collect(Collectors.toList())) {
                        areaMap.put(h.getCreateTime(), h.getNewValue());
                    }
                    heatAreaChangeResponseList.add(new HeatAreaChangeResponse() {{
                        setRelevanceId(id);
                        setAreaValue(ConvertArea.calc(areaMap, dto.getStartTime(), dto.getEndTime()));
                    }});
                }
            }
            for (HeatAreaChangeResponse heatArea : fullHeatAreaList) {
                HeatAreaChangeResponse changeResponse = heatAreaChangeResponseList.stream().filter(s -> Objects.equals(s.getRelevanceId(), heatArea.getRelevanceId())).findFirst().orElse(null);
                if (changeResponse != null) {
                    heatArea.setAreaValue(changeResponse.getAreaValue());
                }
            }
            return fullHeatAreaList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<HeatAreaChangeResponse> checkAreaInfo(Integer[] ids, Integer level) {
        if (ids.length <= 0) {
            return null;
        }
        List<HeatAreaChangeResponse> list = new ArrayList<>();
        //能耗系统及热网信息
        if (level == 1) {
            QueryWrapper<StationFirstNetBaseView> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("heatSystemId", ids);
            List<StationFirstNetBaseView> stationFirstNetBaseViewList = stationFirstNetBaseViewService.list(queryWrapper);
            for (StationFirstNetBaseView s : stationFirstNetBaseViewList) {
                HeatAreaChangeResponse history = new HeatAreaChangeResponse();
                history.setAreaValue(s.getHeatStationArea());
                history.setRelevanceId(s.getHeatSystemId());
                list.add(history);
            }
            return list;
        }
        //能耗热源信息
        if (level == 4) {
            List<SourceFirstNetBaseView> sourceFirstNetBaseViewList = sourceFirstNetBaseViewService.list(Wrappers.<SourceFirstNetBaseView>lambdaQuery().in(SourceFirstNetBaseView::getHeatSystemId, ids));
            for (SourceFirstNetBaseView s : sourceFirstNetBaseViewList) {
                HeatAreaChangeResponse history = new HeatAreaChangeResponse();
                history.setAreaValue(s.getHeatSourceArea());
                history.setRelevanceId(s.getHeatSourceId());
                list.add(history);
            }
            return list;
        }
        return null;
    }

    /**
     * 计算折标热单耗
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return Double
     */
    private Double divide(double dividend, double divisor) {
        if (!Double.isInfinite(dividend) && !Double.isInfinite(divisor)) {
            Double aDouble = ParseUtil.parseDouble(dividend) / ParseUtil.parseDouble(divisor);
            return ParseUtil.parseDouble(aDouble);
        }
        return 0.0;
    }

    private QueryAggregateTdDto configQueryAggregateTdDto(long startTime, long endTime, QueryEsBucketDto dto, Integer heatType) {
        try {
            QueryAggregateTdDto queryTdDto = new QueryAggregateTdDto();
            queryTdDto.setStartTime(startTime);
            queryTdDto.setEndTime(endTime);
            queryTdDto.setGroupId(Arrays.asList(dto.getHeatSystemId()));
//            tdAggregateHistoryPageTd(dto.getCurrentPage(), dto.getSize(), queryTdDto);
            List<AggregatePoint> aggregatePoints = new ArrayList<>();
            for (String name : dto.getIncludeFields()) {
                AggregatePoint aggregatePoint = new AggregatePoint();
                aggregatePoint.setPointName(name);
                aggregatePoint.setAggregateType(AggregateType.sum);
                aggregatePoints.add(aggregatePoint);
            }
            queryTdDto.setPoints(aggregatePoints);
            if (dto.getIndex() == 1) {
                queryTdDto.setGroupType(AggregateGroupType.single);
            } else {
                queryTdDto.setGroupType(AggregateGroupType.group);
            }
            tdAggregateEnumOriginalTd(dto.getDocument(), queryTdDto, heatType);
            return queryTdDto;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }

    private void tdHistoryEnumOriginalTd(HistoryDocument document, QueryTdDto queryTdDto) {
        switch (document) {
            case REAL_DATA:
                queryTdDto.setOriginalTd(OriginalTd.minute);
                break;
            case HOUR:
            case HOUR_AVG:
                queryTdDto.setOriginalTd(OriginalTd.hour);
                break;
            case DAY:
                queryTdDto.setOriginalTd(OriginalTd.day);
                break;
        }
    }

    private void tdAggregateEnumOriginalTd(HistoryDocument document, QueryAggregateTdDto queryTdDto, Integer heatType) {
        switch (document) {
            case HOUR:
                queryTdDto.setOriginalTd(OriginalTd.day);
                if (heatType == 1) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_HOUR.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_HOUR.getIndex());
                }
                break;
            case DAY:
                queryTdDto.setOriginalTd(OriginalTd.day);
                if (heatType == 1) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_DAY.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_DAY.getIndex());
                }
                break;
        }
    }

    private void tdHistoryEnumQueryTypeTd(HistorySourceType sourceType, QueryTdDto queryTdDto, Integer heatType) {
        switch (sourceType) {
            case FIRST:
                queryTdDto.setQueryTypeTd(QueryTypeTd.history);
                if (heatType == 1) {
                    queryTdDto.setTableName(TdTableIndex.STATION_HISTORY.getIndex());
                } else {
                    queryTdDto.setTableName(TdTableIndex.SOURCE_HISTORY.getIndex());
                }
                break;
            case ENERGY_CONVERGE:
                queryTdDto.setQueryTypeTd(QueryTypeTd.energy);
                if (heatType == 1) {
                    queryTdDto.setTableName(TdTableIndex.STATION_ENERGY.getIndex());
                } else {
                    queryTdDto.setTableName(TdTableIndex.SOURCE_ENERGY.getIndex());
                }
                break;
        }
    }

    private void tdHistorySortTypeTd(Boolean sortType, QueryTdDto queryTdDto) {
        if (sortType == null) {
            queryTdDto.setOrder("ASC");
            return;
        }
        if (sortType) {
            queryTdDto.setOrder("ASC");
        } else {
            queryTdDto.setOrder("DESC");
        }
    }

    private void tdHistoryPageTd(Integer currentPage, Integer size, QueryTdDto queryTdDto) {
        if (currentPage == null || size == null) {
            queryTdDto.setLimit(null);
            queryTdDto.setOffset(null);
        } else {
            queryTdDto.setLimit(size);
            if (currentPage > 1) {
                queryTdDto.setOffset(size * (currentPage - 1));
            } else {
                queryTdDto.setOffset(0);
            }
        }
    }

    private void tdAggregateHistoryPageTd(Integer currentPage, Integer size, QueryAggregateTdDto queryTdDto) {
        if (currentPage == null || size == null) {
            queryTdDto.setLimit(null);
            queryTdDto.setOffset(null);
        } else {
            queryTdDto.setLimit(size);
            if (currentPage > 1) {
                queryTdDto.setOffset(size * (currentPage - 1));
            } else {
                queryTdDto.setOffset(0);
            }
        }
    }

    private HashMap<String, String> strToMap(String str) {
        Gson gson = new Gson();
        HashMap<String, String> res = new HashMap<>();
        try {
            res = gson.fromJson(str, new TypeToken<HashMap<String, String>>() {
            }.getType());
        } catch (Exception e) {
            log.info(str);
            e.printStackTrace();
        }
        return res;
    }

    private HashMap strToMapE(String str) {
        HashMap map = new HashMap<>(16);
        try {
            map = JSON.parseObject(str, HashMap.class);
            log.info(map.toString());
        } catch (Exception e) {
            log.error(str);
            e.printStackTrace();
        }
        return map;
    }


//	public static void main(String[] args) {
//		String a = "{area=1.0, level=1, HeatSourceTotalHeat_MtrGunitStandard=42279.875, index=2021-05-10, HeatSourceTotalHeat_MtrG=42279.875}";
//		String b = "{area=Infinity, HeatSourceFTSupply=11062.5, HeatSourceFTSupplyunitStandard=11062.5, HeatSourceTotalHeat_MtrGunitStandard=11065.0, index=表格, relevanceId=820, HeatSourceTotalHeat_MtrG=11065.0, HeatSourceEp=0.0, HeatSourceEpunitStandard=0.0}";
//
//		Gson gson = new Gson();
//		Object o = gson.fromJson(b, new TypeToken<Map<String, String>>() {
//		}.getType());
//		System.out.println(o);
//		Object p = gson.fromJson(a, new TypeToken<Map<String, String>>() {
//		}.getType());
//		System.out.println(p);
//	}
}
