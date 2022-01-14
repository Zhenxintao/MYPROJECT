package com.bmts.heating.web.forecast.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.db.mapper.ForecastSourceHeatSeasonMapper;
import com.bmts.heating.commons.db.mapper.ForecastSourceHistoryMapper;
import com.bmts.heating.commons.db.service.ForecastSourceHeatSeasonService;
import com.bmts.heating.commons.entiy.common.ForecastType;
import com.bmts.heating.commons.entiy.forecast.SearchDataDto;
import com.bmts.heating.commons.entiy.forecast.response.*;
import com.bmts.heating.commons.entiy.gathersearch.request.CurveXTextResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastCoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/4/20 17:28
 **/
@RestController
@Api(tags = "负荷预测首页相关")
@RequestMapping("forecast/core")
@Slf4j
public class ForecastCoreController {

    @Autowired
    ForecastSourceHistoryMapper forecastSourceHistoryMapper;
    @Autowired
    ForecastSourceHeatSeasonService forecastSourceHeatSeasonService;
    @Autowired
    ForecastCoreService forecastCoreService;
    @Autowired
    private ForecastSourceHeatSeasonMapper forecastSourceHeatSeasonMapper;

    @ApiOperation(value = "实际流量/预测流量", response = CurveXTextResponse.class)
    @PostMapping("/sources/flow")
    public Response sourcesFlow() {

        QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
        queryWrapperHistory.eq("c.isValid", true);
        queryWrapperHistory.eq("h.type", ForecastType.Week.value());
        queryWrapperHistory.ge("h.endTime", LocalDateTime.now());
        queryWrapperHistory.le("h.startTime", LocalDateTime.now());
        List<ForecastDataResponse> list = forecastSourceHistoryMapper.listForecastDataResponse(queryWrapperHistory);

        if (list.size() <= 0)
            return Response.success();
        //取采暖季在当前时间范围内的预测流量设置
        List<ForecastSourceHeatSeason> forecastSourceHeatSeasons = forecastSourceHeatSeasonService.list(Wrappers.<ForecastSourceHeatSeason>lambdaQuery().le(ForecastSourceHeatSeason::getStartTime, LocalDateTime.now()).ge(ForecastSourceHeatSeason::getEndTime, LocalDateTime.now()));

        CurveXTextResponse curveXTextResponse = new CurveXTextResponse();
        List<String> x = new ArrayList<>();
        List<BigDecimal> realFlow = new ArrayList<>();
        List<BigDecimal> forecastFlow = new ArrayList<>();
        for (ForecastDataResponse forecastSourceHistory : list) {
            x.add(forecastSourceHistory.getForecastSourceName());
            if (forecastSourceHistory.getRealFlow() != null) {
                realFlow.add(forecastSourceHistory.getRealFlow());
            } else {
                realFlow.add(BigDecimal.ZERO);
            }
            ForecastSourceHeatSeason forecastSourceHeatSeason = forecastSourceHeatSeasons.stream().filter(f -> f.getForcastSourceCoreId() == forecastSourceHistory.getForecastSourceCoreId()).findFirst().orElse(null);
            if (forecastSourceHeatSeason == null) {
                log.warn("预测流量数据没有取到 name:{},id:{}", forecastSourceHistory.getForecastSourceName(), forecastSourceHistory.getForecastSourceCoreId());
                /**
                 * 如果预测未设置 暂时取实际流量
                 */
                forecastFlow.add(forecastSourceHistory.getRealFlow());
            } else forecastFlow.add(forecastSourceHeatSeason.getFirstNetStageFlow());

        }
        Map<String, List<BigDecimal>> realFlowMap = new HashMap<>();
        realFlowMap.put("实际流量", realFlow);
        realFlowMap.put("预测流量", forecastFlow);
        curveXTextResponse.setXLabels(x);
        curveXTextResponse.setYValues(realFlowMap);
        return Response.success(curveXTextResponse);
    }

