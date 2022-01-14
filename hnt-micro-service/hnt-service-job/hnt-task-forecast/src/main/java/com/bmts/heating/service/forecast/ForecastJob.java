package com.bmts.heating.service.forecast;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.common.ForecastType;
import com.bmts.heating.commons.entiy.forecast.history.ForecastRequest;
import com.bmts.heating.commons.entiy.forecast.history.ForecastResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/4/26 11:27
 **/
@Component("forecast_job")
@Slf4j
public class ForecastJob implements Job {


    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private ForecastSourceDetailService forecastSourceDetailService;
    @Autowired
    private ForecastSourceHistoryService forecastSourceHistoryService;
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private WeatherHourService weatherHourService;
    @Autowired
    private WeatherDayService weatherDayService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //1.读取现有配置的负荷预测信息
        List<ForecastSourceCore> list = forecastSourceCoreService.list(Wrappers.<ForecastSourceCore>lambdaQuery().eq(ForecastSourceCore::getIsValid, true));
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("没有查询到负荷预测基础数据！");
        }
        List<ForecastSourceDetail> detailList = new ArrayList<ForecastSourceDetail>();
        for (ForecastSourceCore sourceCore : list) {
            // 查询热源信息
            List<ForecastSourceDetail> listSourceDetail = forecastSourceDetailService.list(Wrappers.<ForecastSourceDetail>lambdaQuery().eq(ForecastSourceDetail::getForcastSourceCoreId, sourceCore.getId()));
            if (CollectionUtils.isEmpty(listSourceDetail)) {
                throw new RuntimeException("没有查询到热源基础数据！");
            }
            // 根据热源信息查询
            if (!CollectionUtils.isEmpty(listSourceDetail)) {
                detailList.addAll(listSourceDetail);
            }
        }
        if (!CollectionUtils.isEmpty(detailList)) {
            Map<Integer, List<ForecastSourceDetail>> collectMap = detailList.stream().collect(Collectors.groupingBy(e -> e.getForcastSourceCoreId()));
            for (Integer forcastSourceCoreId : collectMap.keySet()) {
                //  按照  阶段  小时 天  进行分别处理
                // 处理小时 数据
                updateHour(collectMap, forcastSourceCoreId, ForecastType.HOUR.value());
                // 处理阶段数据
                updateStage(collectMap, forcastSourceCoreId, ForecastType.STAGE.value());
                // 处理天数据
                updateDay(collectMap, forcastSourceCoreId, ForecastType.Week.value());

            }
        }
    }

    /**
     * 处理小时数据
     */
    private void updateHour(Map<Integer, List<ForecastSourceDetail>> collectMap, Integer forcastSourceCoreId, int type) {
        LocalDateTime nowTime = LocalDateTime.now();
        QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("forecastSourceCoreId", forcastSourceCoreId);
        // queryWrapper.eq("data_status", false);
        // 查询小时的数据处理
        queryWrapper.ge("endTime", nowTime.minusHours(12));
        queryWrapper.le("endTime", nowTime);
        queryWrapper.eq("type", type);
        List<ForecastSourceHistory> listHistoory = forecastSourceHistoryService.list(queryWrapper);
        List<ForecastSourceHistory> updateList = new ArrayList<>();
        for (ForecastSourceHistory sourceHistory : listHistoory) {
            try {
                // 处理历史数据
                List<ForecastSourceDetail> sourceDetailList = collectMap.get(forcastSourceCoreId);
                if (!CollectionUtils.isEmpty(sourceDetailList)) {
                    List<Integer> sourceIdList = sourceDetailList.stream().map(ForecastSourceDetail::getHeatSourceId).collect(Collectors.toList());

                    // 组装请求参数
                    ForecastRequest requestDto = new ForecastRequest();
                    requestDto.setRelevanceId(sourceIdList);
                    // 处理小时数据
                    requestDto.setType(1);
                    requestDto.setStartTime(sourceHistory.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                    requestDto.setEndTime(sourceHistory.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());

                    Response sourceResponse = historyDataService.getSource(requestDto);
                    if (sourceResponse == null || sourceResponse.getData() == null) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理小时数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }
                    Gson gson = new Gson();
                    ForecastResponse[] forecastResponseList = gson.fromJson(gson.toJson(sourceResponse.getData()), ForecastResponse[].class);
                    if (forecastResponseList.length == 0) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理小时数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }

                    // 实际热量  实际负荷  实际热指标  实际流量  是进行合计值
                    BigDecimal realHeatTotal = BigDecimal.ZERO;
                    BigDecimal realHeatLoad = BigDecimal.ZERO;
                    BigDecimal realFlow = BigDecimal.ZERO;
                    BigDecimal heatIndex = BigDecimal.ZERO;
                    // 热源 实际一次供温   实际一次回温   是平均值
                    int sizeSource = 0;
                    BigDecimal heatSourceTg = BigDecimal.ZERO;
                    BigDecimal heatSourceTh = BigDecimal.ZERO;

                    for (ForecastResponse forecastResponse : forecastResponseList) {
                        if (forecastResponse.getRealHeatTotal() != null) {
                            realHeatTotal = realHeatTotal.add(forecastResponse.getRealHeatTotal());
                        }
                        if (forecastResponse.getRealHeatLoad() != null) {
                            realHeatLoad = realHeatLoad.add(forecastResponse.getRealHeatLoad());
                        }
                        if (forecastResponse.getRealFlow() != null) {
                            realFlow = realFlow.add(forecastResponse.getRealFlow());
                        }
                        if (forecastResponse.getHeatIndex() != null) {
                            heatIndex = heatIndex.add(forecastResponse.getHeatIndex());
                        }
                        if (forecastResponse.getHeatSourceTg() != null) {
                            heatSourceTg = heatSourceTg.add(forecastResponse.getHeatSourceTg());
                        }
                        if (forecastResponse.getHeatSourceTh() != null) {
                            heatSourceTh = heatSourceTh.add(forecastResponse.getHeatSourceTh());
                        }
                        sizeSource++;

                    }

                    if (sizeSource > 0) {
                        // 计算 热源 实际一次供温   实际一次回温   的平均值
                        heatSourceTg = heatSourceTg.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                        heatSourceTh = heatSourceTh.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                    }

                    boolean updatStatus = false;
                    ForecastSourceHistory updateSourceHistory = new ForecastSourceHistory();
                    updateSourceHistory.setId(sourceHistory.getId());
//                    if (realHeatTotal.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealHot(realHeatTotal);
//                    }
//                    if (realHeatLoad.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealLoad(realHeatLoad);
//                    }
//                    if (heatIndex.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealThermalIndex(heatIndex);
//                    }
//                    if (realFlow.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealFlow(realFlow);
//                    }
//                    if (heatSourceTg.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1g(heatSourceTg);
//                    }
//                    if (heatSourceTh.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1h(heatSourceTh);
//                    }

                    // 处理实际室外温度
                    // 获取天气预报数据  实时数据  forecastType = 1
                    WeatherHour weatherHour = weatherHourService.getOne(Wrappers.<WeatherHour>lambdaQuery()
                            .eq(WeatherHour::getWeatherTime, sourceHistory.getStartTime())
                            .eq(WeatherHour::getForecastType, 1));
                    if (weatherHour != null) {
                        updateSourceHistory.setRealOutTemp(weatherHour.getTemperature());
                    } else {
                        updateSourceHistory.setRealOutTemp(BigDecimal.ZERO);
                    }
                    updateSourceHistory.setDataStatus(true);
                    updateList.add(updateSourceHistory);

                    // 更新数据到历史表
                    // forecastSourceHistoryService.updateById(updateSourceHistory);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            // 进行批量更新操作
            forecastSourceHistoryService.updateBatchById(updateList);
        }
    }


    /**
     * 处理 阶段数据
     */
    private void updateStage(Map<Integer, List<ForecastSourceDetail>> collectMap, Integer forcastSourceCoreId, int type) {
        LocalDateTime nowTime = LocalDateTime.now();
        QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("forecastSourceCoreId", forcastSourceCoreId);
//        queryWrapper.eq("data_status", false);
        // 处理阶段数据 查询到 第二天的 0 点之前的数据
        queryWrapper.ge("startTime", LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MIN));
        queryWrapper.lt("startTime", LocalDateTime.of(nowTime.plusDays(1).toLocalDate(), LocalTime.MIN));
        queryWrapper.eq("type", type);
        queryWrapper.or();
        queryWrapper.ge("endTime", LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MIN));
        queryWrapper.lt("endTime", LocalDateTime.of(nowTime.plusDays(1).toLocalDate(), LocalTime.MIN));
        queryWrapper.eq("type", type);
        List<ForecastSourceHistory> listHistoory = forecastSourceHistoryService.list(queryWrapper);
        List<ForecastSourceHistory> updateList = new ArrayList<>();
        for (ForecastSourceHistory sourceHistory : listHistoory) {
            try {
                // 处理历史数据
                List<ForecastSourceDetail> sourceDetailList = collectMap.get(forcastSourceCoreId);
                if (!CollectionUtils.isEmpty(sourceDetailList)) {
                    List<Integer> sourceIdList = sourceDetailList.stream().map(ForecastSourceDetail::getHeatSourceId).collect(Collectors.toList());

                    // 组装请求参数
                    ForecastRequest requestDto = new ForecastRequest();
                    requestDto.setRelevanceId(sourceIdList);
                    // 处理 阶段数据  也是 1
                    requestDto.setType(1);
                    requestDto.setStartTime(sourceHistory.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                    requestDto.setEndTime(sourceHistory.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());

                    Response sourceResponse = historyDataService.getSource(requestDto);
                    if (sourceResponse == null || sourceResponse.getData() == null) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理阶段数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }
                    Gson gson = new Gson();
                    ForecastResponse[] forecastResponseList = gson.fromJson(gson.toJson(sourceResponse.getData()), ForecastResponse[].class);
                    if (forecastResponseList.length == 0) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理阶段数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }

                    // 实际热量  实际负荷  实际热指标  实际流量  是进行合计值
                    BigDecimal realHeatTotal = BigDecimal.ZERO;
                    BigDecimal realHeatLoad = BigDecimal.ZERO;
                    BigDecimal realFlow = BigDecimal.ZERO;
                    BigDecimal heatIndex = BigDecimal.ZERO;
                    // 热源 实际一次供温   实际一次回温   是平均值
                    int sizeSource = 0;
                    BigDecimal heatSourceTg = BigDecimal.ZERO;
                    BigDecimal heatSourceTh = BigDecimal.ZERO;

                    for (ForecastResponse forecastResponse : forecastResponseList) {
                        if (forecastResponse.getRealHeatTotal() != null) {
                            realHeatTotal = realHeatTotal.add(forecastResponse.getRealHeatTotal());
                        }
                        if (forecastResponse.getRealHeatLoad() != null) {
                            realHeatLoad = realHeatLoad.add(forecastResponse.getRealHeatLoad());
                        }
                        if (forecastResponse.getRealFlow() != null) {
                            realFlow = realFlow.add(forecastResponse.getRealFlow());
                        }
                        if (forecastResponse.getHeatIndex() != null) {
                            heatIndex = heatIndex.add(forecastResponse.getHeatIndex());
                        }
                        if (forecastResponse.getHeatSourceTg() != null) {
                            heatSourceTg = heatSourceTg.add(forecastResponse.getHeatSourceTg());
                        }
                        if (forecastResponse.getHeatSourceTh() != null) {
                            heatSourceTh = heatSourceTh.add(forecastResponse.getHeatSourceTh());
                        }
                        sizeSource++;

                    }

                    if (sizeSource > 0) {
                        // 计算 热源 实际一次供温   实际一次回温   的平均值
                        heatSourceTg = heatSourceTg.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                        heatSourceTh = heatSourceTh.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                    }

                    // 标记状态
                    boolean updatStatus = true;
                    ForecastSourceHistory updateSourceHistory = new ForecastSourceHistory();
                    updateSourceHistory.setId(sourceHistory.getId());
//                    if (realHeatTotal.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealHot(realHeatTotal);
//                    }
//                    if (realHeatLoad.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealLoad(realHeatLoad);
//                    }
//                    if (heatIndex.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealThermalIndex(heatIndex);
//                    }
//                    if (realFlow.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealFlow(realFlow);
//                    }
//                    if (heatSourceTg.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1g(heatSourceTg);
//                    }
//                    if (heatSourceTh.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1h(heatSourceTh);
//                    }
                    // 处理实际室外温度    实时数据  forecastType = 1
                    // 获取天气预报数据  阶段的平均温度
                    QueryWrapper<WeatherHour> queryWrapperWeather = new QueryWrapper<>();
                    queryWrapperWeather.ge("weatherTime", sourceHistory.getStartTime());
                    queryWrapperWeather.le("weatherTime", sourceHistory.getEndTime());
                    queryWrapperWeather.eq("forecastType", 1);
                    List<WeatherHour> listWeatherHour = weatherHourService.list(queryWrapperWeather);
                    if (!CollectionUtils.isEmpty(listWeatherHour)) {
                        int size = listWeatherHour.size();
                        BigDecimal hourTemperature = BigDecimal.ZERO;
                        for (WeatherHour weatherHour : listWeatherHour) {
                            // 温度进行累加
                            hourTemperature = hourTemperature.add(weatherHour.getTemperature());
                        }
                        // 计算 平均温度
                        BigDecimal forecastHourAvgTemperature = hourTemperature.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
                        updateSourceHistory.setRealOutTemp(forecastHourAvgTemperature);
//                        updatStatus = true;
                    }
                    if (updatStatus && nowTime.isBefore(sourceHistory.getEndTime())) {
                        // 判断当前时间 大于等于阶段的结束时间才会 更新为 true
                        updatStatus = false;
                    }
                    updateSourceHistory.setDataStatus(updatStatus);
                    updateList.add(updateSourceHistory);
                    // 更新数据到历史表
                    // forecastSourceHistoryService.updateById(updateSourceHistory);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            // 进行批量更新操作
            forecastSourceHistoryService.updateBatchById(updateList);
        }
    }

    /**
     * 处理 天数据
     */
    private void updateDay(Map<Integer, List<ForecastSourceDetail>> collectMap, Integer forcastSourceCoreId, int type) {
        LocalDateTime nowTime = LocalDateTime.now();
        QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("forecastSourceCoreId", forcastSourceCoreId);
        // queryWrapper.eq("data_status", false);
        // 处理天 数据
        queryWrapper.ge("startTime", LocalDateTime.of(nowTime.toLocalDate(), LocalTime.MIN));
        queryWrapper.lt("startTime", LocalDateTime.of(nowTime.plusDays(1).toLocalDate(), LocalTime.MIN));
        queryWrapper.eq("type", type);
        List<ForecastSourceHistory> listHistoory = forecastSourceHistoryService.list(queryWrapper);
        List<ForecastSourceHistory> updateList = new ArrayList<>();
        for (ForecastSourceHistory sourceHistory : listHistoory) {
            try {
                // 处理历史数据
                List<ForecastSourceDetail> sourceDetailList = collectMap.get(forcastSourceCoreId);
                if (!CollectionUtils.isEmpty(sourceDetailList)) {
                    List<Integer> sourceIdList = sourceDetailList.stream().map(ForecastSourceDetail::getHeatSourceId).collect(Collectors.toList());

                    // 组装请求参数
                    ForecastRequest requestDto = new ForecastRequest();
                    requestDto.setRelevanceId(sourceIdList);
                    // 处理 天数据  也是 1
                    requestDto.setType(1);
                    requestDto.setStartTime(sourceHistory.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                    requestDto.setEndTime(sourceHistory.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());

                    Response sourceResponse = historyDataService.getSource(requestDto);
                    if (sourceResponse == null || sourceResponse.getData() == null) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理天数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }
                    Gson gson = new Gson();
                    ForecastResponse[] forecastResponseList = gson.fromJson(gson.toJson(sourceResponse.getData()), ForecastResponse[].class);
                    if (forecastResponseList.length == 0) {
                        // throw new RuntimeException("没有查询到热源的历史数据！");
                        log.error("处理天数据没有查询到热源的历史数据！----{}", requestDto.toString());
                        continue;
                    }

                    // 实际热量  实际负荷  实际热指标  实际流量  是进行合计值
                    BigDecimal realHeatTotal = BigDecimal.ZERO;
                    BigDecimal realHeatLoad = BigDecimal.ZERO;
                    BigDecimal realFlow = BigDecimal.ZERO;
                    BigDecimal heatIndex = BigDecimal.ZERO;
                    // 热源 实际一次供温   实际一次回温   是平均值
                    int sizeSource = 0;
                    BigDecimal heatSourceTg = BigDecimal.ZERO;
                    BigDecimal heatSourceTh = BigDecimal.ZERO;

                    for (ForecastResponse forecastResponse : forecastResponseList) {
                        if (forecastResponse.getRealHeatTotal() != null) {
                            realHeatTotal = realHeatTotal.add(forecastResponse.getRealHeatTotal());
                        }
                        if (forecastResponse.getRealHeatLoad() != null) {
                            realHeatLoad = realHeatLoad.add(forecastResponse.getRealHeatLoad());
                        }
                        if (forecastResponse.getRealFlow() != null) {
                            realFlow = realFlow.add(forecastResponse.getRealFlow());
                        }
                        if (forecastResponse.getHeatIndex() != null) {
                            heatIndex = heatIndex.add(forecastResponse.getHeatIndex());
                        }
                        if (forecastResponse.getHeatSourceTg() != null) {
                            heatSourceTg = heatSourceTg.add(forecastResponse.getHeatSourceTg());
                        }
                        if (forecastResponse.getHeatSourceTh() != null) {
                            heatSourceTh = heatSourceTh.add(forecastResponse.getHeatSourceTh());
                        }
                        sizeSource++;

                    }

                    if (sizeSource > 0) {
                        // 计算 热源 实际一次供温   实际一次回温   的平均值
                        heatSourceTg = heatSourceTg.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                        heatSourceTh = heatSourceTh.divide(new BigDecimal(sizeSource), 2, BigDecimal.ROUND_HALF_UP);
                    }

                    // 标记状态
                    boolean updatStatus = false;
                    ForecastSourceHistory updateSourceHistory = new ForecastSourceHistory();
                    updateSourceHistory.setId(sourceHistory.getId());
//                    if (realHeatTotal.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealHot(realHeatTotal);
//                    }
//                    if (realHeatLoad.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealLoad(realHeatLoad);
//                    }
//                    if (heatIndex.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealThermalIndex(heatIndex);
//                    }
//                    if (realFlow.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealFlow(realFlow);
//                    }
//                    if (heatSourceTg.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1g(heatSourceTg);
//                    }
//                    if (heatSourceTh.compareTo(BigDecimal.ZERO) != 0) {
                    updateSourceHistory.setRealT1h(heatSourceTh);
//                    }
                    // 处理实际室外温度
                    // 获取天气预报数据  实时 天的平均温度
                    WeatherDay weatherDay = weatherDayService.getOne(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, sourceHistory.getStartTime()));
                    if (weatherDay != null) {
                        updateSourceHistory.setRealOutTemp(weatherDay.getAvgTemp());
//                        updatStatus = true;
                    }
                    if (nowTime.isAfter(sourceHistory.getEndTime())) {
                        // 判断当前时间 大于等于阶段的结束时间才会 更新为 true
                        updatStatus = true;
                    }
                    updateSourceHistory.setDataStatus(updatStatus);
                    updateList.add(updateSourceHistory);
                    // 更新数据到历史表
                    // forecastSourceHistoryService.updateById(updateSourceHistory);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            // 进行批量更新操作
            forecastSourceHistoryService.updateBatchById(updateList);
        }
    }

}
