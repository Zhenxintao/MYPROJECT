package com.bmts.heating.middleground.history.joggle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.service.PointUnitService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.gathersearch.request.CurveDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.HeatTypeEnum;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.HistoryTypeEnum;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.Curve;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.*;
import com.bmts.heating.commons.entiy.gathersearch.response.history.CurveResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.PointBasicInfo;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleground.history.service.TdEngineHistoryData;
import com.bmts.heating.middleground.history.service.TdEngineQueryHistoryData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/9/14 16:42
 * @Version 1.0
 */
@Api(tags = "历史数据管理")
@Slf4j
@RestController
@RequestMapping("/history")
public class HistoryJoggle {
    //
//	@Autowired
//	private HistoryData historyData;
    @Autowired
    private TdEngineHistoryData historyData;
    @Autowired
    private TdEngineQueryHistoryData queryHistoryData;
    @Autowired
    private PointUnitService pointUnitService;
    private WebPageConfigService webPageConfigService;

    @ApiOperation("查询大表格数据曲线")
    @PostMapping("/dataCurve")
    public Response dataCurve(@RequestBody CurveDto dto) {
        CurveResponse curveResponse = new CurveResponse();
        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
            return Response.success();
        }
        QueryEsDto param = setQueryDto(dto);
        param.setHeatSystemId(dto.getListSystem());
        param.setIncludeFields(dto.getListPointName());
        String[] listPointName = dto.getListPointName();
        if (listPointName.length > 0) {
            List<String> listFields = new ArrayList(Arrays.asList(listPointName));//**须定义时就进行转化**
            listFields.add("relevanceId");
            listFields.add("timeStrap");
            param.setIncludeFields(listFields.toArray(new String[listFields.size()]));
        } else {
            return Response.success();
        }
        Collection<Map<String, String>> pointLMap = historyData.findHistoryData(param);
        List<Curve> listCurve = setCurves(pointLMap);
        curveResponse.setCurves(listCurve);
        // 查询单位
        List<PointStandardResponse> listpointStandard = new ArrayList<>();
        for (String columnName : listPointName) {
            QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ps.columnName", columnName);
            List<PointStandardResponse> pointStandardResponses = pointUnitService.listPoint(queryWrapper);
            listpointStandard.addAll(pointStandardResponses);
        }
        // 设置返回参数
        List<PointBasicInfo> listPointBasicInfo = new ArrayList<>();
        for (PointStandardResponse pointStandard : listpointStandard) {
            PointBasicInfo pointBasicInfo = new PointBasicInfo();
            pointBasicInfo.setPointDes(pointStandard.getName());
            pointBasicInfo.setPointName(pointStandard.getColumnName());
            pointBasicInfo.setUnitType(pointStandard.getUnit());
            pointBasicInfo.setUnitValue(pointStandard.getUnitDisable());
            listPointBasicInfo.add(pointBasicInfo);
        }
        curveResponse.setPointBasicInfo(listPointBasicInfo);
        return Response.success(curveResponse);
    }

    //    @ApiOperation("查询历史曲线")
//    @PostMapping("/curve")
//    public Response curve(@RequestBody CurveDto dto) {
//        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
//            return Response.success();
//        }
//        CurveResponse curveResponse = this.CurveConfig(dto);
//        return Response.success(curveResponse);
//    }
    @ApiOperation("查询历史曲线(byTd)")
    @PostMapping("/curve")
    public Response curve(@RequestBody CurveDto dto) {
        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
            return Response.success();
        }
        CurveBaseHistoryResponse curveResponse = new CurveBaseHistoryResponse();
        QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ps.columnName", dto.getListPointName());
        List<PointStandardResponse> responses = pointUnitService.listPoint(queryWrapper);
        List<PointBasicInfo> pointBasicInfoList = responses.stream().map(item -> {
            PointBasicInfo pointBasicInfo = new PointBasicInfo();
            pointBasicInfo.setUnitValue(item.getUnitDisable());
            pointBasicInfo.setUnitType(item.getUnit());
            pointBasicInfo.setPointDes(item.getName());
            pointBasicInfo.setPointName(item.getColumnName());
            return pointBasicInfo;
        }).collect(Collectors.toList());
        curveResponse.setPointBasicInfo(pointBasicInfoList);
        //获取历史查询参数信息
        QueryBaseHistoryDto queryBaseHistoryDto = configTdQueryBaseHistoryDto(dto);
        HistoryBaseDataResponse response = queryHistoryData.queryHistoryBase(queryBaseHistoryDto);
        curveResponse.setCurves(response.getResponseData());
        return Response.success(curveResponse);
    }

    @ApiOperation("查询每日曲线对比")
    @PostMapping("/dayCurveContrast")
    public Response dayCurveContrast(@RequestBody CurveDto dto) {
        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
            return Response.success();
        }
        CurveResponse curveResponse = CurveConfig(dto);
        return Response.success(curveResponse);
    }

    @ApiOperation("查询单站能耗曲线(ByTd)")
    @PostMapping("/energyCurve")
    public Response energyCurve(@RequestBody CurveDto dto) {
        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
            return Response.success();
        }
