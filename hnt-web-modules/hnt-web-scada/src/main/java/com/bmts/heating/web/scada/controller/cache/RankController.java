package com.bmts.heating.web.scada.controller.cache;

import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointRank;
import com.bmts.heating.commons.entiy.gathersearch.request.RankDto;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.PointRankResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.RankItem;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.controller.base.BaseController;
import com.bmts.heating.web.scada.service.monitor.impl.MonitorServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author naming
 * @description
 * @date 2021/1/10 17:48
 **/
@RestController
@Api(tags = "排行查询")
@RequestMapping("rank")
public class RankController extends BaseController {

    @Autowired
    private MonitorServiceImpl monitorServiceImpl;

    @ApiOperation(value = "查询实时排行", response = PointRankResponse.class)
    @PostMapping
    public Response queryRank(@RequestBody RankDto rankDto, HttpServletRequest request) {
        // 根据权限查询
        Integer userId = JwtUtils.getUserId(request);
        List<Integer> stationIds = new ArrayList<>();
        if (userId != null && userId != -1) {// 超级管理员
            // 查询权限
            stationIds = Arrays.asList(template.doHttp("/common/station/user/" + userId, null, baseServer, Integer[].class, HttpMethod.GET));
            if (CollectionUtils.isEmpty(stationIds)) {
                return Response.success();
            }
        }

        final int total = rankDto.getCount();
        if (rankDto.getOrgId() > 0) {
            rankDto.setCount(1000);
        }
        PointRank[] pointRanks = template.doHttp("/rank", rankDto, gathServer, PointRank[].class, HttpMethod.POST);
        if (pointRanks == null) {
            return Response.success();
        }
        List<PointRankResponse> result = new ArrayList<>(pointRanks.length);

        List<FirstNetBase> firstNetBases =
                Arrays.asList(template.doHttp("/common/firstNetBase", null, baseServer, FirstNetBase[].class, HttpMethod.GET));

        if (CollectionUtils.isEmpty(firstNetBases)) {
            return Response.success();
        }

        Stream<FirstNetBase> firstNetBaseStream = firstNetBases.stream().filter(FirstNetBase::isStatus);

        if (!CollectionUtils.isEmpty(stationIds)) {
            // 获取有权限的站  ，对两个list ，进行处理
            //firstNetBases = firstNetBases.stream().filter(s -> finalStationIds.stream().filter(f -> Objects.equals(f, s.getHeatTransferStationId())).findAny().isPresent())
            //        .collect(Collectors.toList());
            //根据用户权限过滤基础信息
            List<Integer> finalStationIds = stationIds;
            firstNetBaseStream = firstNetBaseStream.filter(e -> finalStationIds.contains(e.getHeatTransferStationId()));
        }

        if (rankDto.getOrgId() > 0) {
            // 获取组织结构的下一级
            List<Integer> orgByParentIds = monitorServiceImpl.getOrgByParentIds(Collections.singletonList(rankDto.getOrgId()));
            //搜索条件含公司的数据
            firstNetBaseStream = firstNetBaseStream.filter(e -> orgByParentIds.contains(e.getHeatStationOrgId()) && e.getHeatSystemId() > 0);

            //firstNetBases = firstNetBases.stream().filter(x -> x.getHeatStationOrgId() == rankDto.getOrgId() && x.getHeatSystemId() > 0).collect(Collectors.toList());

            List<Integer> systemIds = firstNetBaseStream.map(x -> x.getHeatSystemId()).collect(Collectors.toList());
            for (PointRank pointRank : pointRanks) {
                //过滤查询机构下带的所有系统
                Map<Integer, Double> maps = new LinkedHashMap<>();
                pointRank.getMap().forEach((k, v) -> {
                    if (maps.size() < total && systemIds.contains(k))
                        maps.put(k, v);
                });
                buildResult(firstNetBases, result, pointRank, maps);
            }
        } else {
            for (PointRank pointRank : pointRanks) {
                buildResult(firstNetBases, result, pointRank, pointRank.getMap());
            }
        }
        return Response.success(result);

    }

    private void buildResult(List<FirstNetBase> firstNetBases, List<PointRankResponse> result, PointRank pointRank, Map<Integer, Double> maps) {
        PointRankResponse pointRankResponse = new PointRankResponse();
        pointRankResponse.setPointColumnName(pointRank.getColumnName());
        List<RankItem> rankItems = new LinkedList<>();
        maps.forEach((k, v) -> {
            if (v != null) {
                FirstNetBase firstNetBase = firstNetBases.stream().filter(x -> Objects.equals(x.getHeatSystemId(), k)).findFirst().orElse(null);
                if (firstNetBase != null) {
                    RankItem rankItem = new RankItem();
                    rankItem.setStationName(firstNetBase.getHeatTransferStationName());
                    rankItem.setSystemName(firstNetBase.getHeatSystemName());
                    rankItem.setSystemId(firstNetBase.getHeatSystemId());
                    rankItem.setValue(v);
                    rankItems.add(rankItem);
                }
            }
        });
        pointRankResponse.setRankDetails(rankItems);
        result.add(pointRankResponse);
    }
}
