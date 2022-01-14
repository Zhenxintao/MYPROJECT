package com.bmts.heating.web.forecast.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.request.ForecastRequest;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.common.ForecastType;
import com.bmts.heating.commons.entiy.forecast.Compensation;
import com.bmts.heating.commons.entiy.forecast.ForecastWebPageConfig;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.compute.forecast.pojo.Dispatch;
import com.bmts.heating.compute.forecast.pojo.TempSetting;
import com.bmts.heating.web.forecast.service.ForecastWeatherCoefService;
import com.bmts.heating.web.forecast.service.impl.CompensationFactorServiceImpl;
import com.bmts.heating.web.forecast.service.impl.FlowServiceImpl;
import com.bmts.heating.web.forecast.service.impl.StandardConditionServiceImpl;
import com.bmts.heating.web.forecast.service.impl.TemperatureServiceImpl;
import com.bmts.heating.web.forecast.strategy.ComputeServiceDto;
import com.bmts.heating.web.forecast.strategy.ComputeType;
import com.bmts.heating.web.forecast.strategy.ForecastComputeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author naming
 * @description
 * @date 2021/4/7 14:30
 **/
@Service
public class ForeCastHandler {


    //  时间格式化
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //  时间格式化
    private static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Autowired
    private ForecastSourceBasicService forecastSourceBasicService;
    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private ForecastSourceHeatSeasonService forecastSourceHeatSeasonService;

    @Autowired
    private WeatherDayService weatherDayService;

    @Autowired
    private WeatherHourService weatherHourService;

    @Autowired
    private ForecastSourceHistoryService forecastSourceHistoryService;

    @Autowired
    private TemperatureServiceImpl temperatureService;
    @Autowired
    private StandardConditionServiceImpl standardConditionService;
    @Autowired
    private FlowServiceImpl flowService;
    @Autowired
    private CompensationFactorServiceImpl compensationFactorService;

    @Autowired
    private ForecastWeatherCoefService forecastWeatherCoefService;

    @Autowired
    private WebPageConfigService webPageConfigService;