//        CurveEnergyHistoryResponse curveResponse = new CurveEnergyHistoryResponse();
//        GetEnergyPointConfig getEnergyPointConfig = getEnergyPointConfig();
//        List<PointBasicInfo> pointBasicInfoList = new ArrayList<>();
//        PointBasicInfo pointBasicInfoWater = new PointBasicInfo();
//        pointBasicInfoWater.setPointDes(getEnergyPointConfig.getStationWaterPoint());
//        PointBasicInfo pointBasicInfoEle = new PointBasicInfo();
//        pointBasicInfoWater.setPointDes(getEnergyPointConfig.getStationElectricityPoint());
//        PointBasicInfo pointBasicInfoHeat = new PointBasicInfo();
//        pointBasicInfoWater.setPointDes(getEnergyPointConfig.getStationHeatPoint());
//        pointBasicInfoList.add(pointBasicInfoWater);
//        pointBasicInfoList.add(pointBasicInfoEle);
//        pointBasicInfoList.add(pointBasicInfoHeat);
//        curveResponse.setPointBasicInfo(pointBasicInfoList);

        //获取历史查询参数信息
        QueryBaseHistoryDto queryBaseHistoryDto = configTdQueryBaseHistoryDto(dto);
        HistoryEnergyDataResponse response = queryHistoryData.queryHistoryEnergy(queryBaseHistoryDto);
//        curveResponse.setCurves(response.getResponseData());
        return Response.success(response.getResponseData());
    }
