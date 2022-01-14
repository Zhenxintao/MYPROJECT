package com.bmts.heating.bussiness.baseInformation.app.joggle.station;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.AreaManagerJoggle;
import com.bmts.heating.bussiness.baseInformation.app.utils.ListUtil;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.HeatCabinetResponse;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationInfo;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.basement.model.db.response.station.HeatTransferStationInfoResponse;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "换热站基础信息")
@RestController
@RequestMapping("/heatTransferStation")
@Slf4j
public class HeatTransferStationJoggle {

    @Autowired
    private HeatTransferStationService heatTransferStationService;
    @Autowired
    private HeatSourceService heatSourceService;
    @Autowired
    private HeatCabinetService heatCabinetService;
    @Autowired
    private AuthrityService authrityService;
    @Autowired
    private HeatSystemService heatSystemService;
    @Autowired
    private AreaManagerJoggle areaManagerJoggle;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Response query(@RequestBody HeatTransferStationDto dto) {
        Page<HeatTransferStationResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            stationIds = authrityService.queryStations(dto.getUserId()).getStations();
            if (CollectionUtils.isEmpty(stationIds)) {
                return Response.success(page);
            }
        }

        try {
            QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
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
            if (dto.getStatus() != null)
                queryWrapper.eq("ht.status", dto.getStatus());
            if (dto.getOrganizationId() != null)
                queryWrapper.eq("ho.id", dto.getOrganizationId());
            if (dto.getHeatSourceId() != null)
                queryWrapper.eq("hs.id", dto.getHeatSourceId());
            if (!CollectionUtils.isEmpty(dto.getBuildType()))
                queryWrapper = queryWrapper.in("ht.buildType", dto.getBuildType());
            if (ListUtil.isValid(dto.getInsulationConstruction()))
                queryWrapper.in("ht.insulationConstruction", dto.getInsulationConstruction());
            if (ListUtil.isValid(dto.getPayType()))
                queryWrapper.in("ht.payType", dto.getPayType());
            if (ListUtil.isValid(dto.getHeatType()))
                queryWrapper.in("ht.heatType", dto.getHeatType());
            if (ListUtil.isValid(dto.getManageType()))
                queryWrapper = queryWrapper.in("ht.manageType", dto.getManageType());
            if (ListUtil.isValid(dto.getStationType()))
                queryWrapper = queryWrapper.in("ht.StationType", dto.getStationType());
            queryWrapper = queryWrapper.eq("ht.deleteFlag", false);
            return Response.success(heatTransferStationService.queryStationPage(page, queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }


    @ApiOperation("查询热力站是否重名")
    @PostMapping("/repeat")
    public Response repeat(@RequestBody HeatTransferStation info) {
        if (info.getName() != null && info.getHeatOrganizationId() != null && info.getCode() != null) {
            QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", info.getName());
            queryWrapper.eq("heatOrganizationId", info.getHeatOrganizationId());
            HeatTransferStation one = heatTransferStationService.getOne(queryWrapper);
            HeatTransferStation codeOne = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getCode, info.getCode()));
            if (info.getId() != null) {
                // 编辑站
                if (one != null && !Objects.equals(one.getId(), info.getId())) {
                    return Response.fail("所选公司下该站名称已经存在！请更改站名！");
                }
                if (codeOne != null && !Objects.equals(codeOne.getId(), info.getId())) {
                    return Response.fail("该热力站编号已经存在！请重新填写编号！");
                }
                return Response.success();
            } else {
                // 新增站
                if (one != null) {
                    return Response.fail("所选公司下该站名称已经存在！请更改站名！");
                }
                if (codeOne != null) {
                    return Response.fail("该热力站编号已经存在！请重新填写编号！");
                }
                return Response.success();
            }

        }
        return Response.fail("站名,所属公司,编号是必填项！");
    }

    @ApiOperation("新增")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addStation(@RequestBody HeatTransferStation heatTransferStation) {
        heatTransferStation.setCreateTime(LocalDateTime.now());
        String JP = PinYinUtils.toFirstChar(heatTransferStation.getName());//首字母简拼
        heatTransferStation.setLogogram(JP);
        if (StringUtils.isNotBlank(heatTransferStation.getCode())) {
            heatTransferStation.setSyncNumber(Integer.parseInt(heatTransferStation.getCode()));
        }
        if (heatTransferStationService.save(heatTransferStation)) {
            //更新面积
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatSystem.level());
            dto.setRelevanceId(heatTransferStation.getId());
            dto.setArea(heatTransferStation.getHeatArea());
            return areaManagerJoggle.update(dto);
        }
        return Response.fail();
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateStation(@RequestBody HeatTransferStation station) {
        String JP = PinYinUtils.toFirstChar(station.getName());//首字母简拼
        station.setLogogram(JP);
        station.setUpdateTime(LocalDateTime.now());
        if (StringUtils.isNotBlank(station.getCode())) {
            station.setSyncNumber(Integer.parseInt(station.getCode()));
        }
        return heatTransferStationService.updateById(station) ? Response.success() : Response.fail();
    }

    @ApiOperation("单站详细信息-编辑时使用")
    @GetMapping("/{id}")
    public Response stationInfo(@PathVariable Integer id) {
        return id != null ? Response.success(heatTransferStationService.getById(id)) : Response.fail();
    }