    /**
     * 负荷预测首页数据昨日对比及同比
     */
    @ApiOperation(value = "负荷预测首页数据昨日对比及同比", response = IndexCompareResponse.class)
    @GetMapping("/sources/forecastCoreCompare")
    public Response forecastCoreCompare(@RequestParam Integer id) {
        return forecastCoreService.forecastCoreCompare(id);
    }

    /**
     * 负荷预测首页数据完成率信息
     */
    @ApiOperation(value = "负荷预测首页数据完成率信息", response = IndexPerformanceResponse.class)
    @GetMapping("/sources/forecastCorePerformance")
    public Response forecastCorePerformance(@RequestParam Integer id) {
        return forecastCoreService.forecastCorePerformance(id);
    }

    /**
     * 负荷预测首页数据指标评价信息
     */
    @ApiOperation(value = "负荷预测首页数据指标评价信息", response = IndexEvaluateResponse.class)
    @GetMapping("/sources/forecastCoreEvaluate")
    public Response forecastCoreEvaluate(@RequestParam Integer id) {
        return forecastCoreService.forecastCoreEvaluate(id);
    }


    @ApiOperation(value = "小时负荷(预测/实际)", response = ForecastDataResponse.class)
    @PostMapping("/forecastLoad")
    public Response forecastLoad(@RequestBody SearchDataDto dto) {
        List<ForecastDataResponse> listResponse = new ArrayList<>();
        // 0 为全部信息
        if (dto.getForecastSourceCoreId() == 0) {
            QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
            queryWrapperHistory.eq("c.isValid", true);
            queryWrapperHistory.eq("h.type", dto.getForecastType());
            queryWrapperHistory.le("h.endTime", dto.getEndTime());
            queryWrapperHistory.ge("h.startTime", dto.getStartTime());
            List<ForecastDataResponse> forecastDataResponseList = forecastSourceHistoryMapper.listForecastDataResponse(queryWrapperHistory);
            if (!CollectionUtils.isEmpty(forecastDataResponseList)) {
                // 进行时间区间的分组
                Map<String, List<ForecastDataResponse>> timeMap = forecastDataResponseList.stream().collect(Collectors.groupingBy(e -> e.getStartTime().toString() + e.getEndTime().toString()));
                for (String key : timeMap.keySet()) {
                    List<ForecastDataResponse> groupList = timeMap.get(key);
                    // 进行累加处理
                    // 实际热量  预测热量  实际流量  预测流量   是进行合计值
                    BigDecimal realHeatTotal = BigDecimal.ZERO;
                    BigDecimal forecastHeatTotal = BigDecimal.ZERO;
                    BigDecimal realFlow = BigDecimal.ZERO;

                    // 预测室外温度、 实际室外温度、 预测一次供温、实际一次供温   是平均值
                    int sizeHistory = 0;
                    BigDecimal forecastOutTemp = BigDecimal.ZERO;
                    BigDecimal realOutTemp = BigDecimal.ZERO;
                    BigDecimal forecastT1g = BigDecimal.ZERO;
                    BigDecimal realT1g = BigDecimal.ZERO;

                    for (ForecastDataResponse forecastDataResponse : groupList) {
                        if (forecastDataResponse.getRealHot() != null) {
                            realHeatTotal = realHeatTotal.add(forecastDataResponse.getRealHot());
                        }
                        if (forecastDataResponse.getForecastHot() != null) {
                            forecastHeatTotal = forecastHeatTotal.add(forecastDataResponse.getForecastHot());
                        }
                        if (forecastDataResponse.getRealFlow() != null) {
                            realFlow = realFlow.add(forecastDataResponse.getRealFlow());
                        }
                        if (forecastDataResponse.getForecastOutTemp() != null) {
                            forecastOutTemp = forecastOutTemp.add(forecastDataResponse.getForecastOutTemp());
                        }
                        if (forecastDataResponse.getRealOutTemp() != null) {
                            realOutTemp = realOutTemp.add(forecastDataResponse.getRealOutTemp());
                        }
                        if (forecastDataResponse.getForecastTg() != null) {
                            forecastT1g = forecastT1g.add(forecastDataResponse.getForecastTg());
                        }
                        if (forecastDataResponse.getRealT1g() != null) {
                            realT1g = realT1g.add(forecastDataResponse.getRealT1g());
                        }
                        sizeHistory++;
                    }

                    if (sizeHistory > 0) {
                        // 计算 预测室外温度  实际室外温度    的平均值
                        forecastOutTemp = forecastOutTemp.divide(new BigDecimal(sizeHistory), 2, BigDecimal.ROUND_HALF_UP);
                        realOutTemp = realOutTemp.divide(new BigDecimal(sizeHistory), 2, BigDecimal.ROUND_HALF_UP);
                        forecastT1g = forecastT1g.divide(new BigDecimal(sizeHistory), 2, BigDecimal.ROUND_HALF_UP);
                        realT1g = realT1g.divide(new BigDecimal(sizeHistory), 2, BigDecimal.ROUND_HALF_UP);
                    }
                    // 每个小时或每天     的量进行计算
                    ForecastDataResponse forecastDataResponse = new ForecastDataResponse();
                    forecastDataResponse.setRealHot(realHeatTotal);
                    forecastDataResponse.setForecastHot(forecastHeatTotal);
                    forecastDataResponse.setForecastOutTemp(forecastOutTemp);
                    forecastDataResponse.setRealOutTemp(realOutTemp);
                    forecastDataResponse.setForecastTg(forecastT1g);
                    forecastDataResponse.setRealT1g(realT1g);
                    forecastDataResponse.setStartTime(groupList.get(0).getStartTime());
                    forecastDataResponse.setEndTime(groupList.get(0).getEndTime());
                    listResponse.add(forecastDataResponse);
                }
            }
        } else {
            // forecastSourceCoreId  不为0
            QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
            queryWrapperHistory.eq("c.isValid", true);
            queryWrapperHistory.eq("h.type", dto.getForecastType());
            queryWrapperHistory.le("h.endTime", dto.getEndTime());
            queryWrapperHistory.ge("h.startTime", dto.getStartTime());
            queryWrapperHistory.eq("h.forecastSourceCoreId", dto.getForecastSourceCoreId());
            List<ForecastDataResponse> forecastDataResponseList = forecastSourceHistoryMapper.listForecastDataResponse(queryWrapperHistory);
            listResponse.addAll(forecastDataResponseList);
        }
        return Response.success(listResponse);
    }


