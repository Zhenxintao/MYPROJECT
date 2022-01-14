package com.bmts.heating.cache.library.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.cache.library.pojo.RealPointsPojo;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.cache.PointUnitAndParamTypeResponse;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.utils.MapperUtils;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.utils.common.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisRealValueService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public Map<Integer, Set<String>> queryPointsByIds(List<Integer> ids, int level) {
        Map<Integer, Set<String>> maps = new HashMap<>(ids.size());
        ids.forEach(x -> {
            Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + level + ":".concat(x + "*")).stream().map(item -> item.substring(item.lastIndexOf(":") + 1)).collect(Collectors.toSet());
            maps.put(x, keys);
        });
        return maps;
    }

    /**
     * @param
     * @return
     */
    public Set<PointCache> queryCachePoint(List<RealPointsPojo> pojos) {
        Long start = System.currentTimeMillis();
        ArrayList<byte[]> cache = getCacheList(pojos);
        List<PointUnitAndParamTypeResponse> pointsList = queryPointBasic(pojos);
        Set<PointCache> results = mapCacheData(cache, pointsList);
        log.info("查询redis映射 :{}", System.currentTimeMillis() - start);
        return results;
    }

    /**
     * 只查询数值，无需匹配点基础数据
     * @param pojos
     * @return
     */
    public Set<PointCache> queryCachePointUnMapPointConfig(List<RealPointsPojo> pojos)
    {
        Long start = System.currentTimeMillis();
        ArrayList<byte[]> cache = getCacheList(pojos);
        Set<PointCache> results = new HashSet<>();
        cache.forEach(x->{
                    if (x != null) {
                        PointCache item = JSONObject.parseObject(x, PointCache.class);
                        results.add(item);
                    }
        });
        log.info("查询redis映射 :{}", System.currentTimeMillis() - start);
        return results;
    }
    /**
     * 取出缓存数据
     *
     * @param pojos
     * @return
     */
    private ArrayList<byte[]> getCacheList(List<RealPointsPojo> pojos) {
        return (ArrayList<byte[]>) redisTemplate.execute((RedisCallback<Object>) conn -> {
            conn.openPipeline();
            pojos.forEach(x -> {
                for (String item : x.getPointNames()) {
                    String key = (RedisKeyConst.First_REAL_DATA + x.getLevel()).concat(":") + x.getRelevanceId() + ":".concat(item);
                    conn.get(key.getBytes());
                }
            });
            return conn.closePipeline();
        });
    }

    /**
     * 取出点配置基础信息
     *
     * @param pojos
     * @return
     */
    private List<PointUnitAndParamTypeResponse> queryPointBasic(List<RealPointsPojo> pojos) {

        List<PointUnitAndParamTypeResponse> pointsList = new ArrayList<>();
        Set<Integer> systems=new HashSet<>();
        pojos.forEach(x -> {
            String pointKey = (RedisKeyConst.POINT_COLLECTCONFIG + x.getLevel()).concat(":") + x.getRelevanceId();
            Set<String> members = redisTemplate.opsForSet().members(pointKey);
            members.forEach(p -> {
                PointUnitAndParamTypeResponse pointUnitAndParamTypeResponse = JSONObject.parseObject(p, PointUnitAndParamTypeResponse.class);
                pointsList.add(pointUnitAndParamTypeResponse);
                systems.add(pointUnitAndParamTypeResponse.getRelevanceId());
            });

        });
        return pointsList;
    }

    /**
     * 匹配数据返回
     *
     * @param cache
     * @param pointsList
     */
    private Set<PointCache> mapCacheData(ArrayList<byte[]> cache, List<PointUnitAndParamTypeResponse> pointsList) {
        Set<PointCache> results = new HashSet<>();
        if (CollectionUtils.isNotEmpty(cache))
            cache.forEach(x -> {
                if (x != null) {
                    PointCache item = JSONObject.parseObject(x, PointCache.class);
                    PointUnitAndParamTypeResponse pointUnitAndParamTypeResponse = pointsList.stream().filter(p -> p.getId().equals(item.getPointId())).findFirst().orElse(null);
                    if (pointUnitAndParamTypeResponse != null) {
                        String value = item.getValue();
                        int pointId = item.getPointId();
                        MapperUtils.copyProperties(pointUnitAndParamTypeResponse, item);
                        item.setValue(value);
                        item.setPointId(pointId);
                        results.add(item);
                    } else {
//                        log.error("pointUnitAndParamTypeResponse is null {} {}",item.getPointId(),item.getLevel());
                    }
//                    results.add(item);
                } else {
//                    log.error("x is null");
                }
            });
        return results;
    }


    /**
     * 区间排序 新版完成
     *
     * @param pointName  点名称
     * @param startValue 起始值
     * @param endValue   终止值
     * @param length     取数据条数
     * @param isAsc      是否为升序
     * @return
     */
    public Tuple<Map<Integer, Double>, Long> queryRank(String pointName, double startValue, double endValue, int length, boolean isAsc, int level) {
        String key = RedisKeyConst.POINT_RANK + level + ":".concat(pointName);
        Set<ZSetOperations.TypedTuple<String>> sets;
        if (isAsc) {
            sets = redisTemplate.opsForZSet().rangeByScoreWithScores(key, startValue, endValue, 0, length);
        } else {
            sets = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, startValue, endValue, 0, length);
        }
        long count = redisTemplate.opsForZSet().count(key, startValue, endValue);
        Map<Integer, Double> map = new LinkedHashMap<>(sets.size());
        sets.forEach(x -> {
            map.put(Integer.valueOf(x.getValue()), Double.valueOf(x.getScore()));
        });
        return new Tuple<>(map, count);
    }

    /**
     * 非区间排序
     *
     * @param pointName 点名称
     * @param length    取数据条数
     * @param isAsc     是否为升序
     * @return key systemId value 点实时值
     */
    public Tuple<Map<Integer, Double>, Long> queryRank(String pointName, int length, boolean isAsc, int level) {
        String key = RedisKeyConst.POINT_RANK + level + ":".concat(pointName);
        Set<ZSetOperations.TypedTuple<String>> sets;
        if (isAsc) {
            sets = redisTemplate.opsForZSet().rangeWithScores(key, 0, length - 1);
        } else {
            sets = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, length - 1);
        }
        Long count = redisTemplate.opsForZSet().count(key, 0, 50000);
        Map<Integer, Double> map = new LinkedHashMap<>(sets.size());
        sets.forEach(x -> {
            map.put(Integer.valueOf(x.getValue()), Double.valueOf(x.getScore()));
        });
        return new Tuple<>(map, count);
    }

}