    public Response handForecast(List<Integer> types) {
        // 当前时间
        LocalDateTime nowTime = LocalDateTime.now();

        // 根据当前时间查询供暖季基础数据
        QueryWrapper<ForecastSourceBasic> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("endTime", nowTime);
        queryWrapper.le("startTime", nowTime);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        ForecastSourceBasic forecastSourceBasic = forecastSourceBasicService.getOne(queryWrapper);
        if (forecastSourceBasic == null) {
            throw new RuntimeException("没有查询到当前供暖季基础数据！");
        }
        // 查询热源基础数据
        List<ForecastSourceCore> list = forecastSourceCoreService.list(Wrappers.<ForecastSourceCore>lambdaQuery().eq(ForecastSourceCore::getIsValid, true));
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("没有查询到热源基础数据！");
        }
        // 查询系数配置
        ForecastWeatherCoef forecastWeatherCoef = null;
        List<ForecastWeatherCoef> listForecastWeather = forecastWeatherCoefService.list();
        if (!CollectionUtils.isEmpty(listForecastWeather)) {
            forecastWeatherCoef = listForecastWeather.get(0);
        }
        // 查询负荷常量配置
        WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery()
                .eq(WebPageConfig::getConfigKey, "forecastSource"));
        if (webPageConfig == null) {
            throw new RuntimeException("没有查询到负荷预测的常量配置信息！");
        }
        ForecastWebPageConfig forecastWebPageConfig = null;
        if (StringUtils.isNotBlank(webPageConfig.getJsonConfig())) {
            forecastWebPageConfig = JSONObject.parseObject(webPageConfig.getJsonConfig(), ForecastWebPageConfig.class);
        }

        Set<String> errMsg = new HashSet<>();
        for (ForecastSourceCore forecastSourceCore : list) {
            ForecastRequest forecastRequest = new ForecastRequest();
            // 设置供暖季基础数据
            forecastRequest.setForecastSourceBasic(forecastSourceBasic);
            // 设置热源基础数据
            forecastRequest.setSourceCore(forecastSourceCore);
            for (Integer type : types) {
                if (type == 1) {
                    // 进行小时预测
                    String dispatch = forecastSourceCore.getDispatch();
                    LocalDateTime formatTime = nowTime.toLocalDate().atTime(LocalDateTime.now().getHour(), 0, 0);
                    if (StringUtils.isBlank(dispatch)) {
//                        List<Dispatch> dispatchList = JSONObject.parseArray(dispatch, Dispatch.class);
                        // 进行24小时预测
                        Response response = forecastHour(formatTime, forecastRequest, forecastWeatherCoef, forecastWebPageConfig);
                        if (response.getCode() == ResponseCode.FAIL.getCode() || response.getCode() == ResponseCode.PARAM_ERROR.getCode()) {
                            errMsg.add(response.getData().toString());
                        }
//                        if (response.getCode() == ResponseCode.FAIL.getCode()) {
//                            return response;
//                        }
                    } else {
                        // 进行阶段预测
                        Response response = forecastStageHour(formatTime, forecastRequest, forecastWeatherCoef, forecastWebPageConfig);
                        if (response.getCode() == ResponseCode.FAIL.getCode() || response.getCode() == ResponseCode.PARAM_ERROR.getCode()) {
                            errMsg.add(response.getData().toString());
                        }
//                        if (response.getCode() == ResponseCode.FAIL.getCode()) {
//                            return response;
//                        }
                    }
                }
                if (type == 2) {
                    // 进行天预测
                    Response response = foreCastDay(nowTime, forecastRequest, forecastWeatherCoef, forecastWebPageConfig);
                    if (response.getCode() == ResponseCode.FAIL.getCode()) {
                        errMsg.add(response.getMsg());
                    }
                    if (response.getCode() == ResponseCode.PARAM_ERROR.getCode()) {
                        errMsg.add(response.getData().toString());
                    }
//                    if (response.getCode() == ResponseCode.FAIL.getCode()) {
//                        return response;
//                    }
                }
            }

        }
        if (errMsg.size() > 0) {
            return Response.paramError(errMsg.toString());
        } else {
            return Response.success();
        }
    }


    // 阶段小时 的记录  预测阶段内的小时
    public Response forecastStageHour(LocalDateTime formatTime, ForecastRequest forecastRequest,
                                      ForecastWeatherCoef forecastWeatherCoef, ForecastWebPageConfig forecastWebPageConfig) {

        Set<String> setMsg = new HashSet<String>();
        ForecastSourceCore sourceCore = forecastRequest.getSourceCore();
        // 预测明天的阶段数据和阶段内的小时数据
        LocalDateTime plusDays = formatTime.plusDays(1);
        // 年月日
        String forTimePlusDay = plusDays.format(formatterDate);

        LocalDateTime createNow = LocalDateTime.now();

        String dispatch = sourceCore.getDispatch();
        if (StringUtils.isNotBlank(dispatch)) {
            List<Dispatch> dispatchList = JSONObject.parseArray(dispatch, Dispatch.class);
            // 进行阶段预测
            for (Dispatch oneDispath : dispatchList) {
                // 预测当前阶段的阶段数据和小时数据    10:00 时间格式
                // 进行时间格式拼接
                String startTimeStr = oneDispath.getStartTime();
                String endTimeStr = oneDispath.getEndTime();
                if (StringUtils.isBlank(startTimeStr) || StringUtils.isBlank(endTimeStr)) {
                    // 哪个预测，的 阶段预测异常
                    setMsg.add(sourceCore.getName() + "调度参数配置错误！");
                    continue;
                }
                // 截取整数
                int startHour = Integer.parseInt(startTimeStr.split(":")[0]);
                int endHour = Integer.parseInt(endTimeStr.split(":")[0]);
                // 判断是否是当天，还是跨天的阶段
                // 结束时间大于 开始时间 表示是当天阶段
                if (endHour > startHour) {
                    String startTime = forTimePlusDay + " " + startTimeStr + ":00";
                    String endTime = forTimePlusDay + " " + endTimeStr + ":00";
                    // 格式转换
                    LocalDateTime localStartTime = strToLocalDateTime(startTime);
                    LocalDateTime localEndTime = strToLocalDateTime(endTime);
                    try {
                        // 预测阶段
                        Response fail = setEachStage(createNow, formatTime, forecastRequest, startTime, endTime,
                                localStartTime, localEndTime, forecastWeatherCoef, forecastWebPageConfig);
                        if (fail != null) {
                            setMsg.add(sourceCore.getName() + "--" + fail.getMsg());
                        }
                    } catch (Exception e) {
                        // 哪个预测，的 阶段预测异常
                        setMsg.add(sourceCore.getName() + "阶段预测参数配置错误！");
                    }

                    // 进行阶段 小时 的预测
                    for (int i = 0; i < (endHour - startHour) + 1; i++) {
                        LocalDateTime startDataTime = localStartTime.plusHours(i);
                        LocalDateTime endDataTime = localStartTime.plusHours(i + 1);
                        String strStartTime = startDataTime.format(formatter);
                        String strEndTime = endDataTime.format(formatter);
                        try {
                            Response hourFail = setEveryHour(createNow, formatTime, forecastRequest, startDataTime, strStartTime,
                                    endDataTime, strEndTime, forecastWeatherCoef, forecastWebPageConfig);
                            if (hourFail != null) {
                                setMsg.add(sourceCore.getName() + "--" + hourFail.getMsg());
                            }
                        } catch (Exception e) {
                            // 哪个预测，的 阶段预测异常
                            setMsg.add(sourceCore.getName() + "阶段小时预测参数配置错误！");
                        }


                    }

                }
                // 结束时间小于 开始时间  表示是跨天阶段
                if (endHour <= startHour) {
                    String startTime = forTimePlusDay + " " + startTimeStr + ":00";
                    LocalDateTime plusTwoDays = formatTime.plusDays(2);
                    String endTime = plusTwoDays.format(formatterDate) + " " + endTimeStr + ":00";
                    // 格式转换
                    LocalDateTime localStartTime = strToLocalDateTime(startTime);
                    LocalDateTime localEndTime = strToLocalDateTime(endTime);
                    try {
                        // 预测阶段
                        Response fail = setEachStage(createNow, formatTime, forecastRequest, startTime, endTime, localStartTime,
                                localEndTime, forecastWeatherCoef, forecastWebPageConfig);
                        if (fail != null) {
                            setMsg.add(sourceCore.getName() + "--" + fail.getMsg());
                        }
                    } catch (Exception e) {
                        // 哪个预测，的 阶段预测异常
                        setMsg.add(sourceCore.getName() + "阶段预测参数配置错误！");
                    }

                    // 进行阶段 小时 的预测
                    for (int i = 0; i < (endHour - startHour) + 24; i++) {
                        LocalDateTime startDataTime = localStartTime.plusHours(i);
                        LocalDateTime endDataTime = localStartTime.plusHours(i + 1);
                        String strStartTime = startDataTime.format(formatter);
                        String strEndTime = endDataTime.format(formatter);
                        try {
                            Response hourFail = setEveryHour(createNow, formatTime, forecastRequest, startDataTime, strStartTime,
                                    endDataTime, strEndTime, forecastWeatherCoef, forecastWebPageConfig);
                            if (hourFail != null) {
                                setMsg.add(sourceCore.getName() + "--" + hourFail.getMsg());
                            }
                        } catch (Exception e) {
                            // 哪个预测，的 阶段预测异常
                            setMsg.add(sourceCore.getName() + "阶段小时预测参数配置错误！");
                        }

                    }

                }
            }
        }
        if (setMsg.size() > 0) {
            return Response.paramError(setMsg.toString());
        } else {
            return Response.success();

        }
    }


    // 小时的是预测当前时间之后 的24个小时 的记录
    public Response forecastHour(LocalDateTime formatTime, ForecastRequest forecastRequest,
                                 ForecastWeatherCoef forecastWeatherCoef, ForecastWebPageConfig forecastWebPageConfig) {
        Set<String> setMsg = new HashSet<String>();
        // 预测明天的 24个小时记录
        LocalDateTime plusDay = formatTime.plusDays(1);
        LocalDateTime zeroTimeStart = LocalDateTime.of(plusDay.toLocalDate(), LocalTime.MIN);

        LocalDateTime createNow = LocalDateTime.now();

        for (int i = 0; i < 24; i++) {
            LocalDateTime localDateTimeStart = zeroTimeStart.plusHours(i);
            String startTime = localDateTimeStart.format(formatter);
            LocalDateTime localDateTimeEnd = zeroTimeStart.plusHours(i + 1);
            String endTime = localDateTimeEnd.format(formatter);
            try {
                Response fail = setEveryHour(createNow, formatTime, forecastRequest, localDateTimeStart, startTime,
                        localDateTimeEnd, endTime, forecastWeatherCoef, forecastWebPageConfig);
                if (fail != null) {
                    setMsg.add(forecastRequest.getSourceCore().getName() + "--" + fail.getMsg());
                }
            } catch (Exception e) {
                // 哪个预测，的 阶段预测异常
                setMsg.add(forecastRequest.getSourceCore().getName() + "小时预测参数配置错误！");
            }
        }
        if (setMsg.size() > 0) {
            return Response.paramError(setMsg.toString());
        } else {
            return Response.success();
        }
    }

    private Response setEveryHour(LocalDateTime createTime, LocalDateTime formatTime, ForecastRequest forecastRequest,
                                  LocalDateTime localDateTimeStart, String startTime, LocalDateTime localDateTimeEnd, String endTime,
                                  ForecastWeatherCoef forecastWeatherCoef, ForecastWebPageConfig forecastWebPageConfig) {
        ForecastSourceCore sourceCore = forecastRequest.getSourceCore();
        // 查询 阶段流量设定数据
        QueryWrapper<ForecastSourceHeatSeason> queryWrapperSeason = new QueryWrapper<>();
        queryWrapperSeason.eq("forcastSourceCoreId", sourceCore.getId());
        queryWrapperSeason.ge("endTime", endTime);
        queryWrapperSeason.le("startTime", startTime);

        ForecastSourceHeatSeason forecastSourceHeatSeason = forecastSourceHeatSeasonService.getOne(queryWrapperSeason);
        if (forecastSourceHeatSeason == null) {
            return Response.fail("没有查询到阶段流量设定数据！");
        }
        // 设置阶段流量数据
        forecastRequest.setHeatSeason(forecastSourceHeatSeason);
        // 判断计算方法
        ComputeServiceDto computeServiceDto = getComputeService(localDateTimeStart, localDateTimeEnd, sourceCore);
        BigDecimal commonValue = computeServiceDto.getCommonValue();
        if (commonValue != null) {
            forecastRequest.setCommonValue(commonValue);
        }
        Integer interval = computeServiceDto.getInterval();
        if (interval == null) {
            return Response.fail("时间间隔业务错误！");
        }
        forecastRequest.setInterval(interval);
        // 获取天气预报数据
        WeatherHour weatherHour = weatherHourService.getOne(Wrappers.<WeatherHour>lambdaQuery().eq(WeatherHour::getWeatherTime, startTime).eq(WeatherHour::getForecastType, 2));
        if (weatherHour == null) {
            return Response.fail("没有查询到天气预报数据！");
        }
        BigDecimal temperature = weatherHour.getTemperature();
        // 进行天气数据比较
        if (temperature.compareTo(forecastWebPageConfig.getForecastTempHigh()) > -1) {
            temperature = forecastWebPageConfig.getForecastTempHigh();
        }
        if (temperature.compareTo(forecastWebPageConfig.getForecastTempLow()) < 1) {
            temperature = forecastWebPageConfig.getForecastTempLow();
        }
        forecastRequest.setForecastHourAvgTemperature(temperature);
        // 设置面积倍数
        BigDecimal areaMultiple = BigDecimal.ONE;
        if (forecastWebPageConfig.getForecastAreaMultiple() != null) {
            areaMultiple = forecastWebPageConfig.getForecastAreaMultiple();
        }
        forecastRequest.setAreaMultiple(areaMultiple);
        // 设置预测类型  小时为2 计算热量时进行方法判断
        forecastRequest.setForecastType(ForecastType.HOUR.value());

        // 获取计算业务service 接口
        ForecastComputeService forecastComputeService = computeServiceDto.getForecastComputeService();
        // 预测一次供回水温度
        ForecastSourceHistory forecastSourceHistory = forecastComputeService.forecastTgTh(forecastRequest);
        // 预测用热量
        ForecastSourceHistory heatHistory = forecastComputeService.forecastUseHeat(forecastRequest);
        forecastSourceHistory.setForecastThermalIndex(heatHistory.getForecastThermalIndex());
        forecastSourceHistory.setForecastLoad(heatHistory.getForecastLoad());
        // 预测热量配置系数
        BigDecimal hourHot = ConfigRatioUtil.setHourRatio(heatHistory.getForecastHot(), weatherHour, forecastWeatherCoef);
        forecastSourceHistory.setForecastHot(hourHot);
        // 天气预测的室外温度
        forecastSourceHistory.setForecastOutTemp(temperature);
        forecastSourceHistory.setStartTime(localDateTimeStart);
        forecastSourceHistory.setEndTime(localDateTimeEnd);
        forecastSourceHistory.setForecastTime(createTime);
        forecastSourceHistory.setComputeType(computeServiceDto.getComputeType());
        // 设置 `type`  '1.阶段 2.小时 3.天',
        forecastSourceHistory.setType(ForecastType.HOUR.value());
        forecastSourceHistory.setForecastSourceCoreId(sourceCore.getId());
        // 设置 预测流量
        forecastSourceHistory.setScheduleFlow(forecastSourceHeatSeason.getFirstNetStageFlow());
        // 插入预测历史表数据  插入之前先判断存不存在。存在就更新，不存在则添加数据
        // 查询 阶段流量设定数据
        QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
        queryWrapperHistory.eq("forecastSourceCoreId", sourceCore.getId());
        queryWrapperHistory.eq("startTime", localDateTimeStart);
        queryWrapperHistory.eq("endTime", localDateTimeEnd);
        queryWrapperHistory.eq("type", 2);
        ForecastSourceHistory oneHistory = forecastSourceHistoryService.getOne(queryWrapperHistory);
        if (oneHistory == null) {
            forecastSourceHistoryService.save(forecastSourceHistory);
        } else {
            forecastSourceHistory.setId(oneHistory.getId());
            forecastSourceHistoryService.updateById(forecastSourceHistory);
        }
        return null;
    }

    // 天 是预测 之后7天的  每天一条数据
    public Response foreCastDay(LocalDateTime formatTime, ForecastRequest forecastRequest,
                                ForecastWeatherCoef forecastWeatherCoef, ForecastWebPageConfig forecastWebPageConfig) {
        Set<String> setMsg = new HashSet<String>();
        ForecastSourceCore sourceCore = forecastRequest.getSourceCore();
        for (int i = 1; i < 8; i++) {

            LocalDateTime timeStart = formatTime.plusDays(i);
            LocalDateTime zeroTimeStart = LocalDateTime.of(timeStart.toLocalDate(), LocalTime.MIN);
            LocalDateTime timeEnd = formatTime.plusDays(i + 1);
            LocalDateTime zeroTimeEnd = LocalDateTime.of(timeEnd.toLocalDate(), LocalTime.MIN);

            // 查询 阶段流量设定数据
            QueryWrapper<ForecastSourceHeatSeason> queryWrapperSeason = new QueryWrapper<>();
            queryWrapperSeason.eq("forcastSourceCoreId", sourceCore.getId());
            // queryWrapperSeason.ge("endTime", zeroTimeEnd);
            queryWrapperSeason.ge("endTime", zeroTimeStart);
            queryWrapperSeason.le("startTime", zeroTimeStart);

            ForecastSourceHeatSeason forecastSourceHeatSeason = forecastSourceHeatSeasonService.getOne(queryWrapperSeason);
            if (forecastSourceHeatSeason == null) {
                return Response.fail(sourceCore.getName() + "--没有查询到阶段流量设定数据！");
            }
            // 设置阶段流量数据
            forecastRequest.setHeatSeason(forecastSourceHeatSeason);
            // 判断计算方法
            ComputeServiceDto computeServiceDto = getComputeService(zeroTimeStart, zeroTimeEnd, sourceCore);
            BigDecimal commonValue = computeServiceDto.getCommonValue();
            if (commonValue != null) {
                forecastRequest.setCommonValue(commonValue);
            }
            Integer interval = computeServiceDto.getInterval();
            if (interval == null) {
                return Response.fail(sourceCore.getName() + "--时间间隔业务错误！");
            }
            forecastRequest.setInterval(interval);
            // 获取天气预报数据   一天的平均温度
            WeatherDay weatherDay = weatherDayService.getOne(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, zeroTimeStart));
            if (weatherDay == null) {
                return Response.fail(sourceCore.getName() + "--没有查询到天气预报数据！");
            }
            // 平均温度
            BigDecimal forecastHourAvgTemperature = weatherDay.getAvgTemp();
            // 进行天气数据比较
            if (forecastHourAvgTemperature.compareTo(forecastWebPageConfig.getForecastTempHigh()) > -1) {
                forecastHourAvgTemperature = forecastWebPageConfig.getForecastTempHigh();
            }
            if (forecastHourAvgTemperature.compareTo(forecastWebPageConfig.getForecastTempLow()) < 1) {
                forecastHourAvgTemperature = forecastWebPageConfig.getForecastTempLow();
            }
            // 设置温度
            forecastRequest.setForecastHourAvgTemperature(forecastHourAvgTemperature);
            // 设置面积倍数
            BigDecimal areaMultiple = BigDecimal.ONE;
            if (forecastWebPageConfig.getForecastAreaMultiple() != null) {
                areaMultiple = forecastWebPageConfig.getForecastAreaMultiple();
            }
            forecastRequest.setAreaMultiple(areaMultiple);
            // 设置预测类型   计算热量时进行方法判断
            forecastRequest.setForecastType(ForecastType.Week.value());


            try {

                // 获取计算业务service 接口
                ForecastComputeService forecastComputeService = computeServiceDto.getForecastComputeService();
                // 预测一次供回水温度
                ForecastSourceHistory forecastSourceHistory = forecastComputeService.forecastTgTh(forecastRequest);
                // 预测用热量
                ForecastSourceHistory heatHistory = forecastComputeService.forecastUseHeat(forecastRequest);
                forecastSourceHistory.setForecastThermalIndex(heatHistory.getForecastThermalIndex());
                forecastSourceHistory.setForecastLoad(heatHistory.getForecastLoad());
                // 预测用热量配置系数
                BigDecimal dayHot = ConfigRatioUtil.setDayRatio(heatHistory.getForecastHot(), weatherDay, forecastWeatherCoef);
                forecastSourceHistory.setForecastHot(dayHot);


                // 天气预测的室外温度
                forecastSourceHistory.setForecastOutTemp(forecastHourAvgTemperature);
                forecastSourceHistory.setStartTime(zeroTimeStart);
                forecastSourceHistory.setEndTime(zeroTimeEnd);
                forecastSourceHistory.setForecastTime(formatTime);
                forecastSourceHistory.setComputeType(computeServiceDto.getComputeType());
                // 设置 `type`  '1.阶段 2.小时 3.天',
                forecastSourceHistory.setType(ForecastType.Week.value());
                forecastSourceHistory.setForecastSourceCoreId(sourceCore.getId());
                // 设置 预测流量
                forecastSourceHistory.setScheduleFlow(forecastSourceHeatSeason.getFirstNetStageFlow());
                // 插入预测历史表数据  插入之前先判断存不存在。存在就更新，不存在则添加数据
                // 查询 阶段流量设定数据
                QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
                queryWrapperHistory.eq("forecastSourceCoreId", sourceCore.getId());
                queryWrapperHistory.eq("startTime", zeroTimeStart);
                queryWrapperHistory.eq("endTime", zeroTimeEnd);
                queryWrapperHistory.eq("type", 3);
                ForecastSourceHistory oneHistory = forecastSourceHistoryService.getOne(queryWrapperHistory);
                if (oneHistory == null) {
                    forecastSourceHistoryService.save(forecastSourceHistory);
                } else {
                    forecastSourceHistory.setId(oneHistory.getId());
                    forecastSourceHistoryService.updateById(forecastSourceHistory);
                }

            } catch (Exception e) {
                // 哪个预测，的 阶段预测异常
//                response = Response.paramError("foreCastDay@" + sourceCore.getName() + "天预测参数配置错误！");
                setMsg.add(sourceCore.getName() + "天预测参数配置错误！");
            }
        }
        if (setMsg.size() > 0) {
            return Response.paramError(setMsg.toString());
        } else {
            return Response.success();
        }

    }

    private Response setEachStage(LocalDateTime createTime, LocalDateTime formatTime, ForecastRequest forecastRequest,
                                  String startTime, String endTime, LocalDateTime localStartTime, LocalDateTime localEndTime,
                                  ForecastWeatherCoef forecastWeatherCoef, ForecastWebPageConfig forecastWebPageConfig) {
        ForecastSourceCore sourceCore = forecastRequest.getSourceCore();
        // 查询 阶段流量设定数据
        QueryWrapper<ForecastSourceHeatSeason> queryWrapperSeason = new QueryWrapper<>();
        queryWrapperSeason.eq("forcastSourceCoreId", sourceCore.getId());
        queryWrapperSeason.ge("endTime", endTime);
        queryWrapperSeason.le("startTime", startTime);

        ForecastSourceHeatSeason forecastSourceHeatSeason = forecastSourceHeatSeasonService.getOne(queryWrapperSeason);
        if (forecastSourceHeatSeason == null) {
            return Response.fail("没有查询到阶段流量设定数据！");
        }
        // 设置阶段流量数据
        forecastRequest.setHeatSeason(forecastSourceHeatSeason);
        // 判断计算方法
        ComputeServiceDto computeServiceDto = getComputeService(localStartTime, localEndTime, sourceCore);
        BigDecimal commonValue = computeServiceDto.getCommonValue();
        if (commonValue != null) {
            forecastRequest.setCommonValue(commonValue);
        }
        Integer interval = computeServiceDto.getInterval();
        if (interval == null) {
            return Response.fail("时间间隔业务错误！");
        }
        forecastRequest.setInterval(interval);
        // 获取天气预报数据  阶段的平均温度
        QueryWrapper<WeatherHour> queryWrapperWeather = new QueryWrapper<>();
        queryWrapperWeather.ge("weatherTime", localStartTime);
        queryWrapperWeather.le("weatherTime", localEndTime);
        queryWrapperWeather.eq("forecastType", 2);
        List<WeatherHour> listWeatherHour = weatherHourService.list(queryWrapperWeather);
        if (CollectionUtils.isEmpty(listWeatherHour)) {
            return Response.fail("没有查询到天气预报数据！");
        }
        int size = listWeatherHour.size();
        BigDecimal hourTemperature = BigDecimal.ZERO;
        for (WeatherHour weatherHour : listWeatherHour) {
            // 温度进行累加
            hourTemperature = hourTemperature.add(weatherHour.getTemperature());
        }
        // 计算 平均温度
        BigDecimal forecastHourAvgTemperature = hourTemperature.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
        // 进行天气数据比较
        if (forecastHourAvgTemperature.compareTo(forecastWebPageConfig.getForecastTempHigh()) > -1) {
            forecastHourAvgTemperature = forecastWebPageConfig.getForecastTempHigh();
        }
        if (forecastHourAvgTemperature.compareTo(forecastWebPageConfig.getForecastTempLow()) < 1) {
            forecastHourAvgTemperature = forecastWebPageConfig.getForecastTempLow();
        }
        // 设置温度
        forecastRequest.setForecastHourAvgTemperature(forecastHourAvgTemperature);
        // 设置面积倍数
        BigDecimal areaMultiple = BigDecimal.ONE;
        if (forecastWebPageConfig.getForecastAreaMultiple() != null) {
            areaMultiple = forecastWebPageConfig.getForecastAreaMultiple();
        }
        forecastRequest.setAreaMultiple(areaMultiple);
        // 设置预测类型   计算热量时进行方法判断
        forecastRequest.setForecastType(ForecastType.STAGE.value());


        // 获取计算业务service 接口
        ForecastComputeService forecastComputeService = computeServiceDto.getForecastComputeService();
        // 预测一次供回水温度
        ForecastSourceHistory forecastSourceHistory = forecastComputeService.forecastTgTh(forecastRequest);
        // 预测用热量
        ForecastSourceHistory heatHistory = forecastComputeService.forecastUseHeat(forecastRequest);
        forecastSourceHistory.setForecastThermalIndex(heatHistory.getForecastThermalIndex());
        forecastSourceHistory.setForecastLoad(heatHistory.getForecastLoad());
        // 预测用热量配置系数
        BigDecimal stageHot = ConfigRatioUtil.setStageRatio(heatHistory.getForecastHot(), listWeatherHour, forecastWeatherCoef);
        forecastSourceHistory.setForecastHot(stageHot);

        // 天气预测的室外温度
        forecastSourceHistory.setForecastOutTemp(forecastHourAvgTemperature);
        forecastSourceHistory.setStartTime(localStartTime);
        forecastSourceHistory.setEndTime(localEndTime);
        forecastSourceHistory.setForecastTime(createTime);
        forecastSourceHistory.setComputeType(computeServiceDto.getComputeType());
        // 设置 `type`  '1.阶段 2.小时 3.天',
        forecastSourceHistory.setType(ForecastType.STAGE.value());
        forecastSourceHistory.setForecastSourceCoreId(sourceCore.getId());
        // 设置 预测流量
        forecastSourceHistory.setScheduleFlow(forecastSourceHeatSeason.getFirstNetStageFlow());
        // 插入预测历史表数据  插入之前先判断存不存在。存在就更新，不存在则添加数据
        // 查询 阶段流量设定数据
        QueryWrapper<ForecastSourceHistory> queryWrapperHistory = new QueryWrapper<>();
        queryWrapperHistory.eq("forecastSourceCoreId", sourceCore.getId());
        queryWrapperHistory.eq("startTime", localStartTime);
        queryWrapperHistory.eq("endTime", localEndTime);
        queryWrapperHistory.eq("type", 1);
        ForecastSourceHistory oneHistory = forecastSourceHistoryService.getOne(queryWrapperHistory);
        if (oneHistory == null) {
            forecastSourceHistoryService.save(forecastSourceHistory);
        } else {
            forecastSourceHistory.setId(oneHistory.getId());
            forecastSourceHistoryService.updateById(forecastSourceHistory);
        }
        return null;
    }

    private ComputeServiceDto getComputeService(LocalDateTime startTime, LocalDateTime endTime, ForecastSourceCore sourceCore) {
        ComputeServiceDto dto = new ComputeServiceDto();

        String tempSetting = sourceCore.getTempSetting();
        List<TempSetting> tempSettingList = JSONObject.parseArray(tempSetting, TempSetting.class);
        if (!CollectionUtils.isEmpty(tempSettingList)) {
            for (TempSetting temp : tempSettingList) {
                // 判断预测时间 在哪个阶段里面
                LocalDateTime startTimeTemp = temp.getStartTime();
                LocalDateTime endTimeTemp = temp.getEndTime();
                if (startTimeTemp != null && endTimeTemp != null) {
                    if ((startTime.isAfter(startTimeTemp) || startTime.equals(startTimeTemp)) && (endTime.isBefore(endTimeTemp) || endTime.equals(endTimeTemp))) {
                        // b.温度法
                        //是否处于温度法-判断标准：
                        // 当前进行预测的时间处于负荷预测热源基础常数（数据来源：I.1.b）中温度预测方式（字段名：tempSetting）开始、结束时间区间内
                        // 并在当前数据项中负荷预测温度设定类型（字段名：tempType为1）。
                        if (temp.getForecastType() == 1) {
                            dto.setCommonValue(temp.getTemp());
                            dto.setForecastComputeService(temperatureService);
                            ////  计算时间间隔
                            //Duration duration = Duration.between(startTime, endTime);
                            //dto.setInterval((int) duration.toHours());

                            //  计算时间间隔
                            //Duration duration = Duration.between(startTime, endTime);
                            //dto.setInterval((int) duration.toHours());

                            // 天 、阶段的、小时的都是为  1
                            dto.setInterval(1);

                            dto.setComputeType(ComputeType.TEMPERATURE_COMPUTE.type());
                            return dto;
                        }
                        // c.流量法
                        //是否处于温度法-判断标准：
                        // 当前进行预测的时间处于负荷预测热源基础常数（数据来源：I.1.b）中温度预测方式（字段名：tempSetting）开始、结束时间区间内
                        // 并在当前数据项中负荷预测温度设定类型（字段名：tempType为2）。
                        if (temp.getForecastType() == 2) {
                            dto.setCommonValue(temp.getTemp());
                            dto.setForecastComputeService(flowService);
                            //  计算时间间隔
                            //Duration duration = Duration.between(startTime, endTime);
                            //dto.setInterval((int) duration.toHours());

                            // 天 、阶段的、小时的都是为  1
                            dto.setInterval(1);
                            dto.setComputeType(ComputeType.FLOW_COMPUTE.type());
                            return dto;
                        }

                    }
                }
            }

        }

        // 调整系数为补偿值系数（数据来源：c中补偿值方式属性值（字段名：compensation中属性compensationValue）
        String compensation = sourceCore.getCompensation();
        List<Compensation> compensationList = JSONObject.parseArray(compensation, Compensation.class);
        if (!CollectionUtils.isEmpty(compensationList)) {
            for (Compensation compens : compensationList) {
                // 判断预测时间 在哪个阶段里面
                LocalDateTime startTimeTemp = compens.getStartTime();
                LocalDateTime endTimeTemp = compens.getEndTime();
                if (startTimeTemp != null && endTimeTemp != null) {
                    if ((startTime.isAfter(startTimeTemp) || startTime.equals(startTimeTemp)) && (endTime.isBefore(endTimeTemp) || endTime.equals(endTimeTemp))) {
                        //d.补偿系数发生改变
                        //是否处于补偿系数计算-判断标准：当前进行预测的时间处于负荷预测热源基础常数（数据来源：I.1.b）中补偿值设置（字段名：compensation）开始、结束时间区间内并在当前数据项中。
                        dto.setCommonValue(compens.getCompensationValue());
                        dto.setForecastComputeService(compensationFactorService);
                        //  计算时间间隔
                        Duration duration = Duration.between(startTime, endTime);
                        dto.setInterval((int) duration.toHours());
                        dto.setComputeType(ComputeType.COMPENSATION_FACTOR.type());
                        return dto;
                    }
                }
            }

        }

        // a.标准工况
        //是否处于标准工况-判断标准：当前进行预测时的时间不在
        // 负荷预测热源基础常数（数据来源：I.1.b）中补偿值系数（字段名：compensation）与温度预测方式（字段名：tempSetting）开始、结束时间区间内。
        dto.setForecastComputeService(standardConditionService);
        //  计算时间间隔
        //Duration duration = Duration.between(startTime, endTime);
        //dto.setInterval((int) duration.toHours());

        // 天 、阶段的、小时的都是为  1
        dto.setInterval(1);
        dto.setComputeType(ComputeType.STANDARD_CONDITION.type());
        return dto;

    }

    // 字符串 转时间格式
    public LocalDateTime strToLocalDateTime(String str) {
        return LocalDateTime.parse(str, formatter);
    }


}
