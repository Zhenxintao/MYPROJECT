package com.bmts.heating.web.energy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.commons.entiy.baseInfo.response.EnergyEvaluateResponse;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import com.bmts.heating.commons.entiy.energy.EvalulateReachStandard;
import com.bmts.heating.commons.entiy.gathersearch.request.HistoryDocument;
import com.bmts.heating.commons.entiy.gathersearch.request.HistorySourceType;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.utils.common.Tuple;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.compute.hitory.energy.UnitStandardCompute;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.EnergyEvaluateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/5/7 10:55
 **/
@Slf4j
@Service
public class EnergyEvaluateImpl implements EnergyEvaluateService {
    @Autowired
    CommonService commonService;

    @Override
    public Response evaluateStatistics(EnergyInfoDto dto) {
        if (dto.getDate() == null) {
            return Response.paramError("????????????date??????");
        }
        EnergyEvaluateResponse energyEvaluateResponse = new EnergyEvaluateResponse();
        //?????????
        WeatherDay weatherDay = commonService.queryDayWeather(dto.getDate().toString());
        List<StationFirstNetBaseView> firstNetBases = null;
        List<SourceFirstNetBaseView> firstNetBasesSource = null;
        //???????????????
        Tuple<List<ReachStandardResponse>, EnergyType> reach = reach(dto);
        QueryEsBucketDto queryEsBucketDto;
        if (dto.getType() == 1) {
            firstNetBasesSource = commonService.filterFirstNetBaseSource();
            queryEsBucketDto = this.buildParamSource(firstNetBasesSource, reach.second, dto);
        } else {
            firstNetBases = commonService.filterFirstNetBase();
            queryEsBucketDto = this.buildParamStation(firstNetBases, reach.second, dto);
        }


//        //???????????????
//        Tuple<List<ReachStandardResponse>, EnergyType> reach = reach(dto);
//        QueryEsBucketDto queryEsBucketDto = this.buildParam(firstNetBases, reach.second, dto);
        //?????????????????????
        List<Object> objects = commonService.queryEnergy(queryEsBucketDto);
        if (objects.size() > 0) {
            Map map = JSONObject.parseObject(JSONObject.toJSONString(objects.get(0)), Map.class);
            //????????????
            BigDecimal totalCost = new BigDecimal((String) map.get(reach.second.name()));

            //????????????
            String unitName = reach.second.name().concat("unitStandard");
            BigDecimal singleValue = new BigDecimal(0);
            if (map.containsKey(unitName)) {
                singleValue = new BigDecimal((String) map.get(unitName));
            }
            energyEvaluateResponse.setSingalCost(singleValue);
            if (objects.size() == 0) {
                log.error("????????????????????????????????????");
                return Response.success();
            }
            BigDecimal area;
            if (dto.getType() == 1) {
                assert firstNetBasesSource != null;
                area = this.computeArea(dto, firstNetBasesSource);
            } else {
                area = this.computeAreaStation(dto, firstNetBases);
            }
            if (dto.getEnergyType() == 3) {
                EnergyUnitStandardConfig energyUnitStandardConfig = commonService.queryUnitStandardConfig();
                //??? ????????????
                BigDecimal total = UnitStandardCompute.unitStandard(energyUnitStandardConfig.getOutdoorTemp(), energyUnitStandardConfig.getIndoorTemp(), weatherDay.getAvgTemp(), totalCost);
                energyEvaluateResponse.setNiggerHeadTotalCost(total);
                //???????????????
                BigDecimal single = UnitStandardCompute.unitStandard(energyUnitStandardConfig.getOutdoorTemp(), energyUnitStandardConfig.getIndoorTemp(), weatherDay.getAvgTemp(), singleValue);
                energyEvaluateResponse.setNiggerHeadSingalCost(single);
            }
            energyEvaluateResponse.setHeatTotalCost(totalCost);
            energyEvaluateResponse.setHeatArea(area);
        } else {
            log.error("??????????????????????????? {}", JSONObject.toJSONString(queryEsBucketDto));
        }
        energyEvaluateResponse.setOutsideTemp(weatherDay.getAvgTemp());
        energyEvaluateResponse.setReachStandardResponseList(reach.first);


        return Response.success(energyEvaluateResponse);
    }

//    public static void main(String[] args) {
////        System.out.print(LocalTimeUtils.);
//
//    }

