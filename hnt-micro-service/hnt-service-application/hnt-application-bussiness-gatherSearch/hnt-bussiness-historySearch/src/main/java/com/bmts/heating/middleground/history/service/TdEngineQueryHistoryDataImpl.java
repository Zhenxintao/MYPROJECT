package com.bmts.heating.middleground.history.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryAreaChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatAreaChangeResponse;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.*;
import com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.tdengine.TdAggregateTableIndex;
import com.bmts.heating.commons.utils.tdengine.TdTableIndex;
import com.bmts.heating.compute.common.ConvertArea;
import com.bmts.heating.compute.hitory.energy.ComprehensiveEnergy;
import com.bmts.heating.compute.hitory.energy.UnitStandardCompute;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TdEngineQueryHistoryDataImpl implements TdEngineQueryHistoryData {

    @Autowired
    private HeatAreaChangeHistoryService heatAreaChangeHistoryService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private HeatTransferStationService heatTransferStationService;
    @Autowired
    private WebPageConfigService webPageConfigService;
    @Autowired
    private WeatherHourService weatherHourService;
    @Autowired
    private EnergyUnitStandardConfigService energyUnitStandardConfigService;
    @Autowired
    private PointStandardService pointStandardService;
    @Autowired
    private HistoryTdGrpcClient client;

    @Override
    public List<Map<String, String>> queryHistoryData(QueryTdDto dto) {
        dto.setPoints(matchTd(dto.getPoints()));
        QueryBaseHistoryResponse response = client.queryTdEngineData(dto);
        JSONArray array = response.getJsonData();
        List<Map<String, String>> list = new ArrayList<>();
        if (array == null || array.size() < 1) {
            return list;
        }
        try {
            queryHistoryTd(list, array, dto.getPoints());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("????????????");
        } finally {
            ManagedChannel managedChannel = (ManagedChannel) response.getManagedChannel().get("managedChannel");
            managedChannel.shutdown();
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("total", String.valueOf(response.getTotal()));
        list.add(map);
        return list;
    }


    @Override
    public List<Map<String, String>> queryHistoryEnergyData(QueryAggregateTdDto dto, Integer level) {
        try {
            List<Map<String, String>> list = queryAggregateData(dto, level);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("????????????");
            List<Map<String, String>> list = new ArrayList<>();
            return list;
        }

    }

    /**
     * Td??????????????????
     */
    @Override
    public HistoryBaseDataResponse queryHistoryBase(QueryBaseHistoryDto dto) {
        //??????td????????????????????????
        dto.setPoints(matchTd(dto.getPoints()));
        QueryTdDto queryTdDto = new QueryTdDto();
        setQueryTdDto(queryTdDto, dto);
        QueryBaseHistoryResponse response = client.queryTdEngineData(queryTdDto);
        JSONArray array = response.getJsonData();
        HistoryBaseDataResponse historyBaseDataResponse = new HistoryBaseDataResponse();
        if (array == null || array.size() < 1) {
            return historyBaseDataResponse;
        }
        try {
            getHistoryBaseResponse(historyBaseDataResponse, array, dto.getPoints());
        } catch (Exception e) {
            log.error("Td???????????????????????????queryHistoryBase??????????????????----{}", e.getMessage());
        } finally {
            ManagedChannel managedChannel = (ManagedChannel) response.getManagedChannel().get("managedChannel");
            managedChannel.shutdown();
        }
        historyBaseDataResponse.setTotal(Integer.parseInt(String.valueOf(response.getTotal())));
        return historyBaseDataResponse;
    }

    /**
     * Td??????????????????????????????
     */
    @Override
    public HistoryEnergyDataResponse queryHistoryEnergy(QueryBaseHistoryDto dto) {
        //??????td????????????????????????
//        dto.setPoints(matchTd(dto.getPoints()));
        QueryTdDto queryTdDto = new QueryTdDto();
        setQueryTdDto(queryTdDto, dto);
        QueryBaseHistoryResponse response = client.queryTdEngineData(queryTdDto);
        JSONArray array = response.getJsonData();
        HistoryEnergyDataResponse historyEnergyDataResponse = new HistoryEnergyDataResponse();
        if (array == null || array.size() < 1) {
            return historyEnergyDataResponse;
        }
        try {
            //????????????????????????
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = getRealCalculateArea(dto.getHeatType(), dto.getStartTime(), dto.getEndTime(), dto.getRelevanceIds());
            if (dto.getHeatType() == HeatTypeEnum.source) {
                updateSourceAreaInfo(heatAreaChangeResponseList);
            }
            //???????????????????????????
            GetNiggerHeadCalculateInfo calculateInfo = getNiggerHeadCalculateInfo(dto.getStartTime(), dto.getEndTime());
            //????????????????????????
            getHistoryEnergyResponse(historyEnergyDataResponse, array, heatAreaChangeResponseList, calculateInfo, dto.getHeatType());
        } catch (Exception e) {
            log.error("Td???????????????????????????????????????queryHistoryEnergy??????????????????----{}", e.getMessage());
        } finally {
            ManagedChannel managedChannel = (ManagedChannel) response.getManagedChannel().get("managedChannel");
            managedChannel.shutdown();
        }
        historyEnergyDataResponse.setTotal(Integer.parseInt(String.valueOf(response.getTotal())));
        return historyEnergyDataResponse;
    }

    /**
     * Td????????????????????????
     */
    @Override
    public HistoryBaseDataResponse queryHistoryAggregate(QueryAggregateHistoryDto dto) {
        //??????td????????????????????????
        dto.setPoints(matchAggregateTd(dto.getPoints()));
        QueryAggregateTdDto queryAggregateTdDto = new QueryAggregateTdDto();
        setQueryAggregateTdDto(queryAggregateTdDto, dto);
        QueryPointsOuterClass.Response response = client.queryTdAggregate(queryAggregateTdDto);
        JSONArray array = JSON.parseArray(response.getJsonData());
        HistoryBaseDataResponse historyBaseDataResponse = new HistoryBaseDataResponse();
        if (array == null || array.size() < 1) {
            return historyBaseDataResponse;
        }
        try {
            List<String> pointNames = dto.getPoints().stream().map(s -> s.getPointName()).collect(Collectors.toList());
            getHistoryBaseResponse(historyBaseDataResponse, array, pointNames);
        } catch (Exception e) {
            log.error("Td???????????????????????????queryHistoryAggregate??????????????????----{}", e.getMessage());
        }
        historyBaseDataResponse.setTotal(Integer.parseInt(String.valueOf(response.getTotal())));
        return historyBaseDataResponse;
    }

    /**
     * Td??????????????????????????????
     */
    @Override
    public HistoryEnergyDataResponse queryHistoryAggregateEnergy(QueryAggregateHistoryDto dto) {
        //??????td????????????????????????
//        dto.setPoints(matchAggregateTd(dto.getPoints()));
        QueryAggregateTdDto queryAggregateTdDto = new QueryAggregateTdDto();
        setQueryAggregateTdDto(queryAggregateTdDto, dto);
        QueryPointsOuterClass.Response response = client.queryTdAggregate(queryAggregateTdDto);
        JSONArray array = JSON.parseArray(response.getJsonData());
        HistoryEnergyDataResponse historyEnergyDataResponse = new HistoryEnergyDataResponse();
        if (array == null || array.size() < 1) {
            return historyEnergyDataResponse;
        }
        try {
            //????????????????????????
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = getRealCalculateArea(dto.getHeatType(), dto.getStartTime(), dto.getEndTime(), dto.getRelevanceIds());
            if (dto.getHeatType() == HeatTypeEnum.source) {
                updateSourceAreaInfo(heatAreaChangeResponseList);
            }
            //???????????????????????????
            GetNiggerHeadCalculateInfo calculateInfo = getNiggerHeadCalculateInfo(dto.getStartTime(), dto.getEndTime());
            //????????????????????????
            getHistoryEnergyResponse(historyEnergyDataResponse, array, heatAreaChangeResponseList, calculateInfo, dto.getHeatType());
        } catch (Exception e) {
            log.error("Td???????????????????????????????????????queryHistoryAggregateEnergy??????????????????----{}", e.getMessage());
        }
        historyEnergyDataResponse.setTotal(Integer.parseInt(String.valueOf(response.getTotal())));
        return historyEnergyDataResponse;


    }

    /**
     * ????????????Td????????????--??????gRPC??????????????????QueryTdDto
     */
    private void setQueryTdDto(QueryTdDto queryTdDto, QueryBaseHistoryDto queryBaseHistoryDto) {
        try {
            if (queryBaseHistoryDto.getHistoryType() == HistoryTypeEnum.energy_hour || queryBaseHistoryDto.getHistoryType() == HistoryTypeEnum.energy_day) {
                GetEnergyPointConfig energyPointConfig = getEnergyPointConfig();
                List<String> energyPoints = new ArrayList<>();
                if (queryBaseHistoryDto.getHeatType() == HeatTypeEnum.station) {
                    energyPoints.add(energyPointConfig.getStationWaterPoint());
                    energyPoints.add(energyPointConfig.getStationElectricityPoint());
                    energyPoints.add(energyPointConfig.getStationHeatPoint());
                } else {
                    energyPoints.add(energyPointConfig.getSourceWaterPoint());
                    energyPoints.add(energyPointConfig.getSourceElectricityPoint());
                    energyPoints.add(energyPointConfig.getSourceHeatPoint());
                }
                queryTdDto.setPoints(energyPoints);
            } else {
                queryTdDto.setPoints(queryBaseHistoryDto.getPoints());
            }
            queryTdDto.setStartTime(queryBaseHistoryDto.getStartTime());
            queryTdDto.setEndTime(queryBaseHistoryDto.getEndTime());
            queryTdDto.setGroupId(queryBaseHistoryDto.getRelevanceIds());
            configTableNameAndQueryType(queryBaseHistoryDto.getHistoryType(), queryTdDto, queryBaseHistoryDto.getHeatType());
            configHistoryPage(queryBaseHistoryDto.getCurrentPage(), queryBaseHistoryDto.getSize(), queryTdDto);
            configHistorySort(queryBaseHistoryDto.getSortType(), queryTdDto);
        } catch (Exception e) {
            log.error("??????gRPC????????????????????????QueryTdDto????????????------{}", e.getMessage());
        }
    }

    /**
     * ????????????Td??????????????????--??????gRPC??????????????????QueryTdDto
     */
    private void setQueryAggregateTdDto(QueryAggregateTdDto queryAggregateTdDto, QueryAggregateHistoryDto queryAggregateHistoryDto) {
        try {
            queryAggregateTdDto.setStartTime(queryAggregateHistoryDto.getStartTime());
            queryAggregateTdDto.setEndTime(queryAggregateHistoryDto.getEndTime());
            queryAggregateTdDto.setPoints(queryAggregateHistoryDto.getPoints());
            queryAggregateTdDto.setGroupId(queryAggregateHistoryDto.getRelevanceIds());
            configAggregateTimeType(queryAggregateHistoryDto.getAggregateTimeType(), queryAggregateTdDto);
            configAggregateDataType(queryAggregateHistoryDto.getAggregateDataType(), queryAggregateTdDto);
            configAggregateTableNameAndQueryType(queryAggregateHistoryDto.getHistoryType(), queryAggregateTdDto, queryAggregateHistoryDto.getHeatType());
            configHistoryAggregatePage(queryAggregateHistoryDto.getCurrentPage(), queryAggregateHistoryDto.getSize(), queryAggregateTdDto);
            configHistoryAggregateSort(queryAggregateHistoryDto.getSortType(), queryAggregateTdDto);
        } catch (Exception e) {
            log.error("??????gRPC??????????????????????????????QueryAggregateTdDto????????????------{}", e.getMessage());
        }
    }

    /**
     * ????????????Td????????????--???????????????????????????????????????????????????or?????????????????????
     */
    private void configTableNameAndQueryType(HistoryTypeEnum historyType, QueryTdDto queryTdDto, HeatTypeEnum heatType) {
        switch (historyType) {
            case history_minute:
                queryTdDto.setQueryTypeTd(QueryTypeTd.history);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_MINUTE.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_MINUTE.getIndex());
                }
                break;
            case history_hour:
                queryTdDto.setQueryTypeTd(QueryTypeTd.history);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_HOUR.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_HOUR.getIndex());
                }
                break;
            case energy_hour:
                queryTdDto.setQueryTypeTd(QueryTypeTd.energy);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_HOUR.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_HOUR.getIndex());
                }
                break;
            case energy_day:
                queryTdDto.setQueryTypeTd(QueryTypeTd.energy);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_DAY.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_DAY.getIndex());
                }
                break;
            case history_hour_avg:
                queryTdDto.setQueryTypeTd(QueryTypeTd.history);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_HOUR_AVG.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_HOUR_AVG.getIndex());
                }
                break;
            case history_day_avg:
                queryTdDto.setQueryTypeTd(QueryTypeTd.history);
                if (heatType == HeatTypeEnum.station) {
                    queryTdDto.setTableName(TdAggregateTableIndex.STATION_DAY_AVG.getIndex());
                } else {
                    queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_DAY_AVG.getIndex());
                }
                break;
        }
    }

    /**
     * ????????????Td??????????????????--???????????????????????????????????????????????????or?????????????????????
     */
    private void configAggregateTableNameAndQueryType(HistoryTypeEnum historyType, QueryAggregateTdDto queryAggregateTdDto, HeatTypeEnum heatType) {
        switch (historyType) {
            case history_minute:
                if (heatType == HeatTypeEnum.station) {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.STATION_MINUTE.getIndex());
                } else {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.SOURCE_MINUTE.getIndex());
                }
                break;
            case history_hour:
                if (heatType == HeatTypeEnum.station) {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.STATION_HOUR.getIndex());
                } else {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.SOURCE_HOUR.getIndex());
                }
                break;
            case energy_hour:
                if (heatType == HeatTypeEnum.station) {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_HOUR.getIndex());
                } else {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_HOUR.getIndex());
                }
                break;
            case energy_day:
                if (heatType == HeatTypeEnum.station) {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.STATION_ENERGY_DAY.getIndex());
                } else {
                    queryAggregateTdDto.setTableName(TdAggregateTableIndex.SOURCE_ENERGY_DAY.getIndex());
                }
                break;
        }
    }

    /**
     * ????????????Td??????????????????--???????????????????????????????????????or?????????????????????
     */
    private void configAggregateTimeType(AggregateTimeTypeEnum aggregateTimeType, QueryAggregateTdDto queryAggregateTdDto) {
        switch (aggregateTimeType) {
            case hour:
                queryAggregateTdDto.setOriginalTd(OriginalTd.hour);
                break;
            case day:
                queryAggregateTdDto.setOriginalTd(OriginalTd.day);
                break;
            case interval:
                queryAggregateTdDto.setOriginalTd(null);
                break;
        }
    }

    /**
     * ????????????Td??????????????????--???????????????????????????????????????or?????????????????????
     */
    private void configAggregateDataType(AggregateDataTypeEnum aggregateDataTypeEnum, QueryAggregateTdDto queryAggregateTdDto) {
        switch (aggregateDataTypeEnum) {
            case single:
                queryAggregateTdDto.setGroupType(AggregateGroupType.single);
                break;
            case whole:
                queryAggregateTdDto.setGroupType(AggregateGroupType.group);
                break;
        }
    }

    /**
     * ????????????Td????????????--????????????
     */
    private void configHistoryPage(Integer currentPage, Integer size, QueryTdDto queryTdDto) {
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

    /**
     * ????????????Td????????????--??????
     */
    private void configHistorySort(Boolean sortType, QueryTdDto queryTdDto) {
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

    /**
     * ????????????Td??????????????????--????????????
     */
    private void configHistoryAggregatePage(Integer currentPage, Integer size, QueryAggregateTdDto queryAggregateTdDto) {
        if (currentPage == null || size == null) {
            queryAggregateTdDto.setLimit(null);
            queryAggregateTdDto.setOffset(null);
        } else {
            queryAggregateTdDto.setLimit(size);
            if (currentPage > 1) {
                queryAggregateTdDto.setOffset(size * (currentPage - 1));
            } else {
                queryAggregateTdDto.setOffset(0);
            }
        }
    }

    /**
     * ????????????Td??????????????????--??????
     */
    private void configHistoryAggregateSort(Boolean sortType, QueryAggregateTdDto queryAggregateTdDto) {
        if (sortType == null) {
            queryAggregateTdDto.setOrder("ASC");
            return;
        }
        if (sortType) {
            queryAggregateTdDto.setOrder("ASC");
        } else {
            queryAggregateTdDto.setOrder("DESC");
        }
    }

    /**
     * ?????????????????????????????????
     */
    private void getHistoryBaseResponse(HistoryBaseDataResponse result, JSONArray array, List<String> pointList) {
        try {
            if (array != null && array.size() > 0) {
                List<HistoryBaseResponse> responseList = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    HistoryBaseResponse response = new HistoryBaseResponse();
                    JSONObject jsonObj = (JSONObject) array.get(i);
                    Object id = jsonObj.get("groupid");
                    if (id != null) {
                        response.setRelevanceId(Integer.parseInt((id.toString())));
                    }
                    Object time = jsonObj.get("ts");
                    if (time != null) {
                        response.setTimeStrap(Long.parseLong(time.toString()));
                    }
                    List<PointsInfo> pointsInfos = new ArrayList<>();
                    for (String field : pointList) {
                        removeSourceNaNObj(jsonObj, field.toLowerCase());
                        PointsInfo p = new PointsInfo();
                        p.setPointName(field);
                        p.setPointValue(new BigDecimal(String.format("%.2f", jsonObj.get(field.toLowerCase()))));
                        pointsInfos.add(p);
                    }
                    response.setPointsInfo(pointsInfos);
                    responseList.add(response);
                }
                result.setResponseData(responseList);
            }
        } catch (Exception e) {
            log.error("Td?????????getHistoryBaseResponse??????????????????----{}", e.getMessage());
        }
    }

    /**
     * ???????????????????????????????????????
     */
    private void getHistoryEnergyResponse(HistoryEnergyDataResponse result, JSONArray array, List<HeatAreaChangeResponse> heatAreaChangeResponses, GetNiggerHeadCalculateInfo calculateInfo, HeatTypeEnum heatType) {
        try {
            if (array != null && array.size() > 0) {
                List<HistoryEnergyResponse> historyEnergyResponseList = new ArrayList<>();
                double area = 0;
                GetEnergyPointConfig energyPointConfig = getEnergyPointConfig();
                HeatAreaChangeResponse areaChangeResponse = new HeatAreaChangeResponse();
                for (int i = 0; i < array.size(); i++) {
                    HistoryEnergyResponse response = new HistoryEnergyResponse();
                    JSONObject jsonObj = (JSONObject) array.get(i);
                    Object groupId = jsonObj.get("groupid");
                    if (groupId != null) {
                        response.setRelevanceId(Integer.parseInt((groupId.toString())));
                        areaChangeResponse = heatAreaChangeResponses.stream().filter(s -> Objects.equals(s.getRelevanceId().toString(), groupId.toString())).findFirst().orElse(null);
                    } else {
                        area = heatAreaChangeResponses.stream().mapToDouble(s -> s.getAreaValue().doubleValue()).sum();
                    }
                    Object timeStrap = jsonObj.get("ts");
                    if (timeStrap != null) {
                        response.setTimeStrap(Long.parseLong(timeStrap.toString()));
                    }
                    if (areaChangeResponse.getAreaValue() != null) {
                        area = areaChangeResponse.getAreaValue().doubleValue();
                    }
                    if (heatType == HeatTypeEnum.station) {
                        //??????????????????????????????????????????
                        removeSourceNaNObj(jsonObj, energyPointConfig.getStationWaterPoint().toLowerCase());
                        removeSourceNaNObj(jsonObj, energyPointConfig.getStationElectricityPoint().toLowerCase());
                        removeSourceNaNObj(jsonObj, energyPointConfig.getStationHeatPoint().toLowerCase());
                        //??????????????????
                        response.setArea(new BigDecimal(area).setScale(2, RoundingMode.HALF_UP));
                        //?????????
                        response.setWaterConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getStationWaterPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setWaterUnitConsumption(new BigDecimal(divide(response.getWaterConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                        //?????????
                        response.setElectricityConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getStationElectricityPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setElectricityUnitConsumption(new BigDecimal(divide(response.getElectricityConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                        //?????????
//                        response.setHeatConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getStationHeatPoint().toLowerCase()).toString()).divide(BigDecimal.valueOf(4.1868),4).multiply(BigDecimal.valueOf(1000000)).setScale(2, RoundingMode.HALF_UP));
                        response.setHeatConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getStationHeatPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setHeatUnitConsumption(new BigDecimal(divide(response.getHeatConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                    } else {
                        //???????????????????????????????????????
                        removeSourceNaNObj(jsonObj, energyPointConfig.getSourceWaterPoint().toLowerCase());
                        removeSourceNaNObj(jsonObj, energyPointConfig.getSourceElectricityPoint().toLowerCase());
                        removeSourceNaNObj(jsonObj, energyPointConfig.getSourceHeatPoint().toLowerCase());
                        //??????????????????
                        response.setArea(new BigDecimal(area).setScale(2, RoundingMode.HALF_UP));
                        //?????????
                        response.setWaterConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getSourceWaterPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setWaterUnitConsumption(new BigDecimal(divide(response.getWaterConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                        //?????????
                        response.setElectricityConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getSourceElectricityPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setElectricityUnitConsumption(new BigDecimal(divide(response.getElectricityConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                        //?????????(?????????)
//                        response.setHeatConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getSourceHeatPoint().toLowerCase()).toString()).divide(BigDecimal.valueOf(4.1868),4).multiply(BigDecimal.valueOf(1000000)).setScale(2, RoundingMode.HALF_UP));
                        response.setHeatConsumption(new BigDecimal(jsonObj.get(energyPointConfig.getSourceHeatPoint().toLowerCase()).toString()).setScale(2, RoundingMode.HALF_UP));
                        response.setHeatUnitConsumption(new BigDecimal(divide(response.getHeatConsumption().doubleValue(), area)).setScale(2, RoundingMode.HALF_UP));
                    }
                    //?????????????????????????????????
                    setNiggerHeadArithmetic(response, calculateInfo);
                    historyEnergyResponseList.add(response);
                }
                result.setResponseData(historyEnergyResponseList);
            }
        } catch (Exception e) {
            log.error("Td????????????????????????????????????????????????getHistoryEnergyResponse??????????????????----{}", e.getMessage());
        }
    }

    /**
     * ?????????????????????????????????
     */
    private List<HeatAreaChangeResponse> getRealCalculateArea(HeatTypeEnum heatType, long startTime, long endTime, List<Integer> relevanceIds) {
        List<HeatAreaChangeResponse> heatAreaChangeResponseList = new ArrayList<>();
        try {
            Integer level;
            if (heatType == HeatTypeEnum.station) {
                level = TreeLevel.HeatStation.level();
            } else {
                level = TreeLevel.HeatSource.level();
            }
            List<HeatAreaChangeResponse> fullHeatAreaList = checkAreaInfoToTd(relevanceIds, level);
            QueryAreaChangeDto queryAreaChangeDto = new QueryAreaChangeDto();
            queryAreaChangeDto.setLevel(level);
            queryAreaChangeDto.setStartTime(LocalTimeUtils.longToLocalDateTime(startTime));
            queryAreaChangeDto.setEndTime(LocalTimeUtils.longToLocalDateTime(endTime));
            queryAreaChangeDto.setRelevanceIds(relevanceIds.toArray(new Integer[0]));
            heatAreaChangeResponseList = queryHeatAreaChange(queryAreaChangeDto, fullHeatAreaList);
            return heatAreaChangeResponseList;
        } catch (Exception e) {
            return heatAreaChangeResponseList;
        }
    }

    /**
     * ??????????????????
     */
    public List<HeatAreaChangeResponse> checkAreaInfoToTd(List<Integer> ids, Integer level) {
        List<HeatAreaChangeResponse> list = new ArrayList<>();
        if (ids.size() <= 0) {
            return null;
        }
        //???????????????????????????
        if (level == TreeLevel.HeatStation.level()) {
            List<HeatTransferStation> station = heatTransferStationService.listByIds(ids);
            for (HeatTransferStation s : station) {
                HeatAreaChangeResponse history = new HeatAreaChangeResponse();
                history.setAreaValue(s.getHeatArea());
                history.setRelevanceId(s.getId());
                list.add(history);
            }
            return list;
        }
        //??????????????????
        if (level == TreeLevel.HeatSource.level()) {
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
     * ????????????id
     */
    public void updateSourceAreaInfo(List<HeatAreaChangeResponse> list) {
        List<SourceFirstNetBaseView> sourceFirstNetBaseViewList = sourceFirstNetBaseViewService.list();
        for (HeatAreaChangeResponse h : list) {
            SourceFirstNetBaseView source = sourceFirstNetBaseViewList.stream().filter(s -> Objects.equals(h.getRelevanceId().toString(), s.getHeatSourceId().toString())).findFirst().orElse(null);
            h.setRelevanceId(source.getHeatSystemId());
        }
    }

    /**
     * ?????????????????????????????????
     */
    private GetEnergyPointConfig getEnergyPointConfig() {
        QueryWrapper<WebPageConfig> webPageConfigQueryWrapper = new QueryWrapper<>();
        webPageConfigQueryWrapper.eq("configKey", "energyPointConfig");
        WebPageConfig webPageConfig = webPageConfigService.getOne(webPageConfigQueryWrapper);
        JSONObject jsonObject = JSONObject.parseObject(webPageConfig.getJsonConfig());
        GetEnergyPointConfig energyPointConfig = JSONArray.toJavaObject(jsonObject, GetEnergyPointConfig.class);
        return energyPointConfig;
    }

    /**
     * ??????????????????????????????
     */
    private GetNiggerHeadCalculateInfo getNiggerHeadCalculateInfo(long startTime, long endTime) {
        GetNiggerHeadCalculateInfo info = new GetNiggerHeadCalculateInfo();
        try {
            LocalDateTime start = LocalTimeUtils.longToLocalDateTime(startTime);
            LocalDateTime end = LocalTimeUtils.longToLocalDateTime(endTime);
            QueryWrapper<WeatherHour> wrapper = new QueryWrapper<>();
            wrapper.eq("forecastType", 1)
                    .ge("weatherTime", start)
                    .le("weatherTime", end);
            wrapper.select("avg(temperature) as avgTemperature ");
            Map<String, Object> map = weatherHourService.getMap(wrapper);
            if (map.get("avgTemperature") != null) {
                info.setForecastTempAvg(new BigDecimal(map.get("avgTemperature").toString()));
            } else {
                info.setForecastTempAvg(new BigDecimal(1));
            }
            EnergyUnitStandardConfig energyUnitStandardConfig = energyUnitStandardConfigService.getOne(Wrappers.<EnergyUnitStandardConfig>lambdaQuery().eq(EnergyUnitStandardConfig::getState, true));
            if (energyUnitStandardConfig != null) {
                info.setOutdoorTemp(energyUnitStandardConfig.getOutdoorTemp());
                info.setIndoorTemp(energyUnitStandardConfig.getIndoorTemp());
            } else {
                info.setOutdoorTemp(new BigDecimal(1));
                info.setIndoorTemp(new BigDecimal(1));
            }
            return info;
        } catch (Exception e) {
            log.error("???????????????????????????????????????------{}", e.getMessage());
            return null;
        }

    }

    /**
     * ???????????????????????????????????????
     */
    private void setNiggerHeadArithmetic(HistoryEnergyResponse response, GetNiggerHeadCalculateInfo calculateInfo) {
        try {
            //????????????
            BigDecimal heatNiggerHead = UnitStandardCompute.unitStandard(calculateInfo.getOutdoorTemp(), calculateInfo.getIndoorTemp(), calculateInfo.getForecastTempAvg(), response.getHeatUnitConsumption());
            response.setNiggerHeadHeatUnitConsumption(heatNiggerHead);
            //????????????
            BigDecimal heatTotal = UnitStandardCompute.unitStandard(calculateInfo.getOutdoorTemp(), calculateInfo.getIndoorTemp(), calculateInfo.getForecastTempAvg(), response.getHeatConsumption());
            //?????????
            BigDecimal coalNiggerHead = ComprehensiveEnergy.comprehensiveEnergy(heatTotal, BigDecimal.ZERO, BigDecimal.ZERO);
            response.setNiggerHeadCoal(coalNiggerHead);
        } catch (Exception e) {
            log.error("????????????,?????????????????????-----{}", e.getMessage());
        }
    }

    /**
     * ??????Td??????????????????????????????
     */
    public List<String> matchTd(List<String> pointNames) {
        // ?????????????????????????????????TD?????????
        List<PointStandard> listPointStandar = pointStandardService.list(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getTdColumn, true));
        List<String> pointList = new ArrayList<>();
        List<String> loseList = new ArrayList<>();
        pointNames.stream().forEach(e -> {
            PointStandard pointStandard = listPointStandar.parallelStream().filter(j -> Objects.equals(j.getColumnName(), e)).findFirst().orElse(null);
            if (pointStandard != null) {
                pointList.add(pointStandard.getColumnName());
            } else {
                loseList.add(e);

            }

        });
        if (!CollectionUtils.isEmpty(loseList)) {
            log.error("TD--ERROR---???????????????TD?????????--- {}", loseList.toString());
        }

        return pointList;
    }

    /**
     * ??????Td????????????????????????????????????
     */
    public List<AggregatePoint> matchAggregateTd(List<AggregatePoint> points) {
        // ?????????????????????????????????TD?????????
        List<PointStandard> listPointStandar = pointStandardService.list(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getTdColumn, true));
        List<AggregatePoint> pointList = new ArrayList<>();
        List<String> loseList = new ArrayList<>();
        points.stream().forEach(e -> {
            PointStandard pointStandard = listPointStandar.parallelStream().filter(j -> Objects.equals(j.getColumnName(), e.getPointName())).findFirst().orElse(null);
            if (pointStandard != null) {
                pointList.add(e);
            } else {
                loseList.add(e.getPointName());
            }

        });
        if (!CollectionUtils.isEmpty(loseList)) {
            log.error("TD--ERROR---???????????????TD?????????--- {}", loseList.toString());
        }
        return pointList;
    }


    private void queryHistoryTd(List<Map<String, String>> maps, JSONArray array, List<String> pointList) {
        try {
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject jsonObj = (JSONObject) array.get(i);
                    jsonObj.put("relevanceId", jsonObj.get("groupid"));
                    jsonObj.put("timeStrap", jsonObj.get("ts"));
                    for (String field : pointList) {
                        removeSourceNaNObj(jsonObj, field.toLowerCase());
                        BigDecimal bd = new BigDecimal(jsonObj.get(field.toLowerCase()).toString());
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        jsonObj.put(field, bd.toString());
                        jsonObj.remove(field.toLowerCase());
                    }
                    Map<String, String> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, String>>() {
                    });
                    maps.add(map);
                }
            }
        } catch (Exception e) {
            log.error("??????????????????????????????????????????????????????{}", e.getMessage());
        }
    }

    private List<Map<String, String>> queryAggregateData(QueryAggregateTdDto dto, Integer level) {
        List<Map<String, String>> mapList = new ArrayList<>();
        QueryPointsOuterClass.Response response = client.queryTdAggregate(dto);
        JSONArray array = JSON.parseArray(response.getJsonData());
        QueryAreaChangeDto queryAreaChangeDto = new QueryAreaChangeDto();
        queryAreaChangeDto.setLevel(level);
        queryAreaChangeDto.setStartTime(LocalTimeUtils.longToLocalDateTime(dto.getStartTime()));
        queryAreaChangeDto.setEndTime(LocalTimeUtils.longToLocalDateTime(dto.getEndTime()));
        queryAreaChangeDto.setRelevanceIds(dto.getGroupId().toArray(new Integer[0]));
        List<HeatAreaChangeResponse> fullHeatAreaList = checkAreaInfo(dto.getGroupId().toArray(new Integer[0]), level);
        List<HeatAreaChangeResponse> heatAreaChangeResponseList = queryHeatAreaChange(queryAreaChangeDto, fullHeatAreaList);
        List<String> pointNameList = dto.getPoints().stream().map(s -> s.getPointName()).collect(Collectors.toList());
        if (dto.getGroupType() == AggregateGroupType.single) {
            singleBuckListInfo(array, mapList, heatAreaChangeResponseList, pointNameList, level);
        } else {
            double area;
            if (heatAreaChangeResponseList != null) {
                area = heatAreaChangeResponseList.stream().mapToDouble(s -> s.getAreaValue().doubleValue()).sum();
            } else {
                area = 0;
            }
            groupBuckListInfo(array, mapList, area, pointNameList);
        }
        return mapList;
    }

    public void singleBuckListInfo(JSONArray array, List<Map<String, String>> mapList, List<HeatAreaChangeResponse> heatAreaChangeResponseList, List<String> includeFields, Integer level) {
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject) array.get(i);
                jsonObj.put("relevanceId", jsonObj.get("groupid"));
                HeatAreaChangeResponse areaChangeResponse = heatAreaChangeResponseList.stream().filter(s -> Objects.equals(s.getRelevanceId().toString(), jsonObj.get("relevanceId").toString())).findFirst().orElse(null);
                BigDecimal area;
                if (areaChangeResponse != null) {
                    area = areaChangeResponse.getAreaValue();
                } else {
                    area = new BigDecimal(0);
                }
                jsonObj.put("area", area);
                for (String field : includeFields) {
                    removeSourceNaNObj(jsonObj, field.toLowerCase());
                    double value = new BigDecimal(jsonObj.get(field.toLowerCase()).toString()).doubleValue();
                    jsonObj.put(field, value);
                    jsonObj.put(field + "unitStandard", divide(value, area.doubleValue()));
                    jsonObj.remove(field.toLowerCase());
                }
                Map<String, String> map = JSONObject.parseObject(jsonObj.toJSONString(), new TypeReference<Map<String, String>>() {
                });
                mapList.add(map);
            }
        }
    }

    public void groupBuckListInfo(JSONArray array, List<Map<String, String>> mapList, double area, List<String> includeFields) {
        if (array != null && array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObj = (JSONObject) array.get(i);
                jsonObj.put("relevanceId", jsonObj.get("groupid"));
                jsonObj.put("area", area);
                for (String field : includeFields) {
                    removeSourceNaNObj(jsonObj, field.toLowerCase());
                    double value = new BigDecimal(jsonObj.get(field.toLowerCase()).toString()).doubleValue();
                    jsonObj.put(field, value);
                    jsonObj.put(field + "unitStandard", divide(value, area));
                    jsonObj.remove(field.toLowerCase());
                }
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
        //???????????????????????????
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
        //??????????????????
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

    private void removeSourceNaNObj(JSONObject jsonObj, String pointName) {
        if (jsonObj.get(pointName) == null) {
//            log.error("TD????????????????????????,"+pointName+"????????????NaN???null??????????????????Id???{}",jsonObj.get("relevanceId").toString());
            jsonObj.put(pointName, 0);
        }
    }

    /**
     * ?????????????????????
     *
     * @param dividend ?????????
     * @param divisor  ??????
     * @return Double
     */
    private Double divide(double dividend, double divisor) {
        if (!Double.isInfinite(dividend) && !Double.isInfinite(divisor)) {
            Double aDouble = ParseUtil.parseDouble(dividend) / ParseUtil.parseDouble(divisor);
            return ParseUtil.parseDouble(aDouble);
        }
        return 0.0;
    }

}
