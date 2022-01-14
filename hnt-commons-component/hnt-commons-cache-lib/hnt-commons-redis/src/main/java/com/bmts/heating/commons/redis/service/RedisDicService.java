package com.bmts.heating.commons.redis.service;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.redis.utils.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisDicService {
    @Autowired
    RedisManager redisManager;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    //    @Cacheable(cacheNames = "hnt:tscc",key = "'dic'")
    public List<Dic> queryAll() {
        if (redisManager.hasKey(RedisKeyConst.DIC)) {
            List<Dic> dics = new LinkedList<>();
            Set<String> result = redisTemplate.opsForSet().members(RedisKeyConst.DIC);
            for (String s : result) {
                dics.add(JSONObject.parseObject(s,Dic.class));
            }
            return dics;
        }
        return null;
    }

    public boolean set(List<Dic> dics) {
        if (!CollectionUtils.isEmpty(dics)) {
            try {
                redisTemplate.execute((RedisCallback<Object>) conn -> {
                    conn.openPipeline();
                    dics.forEach(x -> {
                        conn.sAdd(RedisKeyConst.DIC.getBytes(), JSONObject.toJSONBytes(x));
                    });
                    conn.closePipeline();
                    return null;
                });
                return true;
            } catch (Exception e) {
                log.error("set redis cause exception {}", e);
                return false;
            }

        } else
            return false;

    }

    public List<Dic> queryChild(String code) {
        List<Dic> dics = queryAll();
        if (dics == null) return null;
        return dics.stream().filter(x -> x.getCode().equals(code)).collect(Collectors.toList());
    }

    public List<Dic> queryChild(int id) {
        List<Dic> dics = queryAll();
        if (dics == null) return null;
        return dics.stream().filter(x -> x.getPid().equals(id)).collect(Collectors.toList());
    }

    public boolean clear() {
        try {
            if (redisManager.hasKey(RedisKeyConst.DIC))
                redisManager.del(RedisKeyConst.DIC);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清理字典key出错:{}", e.getMessage());
            return false;
        }
    }

}
