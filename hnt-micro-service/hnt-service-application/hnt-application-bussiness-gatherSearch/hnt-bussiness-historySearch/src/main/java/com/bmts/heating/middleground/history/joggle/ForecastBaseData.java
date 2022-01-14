package com.bmts.heating.middleground.history.joggle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.forecast.history.ForecastRequest;
import com.bmts.heating.commons.entiy.forecast.history.ForecastResponse;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.*;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleground.history.service.TdEngineHistoryData;
import com.bmts.heating.middleground.history.service.TdEngineQueryHistoryData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Api(tags = "负荷预测参数量")
@Slf4j
@RestController
@RequestMapping("/forecast")
public class ForecastBaseData {

    //	@Autowired
//	private HistoryData historyData;
    @Autowired
    private TdEngineHistoryData historyData;
    @Autowired
    private TdEngineQueryHistoryData tdEngineQueryHistoryData;
    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private WebPageConfigService webPageConfigService;
    /**
     * 热量
     */
    final String heatMtrG = "HeatSourceTotalHeat_MtrG";
    /**
     * 流量
     */
    final String heatSupply = "HeatSourceFTSupply";
    final String HeatSourceTg = "Tg";
    final String HeatSourceTh = "Th";
    //瞬时流量
    final String HeatSourceF_MtrG = "HM_Fg";
    final long oneDay = 1000 * 60 * 60 * 24;
    final long oneHour = 1000 * 60 * 60;
    @Autowired
    private HeatSourceService heatSourceService;

    //region 热源

//    @ApiOperation("热源")
//    @PostMapping("/baseSource")
//    public Response findHistoryDataSource(@RequestBody ForecastRequest dto) {
////		if (dto.getRelevanceId().size() > 0 ){
////			return null;
////		}
//        List<ForecastResponse> list = new ArrayList<>();
//        dto.getRelevanceId().forEach(e -> {
//            HeatSourceResponse one = this.getSourceArea(e);
//            if (one == null) {
//                return;
//            }
//            Map<String, BigDecimal> historyDataEnergy = this.getSourceDataEnergy(dto, one.getId());
//            Map<String, BigDecimal> historyDataHour = this.getSourceDataHour(dto, one.getId());
////			if(historyDataEnergy.size() == 0 && historyDataHour.size() == 0){
////				return;
////			}
//            /*天数或者小时数*/
//            final long num;
//            switch (dto.getType()) {
//                case 1:/* 小时 */
//                    num = (dto.getEndTime() - dto.getStartTime()) / oneHour;
//                    break;
//                case 2:/* 天 */
//                    num = (dto.getEndTime() - dto.getStartTime()) / oneDay;
//                    break;
//                default:
//                    throw new RuntimeException("数据类型不存在");
//            }
//            ForecastResponse response = this.setForecastBaseSource(historyDataEnergy, historyDataHour, one.getHeatArea(), num);
//            response.setRelevanceId(e);
//            list.add(response);
//            log.info(response.toString());
//        });
//        return Response.success(list);
//    }

