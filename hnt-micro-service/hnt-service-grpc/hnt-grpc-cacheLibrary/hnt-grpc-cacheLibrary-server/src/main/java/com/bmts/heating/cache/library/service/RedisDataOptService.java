package com.bmts.heating.cache.library.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.service.PointCollectConfigService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RedisDataOptService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisManager redisManager;
    @Autowired
    PointStandardService pointStandardService;
    @Autowired
    HeatSystemMapper heatSystemMapper;
    @Autowired
    PointConfigService pointConfigService;

//    @Cacheable(cacheNames = "tscc:heatnet:firstnet", key = "'firstnetbase'")
    public List<FirstNetBase> queryFirstNetBase() {
        QueryWrapper<FirstNetBase> queryWrapper=new QueryWrapper<>();
        queryWrapper.gt("hts.id",0);
        queryWrapper.gt("hc.id",0);
        return heatSystemMapper.querySystem(queryWrapper);
    }
    /*
      热源上配点基础查询
     */
    public List<FirstNetBase> querySourceFirstNetBase() {
        QueryWrapper<FirstNetBase> queryWrapper=new QueryWrapper<>();
        queryWrapper.gt("hc.heatSourceId",0);
        List<FirstNetBase> list= heatSystemMapper.querySourceSystem(queryWrapper);
       return list;
    }

    @Cacheable(cacheNames = "tscc:heatnet:firstnet", key = "'pointstandard'")
    public List<PointStandard> queryPointStandard() {
        QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleteFlag", false);
        return pointStandardService.list(queryWrapper);
    }


    /**
     * 加载点到redis  系统对应所有点key
     */
    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RedisPointService redisCollectPointService = event.getApplicationContext().getBean(RedisPointService.class);
        redisCollectPointService.clear();
        redisCollectPointService.loadPointCollectConfigData();
        redisCollectPointService.loadSourcePoints();
    }
}
























