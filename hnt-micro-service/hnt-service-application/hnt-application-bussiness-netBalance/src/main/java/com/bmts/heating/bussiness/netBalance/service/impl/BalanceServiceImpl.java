package com.bmts.heating.bussiness.netBalance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import net.sf.json.
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.netBalance.service.BalanceService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.db.mapper.BalanceStepConfigMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.balance.pojo.*;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.middleware.monitor.pojo.MonitorGrpcResult;
import com.bmts.heating.middleware.monitor.service.GrpcIssueService;
import com.bmts.heating.middleware.monitor.service.MonitorIssueService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    //??????????????????????????????????????????
    @Autowired
    private BalanceNetService balanceNetService;
    //???????????????????????????????????????????????????
    @Autowired
    private BalanceStepConfigService balanceStepConfigService;
    //???????????????????????????????????????????????????
    @Autowired
    private BalanceNetLimitService balanceNetLimitService;
    //??????????????????????????????????????????
    @Autowired
    private BalanceMembersService balanceMembersService;
    //????????????????????????????????????
    @Autowired
    private BalanceRejectHistoryService balanceRejectHistoryService;
    //??????????????????????????????????????????
    @Autowired
    private BalanceTargetHistoryService balanceTargetHistoryService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private BalanceNetControlLogService balanceNetControlLogService;
    //??????Redis??????????????????????????????????????????
    @Autowired
    private RedisCacheService redisCacheService;
    //?????????????????????????????????????????????GRPC??????
    @Autowired
    private GrpcIssueService grpcIssueService;
    //??????Signal??????
    @Autowired
    private SignalRTemplate signalRTemplate;
    @Autowired
    private TSCCRestTemplate template;
    @Autowired
    private WebPageConfigService webPageConfigService;

    @Value("${netbalance.arithmeticUrl}")
    private String arithmeticUrl;

    /**
     * ??????????????????????????????
     *
     * @param balanceId ????????????Id(0???????????????),controlType 1??????????????????2?????????
     * @return
     */
    @Override
    public Response start(int balanceId, int controlType) {
        try {
           WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey,"netBalanceDataSourceType"));
           if (Objects.equals(webPageConfig.getJsonConfig().toLowerCase(),"pvss")){
               //????????????????????????????????????---pvss?????????
               return queryBasic(balanceId, controlType, 2);
           }else {
               //????????????????????????????????????---??????????????????
               return queryBasic(balanceId, controlType, 1);
           }

        } catch (Exception e) {
            log.error("??????????????????????????????" + e.getMessage());
            return Response.fail();
        }
    }

    @Override
    /**
     * ????????? ??????????????????db??????
     *
     * @param balanceId ????????????Id(0???????????????),controlType 1??????????????????2?????????,dataSourceType(????????????) 1????????????2???pvss
     * @return
     */
    public Response queryBasic(int balanceId, int controlType, int dataSourceType) {
        try {
            BalanceNet balanceNet = balanceNetService.getById(balanceId);
            if (dataSourceType == 1) {
                //??????????????????????????????????????????
                QueryWrapper<BalanceMembers> balanceMembersQueryWrapper = new QueryWrapper<>();
                balanceMembersQueryWrapper.eq("b.balanceNetId", balanceId);
                List<BalanceSystemInfo> balanceSystemInfoList = balanceMembersService.balanceSystemInfo(balanceMembersQueryWrapper);
                //???????????????????????????????????????
                // List<BalanceStepConfig> balanceStepConfigList = balanceStepConfigMapper.selectList(null);
                List<BalanceStepConfig> balanceStepConfigList = balanceStepConfigService.list();
                //?????????????????????????????????
                QueryWrapper<BalanceNetLimit> queryWrapperNetLimit = new QueryWrapper<>();
                queryWrapperNetLimit.eq("balanceNetId", balanceId);
                List<BalanceNetLimit> balanceNetLimitList = balanceNetLimitService.list(queryWrapperNetLimit);
                //????????????????????????????????????
                ComputDo computDo = new ComputDo();
                //?????????????????????
                computDo.setCompensation(Float.valueOf(balanceNet.getCompensation().toString()));
                //?????????????????????????????????
                computDo.setPermissible_error(Float.valueOf(balanceNet.getErrorValue().toString()));
                //?????????????????????????????????(???)
                computDo.setTimeout(balanceNet.getTimeOut());
                //?????????????????????
                Temps temps = new Temps();
                balanceNetLimitList.stream().filter(s -> s.getPointName().equals("T2g")).forEach(tempObj -> {
                    if (tempObj.getCompareType() == 1) {
                        temps.setHi(Float.valueOf(tempObj.getValue().toString()));
                    }
                    if (tempObj.getCompareType() == 2) {
                        temps.setLo(Float.valueOf(tempObj.getValue().toString()));
                    }
                });
                computDo.setTemps(temps);
                //?????????????????????
                Tempr tempr = new Tempr();
                balanceNetLimitList.stream().filter(s -> s.getPointName().equals("T2h")).forEach(tempObj -> {
                    if (tempObj.getCompareType() == 1) {
                        tempr.setHi(Float.valueOf(tempObj.getValue().toString()));
                    }
                    if (tempObj.getCompareType() == 2) {
                        tempr.setLo(Float.valueOf(tempObj.getValue().toString()));
                    }
                });
                computDo.setTempr(tempr);
                //?????????????????????
                Valve valve = new Valve();
                balanceNetLimitList.stream().filter(s -> s.getPointName().equals("Valve")).forEach(tempObj -> {
                    if (tempObj.getCompareType() == 1) {
                        valve.setHi(Float.valueOf(tempObj.getValue().toString()));
                    }
                    if (tempObj.getCompareType() == 2) {
                        valve.setLo(Float.valueOf(tempObj.getValue().toString()));
                    }
                });
                computDo.setValve(valve);
                //??????????????????
                Pump pump = new Pump();
                balanceNetLimitList.stream().filter(s -> s.getPointName().equals("Pump")).forEach(tempObj -> {
                    if (tempObj.getCompareType() == 1) {
                        pump.setHi(Float.valueOf(tempObj.getValue().toString()));
                    }
                    if (tempObj.getCompareType() == 2) {
                        pump.setLo(Float.valueOf(tempObj.getValue().toString()));
                    }
                });
                computDo.setPump(pump);
                //??????????????????
                computDo.sectionvalve = balanceStepConfigList.stream().filter(s -> s.getWorkType() == 1).map(item -> {
                    Sectionvalve sectionvalve = new Sectionvalve();
                    sectionvalve.setLimit(Float.valueOf(item.getLimits().toString()));
                    sectionvalve.setStep(Float.valueOf(item.getStep().toString()));
                    return sectionvalve;
                }).collect(Collectors.toList());
                //???????????????
                computDo.sectionpump = balanceStepConfigList.stream().filter(s -> s.getWorkType() == 2).map(item -> {
                    Sectionpump sectionpump = new Sectionpump();
                    sectionpump.setLimit(Float.valueOf(item.getLimits().toString()));
                    sectionpump.setStep(Float.valueOf(item.getStep().toString()));
                    return sectionpump;
                }).collect(Collectors.toList());

                //??????????????????????????????
                Map<Integer, String[]> maps = new HashMap<>();
                for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                    maps.put(systemInfo.getRelevanceId(), new String[]{"T2g", "T2h", "CV1_SV", "CV1_U", "CV1_MSP", "CV1_Mode"});
                }
                //??????????????????
                List<PointCache> realValueList = redisCacheService.queryRealDataBySystems(maps, 1);

                //?????????????????????????????????
                List<Unit> units = new ArrayList<>();
                for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                    Unit unit = new Unit();
                    //Id
                    unit.setId(systemInfo.getRelevanceId().toString());
                    List<PointCache> pointCacheList = realValueList.stream().filter(s -> s.getRelevanceId() == systemInfo.getRelevanceId()).collect(Collectors.toList());
                    //????????????
                    PointCache pointCacheTg = pointCacheList.stream().filter(s -> s.getPointName().equals("T2g")).findFirst().orElse(null);
                    if (pointCacheTg != null) {
                        unit.setTg(Float.valueOf(pointCacheTg.getValue()));
                    }
                    //????????????
                    PointCache pointCacheTh = pointCacheList.stream().filter(s -> s.getPointName().equals("T2h")).findFirst().orElse(null);
                    if (pointCacheTh != null) {
                        unit.setTh(Float.valueOf(pointCacheTh.getValue()));
                    }
                    //??????????????????(%)--?????????????????????????????????????????????
                    PointCache pointCacheValve = pointCacheList.stream().filter(s -> s.getPointName().equals("CV1_U")).findFirst().orElse(null);
                    if (pointCacheValve == null) {
                        unit.setFeedback(-1);
                        log.info("???????????????????????????:" + systemInfo.getSystemName() + ",????????????:" + systemInfo.getRelevanceId() + "?????????????????????????????????????????????");
                    } else {
                        unit.setFeedback(Float.valueOf(pointCacheValve.getValue()));
                    }

                    float bcTemp = 0;
                    if (systemInfo.getCompensationValue() == null) {
                        bcTemp = Float.valueOf(systemInfo.getCompensation().toString()) + 0;
                    } else {
                        bcTemp = Float.valueOf(systemInfo.getCompensation().toString()) + Float.valueOf(systemInfo.getCompensationValue().toString());
                    }
                    //????????????(???)
                    unit.setTc(bcTemp);
                    //????????????(??????)
                    unit.setArea(Float.valueOf(systemInfo.getHeatArea().toString()));
                    //?????????
                    long times = new Date().getTime();
                    unit.setTimestamp(times);
                    //?????? 1-??????(% ??????) 2-?????????(Hz ??????????????????) 3-??????(??? ??????)
                    //unit.type = item.ControlType;
                    unit.setType(systemInfo.getControlType());
                    //??????????????????(1-????????2-??????)
                    unit.setRadiatemethod(systemInfo.getHeatingType());
                    //??????(????????????????????????)
                    unit.setEnable(systemInfo.getStatus());
                    //?????????????????????????????????
                    PointCache pointCacheSetValue = pointCacheList.stream().filter(s -> s.getPointName().equals("CV1_SV")).findFirst().orElse(null);
                    if (pointCacheSetValue == null) {
                        unit.setSetting(-1);
                        log.info("???????????????????????????:" + systemInfo.getSystemName() + ",????????????:" + systemInfo.getRelevanceId() + "?????????????????????????????????????????????");
                    } else {
                        unit.setSetting(Float.valueOf(pointCacheSetValue.getValue()));
                    }
                    units.add(unit);
                }
                computDo.units = units;
                //????????????(targetlast)???????????? 0-???????????? 1-????????????
                computDo.setMode(balanceNet.getComputeType());
                if (balanceNet.getComputeType() == 0) {
                    computDo.setTarget(Float.valueOf(balanceNet.getComputeTargetAvg().toString()));
                } else {
                    computDo.setTarget(Float.valueOf(balanceNet.getComputeTargetTemp().toString()));
                }
                //??????????????????
                callCompute(computDo, balanceId, controlType, realValueList, balanceNet);
            } else {
                //??????????????????
                callCompute(balanceId, controlType, balanceNet);
            }
            return Response.success();
        } catch (Exception e) {
            log.error("-----?????????????????????????????? {}",e.getMessage());
            return Response.fail();
        }
    }

    /**
     * ????????? ??????????????????(????????????)
     *
     * @param dos
     * @return
     */
    @Override
    public void callCompute(ComputDo dos, int balanceId, int controlType, List<PointCache> pointCacheList, BalanceNet balanceNet) {
        //???????????????????????????????????????????????????
        ResultBalanceInfo resultBalanceInfo = template.postBalance(arithmeticUrl, "/api/netbalance/target?control=true", dos, ResultBalanceInfo.class);
        //??????????????????????????????
        BalanceTargetHistory balanceTargetHistory = new BalanceTargetHistory();
        balanceTargetHistory.setBalanceNetId(balanceId);
        balanceTargetHistory.setCreateTime(LocalDateTime.now());
        balanceTargetHistory.setImbalance(new BigDecimal(resultBalanceInfo.getImbalance()));
        balanceTargetHistory.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()));
        balanceTargetHistory.setTargetTemp(new BigDecimal(resultBalanceInfo.getTarget()));
        balanceTargetHistory.setRealtargetTemp(new BigDecimal(resultBalanceInfo.getTargetnet()));
        //?????????????????????????????????????????????
        recordBalanceTargetHistory(balanceTargetHistory);
        //????????????????????????????????????
        UpdateWrapper<BalanceNet> queryWrapperBalanceNet = new UpdateWrapper<>();
        queryWrapperBalanceNet.set("computeArea", resultBalanceInfo.getArea());
        queryWrapperBalanceNet.set("realArea", resultBalanceInfo.getAreanet());
        queryWrapperBalanceNet.set("computeTargetAvg", resultBalanceInfo.getTarget());
        queryWrapperBalanceNet.set("realTarget", resultBalanceInfo.getTargetnet());
        queryWrapperBalanceNet.set("computeImbalance", resultBalanceInfo.getImbalance());
        queryWrapperBalanceNet.set("realImbalance", resultBalanceInfo.getImbalancenet());
        queryWrapperBalanceNet.set("systemCount", resultBalanceInfo.getUnits().stream().count());
        if (controlType == 1) {
            queryWrapperBalanceNet.set("nextControlTime", LocalDateTime.now().plusMinutes(balanceNet.getCycle()));
        }
        queryWrapperBalanceNet.eq("Id", balanceId);
        Boolean updBalanceNetResult = balanceNetService.update(queryWrapperBalanceNet);
        if (updBalanceNetResult) {
            log.info("????????????????????????????????????:" + resultBalanceInfo + new Date());
            BalanceBasicVo balanceBasicVo = new BalanceBasicVo();
            balanceBasicVo.setId(balanceId);
            balanceBasicVo.setComputeArea(new BigDecimal(resultBalanceInfo.getArea()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealArea(new BigDecimal(resultBalanceInfo.getAreanet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setComputeTargetAvg(new BigDecimal(resultBalanceInfo.getTarget()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealTarget(new BigDecimal(resultBalanceInfo.getTargetnet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setComputeImbalance(new BigDecimal(resultBalanceInfo.getImbalance()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setSystemCount((int) resultBalanceInfo.getUnits().stream().count());
            Map<String, Object> mapPush = new HashMap<>();
            mapPush.put("netBalanceBasic", balanceBasicVo);
            String args = JSON.toJSONString(mapPush);
            //????????????????????????????????????
            signalRTemplate.send2AllServerSpecial("PushBalanceHeader", args);
        } else {
            log.info("????????????????????????????????????:" + resultBalanceInfo + new Date());
        }
        //?????????BalanceRejectHistory??????????????????????????????????????????
        List<BalanceRejectHistory> balanceRejectHistoryList = resultBalanceInfo.getUnits().stream().filter(s -> !s.getIsvalid()).map(s -> {
            BalanceRejectHistory balanceRejectHistory = new BalanceRejectHistory();
            balanceRejectHistory.setBalanceNetId(balanceId);
            balanceRejectHistory.setRelevanceId(Integer.parseInt(s.getId()));
            balanceRejectHistory.setLevel(1);
            balanceRejectHistory.setTempTg(new BigDecimal(s.getTg()));
            balanceRejectHistory.setTempTh(new BigDecimal(s.getTh()));
            balanceRejectHistory.setRejectTime(LocalDateTime.now());
            balanceRejectHistory.setReason(s.getError());
            return balanceRejectHistory;
        }).collect(Collectors.toList());
        //?????????????????????????????????
        recordRejectHistory(balanceRejectHistoryList);
        //?????????????????????????????????
        if (controlType == 1) {
            List<PointCache> PointCacheList = pointCacheList.stream().filter(s -> s.getPointName().equals("CV1_MSP") || s.getPointName().equals("CV1_Mode")).collect(Collectors.toList());
            List<PointL> pointLSList = new ArrayList();
            List<Unit> unitList = resultBalanceInfo.getUnits().stream().filter(o -> o.getIsvalid()).collect(Collectors.toList());
            for (Unit u : unitList) {
                List<PointCache> pointCacheListTrue = PointCacheList.stream().filter(f -> f.getRelevanceId() == Integer.parseInt(u.getId())).collect(Collectors.toList());
                for (PointCache s : pointCacheListTrue) {
                    PointL pointL = new PointL();
                    pointL.setPointId(s.getPointId());
                    pointL.setPointName(s.getPointName());
                    pointL.setParentSyncNum(s.getParentSyncNum());
                    pointL.setSystemNum(s.getSystemNum());
                    pointL.setType(s.getType());
                    pointL.setDeviceId(s.getDeviceId());
                    pointL.setApplicationName(s.getApplicationName());
                    pointL.setPointlsSign(s.getPointlsSign());
                    pointL.setOrderValue(s.getOrderValue());
                    if (s.getPointName().equals("CV1_Mode")) {
                        pointL.setValue("0");
                    } else {
                        pointL.setValue(Float.toString(u.getSetting()));
                    }
                    pointL.setTimeStrap(s.getTimeStrap());
                    pointL.setQualityStrap(s.getQualityStrap());
                    pointL.setOldValue(s.getOldValue());
                    pointL.setRelevanceId(s.getRelevanceId());
                    pointL.setLevel(s.getLevel());
                    pointL.setDataType(1);
                    pointL.setExpandDesc(s.getExpandDesc());
                    pointL.setPointStandardName(s.getPointStandardName());
                    pointL.setParentSyncNum(s.getParentSyncNum());
                    pointLSList.add(pointL);
                }
            }
            if (!CollectionUtils.isEmpty(pointLSList)) {
                control(pointLSList, balanceId);
            }
        }
    }

    /**
     * ????????? ??????????????????(PVSS????????????)
     *
     * @param
     * @return
     */
    @Override
    public void callCompute(int balanceId, int controlType, BalanceNet balanceNet) {
        GetPvssNetBalanceDto dto = new GetPvssNetBalanceDto();
        dto.setHeatNetID(balanceNet.getSyncNetBalanceId());
        Gson gson = new Gson();
        String json = gson.toJson(dto);
        Object obj = gson.fromJson(json, Object.class);
        //??????PVSS?????????????????????????????????????????????
        ResultBalanceInfo resultBalanceInfo = template.postBalance(arithmeticUrl, "/QueryNetBalanceData", obj, ResultBalanceInfo.class);
        //??????????????????????????????
        BalanceTargetHistory balanceTargetHistory = new BalanceTargetHistory();
        balanceTargetHistory.setBalanceNetId(balanceId);
        balanceTargetHistory.setCreateTime(LocalDateTime.now());
        balanceTargetHistory.setImbalance(new BigDecimal(resultBalanceInfo.getImbalance()));
        balanceTargetHistory.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()));
        balanceTargetHistory.setTargetTemp(new BigDecimal(resultBalanceInfo.getTarget()));
        balanceTargetHistory.setRealtargetTemp(new BigDecimal(resultBalanceInfo.getTargetnet()));
        //?????????????????????????????????????????????
        recordBalanceTargetHistory(balanceTargetHistory);
        //????????????????????????????????????
        UpdateWrapper<BalanceNet> queryWrapperBalanceNet = new UpdateWrapper<>();
        queryWrapperBalanceNet.set("computeArea", resultBalanceInfo.getArea());
        queryWrapperBalanceNet.set("realArea", resultBalanceInfo.getAreanet());
        queryWrapperBalanceNet.set("computeTargetAvg", resultBalanceInfo.getTarget());
        queryWrapperBalanceNet.set("realTarget", resultBalanceInfo.getTargetnet());
        queryWrapperBalanceNet.set("computeImbalance", resultBalanceInfo.getImbalance());
        queryWrapperBalanceNet.set("realImbalance", resultBalanceInfo.getImbalancenet());
        queryWrapperBalanceNet.set("systemCount", resultBalanceInfo.getUnits().stream().count());
        if (controlType == 1) {
            queryWrapperBalanceNet.set("nextControlTime", LocalDateTime.now().plusMinutes(balanceNet.getCycle()));
        }
        queryWrapperBalanceNet.eq("Id", balanceId);
        Boolean updBalanceNetResult = balanceNetService.update(queryWrapperBalanceNet);
        if (updBalanceNetResult) {
            log.info("????????????????????????????????????:" + resultBalanceInfo + new Date());
            BalanceBasicVo balanceBasicVo = new BalanceBasicVo();
            balanceBasicVo.setId(balanceId);
            balanceBasicVo.setComputeArea(new BigDecimal(resultBalanceInfo.getArea()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealArea(new BigDecimal(resultBalanceInfo.getAreanet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setComputeTargetAvg(new BigDecimal(resultBalanceInfo.getTarget()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealTarget(new BigDecimal(resultBalanceInfo.getTargetnet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setComputeImbalance(new BigDecimal(resultBalanceInfo.getImbalance()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()).setScale(2, RoundingMode.HALF_UP));
            balanceBasicVo.setSystemCount((int) resultBalanceInfo.getUnits().stream().count());
            Map<String, Object> mapPush = new HashMap<>();
            mapPush.put("netBalanceBasic", balanceBasicVo);
            String args = JSON.toJSONString(mapPush);
            //????????????????????????????????????
            signalRTemplate.send2AllServerSpecial("PushBalanceHeader", args);
        } else {
            log.info("????????????????????????????????????:" + resultBalanceInfo + new Date());
        }
        QueryWrapper<StationFirstNetBaseView> stationFirstNetBaseViewQueryWrapper = new QueryWrapper<>();
        stationFirstNetBaseViewQueryWrapper.in("systemSyncNum", resultBalanceInfo.getUnits().stream().map(s -> s.getId()).collect(Collectors.toList()));
        stationFirstNetBaseViewQueryWrapper.select("heatSystemId", "systemSyncNum");
        List<StationFirstNetBaseView> viewList = stationFirstNetBaseViewService.list(stationFirstNetBaseViewQueryWrapper);
        //?????????BalanceRejectHistory??????????????????????????????????????????
        List<BalanceRejectHistory> balanceRejectHistoryList = resultBalanceInfo.getUnits().stream().filter(s -> !s.getIsvalid()).map(s -> {
            BalanceRejectHistory balanceRejectHistory = new BalanceRejectHistory();
            StationFirstNetBaseView stationFirstNetBaseView = viewList.stream().filter(v -> Objects.equals(s.getId(), v.getSystemSyncNum().toString())).findFirst().orElse(null);
            balanceRejectHistory.setBalanceNetId(balanceId);
            balanceRejectHistory.setRelevanceId(stationFirstNetBaseView.getHeatSystemId());
            balanceRejectHistory.setLevel(1);
            balanceRejectHistory.setTempTg(new BigDecimal(s.getTg()));
            balanceRejectHistory.setTempTh(new BigDecimal(s.getTh()));
            balanceRejectHistory.setRejectTime(LocalDateTime.now());
            balanceRejectHistory.setReason(s.getError());
            return balanceRejectHistory;
        }).collect(Collectors.toList());
        //?????????????????????????????????
        recordRejectHistory(balanceRejectHistoryList);
    }

    /**
     * ??????????????????
     */
    @Override
    public void recordRejectHistory(List<BalanceRejectHistory> balanceRejectHistoriesList) {
        try {
            if (!CollectionUtils.isEmpty(balanceRejectHistoriesList)) {
                //??????????????????????????????????????????????????????
                Boolean result = balanceRejectHistoryService.saveBatch(balanceRejectHistoriesList);
                if (result) {
                    log.info("?????????????????????????????????????????????" + LocalDateTime.now());
                } else {
                    log.error("?????????????????????????????????????????????" + LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            log.error("?????????????????????????????????????????????" + LocalDateTime.now());
        }
    }

    @Override
    public void recordBalanceTargetHistory(BalanceTargetHistory balanceTargetHistoriesList) {
        try {
            //??????????????????????????????????????????????????????
            Boolean result = balanceTargetHistoryService.save(balanceTargetHistoriesList);
            if (result) {
                log.info("???????????????????????????????????????" + LocalDateTime.now());
            } else {
                log.error("???????????????????????????????????????" + LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("???????????????????????????????????????" + LocalDateTime.now());
        }
    }

    /**
     * ????????? ??????grpc ????????????
     *
     * @param pointLList
     * @return
     */
    @Override
    public void control(List<PointL> pointLList, int balanceId) {
        //??????GRPC??????
        Response response = grpcIssueService.setGrpc(pointLList);
        List<BalanceNetControlLog> balanceNetControlLogs = new ArrayList<>();
        //???????????????
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            balanceNetControlLogs = pointLList.stream().map(s -> {
                BalanceNetControlLog balanceNetControlLog = new BalanceNetControlLog();
                balanceNetControlLog.setRelevanceId(s.getRelevanceId());
                balanceNetControlLog.setLevel(s.getLevel());
                balanceNetControlLog.setPointName(s.getPointStandardName());
                balanceNetControlLog.setPointAddress(s.getParentSyncNum().toString() + ".ValueParaDesc." + s.getSystemNum().toString() + "." + s.getPointName());
                balanceNetControlLog.setStatus(true);
                balanceNetControlLog.setMsg("??????");
                balanceNetControlLog.setBalanceNetId(balanceId);
                balanceNetControlLog.setControlValue(new BigDecimal(s.getValue()));
                balanceNetControlLog.setCreateTime(LocalDateTime.now());
                return balanceNetControlLog;
            }).collect(Collectors.toList());
        }
        //????????????????????????
        if (response.getCode() == ResponseCode.MIDDLE.getCode()) {
            List<PointL> resultPointLS = JSON.parseArray(JSON.toJSONString(response.getData()), PointL.class);
            for (PointL ponit : pointLList) {
                PointL p = resultPointLS.stream().filter(s -> s.getRelevanceId() == ponit.getRelevanceId()).findFirst().orElse(null);
                BalanceNetControlLog balanceNetControlLog = new BalanceNetControlLog();
                if (p != null) {
                    balanceNetControlLog.setRelevanceId(p.getRelevanceId());
                    balanceNetControlLog.setLevel(p.getLevel());
                    balanceNetControlLog.setPointName(p.getPointStandardName());
                    balanceNetControlLog.setPointAddress(p.getParentSyncNum().toString() + ".ValueParaDesc." + p.getSystemNum().toString() + "." + p.getPointName());
                    balanceNetControlLog.setStatus(false);
                    balanceNetControlLog.setMsg("????????????");
                    balanceNetControlLog.setBalanceNetId(balanceId);
                    balanceNetControlLog.setControlValue(new BigDecimal(p.getValue()));
                    balanceNetControlLog.setCreateTime(LocalDateTime.now());
                } else {
                    balanceNetControlLog.setRelevanceId(ponit.getRelevanceId());
                    balanceNetControlLog.setLevel(ponit.getLevel());
                    balanceNetControlLog.setPointName(ponit.getPointStandardName());
                    balanceNetControlLog.setPointAddress(p.getParentSyncNum().toString() + ".ValueParaDesc." + p.getSystemNum().toString() + "." + p.getPointName());
                    balanceNetControlLog.setStatus(true);
                    balanceNetControlLog.setMsg("??????");
                    balanceNetControlLog.setBalanceNetId(balanceId);
                    balanceNetControlLog.setControlValue(new BigDecimal(ponit.getValue()));
                    balanceNetControlLog.setCreateTime(LocalDateTime.now());
                }
                balanceNetControlLogs.add(balanceNetControlLog);
            }
        }
        //????????????
        if (response.getCode() == ResponseCode.FAIL.getCode()) {
            balanceNetControlLogs = pointLList.stream().map(s -> {
                BalanceNetControlLog balanceNetControlLog = new BalanceNetControlLog();
                balanceNetControlLog.setRelevanceId(s.getRelevanceId());
                balanceNetControlLog.setLevel(s.getLevel());
                balanceNetControlLog.setPointName(s.getPointStandardName());
                String address = s.getParentSyncNum().toString() + ".ValueParaDesc." + s.getSystemNum().toString() + "." + s.getPointName();
                balanceNetControlLog.setPointAddress(address);
                balanceNetControlLog.setStatus(false);
                balanceNetControlLog.setMsg("????????????");
                balanceNetControlLog.setBalanceNetId(balanceId);
                balanceNetControlLog.setControlValue(new BigDecimal(s.getValue()));
                balanceNetControlLog.setCreateTime(LocalDateTime.now());
                return balanceNetControlLog;
            }).collect(Collectors.toList());
        }
        recordControlLog(balanceNetControlLogs);
    }

    @Override
    public void recordControlLog(List<BalanceNetControlLog> logsList) {
        try {
            //??????????????????????????????????????????????????????
            Boolean result = balanceNetControlLogService.saveBatch(logsList);
            if (result) {
                log.info("?????????????????????????????????????????????" + LocalDateTime.now());
            } else {
                log.error("?????????????????????????????????????????????" + LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("?????????????????????????????????????????????" + LocalDateTime.now());
        }
    }


}