    @ApiOperation("热源")
    @PostMapping("/baseSource")
    public Response findHistoryDataSource(@RequestBody ForecastRequest dto) {
        List<ForecastResponse> forecastResponseList = new ArrayList<>();
        QueryWrapper<SourceFirstNetBaseView> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("heatSourceId", dto.getRelevanceId());
        List<SourceFirstNetBaseView> sourceList = sourceFirstNetBaseViewService.list(queryWrapper);
        List<Integer> ids = sourceList.stream().map(s -> s.getHeatSystemId()).collect(Collectors.toList());
        QueryAggregateHistoryDto queryAggregateHistoryDto = getQueryAggregateHistoryDto(HeatTypeEnum.source);
        queryAggregateHistoryDto.setStartTime(dto.getStartTime());
        queryAggregateHistoryDto.setEndTime(dto.getEndTime());
        queryAggregateHistoryDto.setAggregateDataType(AggregateDataTypeEnum.whole);
        queryAggregateHistoryDto.setAggregateTimeType(AggregateTimeTypeEnum.interval);
        queryAggregateHistoryDto.setRelevanceIds(ids);
        setAggregatePoints(HeatTypeEnum.source, queryAggregateHistoryDto, 1);
        HistoryEnergyDataResponse historyEnergyDataResponse = tdEngineQueryHistoryData.queryHistoryAggregateEnergy(queryAggregateHistoryDto);
        setAggregatePoints(HeatTypeEnum.source, queryAggregateHistoryDto, 2);
        HistoryBaseDataResponse historyBaseDataResponse = tdEngineQueryHistoryData.queryHistoryAggregate(queryAggregateHistoryDto);
        for (SourceFirstNetBaseView s : sourceList) {
            if (historyBaseDataResponse.getResponseData() == null && historyEnergyDataResponse.getResponseData() == null) {
                continue;
            }
            ForecastResponse response = new ForecastResponse();
            response.setRelevanceId(s.getHeatSourceId());
            if (historyBaseDataResponse.getResponseData() != null) {
                HistoryBaseResponse historyBaseResponse = historyBaseDataResponse.getResponseData().stream().filter(base -> Objects.equals(base.getRelevanceId().toString(), s.getHeatSystemId().toString())).findFirst().orElse(null);
                if (historyBaseResponse != null) {
                    historyBaseResponse.getPointsInfo().forEach(pointsInfo -> {
                        if (Objects.equals(pointsInfo.getPointName(), "HeatSourceTg")) {
                            response.setHeatSourceTg(pointsInfo.getPointValue());
                        } else if (Objects.equals(pointsInfo.getPointName(), "HeatSourceTh")) {
                            response.setHeatSourceTh(pointsInfo.getPointValue());
                        } else {
                            response.setRealFlow(pointsInfo.getPointValue());
                        }
                    });
                }
            }
            if (historyEnergyDataResponse.getResponseData() != null) {
                HistoryEnergyResponse historyEnergyResponse = historyEnergyDataResponse.getResponseData().stream().filter(base -> Objects.equals(base.getRelevanceId().toString(), s.getHeatSystemId().toString())).findFirst().orElse(null);
                if (historyEnergyResponse != null) {
                    response.setRealHeatLoad(historyEnergyResponse.getHeatConsumption());
                    response.setRealHeatTotal(historyEnergyResponse.getHeatConsumption());
                    response.setHeatIndex(historyEnergyResponse.getHeatUnitConsumption());
                }
            }
            forecastResponseList.add(response);
        }
        return Response.success(forecastResponseList);
    }

    /**
     * 获取能耗聚合历史数据参数信息
     */
    private QueryAggregateHistoryDto getQueryAggregateHistoryDto(HeatTypeEnum heatType) {
        QueryAggregateHistoryDto dto = new QueryAggregateHistoryDto();
        dto.setHeatType(heatType);
//        setAggregatePoints(heatType,dto,pointsType);
        return dto;
    }