    /**
     * @param firstNetBases
     * @return
     */
    private QueryEsBucketDto buildParamStation(List<StationFirstNetBaseView> firstNetBases, EnergyType energyType, EnergyInfoDto dto) {
        try {
            Integer[] systemIds = firstNetBases.stream().map(x -> x.getHeatSystemId()).toArray(Integer[]::new);
            QueryEsBucketDto queryEsBucketDto = new QueryEsBucketDto();
            List<TimeRange> timeRanges = new ArrayList<>();
            TimeRange timeRange = new TimeRange();
            Long start = LocalTimeUtils.getTimestampOfDateTime(dto.getDate().atTime(0, 0, 0));
            Long end = LocalTimeUtils.getTimestampOfDateTime(dto.getDate().atTime(23, 59, 59));
            timeRange.setStart(start);
            timeRange.setEnd(end);
            timeRanges.add(timeRange);
            queryEsBucketDto.setTimeRanges(timeRanges);
            queryEsBucketDto.setHeatSystemId(systemIds);
            queryEsBucketDto.setDocument(HistoryDocument.DAY);
            queryEsBucketDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
            String[] points = new String[]{energyType.name()};
            queryEsBucketDto.setIncludeFields(points);
            return queryEsBucketDto;

        } catch (Exception e) {
            log.error("build param cause error {}", e);
            return new QueryEsBucketDto();
        }


    }
    /**
     * @param firstNetBases
     * @return
     */
    private QueryEsBucketDto buildParamSource(List<SourceFirstNetBaseView> firstNetBases, EnergyType energyType, EnergyInfoDto dto) {
        try {
            Integer[] systemIds = firstNetBases.stream().map(SourceFirstNetBaseView::getHeatSystemId).toArray(Integer[]::new);
            QueryEsBucketDto queryEsBucketDto = new QueryEsBucketDto();
            List<TimeRange> timeRanges = new ArrayList<>();
            TimeRange timeRange = new TimeRange();
            Long start = LocalTimeUtils.getTimestampOfDateTime(dto.getDate().atTime(0, 0, 0));
            Long end = LocalTimeUtils.getTimestampOfDateTime(dto.getDate().atTime(23, 59, 59));
            timeRange.setStart(start);
            timeRange.setEnd(end);
            timeRanges.add(timeRange);
            queryEsBucketDto.setTimeRanges(timeRanges);
            queryEsBucketDto.setHeatSystemId(systemIds);
            queryEsBucketDto.setDocument(HistoryDocument.DAY);
            queryEsBucketDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
            String[] points = new String[]{energyType.name()};
            queryEsBucketDto.setIncludeFields(points);
            return queryEsBucketDto;

        } catch (Exception e) {
            log.error("build param cause error {}", e);
            return new QueryEsBucketDto();
        }


    }
    /**
     * ????????????
     *
     * @param dto
     * @param firstNetBases
     * @return
     */
    private BigDecimal computeArea(EnergyInfoDto dto, List<SourceFirstNetBaseView> firstNetBases) {
        //????????????
        BigDecimal area = new BigDecimal(0);
        //???
        Map<Integer, List<SourceFirstNetBaseView>> maps = firstNetBases.stream().collect(Collectors.groupingBy(SourceFirstNetBaseView::getHeatSourceId));

        for (Map.Entry<Integer, List<SourceFirstNetBaseView>> entry : maps.entrySet()) {
            area = area.add(entry.getValue().get(0).getHeatSourceArea());
        }
        return area;
    }

    /**
     * ????????????
     *
     * @param dto
     * @param firstNetBases
     * @return
     */
    private BigDecimal computeAreaStation(EnergyInfoDto dto, List<StationFirstNetBaseView> firstNetBases) {
        //????????????
        BigDecimal area = new BigDecimal(0);
        switch (dto.getType()) {
            case 1:
                //???
                Map<Integer, List<StationFirstNetBaseView>> maps = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSourceId));

                for (Map.Entry<Integer, List<StationFirstNetBaseView>> entry : maps.entrySet()) {
                    area = area.add(entry.getValue().get(0).getHeatSourceArea());
                }

                break;
//            case 2:
//                //???
//                Map<Integer, List<FirstNetBase>> mapNet = firstNetBases.stream().collect(Collectors.groupingBy(x -> x.getHeatNetId()));
//
//                for (Map.Entry<Integer, List<FirstNetBase>> entry : mapNet.entrySet()) {
//                    area = area.add(entry.getValue().get(0).getHeatNetArea());
//                }
//
//                break;
            case 3:
                //???
                Map<Integer, List<StationFirstNetBaseView>> mapStation = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatTransferStationId));

                for (Map.Entry<Integer, List<StationFirstNetBaseView>> entry : mapStation.entrySet()) {
                    area = area.add(entry.getValue().get(0).getHeatStationArea());
                }

                break;
            default:
                throw new RuntimeException("param is error");
        }
        return area;
    }

    /**
     * ???????????????
     *
     * @param dto
     */
    private Tuple<List<ReachStandardResponse>, EnergyType> reach(EnergyInfoDto dto) {
        EvaluateTarget evaluateTarget = mapTarget(dto.getType());
        EnergyType energyType = mapEnergyType(dto.getEnergyType(), evaluateTarget);
        EvalulateReachStandard build = EvalulateReachStandard.builder().target(evaluateTarget).date(dto.getDate()).energyType(energyType).build();
        List<ReachStandardResponse> reachStandardResponses = commonService.queryReach(build);
        return new Tuple<>(reachStandardResponses, energyType);
    }

    private EvaluateTarget mapTarget(int target) {
        switch (target) {
            case 1:
                return EvaluateTarget.heatSource;
            case 2:
                return EvaluateTarget.heatNet;
            case 3:
            default:
                return EvaluateTarget.station;
        }
    }

    private EnergyType mapEnergyType(Integer type, EvaluateTarget evaluateTarget) {
        if (evaluateTarget == EvaluateTarget.heatSource) {
            switch (type) {
                case 1:
                    return EnergyType.HeatSourceFTSupply;
                case 2:
                    return EnergyType.HeatSourceEp;
                case 3:
                default:
                    return EnergyType.HeatSourceTotalHeat_MtrG;
            }
        }
        switch (type) {
            case 1:
                return EnergyType.WM_FT;
            case 2:
                return EnergyType.Ep;
            case 3:
            default:
                return EnergyType.HM_HT;
        }
    }


//    public Integer area()
//    {
//
//    }

}