    @ApiOperation(value = "七日负荷预测", response = ForecastHeatSeasonResponse.class)
    @PostMapping("/dailyLoad")
    public Response dailyLoad(@RequestBody SearchDataDto dto) {
        ForecastHeatSeasonResponse forecastHeatSeasonResponse = new ForecastHeatSeasonResponse();
        // 0 为全部信息
        if (dto.getForecastSourceCoreId() == 0) {

            //  处理实际流量
            List<SeasonCurve> seasonCurveList = new ArrayList<>();

            // 预测 总计 热指标
            BigDecimal forecastThermalIndex = BigDecimal.ZERO;
            // 实际 总计 热指标
            BigDecimal realThermalIndex = BigDecimal.ZERO;


            QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
            queryWrapperHistory.eq("c.isValid", true);
            queryWrapperHistory.eq("h.type", dto.getForecastType());
            queryWrapperHistory.le("h.endTime", dto.getEndTime());
            queryWrapperHistory.ge("h.startTime", dto.getStartTime());
            List<ForecastDataResponse> forecastDataResponseList = forecastSourceHistoryMapper.listForecastDataResponse(queryWrapperHistory);
            if (!CollectionUtils.isEmpty(forecastDataResponseList)) {
                // 进行时间区间的分组
                Map<String, List<ForecastDataResponse>> timeMap = forecastDataResponseList.stream().collect(Collectors.groupingBy(e -> e.getStartTime().toString() + e.getEndTime().toString()));
                for (String key : timeMap.keySet()) {
                    List<ForecastDataResponse> groupList = timeMap.get(key);
                    // 处理预测流量
                    BigDecimal forecastFlow = BigDecimal.ZERO;
                    //处理  预测热量 是进行合计值
                    BigDecimal forecastHot = BigDecimal.ZERO;
                    for (ForecastDataResponse forecastDataResponse : groupList) {
                        if (forecastDataResponse.getRealFlow() != null) {
                            forecastHot = forecastHot.add(forecastDataResponse.getForecastHot());
                        }
                        if (forecastDataResponse.getForecastThermalIndex() != null) {
                            forecastThermalIndex = forecastThermalIndex.add(forecastDataResponse.getForecastThermalIndex());
                        }
                        if (forecastDataResponse.getRealThermalIndex() != null) {
                            realThermalIndex = realThermalIndex.add(forecastDataResponse.getRealThermalIndex());
                        }
                        if (forecastDataResponse.getScheduleFlow() != null) {
                            forecastFlow = forecastFlow.add(forecastDataResponse.getScheduleFlow());
                        }

                    }

                    SeasonCurve seasonCurve = new SeasonCurve();
                    seasonCurve.setStartTime(groupList.get(0).getStartTime());
                    seasonCurve.setEndTime(groupList.get(0).getEndTime());
                    seasonCurve.setForecastFlow(forecastFlow);
                    // 预测热量
                    seasonCurve.setForecastHot(forecastHot);
                    seasonCurveList.add(seasonCurve);

                }
            }
            forecastHeatSeasonResponse.setListSeasonCurve(seasonCurveList);
            forecastHeatSeasonResponse.setForecastThermalIndex(forecastThermalIndex);
            forecastHeatSeasonResponse.setRealThermalIndex(realThermalIndex);


        } else {
            // forecastSourceCoreId  不为0

            //  处理预测流量
            List<SeasonCurve> seasonCurveList = new ArrayList<>();
            // 预测 总计 热指标
            BigDecimal forecastThermalIndex = BigDecimal.ZERO;
            // 实际 总计 热指标
            BigDecimal realThermalIndex = BigDecimal.ZERO;


            QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
            queryWrapperHistory.eq("c.isValid", true);
            queryWrapperHistory.eq("h.type", dto.getForecastType());
            queryWrapperHistory.le("h.endTime", dto.getEndTime());
            queryWrapperHistory.ge("h.startTime", dto.getStartTime());
            queryWrapperHistory.eq("h.forecastSourceCoreId", dto.getForecastSourceCoreId());
            List<ForecastDataResponse> forecastDataResponseList = forecastSourceHistoryMapper.listForecastDataResponse(queryWrapperHistory);
            for (ForecastDataResponse forecastData : forecastDataResponseList) {
                if (forecastData.getForecastThermalIndex() != null) {
                    forecastThermalIndex = forecastThermalIndex.add(forecastData.getForecastThermalIndex());
                }
                if (forecastData.getRealThermalIndex() != null) {
                    realThermalIndex = realThermalIndex.add(forecastData.getRealThermalIndex());
                }

                SeasonCurve seasonCurve = new SeasonCurve();
                seasonCurve.setStartTime(forecastData.getStartTime());
                seasonCurve.setEndTime(forecastData.getEndTime());
                // 预测热量
                seasonCurve.setForecastHot(forecastData.getForecastHot());
                seasonCurve.setForecastFlow(forecastData.getScheduleFlow());
                seasonCurveList.add(seasonCurve);
            }

            forecastHeatSeasonResponse.setRealThermalIndex(realThermalIndex);
            forecastHeatSeasonResponse.setForecastThermalIndex(forecastThermalIndex);
            forecastHeatSeasonResponse.setListSeasonCurve(seasonCurveList);
            forecastHeatSeasonResponse.setForcastSourceCoreId(dto.getForecastSourceCoreId());

        }

        return Response.success(forecastHeatSeasonResponse);
    }


}