    public void setAggregatePoints(HeatTypeEnum heatType, QueryAggregateHistoryDto dto, Integer pointsType) {
        List<AggregatePoint> aggregatePoints = new ArrayList<>();
        if (pointsType == 1) {
            dto.setHistoryType(HistoryTypeEnum.energy_hour);
            GetEnergyPointConfig getEnergyPointConfig = getEnergyPointConfig();
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
        } else {
            dto.setHistoryType(HistoryTypeEnum.history_hour);
            AggregatePoint aggregatePointTg = new AggregatePoint();
            aggregatePointTg.setPointName("HeatSourceTg");
            aggregatePointTg.setAggregateType(AggregateType.avg);
            AggregatePoint aggregatePointTh = new AggregatePoint();
            aggregatePointTh.setPointName("HeatSourceTh");
            aggregatePointTh.setAggregateType(AggregateType.avg);
            AggregatePoint aggregatePointHM_Fg = new AggregatePoint();
            aggregatePointHM_Fg.setPointName("HeatSourceF_MtrG");
            aggregatePointHM_Fg.setAggregateType(AggregateType.avg);
            aggregatePoints.add(aggregatePointTg);
            aggregatePoints.add(aggregatePointTh);
            aggregatePoints.add(aggregatePointHM_Fg);
        }
        dto.setPoints(aggregatePoints);
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
     * 获取水电热能耗点位信息
     */
    private GetEnergyPointConfig getEnergyPointConfig() {
        QueryWrapper<WebPageConfig> webPageConfigQueryWrapper = new QueryWrapper<>();
        webPageConfigQueryWrapper.eq("configKey", "energyPointConfig");
        WebPageConfig webPageConfig = webPageConfigService.getOne(webPageConfigQueryWrapper);
        JSONObject jsonObject = JSONObject.parseObject(webPageConfig.getJsonConfig());
        GetEnergyPointConfig energyPointConfig = JSONArray.toJavaObject(jsonObject, GetEnergyPointConfig.class);
        return energyPointConfig;
    }

    private Map<String, BigDecimal> getSourceDataHour(ForecastRequest dto, int id) {
        QueryEsDto esDto = new QueryEsDto();
        esDto.setIncludeFields(new String[]{HeatSourceTg, HeatSourceTh, HeatSourceF_MtrG});
        esDto.setStart(dto.getStartTime() - 1);
        esDto.setEnd(dto.getEndTime() - 1);
        switch (dto.getType()) {
            /* 小时 */
            case 1:
                esDto.setDocument(HistoryDocument.HOUR);
                break;
            /* 天 */
            case 2:
                esDto.setDocument(HistoryDocument.DAY);
                break;
            default:
                throw new RuntimeException("数据类型不存在");
        }
        //获取面积 包含id:热源系统id, heatArea:热源系统面积
        esDto.setHeatSystemId(new Integer[]{id});
        esDto.setSourceType(HistorySourceType.FIRST);
        esDto.setIncludeTotal(false);
        List<Map<String, Object>> historyData = this.historyData.findHistoryEnergyData(esDto);
        Map<String, BigDecimal> res = new HashMap<>();
        AtomicInteger i = new AtomicInteger(0);
        for (Map<String, Object> historyDatum : historyData) {
            String o1 = historyDatum.get(HeatSourceTg).toString();
            String o2 = historyDatum.get(HeatSourceTh).toString();
            String o3 = historyDatum.get(HeatSourceF_MtrG).toString();
            BigDecimal var1 = new BigDecimal(o1);
            BigDecimal var2 = new BigDecimal(o2);
            BigDecimal var3 = new BigDecimal(o3);
            res.put(HeatSourceTg, var1.add(res.get(HeatSourceTg) != null ? res.get(HeatSourceTg) : new BigDecimal("0")));
            res.put(HeatSourceTh, var2.add(res.get(HeatSourceTh) != null ? res.get(HeatSourceTh) : new BigDecimal("0")));
            res.put(HeatSourceF_MtrG, var3.add(res.get(HeatSourceF_MtrG) != null ? res.get(HeatSourceF_MtrG) : new BigDecimal("0")));
            i.addAndGet(1);
        }
        if (i.intValue() != 0) {
            res.put(HeatSourceTg, res.get(HeatSourceTg).divide(BigDecimal.valueOf(i.intValue()), 3));
            res.put(HeatSourceTh, res.get(HeatSourceTh).divide(BigDecimal.valueOf(i.intValue()), 3));
            res.put(HeatSourceF_MtrG, res.get(HeatSourceF_MtrG).divide(BigDecimal.valueOf(i.intValue()), 3));
        }
        return res;
    }


    private Map<String, BigDecimal> getSourceDataEnergy(ForecastRequest dto, int id) {
        QueryEsDto esDto = new QueryEsDto();
        esDto.setIncludeFields(new String[]{heatMtrG, heatSupply});
        esDto.setStart(dto.getStartTime() - 1);
        esDto.setEnd(dto.getEndTime() - 1);
        switch (dto.getType()) {
            /* 小时 */
            case 1:
                esDto.setDocument(HistoryDocument.HOUR);
                break;
            /* 天 */
            case 2:
                esDto.setDocument(HistoryDocument.DAY);
                break;
            default:
                throw new RuntimeException("数据类型不存在");
        }
        //获取面积 包含id:热源系统id, heatArea:热源系统面积
        esDto.setHeatSystemId(new Integer[]{id});
        esDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
        esDto.setIncludeTotal(false);
        List<Map<String, Object>> historyData = this.historyData.findHistoryEnergyData(esDto);
        Map<String, BigDecimal> res = new HashMap<>();
        AtomicInteger i = new AtomicInteger(0);
        for (Map<String, Object> historyDatum : historyData) {
            Object o1 = (JSON.parseObject(historyDatum.get(heatMtrG).toString(), HashMap.class)).get("consumption");
            Object o2 = (JSON.parseObject(historyDatum.get(heatSupply).toString(), HashMap.class)).get("consumption");
            BigDecimal var1 = new BigDecimal(o1.toString());
            BigDecimal var2 = new BigDecimal(o2.toString());
            res.put(heatMtrG, var1.add(res.get(heatMtrG) != null ? res.get(heatMtrG) : new BigDecimal("0")));
            res.put(heatSupply, var2.add(res.get(heatSupply) != null ? res.get(heatSupply) : new BigDecimal("0")));
            i.addAndGet(1);
        }
        return res;
    }

    private ForecastResponse setForecastBaseSource(Map<String, BigDecimal> map, Map<String, BigDecimal> map2, BigDecimal area, long num) {
        return this.getForecastResponse(map, map2, area, num, heatMtrG, heatSupply, HeatSourceTg, HeatSourceTh, HeatSourceF_MtrG);
    }

    private HeatSourceResponse getSourceArea(int sourceId) {
        return heatSourceService.heatSourceArea(sourceId);
    }
    //endregion


    //region common
    private ForecastResponse getForecastResponse(Map<String, BigDecimal> map, Map<String, BigDecimal> map2, BigDecimal area, long num, String hmHt, String wm_ft, String tg, String th, String fm) {
        BigDecimal hm = map.get(hmHt);
        BigDecimal hs = map.get(wm_ft);
        ForecastResponse response = new ForecastResponse();
        response.setRealHeatTotal(hm);
        response.setRealFlow(hs);
        response.setRealHeatLoad(hm.divide(BigDecimal.valueOf(num), 3, BigDecimal.ROUND_HALF_UP));
        response.setHeatIndex(hm.divide(area, 3, BigDecimal.ROUND_HALF_UP));

        BigDecimal tgVar = map2.get(tg);
        BigDecimal thVar = map2.get(th);
        BigDecimal fmVar = map2.get(fm);
        //一次供
        response.setHeatSourceTg(tgVar);
        //一次回
        response.setHeatSourceTh(thVar);
        response.setRealFlow(fmVar);
        return response;
    }
    //endregion


    //region 热力站
    final String HM_HT = "HM_HT";//热量
    final String WM_FT = "WM_FT";//流量

    @ApiOperation("热力站")
    @PostMapping("/baseStation")
    public List<ForecastResponse> findHistoryDataStation(@RequestBody ForecastRequest dto) {
        List<ForecastResponse> res = new ArrayList<>();
        dto.getRelevanceId().forEach(e -> {
            StationFirstNetBaseView one = getStationArea(e);
            Map<String, BigDecimal> historyData = getStationData(dto, one.getHeatSystemId());
            final long num;//天数或者小时数
            switch (dto.getType()) {
                case 1:/* 小时 */
                    num = (dto.getEndTime() - dto.getStartTime()) / oneHour;
                    break;
                case 2:/* 天 */
                    num = (dto.getEndTime() - dto.getStartTime()) / oneDay;
                    break;
                default:
                    throw new RuntimeException("数据类型不存在");
            }
            res.add(this.setForecastBase(historyData, one.getHeatSourceArea(), num));
        });
        return res;
    }

    private Map<String, BigDecimal> getStationData(ForecastRequest dto, int id) {
        QueryEsDto esDto = new QueryEsDto();
        esDto.setIncludeFields(new String[]{HM_HT, WM_FT});
        esDto.setStart(dto.getStartTime());
        esDto.setEnd(dto.getEndTime());
        switch (dto.getType()) {
            case 1:/* 小时 */
                esDto.setDocument(HistoryDocument.HOUR);
                break;
            case 2:/* 天 */
                esDto.setDocument(HistoryDocument.DAY);
                break;
            default:
                throw new RuntimeException("数据类型不存在");
        }
        //获取面积 包含id:热源系统id, heatArea:热源系统面积
        esDto.setHeatSystemId(new Integer[]{id});
        esDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
        esDto.setIncludeTotal(false);
        List<Map<String, Object>> historyData = this.historyData.findHistoryEnergyData(esDto);
        Map<String, BigDecimal> res = new HashMap<>();
        AtomicInteger i = new AtomicInteger(0);
        for (Map<String, Object> historyDatum : historyData) {
            System.out.println(historyDatum);
            Object o1 = (JSON.parseObject(historyDatum.get(HM_HT).toString(), HashMap.class)).get("consumption");
            Object o2 = (JSON.parseObject(historyDatum.get(WM_FT).toString(), HashMap.class)).get("consumption");
            BigDecimal var1 = new BigDecimal(o1.toString());
            BigDecimal var2 = new BigDecimal(o2.toString());
            res.put(HM_HT, var1.add(res.get(HM_HT) != null ? res.get(HM_HT) : new BigDecimal("0")));
            res.put(WM_FT, var2.add(res.get(WM_FT) != null ? res.get(WM_FT) : new BigDecimal("0")));
            i.addAndGet(1);
        }
        return res;
    }


    private ForecastResponse setForecastBase(Map<String, BigDecimal> map, BigDecimal area, long num) {
//		return getForecastResponse(map,map, area, num, HM_HT, WM_FT);
        return null;
    }


    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;

    private StationFirstNetBaseView getStationArea(int stationId) {
        LambdaQueryWrapper<StationFirstNetBaseView> eq = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getNumber, 1)
                .eq(StationFirstNetBaseView::getStatus, true)
                .eq(StationFirstNetBaseView::getHeatTransferStationId, stationId);
        List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list(eq);
        if (list.size() > 0) {
            return list.get(0);
        }
        throw new RuntimeException("查询的换热站不存在");
    }
//
//	//endregion
}
