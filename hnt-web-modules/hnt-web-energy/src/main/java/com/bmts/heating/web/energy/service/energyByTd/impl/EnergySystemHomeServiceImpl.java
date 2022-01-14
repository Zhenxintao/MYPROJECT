package com.bmts.heating.web.energy.service.energyByTd.impl;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.baseInfo.response.WeatherTempComparison;
import com.bmts.heating.commons.entiy.gathersearch.request.AggregatePoint;
import com.bmts.heating.commons.entiy.gathersearch.request.AggregateType;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryBaseDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyResponse;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.HomeBaseResponse;
import com.bmts.heating.web.energy.pojo.StationEnergyInfoResponse;
import com.bmts.heating.web.energy.pojo.energyByTd.HomeEnergyDataResponse;
import com.bmts.heating.web.energy.pojo.energyByTd.HomeEnergyOverallResponse;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.energyByTd.EnergySystemHomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zxt
 */
@Service
@Slf4j
public class EnergySystemHomeServiceImpl implements EnergySystemHomeService {
    @Autowired
    private TSCCRestTemplate template;
    @Autowired
    private CommonService commonService;

    private final String url = "/energy/";
    private final String gatherSearch = "gather_search";

    /**
     * 能耗系统--首页昨日能耗数据
     */
    @Override
    public Response energyData() {
        HomeEnergyDataResponse homeEnergyDataResponse = new HomeEnergyDataResponse();
        try {
            //获取所有换热站Id信息
            List<StationFirstNetBaseView> stationInfo = commonService.filterFirstNetBase();
            if (stationInfo.size() <= 0) {
                return Response.success();
            }
            //组装Td聚合能耗历史参数
            QueryAggregateHistoryDto dto = getQueryAggregateHistoryDto(HeatTypeEnum.station);
            dto.setRelevanceIds(stationInfo.stream().map(s -> s.getHeatTransferStationId()).collect(Collectors.toList()));
            dto.setAggregateTimeType(AggregateTimeTypeEnum.interval);
            dto.setHistoryType(HistoryTypeEnum.energy_hour);
            dto.setAggregateDataType(AggregateDataTypeEnum.single);
            List<TimeRange> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                TimeRange timeRange = new TimeRange();
                timeRange.setStart(LocalTimeUtils.getDay(-1));
                timeRange.setEnd(LocalTimeUtils.getDay(0));
                timeRange.setIndex("昨日");
                list.add(timeRange);

                timeRange = new TimeRange();
                timeRange.setStart(LocalTimeUtils.getDay(-2));
                timeRange.setEnd(LocalTimeUtils.getDay(-1));
                timeRange.setIndex("环比");
                list.add(timeRange);

                timeRange = new TimeRange();
                timeRange.setStart(LocalTimeUtils.getYear(LocalTimeUtils.getDay(-1), -1));
                timeRange.setEnd(LocalTimeUtils.getYear(LocalTimeUtils.getDay(0), -1));
                timeRange.setIndex("同比");
                list.add(timeRange);
            }
            BigDecimal water = null;
            BigDecimal electricity = null;
            BigDecimal heat = null;
            BigDecimal percentage = new BigDecimal(100);
            WeatherTempComparison weatherTempComparison = commonService.weatherInfo();
            homeEnergyDataResponse.setTemp(weatherTempComparison.getCurrent());
            homeEnergyDataResponse.setTempTb(weatherTempComparison.getBeforeDay());
            homeEnergyDataResponse.setTempHb(weatherTempComparison.getBeforeYear());
            for (TimeRange timeRange : list) {
                dto.setStartTime(timeRange.getStart());
                dto.setEndTime(timeRange.getEnd());
                HistoryEnergyDataResponse response = template.post("tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class);
                if (response.getResponseData() != null) {
                    water = response.getResponseData().get(0).getWaterUnitConsumption();
                    electricity = response.getResponseData().get(0).getElectricityUnitConsumption();
                    heat = response.getResponseData().get(0).getHeatUnitConsumption();
                    if (Objects.equals(timeRange.getIndex(), "昨日")) {
                        homeEnergyDataResponse.setWaterUnitConsumption(water);
                        homeEnergyDataResponse.setElectricityUnitConsumption(electricity);
                        homeEnergyDataResponse.setHeatUnitConsumption(heat);
                        homeEnergyDataResponse.setWaterConsumption(response.getResponseData().get(0).getWaterConsumption());
                        homeEnergyDataResponse.setElectricityConsumption(response.getResponseData().get(0).getElectricityConsumption());
                        homeEnergyDataResponse.setHeatConsumption(response.getResponseData().get(0).getHeatConsumption());
                    } else if (Objects.equals(timeRange.getIndex(), "同比")) {
                        if (homeEnergyDataResponse.getWaterUnitConsumption() != null) {
                            BigDecimal waterTb = new BigDecimal(0);
                            if (!water.equals(BigDecimal.ZERO)) {
                                waterTb = homeEnergyDataResponse.getWaterUnitConsumption().subtract(water).divide(water, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setWaterTb(waterTb);
                        }
                        if (homeEnergyDataResponse.getElectricityUnitConsumption() != null) {
                            BigDecimal electricityTb = new BigDecimal(0);
                            if (!electricity.equals(BigDecimal.ZERO)) {
                                electricityTb = homeEnergyDataResponse.getElectricityUnitConsumption().subtract(electricity).divide(electricity, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setElectricityTb(electricityTb);
                        }
                        if (homeEnergyDataResponse.getHeatUnitConsumption() != null) {
                            BigDecimal heatTb = new BigDecimal(0);
                            if (!heat.equals(BigDecimal.ZERO)) {
                                heatTb = homeEnergyDataResponse.getHeatUnitConsumption().subtract(heat).divide(heat, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setHeatTb(heatTb);
                        }
                    } else {
                        if (homeEnergyDataResponse.getWaterUnitConsumption() != null) {
                            BigDecimal waterHb = new BigDecimal(0);
                            if (!water.equals(BigDecimal.ZERO)) {
                                waterHb = homeEnergyDataResponse.getWaterUnitConsumption().subtract(water).divide(water, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setWaterHb(waterHb);
                        }
                        if (homeEnergyDataResponse.getElectricityUnitConsumption() != null) {
                            BigDecimal electricityHb = new BigDecimal(0);
                            if (!electricity.equals(BigDecimal.ZERO)) {
                                electricityHb = homeEnergyDataResponse.getElectricityUnitConsumption().subtract(electricity).divide(electricity, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setElectricityHb(electricityHb);
                        }
                        if (homeEnergyDataResponse.getHeatUnitConsumption() != null) {
                            BigDecimal heatHb = new BigDecimal(0);
                            if (!heat.equals(BigDecimal.ZERO)) {
                                heatHb = (homeEnergyDataResponse.getHeatUnitConsumption().subtract(heat)).divide(heat, 4).multiply(percentage);
                            }
                            homeEnergyDataResponse.setHeatHb(heatHb);
                        }
                    }
                }
            }
            return Response.success(homeEnergyDataResponse);
        } catch (Exception e) {
            return Response.success(homeEnergyDataResponse);
        }
    }

    /**
     * 能耗系统--首页地图总耗信息
     */
    @Override
    public Response energyDataOverall() {
        HomeEnergyOverallResponse homeEnergyOverallResponse = new HomeEnergyOverallResponse();
        try {
            //获取所有换热站Id信息
            List<StationFirstNetBaseView> stationInfo = commonService.filterFirstNetBase();
            if (stationInfo.size() <= 0) {
                return Response.success();
            }
            //组装Td聚合能耗历史参数
            QueryAggregateHistoryDto dto = getQueryAggregateHistoryDto(HeatTypeEnum.station);
            dto.setRelevanceIds(stationInfo.stream().map(s -> s.getHeatTransferStationId()).collect(Collectors.toList()));
            dto.setAggregateTimeType(AggregateTimeTypeEnum.interval);
            dto.setHistoryType(HistoryTypeEnum.energy_hour);
            dto.setAggregateDataType(AggregateDataTypeEnum.single);
            //获取当前供暖季信息
            Map commonHeatSeason = commonService.getPresentHeatSeason();
            dto.setStartTime(LocalTimeUtils.transferStringDateToLong("yyy-MM-dd HH:mm:ss",commonHeatSeason.get("heatStartTime").toString()));
            dto.setEndTime(LocalTimeUtils.transferStringDateToLong("yyy-MM-dd HH:mm:ss",commonHeatSeason.get("heatEndTime").toString()));
            HistoryEnergyDataResponse response = template.post("tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class);
            HistoryEnergyResponse historyEnergyResponse = response.getResponseData().get(0);
            homeEnergyOverallResponse.setWaterOverall(historyEnergyResponse.getWaterConsumption());
            homeEnergyOverallResponse.setElectricityOverall(historyEnergyResponse.getElectricityConsumption());
            homeEnergyOverallResponse.setHeatOverall(historyEnergyResponse.getHeatConsumption());
            return Response.success(homeEnergyOverallResponse);
        } catch (Exception e) {
            return Response.success(homeEnergyOverallResponse);
        }
    }

    /**
     * 能耗系统--首页单站能耗信息
     */
    @Override
    public Response mapEnergyData(Integer id) {
        StationEnergyInfoResponse stationEnergyInfoResponse = new StationEnergyInfoResponse();
        try {
            //组装Td聚合能耗历史参数
            QueryAggregateHistoryDto dto = getQueryAggregateHistoryDto(HeatTypeEnum.station);
            List<Integer> ids = new ArrayList<>();
            ids.add(id);
            dto.setRelevanceIds(ids);
            dto.setAggregateTimeType(AggregateTimeTypeEnum.day);
            dto.setHistoryType(HistoryTypeEnum.energy_hour);
            dto.setAggregateDataType(AggregateDataTypeEnum.single);
            dto.setStartTime(LocalTimeUtils.getDay(-1));
            dto.setEndTime(LocalTimeUtils.getTimestampOfDateTime(LocalDateTime.now()));
            HistoryEnergyDataResponse response = template.post("tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class);
            HistoryEnergyResponse yData;
            HistoryEnergyResponse nowData;
            if (response.getResponseData() != null) {
                yData = response.getResponseData().get(0);
                nowData = response.getResponseData().get(1);
            } else {
                return Response.success(stationEnergyInfoResponse);
            }
            stationEnergyInfoResponse.setTodayWater(nowData.getWaterConsumption());
            stationEnergyInfoResponse.setTodayEle(nowData.getElectricityConsumption());
            stationEnergyInfoResponse.setTodayHeat(nowData.getHeatConsumption());
            stationEnergyInfoResponse.setLastDayWater(yData.getWaterConsumption());
            stationEnergyInfoResponse.setLastDayEle(yData.getElectricityConsumption());
            stationEnergyInfoResponse.setLastDayHeat(yData.getHeatConsumption());
            return Response.success(stationEnergyInfoResponse);
        } catch (Exception e) {
            return Response.success(stationEnergyInfoResponse);
        }
    }

    /**
     * 能耗系统--首页能耗排行信息
     * */
    @Override
    public Response energyRankData() {
        Map<String, List<HistoryEnergyResponse>> responseMap = new HashMap<>();
        try {
            QueryAggregateHistoryDto dto = getQueryAggregateHistoryDto(HeatTypeEnum.station);
            //获取换热站数据
            List<StationFirstNetBaseView> stationInfo = commonService.filterFirstNetBase();
            if (stationInfo.size() <= 0) {
                return Response.success();
            }
            //组装Td聚合能耗历史参数
            dto.setAggregateTimeType(AggregateTimeTypeEnum.day);
            dto.setAggregateDataType(AggregateDataTypeEnum.whole);
            dto.setRelevanceIds(stationInfo.stream().map(s -> s.getHeatTransferStationId()).collect(Collectors.toList()));
            dto.setHistoryType(HistoryTypeEnum.energy_hour);
            dto.setSortType(true);
            dto.setStartTime(LocalTimeUtils.getDay(-1));
            dto.setEndTime(LocalTimeUtils.getDay(0));

            HistoryEnergyDataResponse response = template.post("tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class);
            List<HistoryEnergyResponse> waterRank = response.getResponseData().stream().sorted(Comparator.comparing(HistoryEnergyResponse::getWaterUnitConsumption).reversed()).collect(Collectors.toList());
            List<HistoryEnergyResponse> electricityRank = response.getResponseData().stream().sorted(Comparator.comparing(HistoryEnergyResponse::getElectricityUnitConsumption).reversed()).collect(Collectors.toList());
            List<HistoryEnergyResponse> heatRank = response.getResponseData().stream().sorted(Comparator.comparing(HistoryEnergyResponse::getHeatUnitConsumption).reversed()).collect(Collectors.toList());
            for (HistoryEnergyResponse h:waterRank) {
                StationFirstNetBaseView station = stationInfo.stream().filter(s->Objects.equals(s.getHeatTransferStationId().toString(),h.getRelevanceId().toString())).findFirst().orElse(null);
                if (station!=null){
                    h.setName(station.getHeatTransferStationName());
                }
            }
            for (HistoryEnergyResponse h:electricityRank) {
                StationFirstNetBaseView station = stationInfo.stream().filter(s->Objects.equals(s.getHeatTransferStationId().toString(),h.getRelevanceId().toString())).findFirst().orElse(null);
                if (station!=null){
                    h.setName(station.getHeatTransferStationName());
                }
            }
            for (HistoryEnergyResponse h:heatRank) {
                StationFirstNetBaseView station = stationInfo.stream().filter(s->Objects.equals(s.getHeatTransferStationId().toString(),h.getRelevanceId().toString())).findFirst().orElse(null);
                if (station!=null){
                    h.setName(station.getHeatTransferStationName());
                }
            }
            responseMap.put("water", waterRank.stream().limit(10).collect(Collectors.toList()));
            responseMap.put("ele", electricityRank.stream().limit(10).collect(Collectors.toList()));
            responseMap.put("heat", heatRank.stream().limit(10).collect(Collectors.toList()));
            return Response.success(responseMap);
        }catch (Exception e){
            return Response.success(responseMap);
        }


    }


    /**
     * 获取能耗聚合历史数据参数信息
     */
    private QueryAggregateHistoryDto getQueryAggregateHistoryDto(HeatTypeEnum heatType) {
        QueryAggregateHistoryDto dto = new QueryAggregateHistoryDto();
        dto.setHeatType(heatType);
        List<AggregatePoint> aggregatePoints = new ArrayList<>();
        GetEnergyPointConfig getEnergyPointConfig = commonService.getEnergyPointConfig();
        AggregatePoint water = new AggregatePoint();
        AggregatePoint electricity = new AggregatePoint();
        AggregatePoint heat = new AggregatePoint();
        water.setAggregateType(AggregateType.sum);
        electricity.setAggregateType(AggregateType.sum);
        heat.setAggregateType(AggregateType.sum);
        if (heatType == HeatTypeEnum.station) {
            dto.setHeatType(HeatTypeEnum.station);
            water.setPointName(getEnergyPointConfig.getStationWaterPoint());
            electricity.setPointName(getEnergyPointConfig.getStationElectricityPoint());
            heat.setPointName(getEnergyPointConfig.getStationHeatPoint());
        } else {
            dto.setHeatType(HeatTypeEnum.source);
            water.setPointName(getEnergyPointConfig.getSourceWaterPoint());
            electricity.setPointName(getEnergyPointConfig.getSourceElectricityPoint());
            heat.setPointName(getEnergyPointConfig.getSourceHeatPoint());
        }
        aggregatePoints.add(water);
        aggregatePoints.add(electricity);
        aggregatePoints.add(heat);
        dto.setPoints(aggregatePoints);
        return dto;
    }

    /**
     * 获取能耗基础历史数据参数信息
     */
    private QueryBaseHistoryDto getQueryBaseHistoryDto() {
        QueryBaseHistoryDto dto = new QueryBaseHistoryDto();
        dto.setHeatType(HeatTypeEnum.station);
        return dto;
    }

    /**
     * 获取所有换热站id
     */
    private List<Integer> getAllStationId() {
        List<Integer> ids = new ArrayList<>();
        try {
            List<StationFirstNetBaseView> stationInfo = commonService.filterFirstNetBase();
            if (stationInfo.size() <= 0) {
                return ids;
            }
            ids = stationInfo.stream().map(s -> s.getHeatTransferStationId()).collect(Collectors.toList());
            return ids;
        } catch (Exception e) {
            return ids;
        }


    }
}
