package com.bmts.heating.web.forecast.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.forecast.TempSetting;
import com.bmts.heating.commons.entiy.forecast.response.IndexEvaluateResponse;
import com.bmts.heating.commons.entiy.forecast.response.IndexCompareResponse;
import com.bmts.heating.commons.entiy.forecast.response.IndexPerformanceResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastCoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForecastCoreServiceImpl extends SavantServices implements ForecastCoreService {
    @Autowired
    private ForecastSourceHistoryService forecastSourceHistoryService;
    @Autowired
    private ForecastSourceEvaluationService forecastSourceEvaluationService;
    @Autowired
    private ForecastSourceDetailService forecastSourceDetailService;
    @Autowired
    private ForecastSourceBasicService forecastSourceBasicService;
    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private WeatherDayService weatherDayService;

    /**
     * 负荷预测首页数据昨日对比及同比
     */
    @Override
    public Response forecastCoreCompare(Integer id) {
        try {
            LocalDateTime realTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            LocalDateTime endRealTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime ayerTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            LocalDateTime endAyerTime = LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MIN);
            LocalDateTime yoyTime = LocalDateTime.of(LocalDate.now().plusMonths(-1), LocalTime.MAX);
            LocalDateTime endYoyTime = LocalDateTime.of(LocalDate.now().plusDays(-1).plusMonths(-1), LocalTime.MIN);
            IndexCompareResponse indexCompareResponse = new IndexCompareResponse() {{
                setForecastTemp(forecastTemp(endRealTime));
                setAyerTemp(forecastTemp(endAyerTime));
                setYoyTemp(forecastTemp(endYoyTime));
                setForecastHeatLoad(sumHeatLoad(realTime, endRealTime, id, 1).getForecastHot());
                setAyerHeatLoad(sumHeatLoad(ayerTime, endAyerTime, id, 2).getForecastHot());
                setYoyHeatLoad(sumHeatLoad(yoyTime, endYoyTime, id, 2).getForecastHot());
                setHeatUnitConsumption(sumHeatLoad(realTime, endRealTime, id, 1).getForecastThermalIndex());
                setAyerHeatUnitConsumption(sumHeatLoad(ayerTime, endAyerTime, id, 2).getForecastThermalIndex());
                setYoyHeatUnitConsumption(sumHeatLoad(yoyTime, endYoyTime, id, 2).getForecastThermalIndex());
            }};
//            IndexCompareResponse indexCompareResponse = new IndexCompareResponse() {{
//                setForecastTemp(forecastTemp(endRealTime));
//                setAyerTemp(forecastTemp(endAyerTime));
//                setYoyTemp(forecastTemp(endYoyTime));
//                setForecastHeatLoad(sumHeatLoad(realTime, endRealTime, id, 1).getRealHot().multiply(new BigDecimal(1.2)));
//                setAyerHeatLoad(sumHeatLoad(ayerTime, endAyerTime, id, 2).getRealHot().multiply(new BigDecimal(1.2)));
//                setYoyHeatLoad(sumHeatLoad(yoyTime, endYoyTime, id, 2).getRealHot().multiply(new BigDecimal(1.2)));
//                setHeatUnitConsumption(sumHeatLoad(realTime, endRealTime, id, 1).getRealThermalIndex().multiply(new BigDecimal(1.2)));
//                setAyerHeatUnitConsumption(sumHeatLoad(ayerTime, endAyerTime, id, 2).getRealThermalIndex().multiply(new BigDecimal(1.2)));
//                setYoyHeatUnitConsumption(sumHeatLoad(yoyTime, endYoyTime, id, 2).getRealThermalIndex().multiply(new BigDecimal(1.2)));
//            }};
            return Response.success(indexCompareResponse);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    /**
     * 负荷预测首页数据完成率信息
     */
    @Override
    public Response forecastCorePerformance(Integer id) {
        try {
            IndexPerformanceResponse indexPerformanceResponse = new IndexPerformanceResponse();
            LocalDateTime realTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            LocalDateTime endRealTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            ForecastSourceBasic com = forecastSourceBasic();
            ForecastSourceHistory forecastSourceHistory = sumHeatLoad(realTime, endRealTime, id, 1);
            BigDecimal forecastHot = forecastSourceHistory.getForecastHot();
            BigDecimal realHot = forecastSourceHistory.getRealHot();
           //演示数据
            // BigDecimal forecastHot = realHot.multiply(new BigDecimal(1.2));
            ForecastSourceHistory seasonHistroy = sumHeatLoad(com.getEndTime(), com.getStartTime(), id, 2);
            indexPerformanceResponse.setDayRealHeat(realHot);
            indexPerformanceResponse.setDayForecastHeat(forecastHot);
            float seasonRealHot = seasonHistroy.getRealHot().floatValue() + realHot.floatValue();
            float seasonForecastHot = seasonHistroy.getForecastHot().floatValue() + forecastHot.floatValue();
//演示数据
            //            float seasonForecastHot = seasonRealHot * (float) 1.2;
            indexPerformanceResponse.setSeasonRealHeat(new BigDecimal(seasonRealHot));
            indexPerformanceResponse.setSeasonForecastHeat(new BigDecimal(seasonForecastHot));
            indexPerformanceResponse.setForecastHeatLoadList(forecastInfo(id));
            return Response.success(indexPerformanceResponse);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    /**
     * 负荷预测首页数据指标评价信息
     */
    @Override
    public Response forecastCoreEvaluate(Integer id) {
        try {
            ForecastSourceBasic com = forecastSourceBasic();
            ForecastSourceHistory forecastSourceHistory = sumHeatLoad(null, null, id, 1);
            BigDecimal dayrealHot = forecastSourceHistory.getRealHot();
            BigDecimal dayforecasthotIndex = forecastSourceHistory.getForecastThermalIndex();
            BigDecimal dayerealhotIndex = forecastSourceHistory.getRealThermalIndex();
            ForecastSourceHistory seasonHistroy = sumHeatLoad(com.getEndTime(), com.getStartTime(), id, 2);
            float realhot = seasonHistroy.getRealHot().floatValue() + dayrealHot.floatValue();
            float forecasthot;
            if (id == 0) {
                QueryWrapper<ForecastSourceCore> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("isValid", true);
                queryWrapper.select("sum(desiredValue) as desiredValue");
                forecasthot = forecastSourceCoreService.getOne(queryWrapper).getDesiredValue().floatValue();
            } else {
                forecasthot = forecastSourceCoreService.getById(id).getDesiredValue().floatValue();
            }
            float realhotIndex = seasonHistroy.getRealThermalIndex().floatValue() + dayerealhotIndex.floatValue();
            float forecasthotIndex = seasonHistroy.getForecastThermalIndex().floatValue() + dayforecasthotIndex.floatValue();
            float status = forecasthot - realhot;
           float evaluationValue = realhot / forecasthot * 100;
//            float evaluationValue = realhot / (realhot * (float) 1.2) * 100;
            String evaluationIndex;
            List<ForecastSourceEvaluation> forecastSourceEvaluations = forecastSourceEvaluationService.list();
            if (forecastSourceEvaluations.stream().count() == 0) {
                evaluationIndex = "优";
            } else {
                Optional<ForecastSourceEvaluation> maxObj = forecastSourceEvaluations.stream().max(Comparator.comparing(ForecastSourceEvaluation::getMaxExtent));
                Optional<ForecastSourceEvaluation> minObj = forecastSourceEvaluations.stream().min(Comparator.comparing(ForecastSourceEvaluation::getMinExtent));
                if (evaluationValue >= maxObj.get().getMaxExtent().floatValue()) {
                    evaluationIndex = maxObj.get().getEvaluationIndex();
                } else if (evaluationValue <= minObj.get().getMinExtent().floatValue()) {
                    evaluationIndex = minObj.get().getEvaluationIndex();
                } else {
                    ForecastSourceEvaluation forecastSourceEvaluation = forecastSourceEvaluations.stream().filter(s -> evaluationValue >= s.getMinExtent().floatValue() && evaluationValue <= s.getMaxExtent().floatValue()).findFirst().orElse(null);
                    if (forecastSourceEvaluation != null) {
                        evaluationIndex = forecastSourceEvaluation.getEvaluationIndex();
                    } else {
                        evaluationIndex = "差";
                    }

                }
            }
            IndexEvaluateResponse forecastEvaluateResponse = new IndexEvaluateResponse() {{
                setRealhot(realhot);
                setRealhotIndex(realhotIndex);
                setForecasthot(forecasthot);
                setForecasthotIndex(forecasthotIndex);
                setStatus(-status);
                setEvaluationValue(evaluationValue);
                setEvaluationInfo(evaluationIndex);
            }};
            //演示数据
//            IndexEvaluateResponse forecastEvaluateResponse = new IndexEvaluateResponse() {{
//                setRealhot(realhot);
//                setRealhotIndex(realhotIndex);
//                setForecasthot(realhot * (float) 1.2);
//                setForecasthotIndex(realhotIndex * (float) 1.2);
//                setStatus(-(realhot * (float) 1.2 - realhot));
//                setEvaluationValue(evaluationValue);
//                setEvaluationInfo(evaluationIndex);
//            }};
            return Response.success(forecastEvaluateResponse);
        } catch (Exception e) {
            return Response.fail();
        }

    }

    public ForecastSourceBasic forecastSourceBasic() {
        QueryWrapper<ForecastSourceBasic> queryCommonHeatSeason = new QueryWrapper<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        queryCommonHeatSeason.lt("startTime", df.format(new Date())).gt("endTime", df.format(new Date()));
        List<ForecastSourceBasic> comList = forecastSourceBasicService.list(queryCommonHeatSeason);
        ForecastSourceBasic com = comList.stream().findFirst().orElse(null);
        return com;
    }

    @Override
    public BigDecimal forecastTemp(LocalDateTime time) {
        WeatherDay forecastTemp = weatherDayService.getOne(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, time));
        if (forecastTemp != null)
            return forecastTemp.getAvgTemp();
        return new BigDecimal(0);
    }

    @Override
    public ForecastSourceHistory sumHeatLoad(LocalDateTime end, LocalDateTime start, Integer id, Integer type) {
        QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
        if (id == 0) {
            if (type == 1) {
                queryWrapper.eq("data_status", 0).eq("type", 1).select("sum(forecastHot) as forecastHot,sum(forecastThermalIndex) as forecastThermalIndex,sum(realHot) as realHot,sum(realThermalIndex) as realThermalIndex");
            } else {
                queryWrapper.ge("startTime", start).le("endTime", end).eq("data_status", 1).eq("type", 1).select("sum(realHot) as realHot,sum(realThermalIndex) as realThermalIndex,sum(forecastHot) as forecastHot,sum(forecastThermalIndex) as forecastThermalIndex");
            }
        } else {
            if (type == 1) {
                queryWrapper.eq("forecastSourceCoreId", id).eq("data_status", 0).eq("type", 1).select("sum(forecastHot) as forecastHot,sum(forecastThermalIndex) as forecastThermalIndex,sum(realHot) as realHot,sum(realThermalIndex) as realThermalIndex");
            } else {
                queryWrapper.eq("forecastSourceCoreId", id).ge("startTime", start).le("endTime", end).eq("data_status", 1).eq("type", 1).select("sum(realHot) as realHot,sum(realThermalIndex) as realThermalIndex,sum(forecastHot) as forecastHot,sum(forecastThermalIndex) as forecastThermalIndex");
            }
        }
        ForecastSourceHistory value = forecastSourceHistoryService.getOne(queryWrapper);
        if (value == null) {
            return new ForecastSourceHistory() {{
                setForecastHot(new BigDecimal(0));
                setForecastThermalIndex(new BigDecimal(0));
                setRealHot(new BigDecimal(0));
                setRealThermalIndex(new BigDecimal(0));
            }};
        }
//        //测试展示使用
//        ForecastSourceHistory absReturn = new ForecastSourceHistory();
//        absReturn.setForecastHot(new BigDecimal(Math.abs(value.getForecastHot().floatValue())));
//        absReturn.setForecastThermalIndex(new BigDecimal(Math.abs(value.getForecastThermalIndex().floatValue())));
//        absReturn.setRealHot(new BigDecimal(Math.abs(value.getRealHot().floatValue())));
//        absReturn.setRealThermalIndex(new BigDecimal(Math.abs(value.getRealThermalIndex().floatValue())));
//        return absReturn;
        return value;
    }

//    @Override
//    public ForecastResponse sumHeatUnitConsumption( LocalDateTime end,LocalDateTime start, Integer id) {
//
//
////        List<SourceFirstNetBaseView> sourceFirstNetBaseViewList = Arrays.asList(tsccRestTemplate.get("/common/sourceFirstNetBase", baseServer, SourceFirstNetBaseView[].class));
////        List<Integer> id = sourceFirstNetBaseViewList.stream().filter(s -> ids.contains(s.getHeatSourceId())).map(SourceFirstNetBaseView::getHeatSystemId).collect(Collectors.toList());
////        ZoneId zone = ZoneId.systemDefault();
////        Long startLong = start.atZone(zone).toInstant().toEpochMilli();
////        Long endLong = start.atZone(zone).toInstant().toEpochMilli();
////        ForecastRequest forecastRequest = new ForecastRequest() {{
////            setRelevanceId(id);
////            setStartTime(startLong);
////            setEndTime(endLong);
////            setType(2);
////        }};
////        ForecastResponse forecastResponse = tsccRestTemplate.post("/forecast/baseSource", forecastRequest, searchServer, ForecastResponse.class);
////        return forecastResponse;
//    }

    @Override
    public List<IndexPerformanceResponse.ForecastHeatLoad> forecastInfo(Integer id) {
        QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_status", 0).eq("type", 1);
        List<IndexPerformanceResponse.ForecastHeatLoad> list = new ArrayList<>();
        if (id == 0) {
            List<ForecastSourceHistory> forecastSourceHistoryList = forecastSourceHistoryService.list(queryWrapper);
            double sumForecastLoad = forecastSourceHistoryList.stream().mapToDouble(s -> s.getForecastHot().doubleValue()).sum();
            double sumForecastFlow = forecastSourceHistoryList.stream().mapToDouble(s -> s.getScheduleFlow().doubleValue()).sum();
            double avgTg = forecastSourceHistoryList.stream().mapToDouble(s -> s.getForecastT1g().doubleValue()).average().orElse(0D);
            double avgTh = forecastSourceHistoryList.stream().mapToDouble(s -> s.getForecastT1h().doubleValue()).average().orElse(0D);
            IndexPerformanceResponse.ForecastHeatLoad obj = new IndexPerformanceResponse.ForecastHeatLoad() {{
                setPredictHeatRate(new BigDecimal(Double.valueOf(sumForecastLoad)));
                setFirstNetCycleFlow(new BigDecimal(Double.valueOf(sumForecastFlow)));
                setFirstNetTg(new BigDecimal(Double.valueOf(avgTg)));
                setFirstNetTh(new BigDecimal(Double.valueOf(avgTh)));
            }};
            list.add(obj);
        } else {
            queryWrapper.eq("forecastSourceCoreId", id);
            List<ForecastSourceHistory> forecastSourceHistoryList = forecastSourceHistoryService.list(queryWrapper);
            forecastSourceHistoryList.stream().forEach(s -> {
                IndexPerformanceResponse.ForecastHeatLoad obj = new IndexPerformanceResponse.ForecastHeatLoad() {{
                    setStartTime(s.getStartTime());
                    setEndTime(s.getEndTime());
                    setFirstNetTg(s.getForecastT1g());
                    setFirstNetTh(s.getForecastT1h());
                    setFirstNetCycleFlow(s.getScheduleFlow());
                    setPredictHeatRate(s.getForecastHot());
                }};
                list.add(obj);
            });
        }
        return list;
    }

    @Override
    public List<Integer> forecastSourceDetailList(Integer id) {
        List<ForecastSourceDetail> forecastSourceDetailList;
        if (id == 0) {
            forecastSourceDetailList = forecastSourceDetailService.list();
        } else {
            forecastSourceDetailList = forecastSourceDetailService.list(Wrappers.<ForecastSourceDetail>lambdaQuery().eq(ForecastSourceDetail::getForcastSourceCoreId, id));
        }
        List<Integer> ids = forecastSourceDetailList.stream().map(ForecastSourceDetail::getHeatSourceId).collect(Collectors.toList());
        return ids;
    }


//    public Object getForecastConfigTime()
//    {
//       List<ForecastSourceCore> list = forecastSourceCoreService.list();
//        List<TempSetting> tempSettingList = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//        for (ForecastSourceCore core:list) {
//            List<TempSetting> singleList = JSONObject.parseArray(core.getDispatch()).toJavaList(TempSetting.class);
//            tempSettingList.addAll(singleList);
//        }
//        Optional<TempSetting> max =  tempSettingList.stream().max(Comparator.comparing(TempSetting::getEndTime));
//        String str = max.get().getEndTime().substring(0,max.get().getEndTime().indexOf(":"));
//        return str;
//    }
}
