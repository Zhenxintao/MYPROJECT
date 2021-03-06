package com.bmts.heating.web.scada.service.monitor.impl;

import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.request.QueryShowPowerDto;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointCache;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.*;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.entiy.converge.HeatStationRealDto;
import com.bmts.heating.commons.entiy.converge.HeatStationRealResponseDto;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.PointDetail;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.StationCacheRealValue;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.converter.PointLRealConverter;
import com.bmts.heating.web.scada.service.monitor.MonitorService;
import com.bmts.heating.web.scada.service.monitor.impl.common.CommonService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MonitorServiceImpl extends CommonService implements MonitorService {


    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String gatherSearch = "gather_search";
    private final String baseServer = "bussiness_baseInfomation";

    public List<PointCache> newCacheData(Map<Integer, String[]> mapRequest) {
        return Arrays.asList(tsccRestTemplate.post("/cache/basic", mapRequest, gatherSearch, PointCache[].class));
    }

    public List<StationFirstNetBaseView> firstNetBase() {
        return Arrays.asList(tsccRestTemplate.get("/common/firstNetBase", baseServer, StationFirstNetBaseView[].class));
    }

    public List<PointCache> monitorData(Map<Integer, String[]> mapRequest) {
        DataRouteParam param = new DataRouteParam();
        param.setMap(mapRequest);
        return Arrays.asList(tsccRestTemplate.post("/route/data", param, gatherSearch, PointCache[].class));
    }

    public List<PointCache> monitorExport(Map<Integer, String[]> mapRequest) {
        return Arrays.asList(tsccRestTemplate.post("/cache/basic", mapRequest, gatherSearch, PointCache[].class));
    }

    public List<Integer> getStation(MonitorDto dto) {
        return Arrays.asList(tsccRestTemplate.post("/monitor/stationInfo", dto, baseServer, Integer[].class));
    }

    public List<Integer> getBeyondStation(MonitorBeyondDto dto) {
        return Arrays.asList(tsccRestTemplate.post("/monitor/beyond", dto, baseServer, Integer[].class));
    }

    public List<Integer> getStationByUserId(Integer id) {
        return Arrays.asList(tsccRestTemplate.get("/common/station/user/" + id, baseServer, Integer[].class));
    }

    public List<Integer> getOrgByParentIds(List<Integer> id) {
        return Arrays.asList(tsccRestTemplate.post("/heatOrganizationService/orgIds", id, baseServer, Integer[].class));
    }

    @Override
    public Map<String, Object> getBigTable(MonitorDto dto) {
        List<StationFirstNetBaseView> firstNetBaseList = this.firstNetBase();
        List<Integer> stations = this.getStation(dto);
        // ???????????????
        Map<Integer, Double> qualityStrap = super.getRank("qualityStrap", false).getMap();
        // ??????????????????
        Map<Integer, Double> timeStampMap = super.getRank("timeStamp", false).getMap();

        int page = dto.getCurrentPage();
        int size = dto.getPageCount();
        //??????????????????????????????,???????????????????????????
        //????????????0,1????????????
        Map<String, List<StationFirstNetBaseView>> map = firstNetBaseList.stream()
                .filter(e -> stations.contains(e.getHeatTransferStationId()) && e.getStatus())
                .collect(Collectors.groupingBy(StationFirstNetBaseView::getNumber));
        List<StationFirstNetBaseView> firstNetBaseListPartZero = map.get("0");
        map.keySet().removeIf(key -> Objects.equals(key, "0"));

        List<StationFirstNetBaseView> firstNetBaseListPart = new ArrayList<>();
        for (String key : map.keySet()) {
            firstNetBaseListPart.addAll(map.get(key));
        }

        List<StationFirstNetBaseView> firstNetBases = new ArrayList<>();
        //??????????????????
        long count = firstNetBaseListPart.size();


        if (StringUtils.isNotBlank(dto.getSortName())) {
            List<StationFirstNetBaseView> newFirstNetBaseList = new ArrayList<>();
            newFirstNetBaseList.addAll(firstNetBaseListPart);
            newFirstNetBaseList.addAll(firstNetBaseListPartZero);
            // ?????????????????????????????????????????????
            List<StationFirstNetBaseView> stationFirstNetBaseViews = super.buildSort(dto, newFirstNetBaseList);

            LinkedHashSet<StationFirstNetBaseView> pageListPart = new LinkedHashSet<>();
            // ?????????0???????????????????????????????????????
            List<StationFirstNetBaseView> finalFirstNetBaseListPart = firstNetBaseListPart;
            stationFirstNetBaseViews.stream().forEach(x -> {
                // ????????????0??????
                if (Objects.equals(x.getNumber(), "0")) {
                    List<StationFirstNetBaseView> collect = finalFirstNetBaseListPart.stream().filter(a -> Objects.equals(a.getHeatTransferStationId(), x.getHeatTransferStationId()))
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        pageListPart.addAll(collect);
                    }
                } else {
                    pageListPart.add(x);
                }

            });
            //??????????????????
            firstNetBaseListPart = pageListPart.stream()
                    .skip((page - 1) * size).limit(size)//??????
                    .collect(Collectors.toList());

        } else {
            //??????
            firstNetBaseListPart = firstNetBaseListPart.stream().sorted(Comparator.comparing(StationFirstNetBaseView::getHeatStationSort))//??????
                    .skip((page - 1) * size).limit(size)//??????
                    .collect(Collectors.toList());
        }
        Set<Integer> stationIds = firstNetBaseListPart.stream().map(StationFirstNetBaseView::getHeatTransferStationId).collect(Collectors.toSet());
        firstNetBaseListPartZero = firstNetBaseListPartZero.stream()
                .filter(e -> stationIds.contains(e.getHeatTransferStationId()))
                .collect(Collectors.toList());


        firstNetBases.addAll(firstNetBaseListPartZero);
        firstNetBases.addAll(firstNetBaseListPart);
        //????????????
        List<PointCache> pointCacheArrayList = null;
        if (dto.getExport()) {
            pointCacheArrayList = this.monitorExport(super.packageParam(firstNetBases, dto.getColumnName()));
        } else {
            // ??????????????????
            pointCacheArrayList = this.newCacheData(super.packageParam(firstNetBases, dto.getColumnName()));
            // pointCacheArrayList = this.monitorData(super.packageParam(firstNetBases, dto.getColumnName()));
        }
        Map<String, Object> res = new HashMap<>();
        res.put("total", count);
        res.put("data", this.packageResultStation(pointCacheArrayList, firstNetBaseListPart, firstNetBaseListPartZero, qualityStrap, timeStampMap));
        return res;
    }

