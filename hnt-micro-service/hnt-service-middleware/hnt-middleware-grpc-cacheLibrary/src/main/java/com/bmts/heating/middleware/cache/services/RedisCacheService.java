package com.bmts.heating.middleware.cache.services;

import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.cache.PointRank;
import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
//import com.bmts.heating.commons.container.performance.config.GrpcClientPool;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.cache.CacheGrpc;
import com.bmts.heating.commons.grpc.lib.services.cache.CacheOuterClass;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.grpc.lib.util.ProtoCompiler;
import com.bmts.heating.commons.utils.common.Tuple;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.spring.SpringContextUtil;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@Service
@Astrict(servicename = "cache-grpc-server", servicetype = Astrict.EnumService.grpc)
public class RedisCacheService extends SavantServices {
    private final String serverName = "cache-grpc-server";
    ConnectionToken cd = null;


    /**
     * 获取热源的点数据信息
     *
     * @return
     * @throws MicroException
     */
    public List<PointL> querySourcePoints() throws MicroException, IOException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        Iterator<PointOuterClass.PointList> pointListIterator = null;
        try {
            pointListIterator = cacheBlockingStubManagedChannelTuple.first.querySourcePoints(Empty.newBuilder().build());
        } catch (Exception e) {
            log.error("call queryComputePoints by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        return mapPointStream(pointListIterator, cacheBlockingStubManagedChannelTuple);
    }

    public List<PointL> getPoints(String deviceId, int start, int end) throws IOException, MicroException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannel = buildStub();
        CacheOuterClass.PointsParam build = CacheOuterClass.PointsParam.newBuilder().setDeviceId(deviceId)
                .setStart(start).setEnd(end).build();
        PointOuterClass.PointList pointList = null;
        try {
            pointList = cacheBlockingStubManagedChannel.first.queryAllPoint(build);
        } catch (Exception e) {
            log.error("call queryAllPoint by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        cacheBlockingStubManagedChannel.second.shutdown();
        return mapPoints(pointList.getPlistList());
    }

    /**
     * 根据id 查找点
     *
     * @param deviceId
     * @return
     * @throws IOException
     * @throws MicroException
     */
    public List<PointL> getPoints(String deviceId) throws IOException, MicroException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        CacheOuterClass.queryPointsParam build = CacheOuterClass.queryPointsParam.newBuilder().setDeviceId(deviceId).build();
        Iterator<PointOuterClass.PointList> pointListIterator = null;
        try {
            //pointList = cacheBlockingStubManagedChannelTuple.first.queryPoints(build);
            pointListIterator = cacheBlockingStubManagedChannelTuple.first.queryPoints(build);

        } catch (Exception e) {
            log.error("call queryPoints by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        return mapPointStream(pointListIterator, cacheBlockingStubManagedChannelTuple);
    }

    /**
     * 获取计算参数名称
     *
     * @return
     * @throws MicroException
     */
    public List<PointL> queryComputePoints() throws MicroException, IOException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        PointOuterClass.PointList pointList = null;
        try {
            pointList = cacheBlockingStubManagedChannelTuple.first.queryComputePoints(Empty.newBuilder().build());
        } catch (Exception e) {
            log.error("call queryComputePoints by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }

        cacheBlockingStubManagedChannelTuple.second.shutdown();
        return mapPoints(pointList.getPlistList());
    }

    /**
     * 根据关联id获取所有点
     *
     * @param systems
     * @param level
     * @return
     * @throws MicroException
     */
    public Map<Integer, String[]> queryPointsByReleverce(List<Integer> systems, int level) throws MicroException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        CacheOuterClass.PointsByIdsResults pointsBySystemResult = null;
        try {
            pointsBySystemResult = cacheBlockingStubManagedChannelTuple.first.queryPointsByIds(CacheOuterClass.queryPointsByIdsDto.newBuilder().addAllRelevanceId(systems).setLevel(level).build());

        } catch (Exception e) {
            log.error("call queryPointsBySystem by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        cacheBlockingStubManagedChannelTuple.second.shutdown();
        Map<Integer, String[]> maps = new HashMap<>(pointsBySystemResult.getPointsByIdItemList().size());
        pointsBySystemResult.getPointsByIdItemList().forEach(x -> {
            String[] points = new String[x.getPointNameCount()];
            for (int i = 0; i < x.getPointNameList().size(); i++) {
                points[i] = x.getPointNameList().get(i);
            }
            maps.put(x.getId(), points);
        });
        return maps;
    }

    /**
     * 获取基础数据层级关系
     *
     * @return
     * @throws MicroException
     */
//    public List<FirstNetBase> queryFirstNetBase() throws MicroException {
//
//        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
//        CacheOuterClass.FirstNetBaseList firstNetBaseList = null;
//        try {
//            firstNetBaseList = cacheBlockingStubManagedChannelTuple.first.queryFirstNetBase(Empty.newBuilder().build());
//        } catch (Exception e) {
//            log.error("call queryFirstNetBase by service {} cause execption {}", serverName, e);
//            super.clearToken(serverName, e);
//        } finally {
//            super.backToken(serverName, cd);
//        }
//        cacheBlockingStubManagedChannelTuple.second.shutdown();
//        List<FirstNetBase> results = new ArrayList<>(firstNetBaseList.getFirstNetBaseList().size());
//        firstNetBaseList.getFirstNetBaseList().forEach(x -> {
//            try {
//                FirstNetBase firstNetBase = ProtoCompiler.toPojoBean(FirstNetBase.class, x);
//                results.add(firstNetBase);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//        return results;
//    }
    /**
     * 获取热源配点基础数据层级关系
     *
     * @return
     * @throws MicroException
     */
//    public List<FirstNetBase> querySourceFirstNetBase() throws MicroException {
//
//        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
//        CacheOuterClass.FirstNetBaseList firstNetBaseList = null;
//        try {
//            firstNetBaseList = cacheBlockingStubManagedChannelTuple.first.querySourceFirstNetBase(Empty.newBuilder().build());
//        } catch (Exception e) {
//            log.error("call queryFirstNetBase by service {} cause execption {}", serverName, e);
//            super.clearToken(serverName, e);
//        } finally {
//            super.backToken(serverName, cd);
//        }
//        cacheBlockingStubManagedChannelTuple.second.shutdown();
//        List<FirstNetBase> results = new ArrayList<>(firstNetBaseList.getFirstNetBaseList().size());
//        firstNetBaseList.getFirstNetBaseList().forEach(x -> {
//            try {
//                FirstNetBase firstNetBase = ProtoCompiler.toPojoBean(FirstNetBase.class, x);
//                results.add(firstNetBase);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//        return results;
//    }

    /**
     * 查询实时数据
     *
     * @param map key 系统id value 点名称集合
     * @return
     * @throws MicroException
     * @throws IOException`
     */
    public List<PointCache> queryRealDataBySystems(Map<Integer, String[]> map, int level) throws MicroException, IOException {

        // log.warn("查询缓存参数{},{}", JSONObject.toJSONString(map), level);
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStub = buildStub();
        CacheOuterClass.PointParam.Builder builder = CacheOuterClass.PointParam.newBuilder();
        builder.setMapPointConfig(true);
        map.forEach((k, v) -> {
            CacheOuterClass.PointParamItem.Builder builderItem = CacheOuterClass.PointParamItem.newBuilder();
            builderItem.addAllPointNames(Arrays.asList(v));
            builderItem.setId(k);
            builderItem.setLevel(level);
            builder.addPointParamItem(builderItem);
        });
        PointOuterClass.PointCacheList pointList = null;
        try {
            pointList = cacheBlockingStub.first.queryRealDataBySystem(builder.build());
        } catch (Exception e) {
            log.error("call queryRealDataBySystem by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        cacheBlockingStub.second.shutdown();
        return mapPointCaches(pointList);
    }

    public List<PointCache> queryRealOnlyValue(Map<Integer, String[]> map, int level) throws MicroException, IOException {

        // log.warn("查询缓存参数{},{}", JSONObject.toJSONString(map), level);
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStub = buildStub();
        CacheOuterClass.PointParam.Builder builder = CacheOuterClass.PointParam.newBuilder();
        builder.setMapPointConfig(false);
        map.forEach((k, v) -> {
            CacheOuterClass.PointParamItem.Builder builderItem = CacheOuterClass.PointParamItem.newBuilder();
            builderItem.addAllPointNames(Arrays.asList(v));
            builderItem.setId(k);
            builderItem.setLevel(level);
            builder.addPointParamItem(builderItem);
        });
        PointOuterClass.PointCacheList pointList = null;
        try {
            pointList = cacheBlockingStub.first.queryRealDataBySystem(builder.build());
        } catch (Exception e) {
            log.error("call queryRealDataBySystem by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        cacheBlockingStub.second.shutdown();
        return mapPointCaches(pointList);
    }

    /**
     * 获取缓存数据排行
     *
     * @param pointName
     * @param startValue
     * @param endValue
     * @param length
     * @param isAsc
     * @param level
     * @return
     * @throws MicroException
     */
    public PointRank queryRank(String pointName, double startValue, double endValue, int length, boolean isAsc, int level) throws MicroException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        CacheOuterClass.RankIntervalParam.Builder builder = CacheOuterClass.RankIntervalParam.newBuilder();
        builder.setStartValue(startValue).setEndValue(endValue).setLength(length).setIsAsc(isAsc).setPointName(pointName).setLevel(level);
        CacheOuterClass.RankList rankList = null;
        try {
            rankList = cacheBlockingStubManagedChannelTuple.first.queryRankInterval(builder.build());
        } catch (Exception e) {
            log.error("call queryRankInterval by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }
        cacheBlockingStubManagedChannelTuple.second.shutdown();
        return buildRank(rankList);
    }

    public PointRank queryRank(String pointName, int length, boolean isAsc, int level) throws MicroException {
        Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> cacheBlockingStubManagedChannelTuple = buildStub();
        CacheOuterClass.RankIntervalParam.Builder builder = CacheOuterClass.RankIntervalParam.newBuilder();
        builder.setLength(length).setIsAsc(isAsc).setPointName(pointName).setLevel(level);
        CacheOuterClass.RankList rankList = null;
        try {
            rankList = cacheBlockingStubManagedChannelTuple.first.queryRank(builder.build());
        } catch (Exception e) {
            log.error("call queryRank by service {} cause execption {}", serverName, e);
            super.clearToken(serverName, e);
        } finally {
            super.backToken(serverName, cd);
        }

        cacheBlockingStubManagedChannelTuple.second.shutdown();
        return buildRank(rankList);
    }

    private String getHost(ConnectionToken cd) throws UnknownHostException {
        return SpringContextUtil.getActiveProfile().equals("debug") ? InetAddress.getLocalHost().getHostAddress() : cd.getHost();
    }

//    @Autowired
//    private GrpcClientPool grpcClientPool;

    private Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> buildStub() throws MicroException {

        String serverName = "cache-grpc-server";
        ManagedChannel serverChannel = null;
        CacheGrpc.CacheBlockingStub stub = null;
        try {
            //获取令牌
            cd = super.getToken(serverName);
            serverChannel = ManagedChannelBuilder.forAddress(getHost(cd), new Integer(cd.getPort())).usePlaintext().build();
//            serverChannel = grpcClientPool.getManagedChannel(cd);
            stub = CacheGrpc.newBlockingStub(serverChannel).withMaxInboundMessageSize(Integer.MAX_VALUE).withMaxOutboundMessageSize(Integer.MAX_VALUE);
        } catch (Exception e) {
//            super.clearToken(serverName, e);
            log.error("get service {} cause execption {}", serverName, e);
        } finally {
//            super.backToken(serverName, cd);
        }

        return new Tuple<>(stub, serverChannel);
    }

    private PointRank buildRank(CacheOuterClass.RankList rankList) {
        PointRank pointRank = new PointRank();
        pointRank.setTotal(rankList.getTotal());
        Map<Integer, Double> map = new LinkedHashMap<>(rankList.getRankItemCount());
        for (CacheOuterClass.RankItem rankItem : rankList.getRankItemList()) {
            map.put(rankItem.getId(), rankItem.getValue());
        }
        pointRank.setMap(map);
        return pointRank;
    }

    private List<PointCache> mapPointCaches(PointOuterClass.PointCacheList plistList) throws IOException {
        List<PointCache> list = new ArrayList<>(plistList.getPlistCount());
        for (PointOuterClass.PointCache pointCache : plistList.getPlistList()) {
//            PointCache pointCacheItem = new PointCache();
//            pointCacheItem.setAccidentLower(new BigDecimal("0"));
//            pointCacheItem.setAccidentHigh(new BigDecimal("0"));
//            pointCacheItem.setRunningLower(new BigDecimal("0"));
//            pointCacheItem.setRunningHigh(new BigDecimal("0"));
//            pointCacheItem.setRangeLower(new BigDecimal("0"));
//            pointCacheItem.setIsAlarm(false);
//            pointCacheItem.setPointParameterTypeName("");
//            pointCacheItem.setUnitValue("");
//            pointCacheItem.setUnitName("");
//            pointCacheItem.setPointId(0);
//            pointCacheItem.setPointName("");
//            pointCacheItem.setPointAddress("");
//            pointCacheItem.setValue("");
//            pointCacheItem.setTimeStrap(0L);
//            pointCacheItem.setQualityStrap(0);
//            pointCacheItem.setOldValue("");
//            pointCacheItem.setHeatingSystemId(0);

            list.add(ProtoCompiler.toPojoBean(PointCache.class, pointCache));
        }
        return list;
    }

    private List<PointL> mapPoints(List<PointOuterClass.PointL> plistList) throws IOException {
        int size = plistList.size();
        List<PointL> result = new ArrayList<>(size);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            PointOuterClass.PointL pointL = plistList.get(i);
            PointL p = ProtoCompiler.toPojoBean(PointL.class, pointL);
            List<Integer> washArrayList = pointL.getWashArrayList();
            int washSize = washArrayList.size();
            if (washArrayList != null && washSize > 0) {
                int[] washArray = new int[washSize];
                for (int j = 0; j < washSize; j++) {
                    washArray[j] = washArrayList.get(j).intValue();
                }
                p.setWashArray(washArray);
            }

            result.add(p);
        }
        log.info("读取点表耗时 {} ms", System.currentTimeMillis() - startTime);
        return result;
    }


    private List<PointL> mapPointStream(Iterator<PointOuterClass.PointList> pointListIterator, Tuple<CacheGrpc.CacheBlockingStub, ManagedChannel> channelTuple) throws IOException {
        long startTime = System.currentTimeMillis();
        PointOuterClass.PointList next = pointListIterator.next();
        List<PointOuterClass.PointL> plistList = next.getPlistList();

        int size = plistList.size();
        List<PointL> result = new ArrayList<>(size);

        plistList.stream().forEach(e -> {
            setPointL(result, e);
        });
        log.info("读取点表耗时 {} ms", System.currentTimeMillis() - startTime);
        channelTuple.second.shutdown();
        return result;
    }

    private void setPointL(List<PointL> result, PointOuterClass.PointL grpcPl) {
        PointL pl = new PointL();
        if (grpcPl.getPointId() != 0) {
            pl.setPointId(grpcPl.getPointId());
        }
        if (StringUtils.isNotBlank(grpcPl.getPointName())) {
            pl.setPointName(grpcPl.getPointName());
        }
        if (grpcPl.getType() != 0) {
            pl.setType(grpcPl.getType());
        }
        if (StringUtils.isNotBlank(grpcPl.getDeviceId())) {
            pl.setDeviceId(grpcPl.getDeviceId());
        }
        if (StringUtils.isNotBlank(grpcPl.getPointlsSign())) {
            pl.setPointlsSign(grpcPl.getPointlsSign());
        }
        if (StringUtils.isNotBlank(grpcPl.getOldValue())) {
            pl.setOldValue(grpcPl.getOldValue());
        }
        if (StringUtils.isNotBlank(grpcPl.getValue())) {
            pl.setValue(grpcPl.getValue());
        }
        pl.setTimeStrap(grpcPl.getTimeStrap());
        if (grpcPl.getQualityStrap() != 0) {
            pl.setQualityStrap(grpcPl.getQualityStrap());
        }
        if (grpcPl.getRelevanceId() != 0) {
            pl.setRelevanceId(grpcPl.getRelevanceId());
        }
        List<Integer> washArrayList = grpcPl.getWashArrayList();
        int washSize = washArrayList.size();
        if (washArrayList != null && washSize > 0) {
            int[] washArray = new int[washSize];
            for (int j = 0; j < washSize; j++) {
                washArray[j] = washArrayList.get(j).intValue();
            }
            pl.setWashArray(washArray);
        }
        if (StringUtils.isNotBlank(grpcPl.getOrderValue())) {
            pl.setOrderValue(grpcPl.getOrderValue());
        }
        if (grpcPl.getDataType() != 0) {
            pl.setDataType(grpcPl.getDataType());
        }
        if (StringUtils.isNotBlank(grpcPl.getExpandDesc())) {
            pl.setExpandDesc(grpcPl.getExpandDesc());
        }
        if (StringUtils.isNotBlank(grpcPl.getApplicationName())) {
            pl.setApplicationName(grpcPl.getApplicationName());
        }
        if (grpcPl.getLevel() != 0) {
            pl.setLevel(grpcPl.getLevel());
        }
        if (StringUtils.isNotBlank(grpcPl.getExpression())) {
            pl.setExpression(grpcPl.getExpression());
        }
        if (grpcPl.getNumber() != 0) {
            pl.setNumber(grpcPl.getNumber());
        }
        if (StringUtils.isNotBlank(grpcPl.getHightLower())) {
            pl.setHightLower(grpcPl.getHightLower());
        }
        if (grpcPl.getPointConfig() != 0) {
            pl.setPointConfig(grpcPl.getPointConfig());
        }
        if (StringUtils.isNotBlank(grpcPl.getSystemNum())) {
            pl.setSystemNum(Integer.parseInt(grpcPl.getSystemNum()));
        }
        if (grpcPl.getParentSyncNum() != 0) {
            pl.setParentSyncNum(grpcPl.getParentSyncNum());
        }
        if (StringUtils.isNotBlank(grpcPl.getPointStandardName())) {
            pl.setPointStandardName(grpcPl.getPointStandardName());
        }
        if (StringUtils.isNotBlank(grpcPl.getEquipmentCode())) {
            pl.setEquipmentCode(grpcPl.getEquipmentCode());
        }
        if (grpcPl.getHeatType() != 0) {
            pl.setHeatType(grpcPl.getHeatType());
        }
        if (StringUtils.isNotBlank(grpcPl.getSyncNumber())) {
            pl.setSyncNumber(grpcPl.getSyncNumber());
        }

        result.add(pl);
    }

}
