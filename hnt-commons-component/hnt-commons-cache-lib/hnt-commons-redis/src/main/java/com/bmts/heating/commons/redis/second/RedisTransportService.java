package com.bmts.heating.commons.redis.second;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class RedisTransportService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 批量更新缓存
     *
     * @param map
     */
    public void setTransportMap(Map<String, Map<String, Object>> map) {
        try {
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                map.forEach((k, v) -> {
                    conn.set(k.getBytes(), JSONObject.toJSONBytes(v));
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新长输缓存数据失败 {}", e);
            throw e;
        }


    }

    /**
     * 查询所有缓存
     */
    public Map<String, Map<String, Object>> queryTransportMap() {
        long start = System.currentTimeMillis();
        Set<String> keys = redisTemplate.keys(RedisKeyConst.DEVICE_OTHER_SYSTEM.concat("*"));
        int pageCount = 20000;
        int total = keys.size();
        Map<String, Map<String, Object>> result = new HashMap<>();
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
        log.info("query transport point datas time-consuming {} ms", System.currentTimeMillis() - start);
        return result;
    }

    private void getRedisMap(Set<String> keys, Map<String, Map<String, Object>> result) {
        if (!CollectionUtils.isEmpty(keys)) {
            keys.stream().forEach(e -> {
                Map<String, Object> map = JSON.parseObject(redisTemplate.opsForValue().get(e), Map.class);
                result.put(e, map);
            });
        }
    }

}
