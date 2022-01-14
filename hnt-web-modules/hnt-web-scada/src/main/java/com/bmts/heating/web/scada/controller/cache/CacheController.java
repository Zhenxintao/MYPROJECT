package com.bmts.heating.web.scada.controller.cache;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointConfigSourceView;
import com.bmts.heating.commons.basement.model.db.entity.PointConfigStationView;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.request.CraftCacheDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryCacheDto;
import com.bmts.heating.commons.entiy.baseInfo.request.RedisCacheDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/1/15 17:26
 **/
@RestController
@RequestMapping("cache")
@Api(tags = "实时数据查询")
public class CacheController extends BaseController {

    @PostMapping("/byCabinet")
    @ApiOperation(value = "查询实时库数据", response = PointCache.class)
    public Response queryCacheByType(@RequestBody RedisCacheDto redisCacheDto) {
        FirstNetBase[] firstNetBases = template.doHttp("/common/firstNetBase", null, baseServer, FirstNetBase[].class, HttpMethod.GET);
        List<Integer> systemIds = Arrays.asList(firstNetBases).stream().filter(x -> x.getHeatSystemId() != 0 && Objects.equals(x.getHeatSystemId(), redisCacheDto.getId())).map(x -> x.getHeatSystemId()).collect(Collectors.toList());
        QueryCacheDto queryCacheDto = new QueryCacheDto();
        queryCacheDto.setSystemIds(systemIds);
        queryCacheDto.setPointStandName(redisCacheDto.getPointStandName());
        Object obj = template.doHttp("/cache/param", queryCacheDto, gathServer, Object.class, HttpMethod.POST);
        return Response.success(obj);
    }

    @ApiOperation(value = "查询工艺图实时数据", response = PointCache.class)
    @PostMapping("/craft")
    public Response queryCacheByPoint(@RequestBody CraftCacheDto dto) {
        Response response = template.doHttp("/pointConfig/byids", dto, baseServer, Response.class, HttpMethod.POST);
        List<PointConfig> pointConfigs = JSONObject.parseArray(JSONObject.toJSONString(response.getData()), PointConfig.class);
        QueryCacheDto queryCacheDto = new QueryCacheDto();
        queryCacheDto.setSystemIds(pointConfigs.stream().map(x -> x.getRelevanceId()).distinct().collect(Collectors.toList()));
        List<String> columns = pointConfigs.stream().map(x -> x.getPointColumnName()).distinct().collect(Collectors.toList());
        String[] pointStandNames = new String[columns.size()];
        for (int i = 0, len = pointStandNames.length; i < len; i++)
            pointStandNames[i] = columns.get(i);
//        columns.forEach(x->Arrays.fill(pointStandNames,x));
        queryCacheDto.setPointStandName(pointStandNames);
        List<PointCache> result = Arrays.stream(template.doHttp("/cache", queryCacheDto, gathServer, PointCache[].class, HttpMethod.POST)).filter(x -> dto.getPointIds().contains(x.getPointId())).collect(Collectors.toList());
        return Response.success(result);
    }

    @ApiOperation(value = "新版工艺图查询实时数据", response = PointCache.class)
    @PostMapping("/craft/points")
    public Response queryCache(@RequestBody CraftCacheDto dto) {
        Response response = template.doHttp("/pointConfig/ids/bycolumns", dto, baseServer, Response.class, HttpMethod.POST);
        QueryCacheDto queryCacheDto = new QueryCacheDto();
        // 处理热力站
        if (dto.getType() == 1) {
            List<PointConfigStationView> pointConfigs = JSONObject.parseArray(JSONObject.toJSONString(response.getData()), PointConfigStationView.class);
            queryCacheDto.setSystemIds(pointConfigs.stream().map(x -> x.getRelevanceId()).distinct().collect(Collectors.toList()));
            List<String> columns = pointConfigs.stream().map(x -> x.getColumnName()).distinct().collect(Collectors.toList());
            String[] pointStandNames = new String[columns.size()];
            for (int i = 0, len = pointStandNames.length; i < len; i++)
                pointStandNames[i] = columns.get(i);
            queryCacheDto.setPointStandName(pointStandNames);

        } else {
            // 处理热源
            List<PointConfigSourceView> pointConfigs = JSONObject.parseArray(JSONObject.toJSONString(response.getData()), PointConfigSourceView.class);
            queryCacheDto.setSystemIds(pointConfigs.stream().map(x -> x.getRelevanceId()).distinct().collect(Collectors.toList()));
            List<String> columns = pointConfigs.stream().map(x -> x.getColumnName()).distinct().collect(Collectors.toList());
            String[] pointStandNames = new String[columns.size()];
            for (int i = 0, len = pointStandNames.length; i < len; i++)
                pointStandNames[i] = columns.get(i);
            queryCacheDto.setPointStandName(pointStandNames);
        }

        List<PointCache> result = Arrays.stream(template.doHttp("/cache", queryCacheDto, gathServer, PointCache[].class, HttpMethod.POST)).collect(Collectors.toList());
//        .filter(x -> dto.getPointIds().contains(x.getPointId())).collect(Collectors.toList())
        return Response.success(result);

    }
}