//    @Override
//    public Map<String, Object> getBigTable(MonitorDto dto) {
//        List<StationFirstNetBaseView> firstNetBaseList = this.firstNetBase();
//        List<Integer> stations = this.getStation(dto);
//        // ???????????????
//        Map<Integer, Double> qualityStrap = super.getRank("qualityStrap", false).getMap();
//        // ??????????????????
//        Map<Integer, Double> timeStampMap = super.getRank("timeStamp", false).getMap();
//
//        int page = dto.getCurrentPage();
//        int size = dto.getPageCount();
//        //??????????????????????????????,???????????????????????????
//        //????????????0,1????????????
//        Map<String, List<StationFirstNetBaseView>> map = firstNetBaseList.stream()
//                .filter(e -> stations.contains(e.getHeatTransferStationId()) && e.getStatus())
//                .collect(Collectors.groupingBy(StationFirstNetBaseView::getNumber));
//        //List<StationFirstNetBaseView> firstNetBaseListPart = map.get("1");
//        List<StationFirstNetBaseView> firstNetBaseListPartZero = map.get("0");
//        map.keySet().removeIf(key -> Objects.equals(key, "0"));
//
//        List<StationFirstNetBaseView> firstNetBaseListPart = new ArrayList<>();
//        for (String key : map.keySet()) {
//            firstNetBaseListPart.addAll(map.get(key));
//        }
//
//        List<StationFirstNetBaseView> firstNetBases = new ArrayList<>();
//        //??????????????????
//        long count = firstNetBaseListPart.size();
//        if (StringUtils.isNotBlank(dto.getSortName())) {
//            List<StationFirstNetBaseView> stationFirstNetBaseViews = super.buildSort(dto, firstNetBaseListPart);
//            //??????????????????
//            firstNetBaseListPart = stationFirstNetBaseViews.stream()
//                    .skip((page - 1) * size).limit(size)//??????
//                    .collect(Collectors.toList());
//        } else {
//            //??????
//            firstNetBaseListPart = firstNetBaseListPart.stream().sorted(Comparator.comparing(StationFirstNetBaseView::getHeatStationSort))//??????
//                    .skip((page - 1) * size).limit(size)//??????
//                    .collect(Collectors.toList());
//        }
//        Set<Integer> stationIds = firstNetBaseListPart.stream().map(StationFirstNetBaseView::getHeatTransferStationId).collect(Collectors.toSet());
//        firstNetBaseListPartZero = firstNetBaseListPartZero.stream()
//                .filter(e -> stationIds.contains(e.getHeatTransferStationId()))
//                .collect(Collectors.toList());
//
//        //// ??????0???????????? ???????????????????????????
//        //Set<Integer> cabinetIds = firstNetBaseListPart.stream().map(StationFirstNetBaseView::getHeatCabinetId).collect(Collectors.toSet());
//        //firstNetBaseListPartZero = firstNetBaseListPartZero.stream()
//        //        .filter(e -> cabinetIds.contains(e.getHeatCabinetId()))
//        //        .collect(Collectors.toList());
//
//        firstNetBases.addAll(firstNetBaseListPartZero);
//        firstNetBases.addAll(firstNetBaseListPart);
////        firstNetBaseListPart.addAll(firstNetBaseListPartZero);
//        //????????????
//        List<PointCache> pointCacheArrayList = null;
//        if (dto.getExport()) {
//            pointCacheArrayList = this.monitorExport(super.packageParam(firstNetBases, dto.getColumnName()));
//        } else {
//            // ??????????????????
//            pointCacheArrayList = this.newCacheData(super.packageParam(firstNetBases, dto.getColumnName()));
//            // pointCacheArrayList = this.monitorData(super.packageParam(firstNetBases, dto.getColumnName()));
//        }
//        Map<String, Object> res = new HashMap<>();
//        res.put("total", count);
//        res.put("data", this.packageResultStation(pointCacheArrayList, firstNetBaseListPart, firstNetBaseListPartZero, qualityStrap, timeStampMap));
//        return res;
//    }


    @Override
    public Map<String, Object> getBigTable(MonitorSingleDto dto) {
        int page = dto.getCurrentPage();
        int size = dto.getPageCount();
        //??????????????????????????????
        List<StationFirstNetBaseView> firstNetBasesList = this.firstNetBase();
        // ???????????????
        Map<Integer, Double> qualityStrapMap = super.getRank("qualityStrap", false).getMap();
        // ??????????????????
        Map<Integer, Double> timeStampMap = super.getRank("timeStamp", false).getMap();

        Stream<StationFirstNetBaseView> stream = firstNetBasesList
                .stream().filter(StationFirstNetBaseView::getStatus);

        if (dto.getUserId() > 0) {
            //??????????????????????????????
            List<Integer> stationIdsByUserId = this.getStationByUserId(dto.getUserId());
            //????????????????????????????????????
            stream = stream.filter(e -> stationIdsByUserId.contains(e.getHeatTransferStationId()));
        }
        if (dto.getHeatSourceId() != null) {//???
            stream = stream.filter(e -> dto.getHeatSourceId().equals(e.getHeatSourceId()));
        } else if (dto.getOrganizationId() != null) {//??????
            List<Integer> orgByParentIds = getOrgByParentIds(Collections.singletonList(dto.getOrganizationId()));
            stream = stream.filter(e -> orgByParentIds.contains(e.getHeatStationOrgId()));
        } else if (dto.getStationId() != null) {//???
            stream = stream.filter(e -> dto.getStationId().equals(e.getHeatTransferStationId()));
        }
        //????????????0,1????????????
        Map<String, List<StationFirstNetBaseView>> map = stream.collect(Collectors.groupingBy(StationFirstNetBaseView::getNumber));
        //List<StationFirstNetBaseView> firstNetBaseListPart = map.get("1");
        List<StationFirstNetBaseView> firstNetBaseListPartZero = map.get("0");

        map.keySet().removeIf(key -> Objects.equals(key, "0"));

        List<StationFirstNetBaseView> firstNetBaseListPart = new ArrayList<>();
        for (String key : map.keySet()) {
            firstNetBaseListPart.addAll(map.get(key));
        }

        List<StationFirstNetBaseView> firstNetBases = new ArrayList<>();

        //???????????????????????????
        long count = firstNetBaseListPart.size();
        if (StringUtils.isNotBlank(dto.getSortName())) {
            firstNetBaseListPart = super.buildSort(dto, firstNetBaseListPart).stream()
                    .skip((page - 1) * size).limit(size)//??????
                    .collect(Collectors.toList());
        } else {
            //?????????????????????????????????
            firstNetBaseListPart = firstNetBaseListPart.stream().sorted(Comparator.comparing(StationFirstNetBaseView::getHeatStationSort))
                    .skip((page - 1) * size).limit(size)//??????
                    .collect(Collectors.toList());
        }
        firstNetBases.addAll(firstNetBaseListPartZero);
        firstNetBases.addAll(firstNetBaseListPart);
        //????????????
        List<PointCache> pointCacheArrayList = null;

        if (dto.getExport()) {
            pointCacheArrayList = this.monitorExport(super.packageParam(firstNetBases, dto.getColumnName()));
        } else {
            // ??????????????????
            pointCacheArrayList = this.newCacheData(super.packageParam(firstNetBases, dto.getColumnName()));
            // pointCacheArrayList = this.monitorData(super.packageParam(firstNetBases, dto.getColumnName()));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", count);
        result.put("data", this.packageResultStation(pointCacheArrayList, firstNetBaseListPart, firstNetBaseListPartZero, qualityStrapMap, timeStampMap));
        return result;
    }

    @Override
    public List<CommonTree> getOrgStationTree(MonitorDto dto) {
        return Arrays.asList(tsccRestTemplate.post("monitor/orgStationTree", dto, baseServer, CommonTree[].class));
    }

    @Override
    public List<CommonTree> getOrgSystemTree(MonitorDto dto) {
        Response post = tsccRestTemplate.post("monitor/orgSystemTree", dto, baseServer, Response.class);
        if (post.getData() != null) {
            Gson gson = new Gson();
            CommonTree[] commonTrees = gson.fromJson(gson.toJson(post.getData()), CommonTree[].class);
            return Arrays.asList(commonTrees);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getSystemRealData(QuerySystemDto dto) {
        Map<String, Object> map = new HashMap<>();
        try {
            //????????????
            List<PointCache> pointCacheArrayList = null;
            Map<Integer, String[]> paramMap = new HashMap<>();
            paramMap.put(dto.getRelevanceId(), dto.getPointsName().toArray(new String[dto.getPointsName().size()]));
            pointCacheArrayList = this.monitorData(paramMap);
            if (pointCacheArrayList.size() <= 0) {
                return map;
            }
            // ???????????????
            Map<Integer, Double> qualityStrapMap = super.getRank("qualityStrap", false).getMap();
            Double qualityData = qualityStrapMap.get(dto.getRelevanceId());
            map.put("qualityData", qualityData);
            // ??????????????????
            Map<Integer, Double> timeStampMap = super.getRank("timeStamp", false).getMap();
            Double timeStampData = timeStampMap.get(dto.getRelevanceId());
            map.put("timeStamp", timeStampData);
            for (PointCache pointCache : pointCacheArrayList) {
                map.put(pointCache.getPointName(), pointCache.getValue());
            }
            return map;
        } catch (Exception e) {
            log.error("????????????????????????????????????????????????{}", e);
            return map;
        }
    }

    @Override
    public List<Map<String, Object>> selShowPower(QueryShowPowerDto dto) {
        return Arrays.asList(tsccRestTemplate.post("realData/selShowPower", dto, baseServer, Map[].class));
    }

    @Override
    public List<CommonTree> sourceSystemTree(MonitorDto dto) {
        Response post = tsccRestTemplate.post("monitor/sourceSystemTree", dto, baseServer, Response.class);
        if (post.getData() != null) {
            Gson gson = new Gson();
            CommonTree[] commonTrees = gson.fromJson(gson.toJson(post.getData()), CommonTree[].class);
            return Arrays.asList(commonTrees);
        }
        return Collections.emptyList();
    }


    //??????????????????-?????????0??????
    private List<StationCacheRealValue> packageResult(List<PointCache> realData, List<StationFirstNetBaseView> baseStream) {
        Map<Integer, List<PointCache>> collect = realData.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
        List<StationCacheRealValue> res = new ArrayList<>();
        baseStream.forEach(e -> {
            StationCacheRealValue cacheRealValue = new StationCacheRealValue();
            cacheRealValue.setCompanyName(e.getHeatStationOrgName());
            cacheRealValue.setStationName(e.getHeatTransferStationName());
            cacheRealValue.setSystemName(e.getHeatSystemName());
            cacheRealValue.setCabinetName(e.getHeatCabinetName());
            cacheRealValue.setArea(e.getHeatSystemArea());
            cacheRealValue.setStationId(e.getHeatTransferStationId());
            cacheRealValue.setHeatSystemId(e.getHeatSystemId());
            // ??????????????? ????????????  ????????? ???????????? ????????? ??? P1g ?????????????????????????????????????????????????????????????????? P1g ???????????????????????????????????????
            if (collect.get(e.getHeatSystemId()) != null) {
                AtomicReference<Integer> qualityStrap = new AtomicReference<>();
                AtomicReference<Integer> qualityStrapAll = new AtomicReference<>();
                qualityStrapAll.set(0);
                List<PointDetail> pointDetails = collect.get(e.getHeatSystemId()).stream().map(point -> {
                    if (point.getNumber() == 1 && Objects.equals(point.getPointName(), "P1g")) {
                        qualityStrap.set(point.getQualityStrap());
                    }
                    if (point.getQualityStrap() > 0) {
                        qualityStrapAll.set(point.getQualityStrap());
                    }
                    PointDetail pointDetail = new PointDetail();
                    pointDetail.setPointId(point.getPointId());
                    pointDetail.setValue(point.getValue());
                    pointDetail.setColumnName(point.getPointName());
                    pointDetail.setAccidentLower(point.getAccidentLower());
                    pointDetail.setAccidentHigh(point.getAccidentHigh());
                    pointDetail.setRunningLower(point.getRunningLower());
                    pointDetail.setRunningHigh(point.getRunningHigh());
                    pointDetail.setDescriptionJson(point.getDescriptionJson());
                    pointDetail.setTimeStamp(point.getTimeStrap());
                    pointDetail.setQualityStrap(point.getQualityStrap());
                    return pointDetail;
                }).collect(Collectors.toList());
                // ?????? 1 ?????? ??? P1g????????????
                if (qualityStrap.get() != null) {
                    cacheRealValue.setQualityStrap(qualityStrap.get());
                } else {
                    // ????????????????????? ????????????1????????? ?????????
                    // ????????????1 ????????????
                    StationFirstNetBaseView stationFirstNetBaseView = baseStream.stream().filter(x -> Objects.equals(x.getNumber(), 1) && Objects.equals(e.getHeatTransferStationId(), x.getHeatTransferStationId()))
                            .findFirst().orElse(null);
                    if (stationFirstNetBaseView != null) {
                        Integer firstHeatSystemId = stationFirstNetBaseView.getHeatSystemId();
                        // ?????? 1????????? P1g  ?????????
                        PointCache p1g = collect.get(firstHeatSystemId).stream().filter(x -> Objects.equals(x.getPointName(), "P1g")).findFirst().orElse(null);
                        if (p1g != null) {
                            cacheRealValue.setQualityStrap(p1g.getQualityStrap());
                        } else {
                            cacheRealValue.setQualityStrap(qualityStrapAll.get());
                        }
                    } else {
                        cacheRealValue.setQualityStrap(qualityStrapAll.get());
                    }
                }

                cacheRealValue.setPointDetailList(pointDetails);
            }
            res.add(cacheRealValue);
        });
        return res;
    }

    //??????????????????-??????0??????
    private List<StationCacheRealValue> packageResultStation(List<PointCache> realData, List<StationFirstNetBaseView> ones, List<StationFirstNetBaseView> zeros,
                                                             Map<Integer, Double> qualityStrapMap, Map<Integer, Double> timeStampMap) {
        Map<Integer, List<StationFirstNetBaseView>> zeroMap = zeros.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatTransferStationId));
        Map<Integer, List<PointCache>> collect = realData.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));

        List<StationCacheRealValue> res = new ArrayList<>();
        ones.forEach(e -> {
            //??????0????????????????????????
            Integer zeroSystemId = null;
            List<StationFirstNetBaseView> firstNetBaseViews = zeroMap.get(e.getHeatTransferStationId());
            // ???????????????id ?????????????????????????????????0??????
            if (!CollectionUtils.isEmpty(firstNetBaseViews)) {
                StationFirstNetBaseView stationSystemZero = firstNetBaseViews.stream()
                        .filter(x -> Objects.equals(e.getHeatCabinetId(), x.getHeatCabinetId())).findFirst().orElse(null);
                if (stationSystemZero != null) {
                    zeroSystemId = stationSystemZero.getHeatSystemId();
                }
            }
            StationCacheRealValue cacheRealValue = new StationCacheRealValue();
            cacheRealValue.setCompanyName(e.getHeatStationOrgName());
            cacheRealValue.setStationName(e.getHeatTransferStationName());
            cacheRealValue.setSystemName(e.getHeatSystemName());
            cacheRealValue.setCabinetName(e.getHeatCabinetName());
            cacheRealValue.setArea(e.getHeatSystemArea());
            cacheRealValue.setStationId(e.getHeatTransferStationId());
            cacheRealValue.setHeatSystemId(e.getHeatSystemId());
            // ???????????????
            Double timeStampDouble = timeStampMap.get(e.getHeatSystemId());
            if (timeStampDouble != null) {
                cacheRealValue.setTimeStamp(new BigDecimal(timeStampDouble).longValue());
            }
            //else {
            //     cacheRealValue.setTimeStamp(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            //}

            // ??????????????? ????????????  ????????? ???????????? ????????? ??? P1g ?????????????????????????????????????????????????????????????????? P1g ???????????????????????????????????????
            // ???????????????  ?????????????????????????????????
            Double rankValue = qualityStrapMap.get(e.getHeatSystemId());
            if (rankValue != null) {
                cacheRealValue.setQualityStrap(rankValue.intValue());
            } else {
                cacheRealValue.setQualityStrap(0);
            }

            if (collect.get(e.getHeatSystemId()) != null) {
                List<PointDetail> pointDetails = collect.get(e.getHeatSystemId()).stream().map(point -> {
                    // cacheRealValue.setQualityStrap(point.getQualityStrap());
                    PointDetail pointDetail = new PointDetail();
                    pointDetail.setSystemId(e.getHeatSystemId());
                    pointDetail.setPointId(point.getPointId());
                    pointDetail.setValue(point.getValue());
                    pointDetail.setColumnName(point.getPointName());
                    pointDetail.setAccidentLower(point.getAccidentLower());
                    pointDetail.setAccidentHigh(point.getAccidentHigh());
                    pointDetail.setRunningLower(point.getRunningLower());
                    pointDetail.setRunningHigh(point.getRunningHigh());
                    pointDetail.setDescriptionJson(point.getDescriptionJson());
                    pointDetail.setTimeStamp(point.getTimeStrap());
                    pointDetail.setQualityStrap(point.getQualityStrap());
                    return pointDetail;
                }).collect(Collectors.toList());
                if (zeroSystemId != null) {
                    Integer finalZeroSystemId = zeroSystemId;
                    if (!CollectionUtils.isEmpty(collect.get(zeroSystemId))) {
                        List<PointDetail> zeroPointDetails = collect.get(zeroSystemId).stream().map(point -> {
                            // cacheRealValue.setQualityStrap(point.getQualityStrap());
                            PointDetail pointDetail = new PointDetail();
                            pointDetail.setSystemId(finalZeroSystemId);
                            pointDetail.setPointId(point.getPointId());
                            pointDetail.setValue(point.getValue());
                            pointDetail.setColumnName(point.getPointName());
                            pointDetail.setAccidentLower(point.getAccidentLower());
                            pointDetail.setAccidentHigh(point.getAccidentHigh());
                            pointDetail.setRunningLower(point.getRunningLower());
                            pointDetail.setRunningHigh(point.getRunningHigh());
                            pointDetail.setDescriptionJson(point.getDescriptionJson());
                            pointDetail.setTimeStamp(point.getTimeStrap());
                            pointDetail.setQualityStrap(point.getQualityStrap());
                            return pointDetail;
                        }).collect(Collectors.toList());
                        pointDetails.addAll(zeroPointDetails);
                    }
                }

                cacheRealValue.setPointDetailList(pointDetails);
            }
            res.add(cacheRealValue);
        });
        return res;
    }

    // ?????????????????????????????? T1g
    public static boolean containsPoint(String[] arry, String columName) {
        for (String str : arry) {
            if (Objects.equals(str, columName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<HeatStationRealResponseDto> getRealData(HeatStationRealDto dto) {
        List<Integer> stationIds = dto.getStationIds();
        String[] pointNames = dto.getPointNames();
        // ??????????????????
        List<StationFirstNetBaseView> firstNetBaseList = this.firstNetBase();
        // ????????????????????????
        List<StationFirstNetBaseView> collect = firstNetBaseList.stream()
                .filter(e -> stationIds.contains(e.getHeatTransferStationId()) && e.getStatus())
                .collect(Collectors.toList());
        // ????????????????????????
        List<PointCache> realDatas = this.newCacheData(super.packageParam(collect, pointNames));
        Map<Integer, List<PointCache>> systemList = realDatas.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
        // ???????????????????????????
        List<HeatStationRealResponseDto> list = new ArrayList<>();

        collect.stream().forEach(e -> {
            // ???????????????
            List<PointCache> pointCaches = systemList.get(e.getHeatSystemId());
            if (!CollectionUtils.isEmpty(pointCaches)) {
                HeatStationRealResponseDto responseDto = new HeatStationRealResponseDto();
                responseDto.setStationId(e.getHeatTransferStationId());
                responseDto.setNumber(Integer.parseInt(e.getNumber()));
                responseDto.setSystemName(e.getHeatSystemName());
                List<HeatStationRealResponseDto.PointReal> pointReals = new ArrayList<>();
                pointCaches.stream().forEach(x -> {
                    pointReals.add(PointLRealConverter.INSTANCE.domainToInfo(x));
                });
                responseDto.setPoints(pointReals);
                list.add(responseDto);
            }
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> queryHydrostaticTempDiagram(QueryHydrostaticTempDto dto) {
        return Arrays.asList(tsccRestTemplate.post("realData/queryHydrostaticTempDiagram", dto, baseServer, Map[].class));

    }

    @Override
    public Map<String, Object> getBeyond(MonitorBeyondDto dto) {
        List<StationFirstNetBaseView> firstNetBaseList = this.firstNetBase();
        List<Integer> stations = this.getBeyondStation(dto);
        // ???????????????
        Map<Integer, Double> qualityStrap = super.getRank("qualityStrap", false).getMap();
        // ??????????????????
        Map<Integer, Double> timeStampMap = super.getRank("timeStamp", false).getMap();

        int page = dto.getCurrentPage();
        int size = dto.getPageCount();
        //??????????????????????????????,???????????????????????????
        //????????????0,1????????????
        Map<String, List<StationFirstNetBaseView>> map = firstNetBaseList.stream()
                .filter(e -> stations.contains(e.getHeatTransferStationId()) && e.getStatus())
                .collect(Collectors.groupingBy(StationFirstNetBaseView::getNumber));
        List<StationFirstNetBaseView> firstNetBaseListPartZero = map.get("0");
        map.keySet().removeIf(key -> Objects.equals(key, "0"));

        List<StationFirstNetBaseView> firstNetBaseListPart = new ArrayList<>();
        for (String key : map.keySet()) {
            firstNetBaseListPart.addAll(map.get(key));
        }

        List<StationFirstNetBaseView> firstNetBases = new ArrayList<>();


        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isNotBlank(dto.getColumnName())) {
            List<StationFirstNetBaseView> newFirstNetBaseList = new ArrayList<>();
            newFirstNetBaseList.addAll(firstNetBaseListPart);
            newFirstNetBaseList.addAll(firstNetBaseListPartZero);
            // ?????????????????????????????????????????????
            List<StationFirstNetBaseView> stationFirstNetBaseViews = super.beyondSort(dto, newFirstNetBaseList);
            if (!CollectionUtils.isEmpty(stationFirstNetBaseViews)) {

                ////??????????????????
                //long count = firstNetBaseListPart.size();

                LinkedHashSet<StationFirstNetBaseView> pageListPart = new LinkedHashSet<>();
                // ?????????0???????????????????????????????????????
                List<StationFirstNetBaseView> finalFirstNetBaseListPart = firstNetBaseListPart;
                stationFirstNetBaseViews.stream().forEach(x -> {
                    // ????????????0??????
                    if (Objects.equals(x.getNumber(), "0")) {
                        List<StationFirstNetBaseView> collect = finalFirstNetBaseListPart.stream().filter(a -> Objects.equals(a.getHeatTransferStationId(), x.getHeatTransferStationId()))
                                .collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)) {
                            pageListPart.addAll(collect);
                        }
                    } else {
                        pageListPart.add(x);
                    }

                });
                //??????????????????
                long count = pageListPart.size();
                //??????????????????
                firstNetBaseListPart = pageListPart.stream()
                        .skip((page - 1) * size).limit(size)//??????
                        .collect(Collectors.toList());

                Set<Integer> stationIds = firstNetBaseListPart.stream().map(StationFirstNetBaseView::getHeatTransferStationId).collect(Collectors.toSet());
                firstNetBaseListPartZero = firstNetBaseListPartZero.stream()
                        .filter(e -> stationIds.contains(e.getHeatTransferStationId()))
                        .collect(Collectors.toList());


                firstNetBases.addAll(firstNetBaseListPartZero);
                firstNetBases.addAll(firstNetBaseListPart);
                //????????????
                List<PointCache> pointCacheArrayList = null;
                if (dto.getExport()) {
                    pointCacheArrayList = this.monitorExport(super.packageParam(firstNetBases, new String[]{dto.getColumnName()}));
                } else {
                    // ??????????????????
                    pointCacheArrayList = this.newCacheData(super.packageParam(firstNetBases, new String[]{dto.getColumnName()}));
                    // pointCacheArrayList = this.monitorData(super.packageParam(firstNetBases, dto.getColumnName()));
                }

                res.put("total", count);
                res.put("data", this.packageResultStation(pointCacheArrayList, firstNetBaseListPart, firstNetBaseListPartZero, qualityStrap, timeStampMap));

            }

        }
        return res;
    }


}
