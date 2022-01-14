package com.bmts.heating.cache.library.rpc;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.cache.library.pojo.RealPointsPojo;
import com.bmts.heating.cache.library.service.RedisDataOptService;
import com.bmts.heating.cache.library.service.RedisRealValueService;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.cache.PointUnitAndParamTypeResponse;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.basement.model.db.response.PointCollectConfigResponse;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.grpc.lib.services.cache.CacheGrpc;
import com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.common.Tuple;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@GrpcService(CacheOuterClass.class)
public class CacheService extends CacheGrpc.CacheImplBase {
    @Autowired
    private PointConfigMapper pointConfigMapper;
    @Autowired
    private RedisDataOptService redisDataOptService;

    @Autowired
    RedisRealValueService redisRealValueService;

    @Autowired
    private PointConfigService pointConfigService;

//    /**
//     * 获取采集量点 过滤条件为非计算量
//     *
//     * @param requestqueryPoints
//     * @param responseObserver
//     */
//    @Override
//    public void queryAllPoint(CacheOuterClass.PointsParam request, StreamObserver<PointOuterClass.PointList> responseObserver) {
//        QueryWrapper<HeatSourceResponse> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByAsc("pc.id");
//        queryWrapper.eq("pvss.nodeCode", request.getDeviceId());
//        queryWrapper.eq("pc.deleteFlag", false);
////        queryWrapper.ne("ppt.type", "1");
//        queryWrapper.in("ps.pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
//        queryWrapper.eq("hts.status", true);
//        List<PointUnitAndParamTypeResponse> records = pointConfigMapper.queryPointsBasic(queryWrapper);
//        mapPointL(records, responseObserver);
//    }

