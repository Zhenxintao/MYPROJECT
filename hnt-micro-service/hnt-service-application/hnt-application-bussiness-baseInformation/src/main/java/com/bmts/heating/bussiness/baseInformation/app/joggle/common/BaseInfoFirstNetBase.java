package com.bmts.heating.bussiness.baseInformation.app.joggle.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.HeatOrganizationJoggle;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryBaseDataDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "获取网~系统基础信息")
@RestController
@RequestMapping("/common")
@Slf4j
public class BaseInfoFirstNetBase {

    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;

    @Autowired
    private AuthrityService authrityService;

    @Autowired
    private WebPageConfigService webPageConfigService;

    @Autowired
    private CommonHeatSeasonService commonHeatSeasonService;

    @Autowired
    private HeatNetService heatNetService;

    @ApiOperation("获取换热站或热源基础信息")
    @PostMapping("/queryStationBaseData")
    public List<StationFirstNetBaseView> queryStationBaseData(@RequestBody QueryBaseDataDto dto) {
        QueryWrapper<StationFirstNetBaseView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", true);
        queryWrapper.in("heatSystemId", dto.getSystemIds());
        if (dto.getSystemNumber() == -1) {
            return stationFirstNetBaseViewService.list();
        } else if (dto.getSystemNumber() == 0) {
            queryWrapper.eq("number", 0);
        } else {
            queryWrapper.ne("number", 0);
        }
        return stationFirstNetBaseViewService.list(queryWrapper);
    }

    @ApiOperation("获取热源基础信息")
    @PostMapping("/querySourceBaseData")
    public List<SourceFirstNetBaseView> querySourceBaseData(@RequestBody QueryBaseDataDto dto) {
        QueryWrapper<SourceFirstNetBaseView> queryWrapperSource = new QueryWrapper<>();
        if (dto.getSystemNumber() != -1) {
            queryWrapperSource.in("heatSystemId", dto.getSystemIds());
        }
        return sourceFirstNetBaseViewService.list(queryWrapperSource);
    }

    @ApiOperation("获取基础信息-站")
    @GetMapping("/firstNetBase")
    public List<StationFirstNetBaseView> queryFirstNetBase() throws MicroException {
        return stationFirstNetBaseViewService.list();
    }

    @ApiOperation("获取基础信息-站-0系统")
    @GetMapping("/firstNetBase/first")
    public List<StationFirstNetBaseView> queryFirstNetBaseFirst() throws MicroException {
        LambdaQueryWrapper<StationFirstNetBaseView> eq = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getNumber, 0)
                .eq(StationFirstNetBaseView::getStatus, true);
        return stationFirstNetBaseViewService.list(eq);
    }

    //新增基础机组一系统信息
    @ApiOperation("获取基础信息-站-1系统")
    @GetMapping("/firstNetBase/firstSystemOne")
    public List<StationFirstNetBaseView> queryFirstNetBaseFirstSystemOne() throws MicroException {
        LambdaQueryWrapper<StationFirstNetBaseView> eq = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getNumber, 1)
                .eq(StationFirstNetBaseView::getStatus, true);
        return stationFirstNetBaseViewService.list(eq);
    }