    @ApiOperation("删除")
    @DeleteMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delete(@RequestParam int id) {
        // 只是简单删除单站，没有删除单站下面的控制柜、系统等数据，后期要加上，把关联数据都要删除
        HeatTransferStation heatStation = new HeatTransferStation();
        heatStation.setId(id);
        // 假删除操作，把删除标识的字符值改为 1 ，表示删除
        heatStation.setDeleteFlag(true);
        return heatTransferStationService.updateById(heatStation) ? Response.success() : Response.fail();
    }

    @ApiOperation("查询换热站详细信息-关联热源、控制柜的详细信息")
    @GetMapping
    public Response info(@RequestParam int id) {
        Response response = Response.fail();
        // 查询单站的详细信息
        HeatTransferStation heatStationById = heatTransferStationService.getById(id);
        if (heatStationById == null) {
            response.setMsg("没有查询到该换热站的信息！");
            return response;
        }
        // 查询单站所属热源的详细信息
        HeatSource heatSourceById = heatSourceService.getById(heatStationById.getHeatSourceId());
        // 查询单站下面所有的控制柜信息
        QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hc.heatTransferStationId", id);
        List<HeatCabinetResponse> listHeatCabinet = heatCabinetService.queryCabinet(queryWrapper);
        // 组装返回实体类
        HeatTransferStationInfoResponse info = new HeatTransferStationInfoResponse();
        info.setHeatSourceEntity(heatSourceById);
        info.setHeatStationEntity(heatStationById);
        info.setHeatCabinetList(listHeatCabinet);
        return Response.success(info);
    }

    @ApiOperation("冻结或解冻")
    @PostMapping("/freeze")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response freeze(@RequestBody FreezeDto dto) {
        HeatTransferStation heatStation = new HeatTransferStation();
        heatStation.setId(dto.getId());
        // `status`  'true 解冻，false 冻结',
        heatStation.setStatus(dto.getStatus());
        if (StringUtils.isNotBlank(dto.getName())) {
            heatStation.setUpdateUser(dto.getName());
        }
        heatStation.setUpdateTime(LocalDateTime.now());
        return heatTransferStationService.updateById(heatStation) ? Response.success() : Response.fail();
    }

    @ApiOperation("分页查询-关联控制柜信息")
    @PostMapping("/pageInfo")
    public Response pageInfo(@RequestBody HeatTransferStationDto dto) {
        Page<HeatTransferStationInfo> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        Set<Integer> stationIds = new HashSet<>();
        // 超级管理员
        if (dto.getUserId() != -1) {
            stationIds = authrityService.queryStations(dto.getUserId()).getStations();
            if (CollectionUtils.isEmpty(stationIds)) {
                return Response.success(page);
            }
        }
        try {
            QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
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
            if (dto.getStatus() != null) {
                queryWrapper.eq("ht.status", dto.getStatus());
            }
            queryWrapper.eq("ht.deleteFlag", false);
            IPage<HeatTransferStationInfo> stationInfoPage = heatTransferStationService.pageStation(page, queryWrapper);
            List<HeatTransferStationInfo> records = stationInfoPage.getRecords();
            for (HeatTransferStationInfo info : records) {
                // 查询单站下面所有的控制柜信息
                QueryWrapper<HeatCabinet> wrapperCabinet = new QueryWrapper<>();
                wrapperCabinet.eq("hc.heatTransferStationId", info.getId());
                List<HeatCabinetResponse> listHeatCabinet = heatCabinetService.queryCabinet(wrapperCabinet);
                for (HeatCabinetResponse heatCabinet : listHeatCabinet) {
                    // 查询单个控制柜下面的机组信息
                    List<HeatSystem> listHeatSystem = heatSystemService.list(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getHeatCabinetId, heatCabinet.getId()));
                    heatCabinet.setListSystem(listHeatSystem);
                }
                info.setHeatCabinetList(listHeatCabinet);
            }
            return Response.success(stationInfoPage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Response.fail();
        }
    }

    @ApiOperation(value = "根据id排序")
    @PutMapping("/sort")
    public Response updateSort(@RequestBody List<String> list){
        try {
            List<Integer> ids = new ArrayList<>();
            Map<Integer,Integer> maps = new HashMap<>();
            for (String s : list) {
                //这里应该是把每一个json转成map
                Map<String,String> map = (Map<String, String>) JSONObject.parse(s);
                for (Map.Entry<String, String> integerIntegerEntry : map.entrySet()) {
                    maps.put(Integer.valueOf(integerIntegerEntry.getKey()),Integer.valueOf(integerIntegerEntry.getValue()));
                }
            }
            for (Map.Entry<Integer, Integer> integerIntegerEntry : maps.entrySet()) {
                ids.add(integerIntegerEntry.getKey());
            }
            List<HeatTransferStation> heatTransferStations = heatTransferStationService.listByIds(ids);
            for (HeatTransferStation  heatTransferStation : heatTransferStations) {
                heatTransferStation.setSort(maps.get(heatTransferStation.getId()));
            }
            boolean b = heatTransferStationService.updateBatchById(heatTransferStations);
            if (b){
                return Response.success();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("修改出错");
        }
        return Response.fail();
    }
}