    /**
     * 获取 所有热源的点数据信息
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void querySourcePoints(Empty request, StreamObserver<PointOuterClass.PointList> responseObserver) {
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.deleteFlag", false);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
        List<PointUnitAndParamTypeResponse> collect = pointConfigMapper.querySourcePointsBasic(queryWrapper);

        // 查询报警值
        QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
        queryView.in("pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
        queryView.eq("isAlarm", true);
        List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);

        // 设置报警值
        setAlarm(collect, alarmList);
        mapSourcePointL(collect, responseObserver);
    }

    /**
     * 获取计算量的 参量名
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void queryComputePoints(Empty request, StreamObserver<PointOuterClass.PointList> responseObserver) {
        // 查询热力站的计算量
        QueryWrapper<HeatSourceResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.deleteFlag", false);
//        queryWrapper.ne("ppt.type", "1");
        queryWrapper.eq("ps.pointConfig", PointProperties.Compute.type());
        queryWrapper.eq("hts.status", true);
        List<PointUnitAndParamTypeResponse> records = pointConfigMapper.queryPointsBasic(queryWrapper);
        // 查询热源的计算量
        QueryWrapper<PointCollectConfigResponse> sourceWrapper = new QueryWrapper<>();
        sourceWrapper.eq("pc.deleteFlag", false);
        sourceWrapper.in("ps.pointConfig", PointProperties.Compute.type());
        List<PointUnitAndParamTypeResponse> sourceList = pointConfigMapper.querySourcePointsBasic(sourceWrapper);
        if (!CollectionUtils.isEmpty(sourceList)) {
            records.addAll(sourceList);
        }

        // 查询报警值
        QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
        queryView.eq("pointConfig", PointProperties.Compute.type());
        queryView.eq("isAlarm", true);
        List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
        // 设置报警值
        setAlarm(records, alarmList);
        mapPointL(records, responseObserver);
    }

    /**
     * 查询点配置
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void queryPoints(CacheOuterClass.queryPointsParam request, StreamObserver<PointOuterClass.PointList> responseObserver) {
        //long startTime = System.currentTimeMillis();
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hts.status", true);
        queryWrapper.eq("dc.nodeCode", request.getDeviceId().trim());
        queryWrapper.eq("pc.deleteFlag", false);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
        List<PointUnitAndParamTypeResponse> collect = pointConfigMapper.queryPointsBasic(queryWrapper);
        //System.out.println("---sql 语句---queryPointsBasic---查询耗时------" + (System.currentTimeMillis() - startTime));
        //long twoTime = System.currentTimeMillis();
        List<PointUnitAndParamTypeResponse> sourceBasic = querySourceBasic(request);
        if (!CollectionUtils.isEmpty(sourceBasic)) {
            collect.addAll(sourceBasic);
        }
        //System.out.println("---sql 语句---querySourceBasic---查询耗时------" + (System.currentTimeMillis() - twoTime));

        //long threeTime = System.currentTimeMillis();
        // 查询报警值
        QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
        queryView.in("pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
        queryView.eq("isAlarm", true);
        List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
        // System.out.println("---sql 语句---queryAlarm---查询耗时------" + (System.currentTimeMillis() - threeTime));
        //long fourTime = System.currentTimeMillis();

        // 设置报警值
        setAlarm(collect, alarmList);
        //System.out.println("---设置报警值耗时------" + (System.currentTimeMillis() - fourTime));
        mapPointL(collect, responseObserver);
        //System.out.println("queryPoints---方法耗时：" + (System.currentTimeMillis() - startTime));
    }


    // 设置报警值
    private void setAlarm(List<PointUnitAndParamTypeResponse> list, List<PointAlarmView> alarmList) {
        list.parallelStream().forEach(e -> {
            PointAlarmView pointAlarmView = alarmList.stream().filter(j -> Objects.equals(j.getId(), e.getId())).findFirst().orElse(null);
            if (pointAlarmView != null) {
                if (pointAlarmView.getIsAlarm() != null) {
                    e.setIsAlarm(pointAlarmView.getIsAlarm());
                }
                if (pointAlarmView.getAccidentHigh() != null) {
                    e.setAccidentHigh(pointAlarmView.getAccidentHigh());
                }
                if (pointAlarmView.getAccidentLower() != null) {
                    e.setAccidentLower(pointAlarmView.getAccidentLower());
                }
                if (pointAlarmView.getAlarmConfigId() != null) {
                    e.setAlarmConfigId(pointAlarmView.getAlarmConfigId());
                }
                if (pointAlarmView.getAlarmValue() != null) {
                    e.setAlarmValue(pointAlarmView.getAlarmValue());
                }
                if (pointAlarmView.getRunningHigh() != null) {
                    e.setRunningHigh(pointAlarmView.getRunningHigh());
                }
                if (pointAlarmView.getRunningLower() != null) {
                    e.setRunningLower(pointAlarmView.getRunningLower());
                }
                if (pointAlarmView.getGrade() != null) {
                    e.setGrade(pointAlarmView.getGrade());
                }
                if (pointAlarmView.getAlarmDesc() != null) {
                    e.setAlarmDesc(pointAlarmView.getAlarmDesc());
                }
            }
        });
    }

    /**
     * 查询热源点基础数据
     *
     * @param request
     * @return
     */
    private List<PointUnitAndParamTypeResponse> querySourceBasic(CacheOuterClass.queryPointsParam request) {
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dc.nodeCode", request.getDeviceId().trim());
        queryWrapper.eq("pc.deleteFlag", false);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadAndControl.type(), PointProperties.ReadOnly.type());
        List<PointUnitAndParamTypeResponse> collect = pointConfigMapper.querySourcePointsBasic(queryWrapper);
        return collect;
    }

    @Override
    public void querySourceFirstNetBase(Empty request, StreamObserver<CacheOuterClass.FirstNetBaseList> responseObserver) {
        CacheOuterClass.FirstNetBaseList.Builder builder = CacheOuterClass.FirstNetBaseList.newBuilder();
        List<FirstNetBase> firstNetBases = redisDataOptService.querySourceFirstNetBase();
        firstNetBases.forEach(x -> {

            CacheOuterClass.FirstNetBase.Builder firstNetBase = CacheOuterClass.FirstNetBase.newBuilder();
            buildFirstNetBase(x, firstNetBase);
            builder.addFirstNetBase(firstNetBase);
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private void buildFirstNetBase(FirstNetBase x, CacheOuterClass.FirstNetBase.Builder firstNetBase) {
        firstNetBase.setHeatNetId(x.getHeatNetId());
        if (!StringUtils.isEmpty(x.getHeatNetName()))
            firstNetBase.setHeatNetName(x.getHeatNetName());
        firstNetBase.setHeatNetOrgId(x.getHeatNetOrgId());
        if (!StringUtils.isEmpty(x.getHeatNetOrgName()))
            firstNetBase.setHeatNetOrgName(x.getHeatNetOrgName());
        firstNetBase.setHeatSourceId(x.getHeatSourceId());
        if (!StringUtils.isEmpty(x.getHeatNetName()))
            firstNetBase.setHeatNetName(x.getHeatNetName());
        firstNetBase.setHeatSourceOrgId(x.getHeatSourceOrgId());
        if (!StringUtils.isEmpty(x.getHeatSourceOrgName()))
            firstNetBase.setHeatSourceOrgName(x.getHeatSourceOrgName());
        firstNetBase.setHeatTransferStationId(x.getHeatTransferStationId());
        if (!StringUtils.isEmpty(x.getHeatTransferStationName()))
            firstNetBase.setHeatTransferStationName(x.getHeatTransferStationName());
        if (!StringUtils.isEmpty(x.getHeatSourceName()))
            firstNetBase.setHeatSourceName(x.getHeatSourceName());
        firstNetBase.setHeatStationOrgId(x.getHeatStationOrgId());
        if (!StringUtils.isEmpty(x.getHeatStationOrgName()))
            firstNetBase.setHeatStationOrgName(x.getHeatStationOrgName());
        firstNetBase.setHeatCabinetId(x.getHeatCabinetId());
        if (!StringUtils.isEmpty(x.getHeatCabinetName()))
            firstNetBase.setHeatCabinetName(x.getHeatCabinetName());
        firstNetBase.setHeatSystemId(x.getHeatSystemId());
        if (!StringUtils.isEmpty(x.getHeatSystemName()))
            firstNetBase.setHeatSystemName(x.getHeatSystemName());
        if (x.getHeatNetArea() != null)
            firstNetBase.setHeatNetArea(x.getHeatNetArea().doubleValue());
        if (x.getHeatSourceArea() != null)
            firstNetBase.setHeatSourceArea(x.getHeatSourceArea().doubleValue());
        if (x.getHeatStationArea() != null)
            firstNetBase.setHeatStationArea(x.getHeatStationArea().doubleValue());
        if (x.getHeatSystemArea() != null)
            firstNetBase.setHeatSystemArea(x.getHeatSystemArea().doubleValue());
        if (x.getHeatStationNetArea() != null)
            firstNetBase.setHeatStationNetArea(x.getHeatStationNetArea().doubleValue());
        firstNetBase.setStatus(x.isStatus());
    }

    @Override
    public void queryFirstNetBase(Empty request, StreamObserver<CacheOuterClass.FirstNetBaseList> responseObserver) {
        CacheOuterClass.FirstNetBaseList.Builder builder = CacheOuterClass.FirstNetBaseList.newBuilder();
        List<FirstNetBase> firstNetBases = redisDataOptService.queryFirstNetBase();
        firstNetBases.forEach(x -> {

            CacheOuterClass.FirstNetBase.Builder firstNetBase = CacheOuterClass.FirstNetBase.newBuilder();
//            try {
//                ProtoCompiler.toProtoBean(firstNetBase,x);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            MapperUtils.copyProperties(x, firstNetBase);
            buildFirstNetBase(x, firstNetBase);
            builder.addFirstNetBase(firstNetBase);
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     * 新版完成
     * queryPoints
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void queryRealDataBySystem(CacheOuterClass.PointParam request, StreamObserver<PointOuterClass.PointCacheList> responseObserver) {
        try {
            long start = System.currentTimeMillis();
            List<RealPointsPojo> pojos = new ArrayList<>(request.getPointParamItemList().size());
            request.getPointParamItemList().forEach(x -> {
                pojos.add(
                        RealPointsPojo.builder()
                                .level(x.getLevel())
                                .pointNames(x.getPointNamesList().toArray(new String[0]))
                                .relevanceId(x.getId())
                                .build()
                );
            });
            if (request.getMapPointConfig()) {
                Set<PointCache> list = redisRealValueService.queryCachePoint(pojos);
                log.info("查询redis耗时{} ms", System.currentTimeMillis() - start);
                mapPointCache(list, responseObserver);

            } else {
                Set<PointCache> list = redisRealValueService.queryCachePointUnMapPointConfig(pojos);
                log.info("查询redis不拼接点基础数据耗时{} ms", System.currentTimeMillis() - start);
                mapPointCacheWithUnPoint(list, responseObserver);
            }

        } catch (Exception e) {
            log.error("查询实时数据出错 {}", e);
        }
    }

    @Override
    public void queryRankInterval(CacheOuterClass.RankIntervalParam request, StreamObserver<CacheOuterClass.RankList> responseObserver) {
        Tuple<Map<Integer, Double>, Long> mapLongTuple = redisRealValueService.queryRank(
                request.getPointName(), request.getStartValue(), request.getEndValue(), request.getLength(), request.getIsAsc(), request.getLevel()
        );
        responseRank(mapLongTuple, responseObserver);
    }

    @Override
    public void queryRank(CacheOuterClass.RankIntervalParam request, StreamObserver<CacheOuterClass.RankList> responseObserver) {
        Tuple<Map<Integer, Double>, Long> mapLongTuple = redisRealValueService.queryRank(
                request.getPointName(), request.getLength(), request.getIsAsc(), request.getLevel()
        );
        responseRank(mapLongTuple, responseObserver);
    }

    @Override
    public void queryPointsByIds(CacheOuterClass.queryPointsByIdsDto request, StreamObserver<CacheOuterClass.PointsByIdsResults> responseObserver) {
        Map<Integer, Set<String>> points = redisRealValueService.queryPointsByIds(request.getRelevanceIdList(), request.getLevel());
        CacheOuterClass.PointsByIdsResults.Builder builder = CacheOuterClass.PointsByIdsResults.newBuilder();
        points.forEach((k, v) -> {
            CacheOuterClass.PointsByIdItem build = CacheOuterClass.PointsByIdItem.newBuilder().setId(k).addAllPointName(v).build();
            builder.addPointsByIdItem(build);
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private void responseRank(Tuple<Map<Integer, Double>, Long> mapLongTuple, StreamObserver<CacheOuterClass.RankList> responseObserver) {
        CacheOuterClass.RankList.Builder builder = CacheOuterClass.RankList.newBuilder();
        builder.setTotal(mapLongTuple.second);
        mapLongTuple.first.forEach((k, v) -> {
            CacheOuterClass.RankItem.Builder item = CacheOuterClass.RankItem.newBuilder();
            item.setId(k);
            item.setValue(v);
            builder.addRankItem(item);
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private void mapPointCacheWithUnPoint(Set<PointCache> list, StreamObserver<PointOuterClass.PointCacheList> responseObserver) {
        PointOuterClass.PointCacheList.Builder builder = PointOuterClass.PointCacheList.newBuilder();
        long start = System.currentTimeMillis();
        list.forEach(point -> {
            try {
                PointOuterClass.PointCache.Builder pointBuilder = PointOuterClass.PointCache.newBuilder();
                pointBuilder.setPointId(point.getPointId());
                pointBuilder.setPointName(point.getPointName());
                pointBuilder.setValue(point.getValue());
                pointBuilder.setRelevanceId(point.getRelevanceId());
                pointBuilder.setQualityStrap(point.getQualityStrap());
                pointBuilder.setTimeStrap(point.getTimeStrap());
                builder.addPlist(pointBuilder);
            } catch (Exception e) {
                log.error("映射数据出错{}", e);
            }
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
        log.info("查询缓存grpc耗时:{} ms", System.currentTimeMillis() - start);
    }

    private void mapPointCache(Set<PointCache> list, StreamObserver<PointOuterClass.PointCacheList> responseObserver) {
        PointOuterClass.PointCacheList.Builder builder = PointOuterClass.PointCacheList.newBuilder();
        long start = System.currentTimeMillis();
        list.forEach(point -> {
            try {

                PointOuterClass.PointCache.Builder pointBuilder = PointOuterClass.PointCache.newBuilder();
                pointBuilder.setPointId(point.getPointId());
                pointBuilder.setPointName(point.getPointName());
                if (point.getOldValue() != null)
                    pointBuilder.setOldValue(point.getOldValue());
                pointBuilder.setValue(point.getValue());
                pointBuilder.setTimeStrap(point.getTimeStrap());
                pointBuilder.setQualityStrap(point.getQualityStrap());
                pointBuilder.setRelevanceId(point.getRelevanceId());
                if (point.getAccidentLower() != null)
                    pointBuilder.setAccidentLower(point.getAccidentLower().doubleValue());
                if (point.getAccidentHigh() != null)
                    pointBuilder.setAccidentHigh(point.getAccidentHigh().doubleValue());
                if (point.getRunningLower() != null)
                    pointBuilder.setRunningLower(point.getRunningLower().doubleValue());
                if (point.getRunningHigh() != null)
                    pointBuilder.setRunningHigh(point.getRunningHigh().doubleValue());
                if (point.getRangeLower() != null)
                    pointBuilder.setRangeLower(point.getRangeLower().doubleValue());
                if (point.getIsAlarm() != null)
                    pointBuilder.setIsAlarm(point.getIsAlarm());
                if (point.getPointParameterTypeName() != null)
                    pointBuilder.setPointParameterTypeName(point.getPointParameterTypeName());
                if (point.getUnitValue() != null)
                    pointBuilder.setUnitValue(point.getUnitValue());
                if (point.getUnitName() != null)
                    pointBuilder.setUnitName(point.getUnitName());
                if (point.getSystemName() != null)
                    pointBuilder.setSystemName(point.getSystemName());
                pointBuilder.setNetFlag(point.getNetFlag());
                if (point.getPointStandardName() != null)
                    pointBuilder.setPointStandardName(point.getPointStandardName());
                if (point.getApplicationName() != null)
                    pointBuilder.setApplicationName(point.getApplicationName());
                if (point.getPointAddress() != null)
                    pointBuilder.setPointAddress(point.getPointAddress());
                if (point.getDeviceId() != null)
                    pointBuilder.setDeviceId(point.getDeviceId());
                if (point.getPointlsSign() != null)
                    pointBuilder.setPointlsSign(point.getPointlsSign());
                if (point.getDescriptionJson() != null)
                    pointBuilder.setDescriptionJson(point.getDescriptionJson());
                if (!StringUtils.isEmpty(point.getExpression()))
                    pointBuilder.setExpression(point.getExpression());
                if (point.getNumber() != null)
                    pointBuilder.setNumber(point.getNumber());
                pointBuilder.setPointConfig(point.getPointConfig());
                if (point.getSystemNum() != null) {
                    pointBuilder.setSystemNum(String.valueOf(point.getSystemNum()));
                }
                if (point.getParentSyncNum() != null) {
                    pointBuilder.setParentSyncNum(point.getParentSyncNum());
                }
                if (point.getEquipmentCode() != null) {
                    pointBuilder.setEquipmentCode(point.getEquipmentCode());
                }
                if (point.getAlarmValue() != null) {
                    pointBuilder.setAlarmValue(point.getAlarmValue());
                }
                if (point.getHeatType() != null) {
                    pointBuilder.setHeatType(point.getHeatType());
                }
                if (point.getSyncNumber() != null) {
                    pointBuilder.setSyncNumber(point.getSyncNumber());
                }
                if (point.getGrade() != null) {
                    pointBuilder.setGrade(point.getGrade());
                }
                if (point.getAlarmDesc() != null) {
                    pointBuilder.setAlarmDesc(point.getAlarmDesc());
                }

                builder.addPlist(pointBuilder);
            } catch (Exception e) {
                log.error("映射数据出错{}", e);
            }
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
        log.info("查询缓存grpc耗时:{} ms", System.currentTimeMillis() - start);
    }

    private void mapPointL(List<PointUnitAndParamTypeResponse> datas, StreamObserver<PointOuterClass.PointList> responseObserver) {
        PointOuterClass.PointList.Builder builder = PointOuterClass.PointList.newBuilder();
        long startTime = System.currentTimeMillis();
        for (int i = 0, size = datas.size(); i < size; i++) {
            try {

                PointUnitAndParamTypeResponse point = datas.get(i);
//                if (point.getPointAddress() == null)
//                    break;
                PointOuterClass.PointL.Builder pointBuilder = PointOuterClass.PointL.newBuilder();
                pointBuilder.setPointId(point.getId());
                pointBuilder.setPointName(point.getColumnName());
                pointBuilder.setRelevanceId(point.getRelevanceId());
                pointBuilder.setDeviceId(point.getNodeCode());
                if (point.getAccidentHigh() != null && point.getRunningHigh() != null && point.getAccidentLower() != null && point.getRunningLower() != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accidentHigh", point.getAccidentHigh());
                    jsonObject.put("runningHigh", point.getRunningHigh());
                    jsonObject.put("accidentLower", point.getAccidentLower());
                    jsonObject.put("runningLower", point.getRunningLower());
                    pointBuilder.setHightLower(jsonObject.toJSONString());
                }
                if (point.getAlarmValue() != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("alarmValue", point.getAlarmValue());
                    pointBuilder.setHightLower(jsonObject.toJSONString());
                }
                pointBuilder.setType(point.getDataLengthType());
//                pointBuilder.setPointAddress(point.getPointAddress());
                pointBuilder.setLevel(point.getLevel());
                if (!StringUtils.isEmpty(point.getExpression()))
                    pointBuilder.setExpression(point.getExpression());
                pointBuilder.setNumber(point.getNumber());
                pointBuilder.setPointConfig(point.getPointConfig());

                if (point.getSystemNum() != null) {
                    pointBuilder.setSystemNum(String.valueOf(point.getSystemNum()));
                }
                if (point.getParentSyncNum() != null) {
                    pointBuilder.setParentSyncNum(point.getParentSyncNum());
                }
                if (StringUtils.isNotBlank(point.getPointStandardName())) {
                    pointBuilder.setPointStandardName(point.getPointStandardName());
                }
                if (StringUtils.isNotBlank(point.getEquipmentCode())) {
                    pointBuilder.setEquipmentCode(point.getEquipmentCode());
                }
                if (point.getHeatType() != null) {
                    pointBuilder.setHeatType(point.getHeatType());
                }
                if (StringUtils.isNotBlank(point.getSyncNumber())) {
                    pointBuilder.setSyncNumber(point.getSyncNumber());
                }
                if (point.getGrade() != null) {
                    pointBuilder.setGrade(point.getGrade());
                }
                if (StringUtils.isNotBlank(point.getAlarmDesc())) {
                    pointBuilder.setAlarmDesc(point.getAlarmDesc());
                }

//                if (point.getExpandDesc() != null)
//                    pointBuilder.setExpandDesc(point.getExpandDesc());

//                if (point.getWashArray().contains(",")) {
//                    Object[] objects = Arrays.stream(point.getWashArray().split(",")).toArray();
//                    if (objects != null) {
//                        for (int j = 0, len = objects.length; j < len; j++) {
//                            pointBuilder.addWashArray(Integer.parseInt(objects[j].toString()));
//                        }
//                    }
//                }
                builder.addPlist(pointBuilder);
            } catch (Exception e) {
                log.error("映射数据出错{}", e);
            }

        }
        builder.setTotal(datas.size());
        log.info("---转换需要 time= {} ms", (System.currentTimeMillis() - startTime));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
        log.info("---时间 = {} ms", (System.currentTimeMillis() - startTime));
    }


    private void mapSourcePointL(List<PointUnitAndParamTypeResponse> datas, StreamObserver<PointOuterClass.PointList> responseObserver) {
        PointOuterClass.PointList.Builder builder = PointOuterClass.PointList.newBuilder();
        long startTime = System.currentTimeMillis();
        datas.stream().forEach(point -> {
            try {
                PointOuterClass.PointL.Builder pointBuilder = PointOuterClass.PointL.newBuilder();
                pointBuilder.setPointId(point.getId());
                pointBuilder.setPointName(point.getColumnName());
                pointBuilder.setRelevanceId(point.getRelevanceId());
                if (point.getAccidentHigh() != null && point.getRunningHigh() != null && point.getAccidentLower() != null && point.getRunningLower() != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("accidentHigh", point.getAccidentHigh());
                    jsonObject.put("runningHigh", point.getRunningHigh());
                    jsonObject.put("accidentLower", point.getAccidentLower());
                    jsonObject.put("runningLower", point.getRunningLower());
                    pointBuilder.setHightLower(jsonObject.toJSONString());
                }
                if (point.getAlarmValue() != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("alarmValue", point.getAlarmValue());
                    pointBuilder.setHightLower(jsonObject.toJSONString());
                }
                pointBuilder.setType(point.getDataLengthType());
                pointBuilder.setLevel(point.getLevel());
                if (!StringUtils.isEmpty(point.getExpression()))
                    pointBuilder.setExpression(point.getExpression());
                pointBuilder.setNumber(point.getNumber());
                pointBuilder.setPointConfig(point.getPointConfig());

                if (point.getSystemNum() != null) {
                    pointBuilder.setSystemNum(String.valueOf(point.getSystemNum()));
                }
                if (point.getParentSyncNum() != null) {
                    pointBuilder.setParentSyncNum(point.getParentSyncNum());
                }
                if (StringUtils.isNotBlank(point.getPointStandardName())) {
                    pointBuilder.setPointStandardName(point.getPointStandardName());
                }
                if (StringUtils.isNotBlank(point.getEquipmentCode())) {
                    pointBuilder.setEquipmentCode(point.getEquipmentCode());
                }
                if (point.getHeatType() != null) {
                    pointBuilder.setHeatType(point.getHeatType());
                }
                if (StringUtils.isNotBlank(point.getSyncNumber())) {
                    pointBuilder.setSyncNumber(point.getSyncNumber());
                }
                if (point.getGrade() != null) {
                    pointBuilder.setGrade(point.getGrade());
                }
                if (StringUtils.isNotBlank(point.getAlarmDesc())) {
                    pointBuilder.setAlarmDesc(point.getAlarmDesc());
                }
                builder.addPlist(pointBuilder);
            } catch (Exception e) {
                log.error("映射数据出错{}", e);
            }
        });
        builder.setTotal(datas.size());
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
        log.info("grpc---获取热源点数据时间 = {} ms", (System.currentTimeMillis() - startTime));
    }

}
