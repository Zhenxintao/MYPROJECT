package com.bmts.heating.commons.redis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class RedisEnergyPointService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WebPageConfigService webPageConfigService;

    /**
     * 查询 热源的  能耗点 水、电、热 的点实时数据
     *
     * @return
     */
    public List<PointL> cacheSourceEnery() {
        long start = System.currentTimeMillis();
        // 点的 key 值 格式是  tscc:heatnet:firstnet:realdata:level_1:*:Ep
        Set<String> keySets = new HashSet<>();

        // 从webPageConfig 表中查询水电热的点
        WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "energyPointConfig"));
        if (webPageConfig != null && StringUtils.isNotBlank(webPageConfig.getJsonConfig())) {
            GetEnergyPointConfig energyPointConfig = JSON.parseObject(webPageConfig.getJsonConfig(), GetEnergyPointConfig.class);
            if (energyPointConfig != null) {

                // 热源的  热   能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getSourceHeatPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getSourceHeatPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

                // 热源的  水  能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getSourceWaterPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getSourceWaterPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

                // 热源的  电  能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getSourceElectricityPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getSourceElectricityPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

            }

        }

        List<PointL> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(keySets)) {
            int pageCount = 20000;
            int total = keySets.size();
            if (keySets.size() > pageCount) {
                for (int i = 0; i < (total / pageCount); i++) {
                    Set<String> keyItems = keySets.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
                if (total % pageCount > 0) {
                    Set<String> keyItems = keySets.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
            } else {
                result.addAll(queryEneryCache(keySets));
            }
        }
        log.info("cacheSourceEnery datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }


    /**
     * 查询 热力站的  能耗点 水、电、热 的点实时数据
     *
     * @return
     */
    public List<PointL> cacheStationEnery() {
        long start = System.currentTimeMillis();
        // 点的 key 值 格式是  tscc:heatnet:firstnet:realdata:level_1:*:Ep
        Set<String> keySets = new HashSet<>();

        // 从webPageConfig 表中查询水电热的点
        WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "energyPointConfig"));
        if (webPageConfig != null && StringUtils.isNotBlank(webPageConfig.getJsonConfig())) {
            GetEnergyPointConfig energyPointConfig = JSON.parseObject(webPageConfig.getJsonConfig(), GetEnergyPointConfig.class);
            if (energyPointConfig != null) {

                // 热力站的  热   能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getStationHeatPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getStationHeatPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

                // 热力站的  水  能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getStationWaterPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getStationWaterPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

                // 热力站的  电  能耗点
                if (StringUtils.isNotBlank(energyPointConfig.getStationElectricityPoint())) {
                    Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + energyPointConfig.getStationElectricityPoint());
                    if (!CollectionUtils.isEmpty(keys)) {
                        keySets.addAll(keys);
                    }
                }

            }

        }

        List<PointL> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(keySets)) {
            int pageCount = 20000;
            int total = keySets.size();
            if (keySets.size() > pageCount) {
                for (int i = 0; i < (total / pageCount); i++) {
                    Set<String> keyItems = keySets.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
                if (total % pageCount > 0) {
                    Set<String> keyItems = keySets.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
            } else {
                result.addAll(queryEneryCache(keySets));
            }
        }
        log.info("cacheSourceEnery datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }

    /**
     * 查询 能耗点 水、电、热 的点实时数据
     *
     * @return
     */
    public List<PointL> cacheAllEnery() {
        long start = System.currentTimeMillis();
        // 点的 key 值 格式是  tscc:heatnet:firstnet:realdata:level_1:*:Ep
        Set<String> keySets = new HashSet<>();

        // 从webPageConfig 表中查询水电热的点
        Map<String, String> map = new HashMap<>();
        WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "energyPointConfig"));
        if (webPageConfig != null && StringUtils.isNotBlank(webPageConfig.getJsonConfig())) {
            map.putAll(JSONObject.parseObject(webPageConfig.getJsonConfig(), Map.class));
        }

        map.forEach((k, v) -> {
            Set<String> keys = redisTemplate.keys(RedisKeyConst.First_REAL_DATA + "1:*:" + v);
            if (!CollectionUtils.isEmpty(keys)) {
                keySets.addAll(keys);
            }
        });

        List<PointL> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(keySets)) {
            int pageCount = 20000;
            int total = keySets.size();
            if (keySets.size() > pageCount) {
                for (int i = 0; i < (total / pageCount); i++) {
                    Set<String> keyItems = keySets.stream().skip(i * pageCount).limit(pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
                if (total % pageCount > 0) {
                    Set<String> keyItems = keySets.stream().skip(total / pageCount * pageCount).limit(total % pageCount).collect(Collectors.toSet());
                    result.addAll(queryEneryCache(keyItems));
                }
            } else {
                result.addAll(queryEneryCache(keySets));
            }
        }
        log.info("query eneryCache datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }

    protected List<PointL> queryEneryCache(Set<String> keys) {
        long start = System.currentTimeMillis();
        List<String> cache = redisTemplate.opsForValue().multiGet(keys);
        List<PointL> results = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cache)) {
            cache.forEach(x -> {
                results.add(JSONObject.parseObject(x, PointL.class));
            });
        }
        log.info("query eneryCache time: {} tread-name:{}", System.currentTimeMillis() - start, Thread.currentThread().getName());
        return results;
    }


}
