package com.bmts.heating.commons.redis.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.cache.PointUnitAndParamTypeResponse;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.response.PointCollectConfigResponse;
import com.bmts.heating.commons.basement.utils.MapperUtils;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class RedisPointService {

    @Autowired
    PointConfigMapper pointConfigMapper;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    ValueOperations<String, Object> valueOperations;

    @Autowired
    ZSetOperations<String, Object> zSetOperations;

    @Autowired
    private PointConfigService pointConfigService;

    @Async
    public void loadPointCollectConfigData() {
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hts.status", true);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadOnly.type(), PointProperties.ReadAndControl.type(), PointProperties.Compute.type());
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.queryPointsBasic(queryWrapper);

        // 查询报警值
        QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
        queryView.in("pointConfig", PointProperties.ReadOnly.type(), PointProperties.ReadAndControl.type(), PointProperties.Compute.type());
        queryView.eq("isAlarm", true);
        List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
        // 设置报警值
        setAlarm(list, alarmList);
        loadPointCollect(list);
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
                if (StringUtils.isNotBlank(pointAlarmView.getAlarmDesc())) {
                    e.setAlarmDesc(pointAlarmView.getAlarmDesc());
                }

            }
        });
    }

    @Async
    public void loadSourcePoints() {
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("hts.status", true);
        queryWrapper.in("ps.pointConfig", PointProperties.ReadOnly.type(), PointProperties.ReadAndControl.type(), PointProperties.Compute.type());
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.querySourcePointsBasic(queryWrapper);

        // 查询报警值
        QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
        queryView.in("pointConfig", PointProperties.ReadOnly.type(), PointProperties.ReadAndControl.type(), PointProperties.Compute.type());
        queryView.eq("isAlarm", true);
        List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
        // 设置报警值
        setAlarm(list, alarmList);
        loadPointCollect(list);
    }

    @Async
    public void saveOrUpdateByrelevanceId(int id, int level) {
        // 查询热力站的点
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.relevanceId", id);
        queryWrapper.eq("pc.level", level);
        queryWrapper.eq("hts.status", true);
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.queryPointsBasic(queryWrapper);

        // 查询热源点
        QueryWrapper<PointCollectConfigResponse> sourceQuery = new QueryWrapper<>();
        sourceQuery.eq("pc.level", level);
        sourceQuery.eq("pc.relevanceId", id);
        List<PointUnitAndParamTypeResponse> listSource = pointConfigMapper.querySourcePointsBasic(sourceQuery);
        if (!CollectionUtils.isEmpty(listSource)) {
            list.addAll(listSource);
        }
        if (!CollectionUtils.isEmpty(list)) {
            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.eq("relevanceId", id);
            queryView.eq("level", level);
            queryView.eq("isAlarm", true);
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(list, alarmList);

            loadPointCollect(list);

        }

    }

    @Async
    public void saveOrUpdateByrelevanceId(List<Integer> ids, int level) {
        // 先清空缓存
        delete(ids, level);
        // 查询热力站的点
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.level", level);
        queryWrapper.in("pc.relevanceId", ids);
        queryWrapper.eq("hts.status", true);
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.queryPointsBasic(queryWrapper);

        //// 取出系统id
        //Set<Integer> systemIdList = list.parallelStream().map(e -> e.getRelevanceId()).collect(Collectors.toSet());
        //if (!CollectionUtils.isEmpty(systemIdList) && systemIdList.size() < ids.size()) {
        //    //说明有删除的系统
        //    ids.removeAll(list.stream().map(x -> x.getRelevanceId()).collect(Collectors.toList()));
        //
        //}

        // 查询热源点
        QueryWrapper<PointCollectConfigResponse> sourceQuery = new QueryWrapper<>();
        sourceQuery.eq("pc.level", level);
        sourceQuery.in("pc.relevanceId", ids);
        List<PointUnitAndParamTypeResponse> listSource = pointConfigMapper.querySourcePointsBasic(sourceQuery);
        if (!CollectionUtils.isEmpty(listSource)) {
            list.addAll(listSource);
            //if (list.size() < ids.size()) {
            //    //说明有删除的系统
            //    ids.removeAll(list.stream().map(x -> x.getRelevanceId()).collect(Collectors.toList()));
            //    delete(ids, level);
            //}
        }
        if (!CollectionUtils.isEmpty(list)) {

            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.in("relevanceId", ids);
            queryView.eq("level", level);
            queryView.eq("isAlarm", true);
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(list, alarmList);

            loadPointCollect(list);
        }

    }


    @Async
    public void syncByRelevanceIds(List<Integer> ids, int level) {
        // 先清空缓存
        delete(ids, level);
        // 查询热力站点
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.level", level);
        queryWrapper.in("pc.relevanceId", ids);
        queryWrapper.eq("hts.status", true);
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.queryPointsBasic(queryWrapper);

        // 查询热源点
        QueryWrapper<PointCollectConfigResponse> sourceQuery = new QueryWrapper<>();
        sourceQuery.eq("pc.level", level);
        sourceQuery.in("pc.relevanceId", ids);
        List<PointUnitAndParamTypeResponse> listSource = pointConfigMapper.querySourcePointsBasic(sourceQuery);
        if (!CollectionUtils.isEmpty(listSource)) {
            list.addAll(listSource);
        }
        if (!CollectionUtils.isEmpty(list)) {
            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.in("relevanceId", ids);
            queryView.eq("level", level);
            queryView.eq("isAlarm", true);
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(list, alarmList);

            loadPointCollect(list);
        }
    }

    @Async
    public void syncByRelevanceId(int id, int level) {
        QueryWrapper<PointCollectConfigResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pc.relevanceId", id);
        queryWrapper.eq("pc.level", level);
        queryWrapper.eq("hts.status", true);
        List<PointUnitAndParamTypeResponse> list = pointConfigMapper.queryPointsBasic(queryWrapper);
        // 查询热源点
        QueryWrapper<PointCollectConfigResponse> sourceQuery = new QueryWrapper<>();
        sourceQuery.eq("pc.level", level);
        sourceQuery.eq("pc.relevanceId", id);
        List<PointUnitAndParamTypeResponse> listSource = pointConfigMapper.querySourcePointsBasic(sourceQuery);
        if (!CollectionUtils.isEmpty(listSource)) {
            list.addAll(listSource);
        }
        if (!CollectionUtils.isEmpty(list)) {
            // 查询报警值
            QueryWrapper<PointAlarmView> queryView = new QueryWrapper<>();
            queryView.eq("relevanceId", id);
            queryView.eq("level", level);
            queryView.eq("isAlarm", true);
            List<PointAlarmView> alarmList = pointConfigService.queryAlarm(queryView);
            // 设置报警值
            setAlarm(list, alarmList);

            loadPointCollect(list);

        }

    }


    public void clear() {
        String key = RedisKeyConst.POINT_COLLECTCONFIG;
        Set<String> keys = redisTemplate.keys(key.concat("*"));
        redisTemplate.delete(keys);
    }

    /**
     * 删除点配置
     *
     * @param ids
     * @param level
     */
    @Async
    public void delete(List<Integer> ids, int level) {
        try {
            String key = RedisKeyConst.POINT_COLLECTCONFIG;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                //conn.openPipeline();
                for (Integer relevanceId : ids) {
                    conn.del((key + level + ":".concat(relevanceId.toString())).getBytes());
                }
                conn.closePipeline();
                return null;
            });

        } catch (Exception e) {
            log.error("delete redis by ids: {} level: cause exception {}", ids, level, e.getStackTrace());
        }
    }

    /**
     * 删除缓存数据
     *
     * @param ids
     * @param level
     */
    @Async
    public void deleteCache(List<Integer> ids, int level) {
        try {
            String key = RedisKeyConst.First_REAL_DATA;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                //conn.openPipeline();
                for (Integer relevanceId : ids) {
                    Set<byte[]> keys = conn.keys((key + level + ":".concat(relevanceId.toString()) + ":*").getBytes());
                    keys.forEach(x -> {
                        conn.del(x);
                    });
                }
                conn.closePipeline();
                return null;
            });

        } catch (Exception e) {
            log.error("delete redis by ids: {} level: cause exception {}", ids, level, e.getStackTrace());
        }
    }

    /**
     * 加载点信息到缓存
     *
     * @param list
     */
    private void loadPointCollect(List<PointUnitAndParamTypeResponse> list) {
        try {
            Map<Integer, List<PointUnitAndParamTypeResponse>> collect = list.stream().collect(Collectors.groupingBy(x -> x.getLevel()));
            String key = RedisKeyConst.POINT_COLLECTCONFIG;
            collect.forEach((level, points) -> {
                Map<Integer, List<PointUnitAndParamTypeResponse>> maps = points.stream().collect(Collectors.groupingBy(x -> x.getRelevanceId()));
                redisTemplate.execute((RedisCallback<Object>) conn -> {
                    //conn.openPipeline();
                    //设置点实体数据 key：前缀:：systemid  value ：model
                    maps.forEach((k, v) -> {
                        List<byte[]> members = v.stream().map(x -> JSONObject.toJSONBytes(x)).collect(Collectors.toList());
                        byte[] bytes = (key + level + ":".concat(k.toString())).getBytes();
                        conn.del(bytes);
                        members.forEach(x -> {
                            conn.sAdd(bytes, x);
                        });
                    });
                    conn.closePipeline();
                    return null;
                });
            });
        } catch (Exception e) {
            log.error("loadPointConfigs redis cause exception {}", list, e.getStackTrace());
        }

    }

    /**
     * 查询单个点配置
     *
     * @param relevanceId
     * @param pointName
     * @param level
     * @return
     */
    public PointUnitAndParamTypeResponse querySingle(int relevanceId, String pointName, int level) {
        try {
            String key = RedisKeyConst.POINT_COLLECTCONFIG;
            Set<String> members = redisTemplate.opsForSet().members(key + level + ":" + relevanceId);
            List<PointUnitAndParamTypeResponse> collect =
                    members.stream().map(x -> JSONObject.parseObject(x, PointUnitAndParamTypeResponse.class)).collect(Collectors.toList());
            PointUnitAndParamTypeResponse pointCollectConfigResponse =
                    collect.stream().filter(x -> x.getColumnName().equals(pointName)).
                            findFirst().orElse(null);
            return pointCollectConfigResponse;
        } catch (Exception e) {
            log.error("query single point config cause exception{}", e);
            return new PointUnitAndParamTypeResponse();
        }

    }

    /**
     * 批量更新缓存
     *
     * @param list
     */
    public void setCachePoint(List<PointL> list) {
        try {
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                list.forEach(x -> {
                    String key = (RedisKeyConst.First_REAL_DATA + x.getLevel()).concat(":") + x.getRelevanceId() + ":".concat(x.getPointName());
                    conn.set(key.getBytes(), JSONObject.toJSONBytes(x));
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新缓存数据失败 {}", e);
            throw e;
        }


        try {
            String key = RedisKeyConst.POINT_RANK;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                list.forEach(x -> {
                    String rankKey = key + x.getLevel() + ":".concat(x.getPointName());
                    if (StringUtils.isNotBlank(x.getValue())) {
                        conn.zAdd(rankKey.getBytes(), Double.valueOf(x.getValue()), (x.getRelevanceId() + "").getBytes());
                    }
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新排行数据失败 {}", e);
            throw e;
        }

    }

    /**
     * 第一个  string 缓存key;object 缓存value
     */
    public void setCacheByKey(Map<String, Object> map) {
        try {
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                map.forEach((key, value) -> {
                    if(StringUtils.isNotBlank(key)){
                      conn.set(key.getBytes(),JSONObject.toJSONBytes(value));
                    }

                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新缓存数据失败{}", e);
        }

    }


    /**
     * 批量更新缓存 时间戳和质量戳数据排行
     *
     * @param list
     */
    public void setTimeStampAndQualityStrap(List<PointL> list) {
        try {
            String key = RedisKeyConst.POINT_RANK;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                list.forEach(x -> {
                    String rankKey = key + x.getLevel() + ":".concat(x.getPointName());
                    if (StringUtils.isNotBlank(x.getValue())) {
                        conn.zAdd(rankKey.getBytes(), Double.valueOf(x.getValue()), (x.getRelevanceId() + "").getBytes());
                    }
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新时间戳和质量戳排行数据失败 {}", e);
            throw e;
        }
    }


    /**
     * set
     * 设置缓存数据
     *
     * @param pointL
     */
//    public static
    public void setCachePoint(PointL pointL) {

        //TODO 目前所有点都存入排序 后续通过添加pointL字段 是否参与排行 来控制
        //1.存入缓存格式 实时数据前缀:system_{id}:pointName pointL(扩展成PointCache实体)
        //2.存入排序块 格式 排序前缀:pointName value score systemId
        PointUnitAndParamTypeResponse pointCollectConfigResponse = querySingle(pointL.getRelevanceId(), pointL.getPointName(), pointL.getLevel());
        if (pointCollectConfigResponse != null) {
            PointCache pointCache = new PointCache();
            MapperUtils.copyProperties(pointL, pointCache);
            MapperUtils.copyProperties(pointCollectConfigResponse, pointCache);
            setDefaultCache(pointCache);
            setRankCache(pointCache);
        } else {
            log.error("set redis pointCollectConfigResponse  is NULL relevanceId {} level {},pointName {} ", pointL.getRelevanceId(), pointL.getLevel(), pointL.getPointName());
        }
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }


    private void setDefaultCache(PointCache pointCache) {
        try {
            String key = RedisKeyConst.First_REAL_DATA;
            valueOperations.set(
                    (key + pointCache.getLevel() + ":" + pointCache.getRelevanceId()).concat(":").concat(pointCache.getPointName()),
                    pointCache
            );
        } catch (Exception e) {
            log.error("set redis realValue cause execption {}", e);
        }

    }

    /**
     * 设置排行数据
     *
     * @param pointCache
     */
    private void setRankCache(PointCache pointCache) {
        try {
            String key = RedisKeyConst.POINT_RANK;
            zSetOperations.add(key + pointCache.getLevel() + ":".concat(pointCache.getPointName()), pointCache.getRelevanceId(), Double.valueOf(pointCache.getValue()));
        } catch (Exception e) {
            log.error("set redis rankRealValue cause execption {}", e);
        }
    }

    public List<PointCache> queryAllPointsCache() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*");
        int pageCount = 20000;
        int total = keys.size();
        List<PointCache> result = new ArrayList<>();
//        result.addAll(queryPointsCache(keys).get());
        if (keys.size() > pageCount) {
            for (int i = 0; i < (total / pageCount); i++) {
                Set<String> keyItems = keys.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                result.addAll(queryPointsCache(keyItems).get());
            }
            if (total % pageCount > 0) {
                Set<String> keyItems = keys.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                result.addAll(queryPointsCache(keyItems).get());
            }
        } else {
            result.addAll(queryPointsCache(keys).get());
        }
        log.info("query all pointcache datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }

    @Async("getPointCacheTask")
    protected Future<List<PointCache>> queryPointsCache(Set<String> keys) {
        long start = System.currentTimeMillis();
        List<String> cache = redisTemplate.opsForValue().multiGet(keys);
//        ArrayList<byte[]> cache = (ArrayList<byte[]>) redisTemplate.execute((RedisCallback<Object>) conn -> {
//            conn.openPipeline();
//            keys.forEach(x -> {
//                conn.get(x.getBytes());
//            });
//            return conn.closePipeline();
//        });
        List<PointCache> results = new LinkedList<>();
        if (!CollectionUtils.isEmpty(cache))
            cache.forEach(x -> {
                results.add(JSONObject.parseObject(x, PointCache.class));
            });
        log.info("query child thread time: {} tread-name:{}", System.currentTimeMillis() - start, Thread.currentThread().getName());
        return new AsyncResult(results);
    }


    public List<DeviceInfoDto> querySecondDevice(String device) {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(RedisKeyConst.SECOND_REAL_DATA.concat(device) + "*");
        int pageCount = 20000;
        int total = keys.size();
        List<DeviceInfoDto> result = new ArrayList<>();
        if (keys.size() > pageCount) {
            for (int i = 0; i < (total / pageCount); i++) {
                Set<String> keyItems = keys.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                getRedisList(keyItems, result);
            }
            if (total % pageCount > 0) {
                Set<String> keyItems = keys.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                getRedisList(keyItems, result);
            }
        } else {
            getRedisList(keys, result);
        }
        log.info("query second datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }

    private void getRedisList(Set<String> keys, List<DeviceInfoDto> result) {
        List<String> infos = redisTemplate.opsForValue().multiGet(keys);
        if (!CollectionUtils.isEmpty(infos)) {
            infos.stream().forEach(e -> {
                result.add(JSONObject.parseObject(e, DeviceInfoDto.class));
            });
        }
    }

    public List<Map<Object, Object>> querySecondMap(String device) {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(RedisKeyConst.SECOND_REAL_DATA.concat(device) + "*");
        int pageCount = 20000;
        int total = keys.size();
        List<Map<Object, Object>> result = new ArrayList<>();
        if (keys.size() > pageCount) {
            for (int i = 0; i < (total / pageCount); i++) {
                Set<String> keyItems = keys.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                getRedisMap(keyItems, result);
            }
            if (total % pageCount > 0) {
                Set<String> keyItems = keys.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                getRedisMap(keyItems, result);
            }
        } else {
            getRedisMap(keys, result);
        }
        log.info("second datas Map time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }


    private void getRedisMap(Set<String> keys, List<Map<Object, Object>> result) {
        if (!CollectionUtils.isEmpty(keys)) {
            keys.stream().forEach(key -> {
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
                result.add(entries);

            });
        }
    }


    public List<Map<Object, Object>> querySecondMap() {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(RedisKeyConst.SECOND_REAL_DATA + "*");
        int pageCount = 20000;
        int total = keys.size();
        List<Map<Object, Object>> result = new ArrayList<>();
        if (keys.size() > pageCount) {
            for (int i = 0; i < (total / pageCount); i++) {
                Set<String> keyItems = keys.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                getRedisMap(keyItems, result);
            }
            if (total % pageCount > 0) {
                Set<String> keyItems = keys.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                getRedisMap(keyItems, result);
            }
        } else {
            getRedisMap(keys, result);
        }
        log.info("second datas Map time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }


    public void deleteRank(String pointName, List<Integer> listRelevanceId, Integer level) {
        try {
            String key = RedisKeyConst.POINT_RANK;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                String rankKey = key + level + ":".concat(pointName);
                listRelevanceId.stream().forEach(relevanceId -> {
                    conn.zRem(rankKey.getBytes(), String.valueOf(relevanceId).getBytes());
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("delete redis rankRealValue cause execption {}", e);
        }
    }

    public void deleteRealData(String pointName, List<Integer> listRelevanceId, Integer level) {
        try {
            String key = RedisKeyConst.First_REAL_DATA;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                for (Integer relevanceId : listRelevanceId) {
                    //String skey = key + level + ":".concat(relevanceId.toString()) + ":*";
                    //List<String> result = new ArrayList<>();
                    //Cursor<byte[]> scan = conn.scan(ScanOptions.scanOptions().match(skey).build());
                    //while (scan.hasNext()) {
                    //    result.add(scan.next().toString());
                    //}

                    Set<byte[]> keys = conn.keys((key + level + ":".concat(relevanceId.toString()) + ":".concat(pointName)).getBytes());
                    keys.forEach(x -> {
                        conn.del(x);
                    });
                }
                conn.closePipeline();
                return null;
            });

        } catch (Exception e) {
            log.error("delete redis by relevances: {} level: cause exception {}", listRelevanceId, level, e.getStackTrace());
        }
    }

}
