package com.bmts.heating.service.task.energy.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.common.WeatherType;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Collector;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/4/27 16:37
 **/
@Service
@Slf4j
public class EvaluateHandler {
    @Autowired
    private TSCCRestTemplate tsccRestTemplate;
    @Autowired
    EnergyEvaluateConfigService energyEvaluateConfigService;
    @Autowired
    EnergyUnitStandardConfigService energyUnitStandardConfigService;
    @Autowired
    EnergyEvaluateHistoryService energyEvaluateHistoryService;
    @Autowired
    WeatherHourService weatherHourService;
    @Autowired
    StationHandler statationHandler;
    @Autowired
    HeatNetHandler heatNetHandler;

    @Autowired
    HeatTransferStationService heatTransferStationService;
    @Autowired
    HeatSourceHandler heatSourceHandler;

    private final String gatherSearch = "gather_search";
    private final String baseInfo = "bussiness_baseInfomation";

    public void start() {
        EnergyUnitStandardConfig energyUnitStandardConfig = energyUnitStandardConfigService.getOne(Wrappers.<EnergyUnitStandardConfig>lambdaQuery().eq(EnergyUnitStandardConfig::getState, true));
        if (energyUnitStandardConfig == null) {
            log.error("query energyUnitStandardConfig from db is empty");
            return;
        }
        //取出天气预报 如果没有 不计入评价 取24小时平均值
        List<WeatherHour> weatherHours = weatherHourService.list(Wrappers.<WeatherHour>lambdaQuery().eq(WeatherHour::getForecastType, WeatherType.REAL.value()).ge(WeatherHour::getWeatherTime, LocalTimeUtils.getHourTime(-24)).le(WeatherHour::getWeatherTime, LocalTimeUtils.getHourTime(0)));
        //平均气温
        BigDecimal avgTemp = new BigDecimal(weatherHours.stream().map(x -> x.getTemperature()).collect(Collectors.averagingDouble(BigDecimal::doubleValue)));

        List<HeatTransferStation> stations = heatTransferStationService.list(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getStatus, true));
        List<Integer> stationIds = stations.stream().map(HeatTransferStation::getId).collect(Collectors.toList());
        QueryAggregateHistoryDto dto = buildParam(stationIds);
        //调用gathersearch 取历史数据 水电热
        HistoryEnergyDataResponse historyEnergyDataResponse = tsccRestTemplate
                .doHttp("/tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class, HttpMethod.POST);
        if (CollectionUtils.isEmpty(historyEnergyDataResponse.getResponseData())) {
            log.error("查询到历史水电热数据为空,入参{}", JSONObject.toJSONString(dto));
            return;
        }

        //读取数据库评价配置
        List<EnergyEvaluateConfig> list = energyEvaluateConfigService.list();
        if (list.size() == 0) {
            log.error("查询评价数据为空");
            return;
        }
        statationHandler.excute(energyUnitStandardConfig, avgTemp, stations, historyEnergyDataResponse, list);

    }

    @Autowired
    private WebPageConfigService webPageConfigService;


//    public static String[] STATION_FIELDS_INIT = {"HM_HT", "WM_FT", "Ep"};

    /**
     * 创建参数
     *
     * @param
     * @return
     */
    private QueryAggregateHistoryDto buildParam(List<Integer> stationIds) {
        try {
            QueryAggregateHistoryDto queryAggregateHistoryDto = new QueryAggregateHistoryDto();
            queryAggregateHistoryDto.setStartTime(LocalTimeUtils.getDay(-1));
            queryAggregateHistoryDto.setEndTime(LocalTimeUtils.getHour(0));
            queryAggregateHistoryDto.setRelevanceIds(stationIds);
            queryAggregateHistoryDto.setHeatType(HeatTypeEnum.station);
            queryAggregateHistoryDto.setHistoryType(HistoryTypeEnum.energy_hour);
            queryAggregateHistoryDto.setAggregateTimeType(AggregateTimeTypeEnum.interval);
            queryAggregateHistoryDto.setAggregateDataType(AggregateDataTypeEnum.whole);
            queryAggregateHistoryDto.setSize(100000);
            queryAggregateHistoryDto.setSortType(true);
            queryAggregateHistoryDto.setCurrentPage(1);
            //查询标准点
            QueryWrapper<WebPageConfig> webPageConfigQueryWrapper = new QueryWrapper<>();
            webPageConfigQueryWrapper.eq("configKey", "energyPointConfig");
            WebPageConfig webPageConfig = webPageConfigService.getOne(webPageConfigQueryWrapper);
            JSONObject jsonObject = JSONObject.parseObject(webPageConfig.getJsonConfig());
            GetEnergyPointConfig energyPointConfig = JSONArray.toJavaObject(jsonObject, GetEnergyPointConfig.class);
            List<AggregatePoint> list = new ArrayList<>();
            buildPoint(energyPointConfig.getStationElectricityPoint(), list);
            buildPoint(energyPointConfig.getStationHeatPoint(), list);
            buildPoint(energyPointConfig.getStationWaterPoint(), list);
            queryAggregateHistoryDto.setPoints(list);
            return queryAggregateHistoryDto;

        } catch (Exception e) {
            log.error("build param cause error {}", e);
            return new QueryAggregateHistoryDto();
        }
    }
    /**
     * 设置汇聚点
     *
     * @param pointName
     * @param list
     */
    private void buildPoint(String pointName, List<AggregatePoint> list) {
        AggregatePoint aggregatePoint = new AggregatePoint();
        aggregatePoint.setPointName(pointName);
        aggregatePoint.setAggregateType(AggregateType.sum);
        list.add(aggregatePoint);
    }

}