//    @ApiOperation("获取基础信息-站-0系统没有就获取一次侧")
//    @GetMapping("/firstNetBase/firstOrZero")
//    public List<StationFirstNetBaseView> queryFirstNetBaseStationOrFirst() {
//        LambdaQueryWrapper<StationFirstNetBaseView> wrapper0 = Wrappers.<StationFirstNetBaseView>lambdaQuery()
//                .eq(StationFirstNetBaseView::getNumber, 0)
//                .eq(StationFirstNetBaseView::getStatus,true);
//        List<StationFirstNetBaseView> list0 = stationFirstNetBaseViewService.list(wrapper0);
//        List<Integer> collect = list0.stream().map(StationFirstNetBaseView::getHeatTransferStationId).collect(Collectors.toList());
//        LambdaQueryWrapper<StationFirstNetBaseView> wrapper1 = Wrappers.<StationFirstNetBaseView>lambdaQuery()
//                .eq(StationFirstNetBaseView::getNumber, 1)
//                .notIn(StationFirstNetBaseView::getHeatTransferStationId,collect)
//                .eq(StationFirstNetBaseView::getStatus,true);
//        List<StationFirstNetBaseView> list1 = stationFirstNetBaseViewService.list(wrapper1);
//        if (CollectionUtils.isNotEmpty(list0)){
//            list1.addAll(list0);
//        }
//        return list1;
//    }

    @Autowired
    private HeatOrganizationJoggle joggle;


    @ApiOperation("获取基础信息-站-一次侧  公司id")
    @GetMapping("/firstNetBase/first/{id}")
    public List<StationFirstNetBaseView> queryFirstNetBaseFirstByCompanyId(@PathVariable Integer id) throws MicroException {
        List<Integer> orgIds = new ArrayList<>();
        orgIds.add(id);
        Set<Integer> integers = joggle.queryByParentOrgIds(orgIds);
        LambdaQueryWrapper<StationFirstNetBaseView> eq = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getNumber, 0)
                .eq(StationFirstNetBaseView::getStatus, true)
                .in(StationFirstNetBaseView::getHeatStationOrgId, integers);
        return stationFirstNetBaseViewService.list(eq);
    }

    //新增基础机组一系统信息
    @ApiOperation("获取基础信息-站-一次侧  公司id")
    @GetMapping("/firstNetBase/firstSystemOne/{id}")
    public List<StationFirstNetBaseView> queryFirstNetBaseFirstSystemOneByCompanyId(@PathVariable Integer id) throws MicroException {
        List<Integer> orgIds = new ArrayList<>();
        orgIds.add(id);
        Set<Integer> integers = joggle.queryByParentOrgIds(orgIds);
        LambdaQueryWrapper<StationFirstNetBaseView> eq = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getNumber, 1)
                .eq(StationFirstNetBaseView::getStatus, true)
                .in(StationFirstNetBaseView::getHeatStationOrgId, integers);
        return stationFirstNetBaseViewService.list(eq);
    }

    @ApiOperation("获取缓存基础信息-源")
    @GetMapping("/sourceFirstNetBase")
    public List<SourceFirstNetBaseView> queryFirstNetBaseSource() throws MicroException {
        return sourceFirstNetBaseViewService.list();
    }

    @ApiOperation("据权限获取查询站Id")
    @GetMapping("/station/user/{id}")
    public Set<Integer> getStationIdsByUserId(@PathVariable Integer id) {
        if (id > 0) {
            return authrityService.queryStations(id).getStations();
        }
        return null;
    }

    @ApiOperation("据权限获取查询站")
    @GetMapping("/firstNetBase/user/{id}")
    public List<StationFirstNetBaseView> queryFirstNetBaseByUserId(@PathVariable Integer id) throws MicroException {
        List<StationFirstNetBaseView> firstNetBaseList = stationFirstNetBaseViewService.list();
        HashSet<Integer> stations = authrityService.queryStations(id).getStations();
        return firstNetBaseList.stream().filter(e -> stations.contains(e.getHeatTransferStationId())).collect(Collectors.toList());
    }

    /**
     * 获取网-源
     *
     * @param id netId
     * @return NetSources
     */
    @ApiOperation("获取网-源")
    @GetMapping("/netJoinSource/{id}")
    public List<NetSource> netJoinSource(@PathVariable Integer id) {
        QueryWrapper<NetSource> queryWrapper = new QueryWrapper<>();
        if (id != -1) {
            queryWrapper.eq("hn.id", id);
        }
        return heatNetService.sourceNet(queryWrapper);
    }

    /**
     * 获取水电热能耗点位信息
     */
    @ApiOperation("获取水电热能耗点位信息")
    @GetMapping("/getEnergyPointConfig")
    private GetEnergyPointConfig getEnergyPointConfig() {
        QueryWrapper<WebPageConfig> webPageConfigQueryWrapper = new QueryWrapper<>();
        webPageConfigQueryWrapper.eq("configKey", "energyPointConfig");
        WebPageConfig webPageConfig = webPageConfigService.getOne(webPageConfigQueryWrapper);
        JSONObject jsonObject = JSONObject.parseObject(webPageConfig.getJsonConfig());
        GetEnergyPointConfig energyPointConfig = JSONArray.toJavaObject(jsonObject, GetEnergyPointConfig.class);
        return energyPointConfig;
    }

    @ApiOperation("根据权限获取热源信息")
    @GetMapping("/querySourceInfoByUserId")
    private List<SourceFirstNetBaseView> querySourceInfoByUserId(@RequestParam Integer id) {
        List<SourceFirstNetBaseView> list = new ArrayList<>();
        if (id==-1){
            list = sourceFirstNetBaseViewService.list();
        }else {
            List<Integer> ids = authrityService.queryRelevanceList(id, TreeLevel.HeatSource.level());
            if (ids.size() > 0) {
                QueryWrapper<SourceFirstNetBaseView> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("heatSystemId", ids);
                list = sourceFirstNetBaseViewService.list(queryWrapper);
            }
        }
        return list;
    }

    @ApiOperation("获取当前日期供暖季信息")
    @GetMapping("/getPresentHeatSeason")
    private Map getPresentHeatSeason() {
        QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
        Map com = commonHeatSeasonService.getMap(queryCommonHeatSeason);
        return com;
    }
}
