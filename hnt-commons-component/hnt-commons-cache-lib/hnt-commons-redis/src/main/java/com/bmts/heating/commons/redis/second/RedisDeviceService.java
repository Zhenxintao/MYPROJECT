package com.bmts.heating.commons.redis.second;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.second.enums.SecondDeviceType;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Slf4j
@Service
public class RedisDeviceService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public void clear() {
        String key = RedisKeyConst.SECOND_REAL_DATA;
        Set<String> keys = redisTemplate.keys(key.concat("*"));
        redisTemplate.delete(keys);
    }


    /**
     * 删除缓存数据
     *
     * @param listCode
     * @param deviceType
     */
    @Async
    public void deleteCache(List<String> listCode, int deviceType) {
        try {
            String key = RedisKeyConst.SECOND_REAL_DATA;
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                conn.openPipeline();
                String deviceName = SecondDeviceType.getRedisKey(deviceType);
                if (StringUtils.isNotBlank(deviceName)) {
                    for (String code : listCode) {
                        Set<byte[]> keys = conn.keys((key.concat(deviceName)).getBytes());
                        keys.forEach(x -> {
                            conn.del(x);
                        });
                    }
                }
                conn.closePipeline();
                return null;
            });

        } catch (Exception e) {
            log.error("delete redis by listCode: {} deviceType: cause exception {}", listCode, deviceType, e.getStackTrace());
        }
    }


    /**
     * 批量更新缓存
     *
     * @param list
     */
    public void setCachePoint(List<DeviceInfoDto> list) {
        try {
            redisTemplate.execute((RedisCallback<Object>) conn -> {
                list.forEach(x -> {
                    String deviceName = SecondDeviceType.getRedisKey(x.getDeviceType());
                    if (StringUtils.isNotBlank(deviceName) && StringUtils.isNotBlank(x.getDeviceCode())) {
                        String key = RedisKeyConst.SECOND_REAL_DATA.concat(deviceName) + x.getDeviceCode();
                        conn.set(key.getBytes(), JSONObject.toJSONBytes(x));
                    }
                });
                return conn.closePipeline();
            });
        } catch (Exception e) {
            log.error("更新二网缓存数据失败 {}", e);
            throw e;
        }

    }


    /**
     * 批量更新缓存 HashMap 类型数据
     *
     * @param maps
     */
    public void setCachMap(Map<String, Map<String, String>> maps) {
        try {

            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    maps.forEach((key, map) -> {
                        redisOperations.opsForHash().putAll(key, map);
                    });
                    return null;
                }
            });

        } catch (Exception e) {
            log.error("更新二网缓存数据失败 {}", e);
            throw e;
        }

    }


}
