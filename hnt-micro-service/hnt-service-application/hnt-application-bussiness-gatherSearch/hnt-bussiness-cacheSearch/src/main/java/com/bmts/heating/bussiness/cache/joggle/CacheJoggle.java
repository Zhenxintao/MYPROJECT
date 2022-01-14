package com.bmts.heating.bussiness.cache.joggle;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.bussiness.cache.service.RealDataService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryCacheDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;

import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author naming
 * @description
 * @date 2021/1/13 20:08
 **/
@RestController
@Api(tags = "实时库查询")
@RequestMapping("cache")
@Slf4j
public class CacheJoggle {
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    RealDataService realDataService;

    @PostMapping
    public List<PointCache> queryCache(@RequestBody QueryCacheDto queryCacheDto) throws MicroException, IOException {
//        Map<Integer, String[]> integerMap = redisCacheService.queryPointsByReleverce(systemIds,TreeLevel.HeatSystem.level() );
        Map<Integer, String[]> integerMap = new HashMap<>();
        for (Integer systemId : queryCacheDto.getSystemIds()) {
            integerMap.put(systemId, queryCacheDto.getPointStandName());
        }
        List<PointCache> pointCacheList = redisCacheService.queryRealDataBySystems(integerMap, TreeLevel.HeatSystem.level());
        return pointCacheList;
    }

    @PostMapping("/param")
    public List<PointCache> queryCacheParam(@RequestBody QueryCacheDto queryCacheDto) throws MicroException, IOException {

        log.info(JSONObject.toJSONString(queryCacheDto));
        Map<Integer, String[]> integerMap = redisCacheService.queryPointsByReleverce(queryCacheDto.getSystemIds(), TreeLevel.HeatSystem.level());
        Map<Integer, String[]> queryMap = new HashMap<>();
        integerMap.forEach((k, v) -> {
            queryMap.put(k, string.StringArrayUtil.intersect(queryCacheDto.getPointStandName(), v));
        });
        List<PointCache> pointCacheList = redisCacheService.queryRealDataBySystems(queryMap, TreeLevel.HeatSystem.level());
        log.info(JSONObject.toJSONString(pointCacheList));
        return pointCacheList;
    }

    @PostMapping("/basic")
    public List<PointCache> queryCacheBasic(@RequestBody Map<Integer, String[]> map) throws IOException, MicroException {
        return redisCacheService.queryRealOnlyValue(map, TreeLevel.HeatSystem.level());
    }
}
