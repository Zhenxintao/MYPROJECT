package com.bmts.heating.bussiness.baseInformation.app.joggle.monitor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.HeatOrganizationJoggle;
import com.bmts.heating.bussiness.baseInformation.app.utils.ListUtil;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorBeyondDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Api(tags = "获取网~系统监测数据")
@RestController
@RequestMapping("/monitor")
@Slf4j
public class BaseInfoJoggle {

    @Autowired
    private HeatTransferStationService heatTransferStationService;

    @Autowired
    private AuthrityService authrityService;

    @Autowired
    private HeatOrganizationJoggle joggle;

    @Autowired
    private HeatSourceService heatSourceService;

    @ApiOperation("查询满足条件的站点")
    @PostMapping("/beyond")
    public List<Integer> beyond(@RequestBody MonitorBeyondDto dto) {
        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            Set<Integer> stationIdsByUserId = this.getStationIdsByUserId(dto.getUserId());
            if (CollectionUtils.isEmpty(stationIdsByUserId)) {
                return new ArrayList<>();
            }
            stationIds = stationIdsByUserId;
        }
        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            //过滤掉已冻结站点
            queryWrapper.eq("ht.status", true);
            if (!CollectionUtils.isEmpty(stationIds)) {
                queryWrapper.in("ht.id", stationIds);
            }
            queryWrapper = queryWrapper.eq("ht.deleteFlag", false);
            return heatTransferStationService.queryStationList(queryWrapper).stream().map(HeatTransferStationResponse::getId).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    @ApiOperation("查询满足条件的站点")
    @PostMapping("/stationInfo")
    public List<Integer> query(@RequestBody MonitorDto dto) {
        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            Set<Integer> stationIdsByUserId = this.getStationIdsByUserId(dto.getUserId());
            if (CollectionUtils.isEmpty(stationIdsByUserId)) {
                return new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(dto.getStationIds())) {
                // 过滤用户有权限的站  取两个list 的交集
                stationIds = dto.getStationIds().stream().filter(e -> stationIdsByUserId.contains(e)).collect(Collectors.toSet());
            } else {
                stationIds = stationIdsByUserId;
            }
        } else if (!CollectionUtils.isEmpty(dto.getStationIds())) {
            stationIds.addAll(dto.getStationIds());
        }


        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                //判断是否包含中文
                Matcher station = pattern.matcher(dto.getKeyWord());
                //包含中文
                if (station.find()) {
                    queryWrapper = queryWrapper.like("ht.name", dto.getKeyWord());
                } else {//简拼
                    queryWrapper = queryWrapper.like("ht.logogram", dto.getKeyWord());
                }
            }
            //过滤掉已冻结站点
            queryWrapper.eq("ht.status", true);
            if (!CollectionUtils.isEmpty(stationIds)) {
                queryWrapper.in("ht.id", stationIds);
            }
            if (ListUtil.isValid(dto.getOrganizationId())) {
                Set<Integer> integers = joggle.queryByParentOrgIds(dto.getOrganizationId());
                if (ListUtil.isValid(integers)) {
                    queryWrapper.in("ho.id", integers);
                }
            }
            if (ListUtil.isValid(dto.getHeatSourceId())) {
                queryWrapper.in("hs.id", dto.getHeatSourceId());
            }
            if (ListUtil.isValid(dto.getBuildType())) {
                queryWrapper = queryWrapper.in("ht.buildType", dto.getBuildType());
            }
            if (ListUtil.isValid(dto.getInsulationConstruction())) {
                queryWrapper.in("ht.insulationConstruction", dto.getInsulationConstruction());
            }
            if (ListUtil.isValid(dto.getPayType())) {
                queryWrapper.in("ht.payType", dto.getPayType());
            }
            if (ListUtil.isValid(dto.getHeatType())) {
                queryWrapper.in("ht.heatType", dto.getHeatType());
            }
            if (ListUtil.isValid(dto.getManageType())) {
                queryWrapper = queryWrapper.in("ht.manageType", dto.getManageType());
            }
            if (ListUtil.isValid(dto.getStationType())) {
                queryWrapper = queryWrapper.in("ht.StationType", dto.getStationType());
            }
            //if (ListUtil.isValid(dto.getStationIds())) {
            //    queryWrapper.or().in("ht.id", dto.getStationIds());
            //}
            queryWrapper = queryWrapper.eq("ht.deleteFlag", false);
            return heatTransferStationService.queryStationList(queryWrapper).stream().map(HeatTransferStationResponse::getId).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /*
     * 根据权限获取查询站-组织机构树
     */
    @ApiOperation("据权限获取查询站-组织机构树")
    @PostMapping("/orgStationTree")
    public Set<CommonTree> queryTree(@RequestBody MonitorDto dto) {

        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            stationIds = this.getStationIdsByUserId(dto.getUserId());
            if (CollectionUtils.isEmpty(stationIds)) {
                return new HashSet<>();
            }
        }

        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher station = pattern.matcher(dto.getKeyWord());//判断是否包含中文
                if (station.find()) {//包含中文
                    queryWrapper = queryWrapper.like("ht.name", dto.getKeyWord());
                } else {//简拼
                    queryWrapper = queryWrapper.like("ht.logogram", dto.getKeyWord());
                }
            }
            if (!CollectionUtils.isEmpty(stationIds)) {
                queryWrapper.in("ht.id", stationIds);
            }
            if (ListUtil.isValid(dto.getOrganizationId())) {
                queryWrapper.in("ho.id", joggle.queryByParentOrgIds(dto.getOrganizationId()));
            }
            if (ListUtil.isValid(dto.getHeatSourceId())) {
                queryWrapper.in("hs.id", dto.getHeatSourceId());
            }

            if (ListUtil.isValid(dto.getBuildType())) {
                queryWrapper = queryWrapper.in("ht.buildType", dto.getBuildType());
            }
            if (ListUtil.isValid(dto.getInsulationConstruction())) {
                queryWrapper.in("ht.insulationConstruction", dto.getInsulationConstruction());
            }
            if (ListUtil.isValid(dto.getPayType())) {
                queryWrapper.in("ht.payType", dto.getPayType());
            }
            if (ListUtil.isValid(dto.getHeatType())) {
                queryWrapper.in("ht.heatType", dto.getHeatType());
            }
            if (ListUtil.isValid(dto.getManageType())) {
                queryWrapper = queryWrapper.in("ht.manageType", dto.getManageType());
            }
            if (ListUtil.isValid(dto.getStationType())) {
                queryWrapper = queryWrapper.in("ht.StationType", dto.getStationType());
            }
            if (dto.getStatus() != null) {
                queryWrapper.eq("ht.status", dto.getStatus());
            }
            queryWrapper = queryWrapper.eq("ht.deleteFlag", false);
            //获取所有组织架构及所查询的Station列表
            List<CommonTree> commonTrees = heatTransferStationService.queryStationTree(queryWrapper);
            return this.filterTree(commonTrees);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    //获取用户权限站
    public Set<Integer> getStationIdsByUserId(Integer id) {
        if (id < 0) return new HashSet<>();
        return authrityService.queryStations(id).getStations();
    }


    private Set<CommonTree> filterTree(List<CommonTree> commonTrees) {
        //获取最大层级数
        int maxLeven = commonTrees.stream().max(Comparator.comparing(CommonTree::getLevel)).map(CommonTree::getLevel).orElse(0);
        //根据等级分组
        Map<Integer, List<CommonTree>> treeMap = commonTrees.stream().collect(Collectors.groupingBy(CommonTree::getLevel));
        Set<CommonTree> result = treeMap.get(maxLeven).stream().filter(e -> "station".equals(e.getProperties())).collect(Collectors.toSet());//筛选最大level的站
        List<String> parentLevelIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (int i = maxLeven; i > 1; i--) {
                List<String> pids = treeMap.get(i).stream().filter(e -> "station".equals(e.getProperties())).map(CommonTree::getPid).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(parentLevelIds)) {
                    pids.addAll(parentLevelIds);
                }
                List<CommonTree> collect = treeMap.get(i - 1).stream().filter(e -> pids.contains(e.getId())).collect(Collectors.toList());
                result.addAll(collect);
                parentLevelIds = collect.stream().map(CommonTree::getPid).collect(Collectors.toList());
            }
        }
        if (result.size() > 0) {
            List<CommonTree> collect = new ArrayList<>(treeMap.get(1));
            result.addAll(collect);
        }
        return result;
    }

    @ApiOperation("据权限获取查询站-组织机构树")
    @PostMapping("/orgSystemTree")
    public Response querySystemTree(@RequestBody MonitorDto dto) {

        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            stationIds = this.getStationIdsByUserId(dto.getUserId());
            if (CollectionUtils.isEmpty(stationIds)) {
                return Response.success();
            }
        }

        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher station = pattern.matcher(dto.getKeyWord());//判断是否包含中文
                if (station.find()) {//包含中文
                    queryWrapper = queryWrapper.like("ht.name", dto.getKeyWord());
                } else {//简拼
                    queryWrapper = queryWrapper.like("ht.logogram", dto.getKeyWord());
                }
            }
            if (!CollectionUtils.isEmpty(stationIds)) {
                queryWrapper.in("ht.id", stationIds);
            }
            if (ListUtil.isValid(dto.getOrganizationId())) {
                queryWrapper.in("ho.id", joggle.queryByParentOrgIds(dto.getOrganizationId()));
            }
            if (ListUtil.isValid(dto.getHeatSourceId())) {
                queryWrapper.in("hs.id", dto.getHeatSourceId());
            }

            if (ListUtil.isValid(dto.getBuildType())) {
                queryWrapper = queryWrapper.in("ht.buildType", dto.getBuildType());
            }
            if (ListUtil.isValid(dto.getInsulationConstruction())) {
                queryWrapper.in("ht.insulationConstruction", dto.getInsulationConstruction());
            }
            if (ListUtil.isValid(dto.getPayType())) {
                queryWrapper.in("ht.payType", dto.getPayType());
            }
            if (ListUtil.isValid(dto.getHeatType())) {
                queryWrapper.in("ht.heatType", dto.getHeatType());
            }
            if (ListUtil.isValid(dto.getManageType())) {
                queryWrapper = queryWrapper.in("ht.manageType", dto.getManageType());
            }
            if (ListUtil.isValid(dto.getStationType())) {
                queryWrapper = queryWrapper.in("ht.StationType", dto.getStationType());
            }
            if (dto.getStatus() != null) {
                queryWrapper.eq("ht.status", dto.getStatus());
            }
            queryWrapper = queryWrapper.eq("ht.deleteFlag", false);
            // 排除 0 系统
            queryWrapper.gt("hs.number", 0);
            //获取所有组织架构及所查询的Station列表
            List<CommonTree> commonTrees = heatTransferStationService.querySystemTree(queryWrapper);
            return Response.success(commonTrees);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }


    @ApiOperation("据权限获取查询热源-系统树")
    @PostMapping("/sourceSystemTree")
    public Response sourceSystemTree(@RequestBody BaseDto dto) {
        Set<Integer> systemIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            systemIds = authrityService.queryRelevanceList(dto.getUserId(), TreeLevel.HeatSource.level()).stream().collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(systemIds)) {
                return Response.success();
            }
        }
        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher station = pattern.matcher(dto.getKeyWord());//判断是否包含中文
                if (station.find()) {//包含中文
                    queryWrapper = queryWrapper.like("ht.name", dto.getKeyWord());
                } else {//简拼
                    queryWrapper = queryWrapper.like("ht.logogram", dto.getKeyWord());
                }
            }
            if (!CollectionUtils.isEmpty(systemIds)) {
                queryWrapper.in("hss.id", systemIds);
            }
            List<CommonTree> commonTrees = heatSourceService.sourceSystemTree(queryWrapper);
            return Response.success(commonTrees);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }

}