//    @ApiOperation("查询单站能耗曲线")
//    @PostMapping("/energyCurve")
//    public Response energyCurve(@RequestBody CurveDto dto) {
//        if (CollectionUtils.isEmpty(Arrays.asList(dto.getListSystem()))) {
//            return Response.success();
//        }
//        CurveResponse curveResponse = energyCurveConfig(dto);
//        return Response.success(curveResponse);
//    }

    /**
     * 组装Td历史查询数据参数
     */
    private QueryBaseHistoryDto configTdQueryBaseHistoryDto(CurveDto curveDto) {
        QueryBaseHistoryDto dto = new QueryBaseHistoryDto();
        try {
            String dateFort = "yyy-MM-dd HH:mm:ss";
            dto.setStartTime(LocalTimeUtils.transferStringDateToLong(dateFort, curveDto.getStartTime()));
            dto.setEndTime(LocalTimeUtils.transferStringDateToLong(dateFort, curveDto.getEndTime()));
            if (curveDto.getSourceType() == 4) {
                if (curveDto.getQueryType() == 4) {
                    dto.setHistoryType(HistoryTypeEnum.energy_hour);
                } else {
                    dto.setHistoryType(HistoryTypeEnum.energy_day);
                }
            } else {
                if (curveDto.getQueryType() == 1) {
                    dto.setHistoryType(HistoryTypeEnum.history_minute);
                }
                if (curveDto.getQueryType() == 2) {
                    dto.setHistoryType(HistoryTypeEnum.history_hour);
                }
            }
            dto.setHeatType(HeatTypeEnum.station);
            dto.setRelevanceIds(Arrays.asList(curveDto.getListSystem()));
            if (curveDto.getListPointName()!=null&&curveDto.getListPointName().length>0) {
                dto.setPoints(Arrays.asList(curveDto.getListPointName()));
            }
            return dto;
        } catch (Exception e) {
            return null;
        }

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

    private CurveResponse energyCurveConfig(CurveDto dto) {
        CurveResponse curveResponse = new CurveResponse();
        QueryEsDto queryDto = setQueryDto(dto);
        List<String> listIncludeFields = new ArrayList<>();
        listIncludeFields.add("relevanceId");
        listIncludeFields.add("timeStrap");
        listIncludeFields.add("HM_HT");
        listIncludeFields.add("WM_FT");
        listIncludeFields.add("Ep");
        queryDto.setIncludeFields(listIncludeFields.toArray(new String[listIncludeFields.size()]));
        QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ps.columnName", listIncludeFields);
        List<PointStandardResponse> responses = pointUnitService.listPoint(queryWrapper);
        List<PointBasicInfo> pointBasicInfoList = responses.stream().map(item -> {
            PointBasicInfo pointBasicInfo = new PointBasicInfo();
            pointBasicInfo.setUnitValue(item.getUnitDisable());
            pointBasicInfo.setUnitType(item.getUnit());
            pointBasicInfo.setPointDes(item.getName());
            pointBasicInfo.setPointName(item.getColumnName());
            return pointBasicInfo;
        }).collect(Collectors.toList());
        curveResponse.setPointBasicInfo(pointBasicInfoList);
        Collection<Map<String, Object>> list = historyData.findHistoryEnergyData(queryDto);
        List<Curve> curves = setCurvesEnergy(list);
        curveResponse.setCurves(curves);
        return curveResponse;
    }

    private CurveResponse CurveConfig(CurveDto dto) {
        CurveResponse curveResponse = new CurveResponse();
        QueryEsDto queryDto = setQueryDto(dto);
        List<String> listIncludeFields = new ArrayList<>();
        listIncludeFields.add("relevanceId");
        listIncludeFields.add("timeStrap");
        List<String> ListPointName = Arrays.stream(dto.getListPointName()).collect(Collectors.toList());
        listIncludeFields.addAll(ListPointName);
        queryDto.setIncludeFields(listIncludeFields.toArray(new String[listIncludeFields.size()]));
        QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ps.columnName", listIncludeFields);
        List<PointStandardResponse> responses = pointUnitService.listPoint(queryWrapper);
        List<PointBasicInfo> pointBasicInfoList = responses.stream().map(item -> {
            PointBasicInfo pointBasicInfo = new PointBasicInfo();
            pointBasicInfo.setUnitValue(item.getUnitDisable());
            pointBasicInfo.setUnitType(item.getUnit());
            pointBasicInfo.setPointDes(item.getName());
            pointBasicInfo.setPointName(item.getColumnName());
            return pointBasicInfo;
        }).collect(Collectors.toList());
        curveResponse.setPointBasicInfo(pointBasicInfoList);
        Collection<Map<String, String>> list = historyData.findHistoryData(queryDto);
        List<Curve> curves = setCurves(list);
        curveResponse.setCurves(curves);
        return curveResponse;
    }

    private QueryEsDto setParam(CurveDto dto, CurveResponse curveResponse) {
        QueryEsDto param = setQueryDto(dto);
        // 组装 参数
        List<String> listFields = new ArrayList<>();
        listFields.add("relevanceId");
        listFields.add("timeStrap");
        List<String> listUnitType = dto.getListUnitType();
        List<PointStandardResponse> listpointStandard = new ArrayList<>();
        for (String unitType : listUnitType) {
            // pu.unitName
            QueryWrapper<PointUnit> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pu.unitName", unitType);
            List<PointStandardResponse> pointStandardResponses = pointUnitService.listPoint(queryWrapper);
            listFields.addAll(pointStandardResponses.stream().filter(e -> e.getColumnName() != null).map(PointStandardResponse::getColumnName).collect(Collectors.toList()));
            listpointStandard.addAll(pointStandardResponses);
        }
        param.setIncludeFields(listFields.toArray(new String[listFields.size()]));
        // 设置返回参数
        List<PointBasicInfo> listPointBasicInfo = new ArrayList<>();
        for (PointStandardResponse pointStandard : listpointStandard) {
            PointBasicInfo pointBasicInfo = new PointBasicInfo();
            pointBasicInfo.setPointDes(pointStandard.getName());
            pointBasicInfo.setPointName(pointStandard.getColumnName());
            pointBasicInfo.setUnitType(pointStandard.getUnit());
            pointBasicInfo.setUnitValue(pointStandard.getUnitDisable());
            listPointBasicInfo.add(pointBasicInfo);
        }
        curveResponse.setPointBasicInfo(listPointBasicInfo);
        return param;
    }

    private QueryEsDto setQueryDto(CurveDto dto) {
        // 组装请求参数
        QueryEsDto param = new QueryEsDto();
        param.setHeatSystemId(dto.getListSystem());
        param.setStart(this.verifyTime(dto.getQueryType(), dto.getStartTime()));
        param.setEnd(this.verifyTime(dto.getQueryType(), dto.getEndTime()));
        // 设置 查询 1.分钟 2.小时 3.天
        this.verifyQueryType(dto.getQueryType(), param);
        // 设置 一次网数据、二次网数据、室温数据
        this.verifySourceType(dto.getSourceType(), param);
        return param;
    }

    private void verifySourceType(int sourceType, QueryEsDto param) {
        switch (sourceType) {
            case 1:
                param.routeFirst();
                break;
            case 2:
                param.routeSecond();
                break;
            case 3:
                param.routeIndoorTemp();
                break;
            case 4:
                param.routeEnergy();
                break;
            default:
                throw new RuntimeException("param is error");
        }
    }

    private void verifyQueryType(int queryType, QueryEsDto param) {
        switch (queryType) {
            case 1:
                param.realData();
                break;
            case 2:
                param.hourAvg();
                break;
            case 3:
                param.day();
                break;
            case 4:
                param.hour();
                break;
            default:
                throw new RuntimeException("param is error");
        }
    }

    private long verifyTime(int queryType, String date) {
        long dateTime = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        switch (queryType) {
            case 1:
                if (!StringUtils.isEmpty(date)) {
                    LocalDateTime startTime = LocalDateTime.parse(date, formatter);
                    startTime = startTime.withSecond(0);
                    log.info("" + startTime);
                    dateTime = LocalTimeUtils.getTimestampOfDateTime(startTime);
                    log.info("" + dateTime);
                }
                break;
            case 4:
            case 2:
                if (!StringUtils.isEmpty(date)) {
                    LocalDateTime startTime = LocalDateTime.parse(date, formatter);
                    startTime = startTime.withSecond(0).withMinute(0);
                    log.info("" + startTime);
                    dateTime = LocalTimeUtils.getTimestampOfDateTime(startTime);
                    log.info("" + dateTime);
                }
                break;
            case 3:
                if (!StringUtils.isEmpty(date)) {
                    LocalDateTime startTime = LocalDateTime.parse(date, formatter);
                    startTime = startTime.withSecond(0).withMinute(0).withHour(0);
                    log.info("" + startTime);
                    dateTime = LocalTimeUtils.getTimestampOfDateTime(startTime);
                    log.info("" + dateTime);
                }
                break;
            default:
                throw new RuntimeException("{queryType} param is error");
        }
        return dateTime;
    }


    private List<Curve> setCurves(Collection<Map<String, String>> pointLMap) {
        List<Curve> listCurve = new ArrayList<>();
        for (Map<String, String> map : pointLMap) {
            Curve curve = new Curve();
            Map<String, BigDecimal> curveMap = new HashMap<>();
            for (String key : map.keySet()) {
                String value = map.get(key);
                if (Objects.equals(key, "relevanceId")) {
                    curve.setHeatSystemId(Integer.valueOf(value));
                    continue;
                }
                if (Objects.equals(key, "timeStrap")) {
                    long timeStrap = Long.parseLong(value);
//                    curve.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStrap), ZoneId.systemDefault()));
                    curve.setTime(timeStrap);
                    continue;
                }
                if (StringUtils.isNotBlank(value)) {
                    BigDecimal bd = new BigDecimal(value);
                    curveMap.put(key, bd);
                }
            }
            curve.setPoint(curveMap);
            listCurve.add(curve);
        }
        return listCurve;
    }

    private List<Curve> setCurvesEnergy(Collection<Map<String, Object>> pointLMap) {
        Set<Curve> listCurve = new HashSet<>();
        for (Map<String, Object> map : pointLMap) {
            Curve curve = new Curve();
            Map<String, BigDecimal> curveMap = new HashMap<>();
            for (String key : map.keySet()) {
                String value = map.get(key).toString();
                if (Objects.equals(key, "relevanceId")) {
                    curve.setHeatSystemId(Integer.valueOf(value));
                    continue;
                }
                if (Objects.equals(key, "timeStrap")) {
                    long timeStrap = Long.parseLong(value);
//                    curve.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStrap), ZoneId.systemDefault()));
                    curve.setTime(timeStrap);
                    continue;
                }
                if (StringUtils.isNotBlank(value)) {
                    try {
                        JSONObject object = JSONObject.parseObject(value);
                        Object t = object.get("consumption");
                        BigDecimal bd = new BigDecimal(t.toString());
                        curveMap.put(key, bd);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
            curve.setPoint(curveMap);
            listCurve.add(curve);
        }
        return new ArrayList<>(listCurve);
    }
}
