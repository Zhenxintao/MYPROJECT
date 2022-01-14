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
    //获取全网平衡基础信息服务服务
    @Autowired
    private BalanceNetService balanceNetService;
    //获取全网平衡步幅、步长配置信息服务
    @Autowired
    private BalanceStepConfigService balanceStepConfigService;
    //获取全网平衡高限、底线配置信息服务
    @Autowired
    private BalanceNetLimitService balanceNetLimitService;
    //获取全网平衡所属系统信息服务
    @Autowired
    private BalanceMembersService balanceMembersService;
    //获取全网平衡剔除信息服务
    @Autowired
    private BalanceRejectHistoryService balanceRejectHistoryService;
    //获取全网平衡历史数据信息服务
    @Autowired
    private BalanceTargetHistoryService balanceTargetHistoryService;
    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    private BalanceNetControlLogService balanceNetControlLogService;
    //获取Redis缓存信息，查询实时数据服务。
    @Autowired
    private RedisCacheService redisCacheService;
    //注册控制下发服务，获取控制下发GRPC接口
    @Autowired
    private GrpcIssueService grpcIssueService;
    //注册Signal服务
    @Autowired
    private SignalRTemplate signalRTemplate;
    @Autowired
    private TSCCRestTemplate template;
    @Autowired
    private WebPageConfigService webPageConfigService;

    @Value("${netbalance.arithmeticUrl}")
    private String arithmeticUrl;

    /**
     * 启动全网平衡参数入口
     *
     * @param balanceId 全网平衡Id(0为查询全部),controlType 1为调控下发，2为空算
     * @return
     */
    @Override
    public Response start(int balanceId, int controlType) {
        try {
           WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey,"netBalanceDataSourceType"));
           if (Objects.equals(webPageConfig.getJsonConfig().toLowerCase(),"pvss")){
               //调用全网平衡信息服务接口---pvss数据源
               return queryBasic(balanceId, controlType, 2);
           }else {
               //调用全网平衡信息服务接口---平台内部算法
               return queryBasic(balanceId, controlType, 1);
           }

        } catch (Exception e) {
            log.error("全网平衡应用服务出错" + e.getMessage());
            return Response.fail();
        }
    }

    @Override
    /**
     * 第一步 查询全网平衡db数据
     *
     * @param balanceId 全网平衡Id(0为查询全部),controlType 1为调控下发，2为空算,dataSourceType(数据来源) 1为平台，2为pvss
     * @return
     */
    public Response queryBasic(int balanceId, int controlType, int dataSourceType) {
        try {
            BalanceNet balanceNet = balanceNetService.getById(balanceId);
            if (dataSourceType == 1) {
                //获取该全网平衡下所有机组信息
                QueryWrapper<BalanceMembers> balanceMembersQueryWrapper = new QueryWrapper<>();
                balanceMembersQueryWrapper.eq("b.balanceNetId", balanceId);
                List<BalanceSystemInfo> balanceSystemInfoList = balanceMembersService.balanceSystemInfo(balanceMembersQueryWrapper);
                //获取阀门、泵步幅，步长参数
                // List<BalanceStepConfig> balanceStepConfigList = balanceStepConfigMapper.selectList(null);
                List<BalanceStepConfig> balanceStepConfigList = balanceStepConfigService.list();
                //获取全网平衡高低限信息
                QueryWrapper<BalanceNetLimit> queryWrapperNetLimit = new QueryWrapper<>();
                queryWrapperNetLimit.eq("balanceNetId", balanceId);
                List<BalanceNetLimit> balanceNetLimitList = balanceNetLimitService.list(queryWrapperNetLimit);
                //定义全网平衡计算服务入参
                ComputDo computDo = new ComputDo();
                //目标温度补偿值
                computDo.setCompensation(Float.valueOf(balanceNet.getCompensation().toString()));
                //给定与反馈值得允许误差
                computDo.setPermissible_error(Float.valueOf(balanceNet.getErrorValue().toString()));
                //判断阀门可用的超时时间(秒)
                computDo.setTimeout(balanceNet.getTimeOut());
                //二次供温高低限
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
                //二次回温高低限
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
                //阀门开度高低限
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
                //泵频率高低限
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
                //阀门步幅步长
                computDo.sectionvalve = balanceStepConfigList.stream().filter(s -> s.getWorkType() == 1).map(item -> {
                    Sectionvalve sectionvalve = new Sectionvalve();
                    sectionvalve.setLimit(Float.valueOf(item.getLimits().toString()));
                    sectionvalve.setStep(Float.valueOf(item.getStep().toString()));
                    return sectionvalve;
                }).collect(Collectors.toList());
                //泵步幅步长
                computDo.sectionpump = balanceStepConfigList.stream().filter(s -> s.getWorkType() == 2).map(item -> {
                    Sectionpump sectionpump = new Sectionpump();
                    sectionpump.setLimit(Float.valueOf(item.getLimits().toString()));
                    sectionpump.setStep(Float.valueOf(item.getStep().toString()));
                    return sectionpump;
                }).collect(Collectors.toList());

                //定义实时数据参数条件
                Map<Integer, String[]> maps = new HashMap<>();
                for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                    maps.put(systemInfo.getRelevanceId(), new String[]{"T2g", "T2h", "CV1_SV", "CV1_U", "CV1_MSP", "CV1_Mode"});
                }
                //获取实时数据
                List<PointCache> realValueList = redisCacheService.queryRealDataBySystems(maps, 1);

                //创建换热站机组信息集合
                List<Unit> units = new ArrayList<>();
                for (BalanceSystemInfo systemInfo : balanceSystemInfoList) {
                    Unit unit = new Unit();
                    //Id
                    unit.setId(systemInfo.getRelevanceId().toString());
                    List<PointCache> pointCacheList = realValueList.stream().filter(s -> s.getRelevanceId() == systemInfo.getRelevanceId()).collect(Collectors.toList());
                    //供水温度
                    PointCache pointCacheTg = pointCacheList.stream().filter(s -> s.getPointName().equals("T2g")).findFirst().orElse(null);
                    if (pointCacheTg != null) {
                        unit.setTg(Float.valueOf(pointCacheTg.getValue()));
                    }
                    //回水温度
                    PointCache pointCacheTh = pointCacheList.stream().filter(s -> s.getPointName().equals("T2h")).findFirst().orElse(null);
                    if (pointCacheTh != null) {
                        unit.setTh(Float.valueOf(pointCacheTh.getValue()));
                    }
                    //阀门开度反馈(%)--获取阀门反馈开度（实际）数据值
                    PointCache pointCacheValve = pointCacheList.stream().filter(s -> s.getPointName().equals("CV1_U")).findFirst().orElse(null);
                    if (pointCacheValve == null) {
                        unit.setFeedback(-1);
                        log.info("无法获取系统名称为:" + systemInfo.getSystemName() + ",系统号为:" + systemInfo.getRelevanceId() + "的采集量阀门反馈开度标识无数据");
                    } else {
                        unit.setFeedback(Float.valueOf(pointCacheValve.getValue()));
                    }

                    float bcTemp = 0;
                    if (systemInfo.getCompensationValue() == null) {
                        bcTemp = Float.valueOf(systemInfo.getCompensation().toString()) + 0;
                    } else {
                        bcTemp = Float.valueOf(systemInfo.getCompensation().toString()) + Float.valueOf(systemInfo.getCompensationValue().toString());
                    }
                    //补偿温度(℃)
                    unit.setTc(bcTemp);
                    //供热面积(万㎡)
                    unit.setArea(Float.valueOf(systemInfo.getHeatArea().toString()));
                    //时间戳
                    long times = new Date().getTime();
                    unit.setTimestamp(times);
                    //类型 1-阀门(% 间连) 2-变频器(Hz 分布式变频泵) 3-供温(℃ 混水)
                    //unit.type = item.ControlType;
                    unit.setType(systemInfo.getControlType());
                    //机组采暖类型(1-挂暖 2-地暖)
                    unit.setRadiatemethod(systemInfo.getHeatingType());
                    //有效(参与全网平衡计算)
                    unit.setEnable(systemInfo.getStatus());
                    //获取阀门给定开度数据值
                    PointCache pointCacheSetValue = pointCacheList.stream().filter(s -> s.getPointName().equals("CV1_SV")).findFirst().orElse(null);
                    if (pointCacheSetValue == null) {
                        unit.setSetting(-1);
                        log.info("无法获取系统名称为:" + systemInfo.getSystemName() + ",系统号为:" + systemInfo.getRelevanceId() + "的采集量阀门给定开度标识点数据");
                    } else {
                        unit.setSetting(Float.valueOf(pointCacheSetValue.getValue()));
                    }
                    units.add(unit);
                }
                computDo.units = units;
                //目标均温(targetlast)计算模式 0-自动计算 1-手动给定
                computDo.setMode(balanceNet.getComputeType());
                if (balanceNet.getComputeType() == 0) {
                    computDo.setTarget(Float.valueOf(balanceNet.getComputeTargetAvg().toString()));
                } else {
                    computDo.setTarget(Float.valueOf(balanceNet.getComputeTargetTemp().toString()));
                }
                //调用计算服务
                callCompute(computDo, balanceId, controlType, realValueList, balanceNet);
            } else {
                //调用计算服务
                callCompute(balanceId, controlType, balanceNet);
            }
            return Response.success();
        } catch (Exception e) {
            log.error("-----全网平衡内部算法错误 {}",e.getMessage());
            return Response.fail();
        }
    }

    /**
     * 第二步 调用计算服务(平台自用)
     *
     * @param dos
     * @return
     */
    @Override
    public void callCompute(ComputDo dos, int balanceId, int controlType, List<PointCache> pointCacheList, BalanceNet balanceNet) {
        //调用全网平衡算法并获取返回全网信息
        ResultBalanceInfo resultBalanceInfo = template.postBalance(arithmeticUrl, "/api/netbalance/target?control=true", dos, ResultBalanceInfo.class);
        //获取全网平衡历史信息
        BalanceTargetHistory balanceTargetHistory = new BalanceTargetHistory();
        balanceTargetHistory.setBalanceNetId(balanceId);
        balanceTargetHistory.setCreateTime(LocalDateTime.now());
        balanceTargetHistory.setImbalance(new BigDecimal(resultBalanceInfo.getImbalance()));
        balanceTargetHistory.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()));
        balanceTargetHistory.setTargetTemp(new BigDecimal(resultBalanceInfo.getTarget()));
        balanceTargetHistory.setRealtargetTemp(new BigDecimal(resultBalanceInfo.getTargetnet()));
        //调取并传入全网平衡历史信息数据
        recordBalanceTargetHistory(balanceTargetHistory);
        //更改全网平衡计算统计信息
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
            log.info("全网平衡配置信息更新成功:" + resultBalanceInfo + new Date());
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
            //推送全网平衡配置数据信息
            signalRTemplate.send2AllServerSpecial("PushBalanceHeader", args);
        } else {
            log.info("全网平衡配置信息更新失败:" + resultBalanceInfo + new Date());
        }
        //转换为BalanceRejectHistory准备传入批量修改全网平衡日志
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
        //调取并传入剔除信息数据
        recordRejectHistory(balanceRejectHistoryList);
        //判断是否为控制下发周期
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
     * 第二步 调用计算服务(PVSS数据同步)
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
        //调用PVSS全网平衡算法并获取返回全网信息
        ResultBalanceInfo resultBalanceInfo = template.postBalance(arithmeticUrl, "/QueryNetBalanceData", obj, ResultBalanceInfo.class);
        //获取全网平衡历史信息
        BalanceTargetHistory balanceTargetHistory = new BalanceTargetHistory();
        balanceTargetHistory.setBalanceNetId(balanceId);
        balanceTargetHistory.setCreateTime(LocalDateTime.now());
        balanceTargetHistory.setImbalance(new BigDecimal(resultBalanceInfo.getImbalance()));
        balanceTargetHistory.setRealImbalance(new BigDecimal(resultBalanceInfo.getImbalancenet()));
        balanceTargetHistory.setTargetTemp(new BigDecimal(resultBalanceInfo.getTarget()));
        balanceTargetHistory.setRealtargetTemp(new BigDecimal(resultBalanceInfo.getTargetnet()));
        //调取并传入全网平衡历史信息数据
        recordBalanceTargetHistory(balanceTargetHistory);
        //更改全网平衡计算统计信息
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
            log.info("全网平衡配置信息更新成功:" + resultBalanceInfo + new Date());
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
            //推送全网平衡配置数据信息
            signalRTemplate.send2AllServerSpecial("PushBalanceHeader", args);
        } else {
            log.info("全网平衡配置信息更新失败:" + resultBalanceInfo + new Date());
        }
        QueryWrapper<StationFirstNetBaseView> stationFirstNetBaseViewQueryWrapper = new QueryWrapper<>();
        stationFirstNetBaseViewQueryWrapper.in("systemSyncNum", resultBalanceInfo.getUnits().stream().map(s -> s.getId()).collect(Collectors.toList()));
        stationFirstNetBaseViewQueryWrapper.select("heatSystemId", "systemSyncNum");
        List<StationFirstNetBaseView> viewList = stationFirstNetBaseViewService.list(stationFirstNetBaseViewQueryWrapper);
        //转换为BalanceRejectHistory准备传入批量修改全网平衡日志
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
        //调取并传入剔除信息数据
        recordRejectHistory(balanceRejectHistoryList);
    }

    /**
     * 记录剔除日志
     */
    @Override
    public void recordRejectHistory(List<BalanceRejectHistory> balanceRejectHistoriesList) {
        try {
            if (!CollectionUtils.isEmpty(balanceRejectHistoriesList)) {
                //获取剔除信息数据，并进行批量数据存储
                Boolean result = balanceRejectHistoryService.saveBatch(balanceRejectHistoriesList);
                if (result) {
                    log.info("批量插入剔除全网平衡日志成功！" + LocalDateTime.now());
                } else {
                    log.error("批量插入剔除全网平衡日志失败！" + LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            log.error("批量插入剔除全网平衡日志失败！" + LocalDateTime.now());
        }
    }

    @Override
    public void recordBalanceTargetHistory(BalanceTargetHistory balanceTargetHistoriesList) {
        try {
            //获取剔除信息数据，并进行批量数据存储
            Boolean result = balanceTargetHistoryService.save(balanceTargetHistoriesList);
            if (result) {
                log.info("插入全网平衡历史信息成功！" + LocalDateTime.now());
            } else {
                log.error("插入全网平衡历史信息失败！" + LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("插入全网平衡历史信息失败！" + LocalDateTime.now());
        }
    }

    /**
     * 第三步 调用grpc 服务下发
     *
     * @param pointLList
     * @return
     */
    @Override
    public void control(List<PointL> pointLList, int balanceId) {
        //调用GRPC服务
        Response response = grpcIssueService.setGrpc(pointLList);
        List<BalanceNetControlLog> balanceNetControlLogs = new ArrayList<>();
        //下发成功后
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            balanceNetControlLogs = pointLList.stream().map(s -> {
                BalanceNetControlLog balanceNetControlLog = new BalanceNetControlLog();
                balanceNetControlLog.setRelevanceId(s.getRelevanceId());
                balanceNetControlLog.setLevel(s.getLevel());
                balanceNetControlLog.setPointName(s.getPointStandardName());
                balanceNetControlLog.setPointAddress(s.getParentSyncNum().toString() + ".ValueParaDesc." + s.getSystemNum().toString() + "." + s.getPointName());
                balanceNetControlLog.setStatus(true);
                balanceNetControlLog.setMsg("暂无");
                balanceNetControlLog.setBalanceNetId(balanceId);
                balanceNetControlLog.setControlValue(new BigDecimal(s.getValue()));
                balanceNetControlLog.setCreateTime(LocalDateTime.now());
                return balanceNetControlLog;
            }).collect(Collectors.toList());
        }
        //部分数据下发失败
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
                    balanceNetControlLog.setMsg("下发失败");
                    balanceNetControlLog.setBalanceNetId(balanceId);
                    balanceNetControlLog.setControlValue(new BigDecimal(p.getValue()));
                    balanceNetControlLog.setCreateTime(LocalDateTime.now());
                } else {
                    balanceNetControlLog.setRelevanceId(ponit.getRelevanceId());
                    balanceNetControlLog.setLevel(ponit.getLevel());
                    balanceNetControlLog.setPointName(ponit.getPointStandardName());
                    balanceNetControlLog.setPointAddress(p.getParentSyncNum().toString() + ".ValueParaDesc." + p.getSystemNum().toString() + "." + p.getPointName());
                    balanceNetControlLog.setStatus(true);
                    balanceNetControlLog.setMsg("暂无");
                    balanceNetControlLog.setBalanceNetId(balanceId);
                    balanceNetControlLog.setControlValue(new BigDecimal(ponit.getValue()));
                    balanceNetControlLog.setCreateTime(LocalDateTime.now());
                }
                balanceNetControlLogs.add(balanceNetControlLog);
            }
        }
        //下发失败
        if (response.getCode() == ResponseCode.FAIL.getCode()) {
            balanceNetControlLogs = pointLList.stream().map(s -> {
                BalanceNetControlLog balanceNetControlLog = new BalanceNetControlLog();
                balanceNetControlLog.setRelevanceId(s.getRelevanceId());
                balanceNetControlLog.setLevel(s.getLevel());
                balanceNetControlLog.setPointName(s.getPointStandardName());
                String address = s.getParentSyncNum().toString() + ".ValueParaDesc." + s.getSystemNum().toString() + "." + s.getPointName();
                balanceNetControlLog.setPointAddress(address);
                balanceNetControlLog.setStatus(false);
                balanceNetControlLog.setMsg("下发失败");
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
            //获取剔除信息数据，并进行批量数据存储
            Boolean result = balanceNetControlLogService.saveBatch(logsList);
            if (result) {
                log.info("插入全网平衡控制记录信息成功！" + LocalDateTime.now());
            } else {
                log.error("插入全网平衡控制记录信息失败！" + LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("插入全网平衡控制记录信息失败！" + LocalDateTime.now());
        }
    }


}
