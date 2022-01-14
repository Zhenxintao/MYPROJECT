package com.bmts.heating.bussiness.baseInformation.app.joggle.station;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.bussiness.baseInformation.app.converter.HeatTransferStationConverter;
import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.AreaManagerJoggle;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.CabinetPoint;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.HeatCabinetMapper;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.mapper.HeatTransferStationMapper;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyNewDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopySourceDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CopyStationJoggle
 * @Author naming
 * @Date 2020/11/19 20:01
 **/
@Api(tags = "复制站")
@Slf4j
@RestController
@RequestMapping("/copy")
public class CopyStationJoggle {


    @Autowired
    HeatCabinetService heatCabinetService;
    @Autowired
    HeatSystemService heatSystemService;
    @Autowired
    PointConfigService pointConfigService;
    @Autowired
    PointConfigMapper pointConfigMapper;
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    HeatTransferStationMapper heatTransferStationMapper;
    @Autowired
    HeatTransferStationService heatTransferStationService;
    @Autowired
    HeatSystemMapper heatSystemMapper;
    @Autowired
    HeatCabinetMapper heatCabinetMapper;
    @Autowired
    RedisPointService redisPointService;
    @Autowired
    private AreaManagerJoggle areaManagerJoggle;


    @Autowired
    private CraftService craftService;

    private String userName;
    private Integer userId;


    /**
     * 复制工艺图到 热力站
     */
    private boolean copyCraft(CopyDto dto) {
        // 热力站的id
        Integer copySourceId = dto.getCopySourceId();
        if (copySourceId != null && dto.getTargetIds().size() > 0) {
            Craft infoCraft = craftService.getOne(Wrappers.<Craft>lambdaQuery().eq(Craft::getRelevanceId, copySourceId).eq(Craft::getType, 1));
            if (infoCraft != null) {
                // 进行复制
                craftService.remove(Wrappers.<Craft>lambdaQuery().eq(Craft::getType, 1).in(Craft::getRelevanceId, dto.getTargetIds()));
                List<Craft> addList = new ArrayList<>();
                dto.getTargetIds().forEach(x -> {
                    Craft craft = new Craft();
                    craft.setContent(infoCraft.getContent());
                    craft.setRelevanceId(x);
                    craft.setType(1);
                    addList.add(craft);
                });
                return craftService.saveBatch(addList);
            }
        }
        return true;
    }


    @ApiOperation("复制到现有站点")
    @PostMapping("/exist")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response toExist(@RequestBody CopyDto dto) {
        if (CollectionUtils.isEmpty(dto.getTargetIds())) {
            return Response.fail("请先选择需要复制的站！");
        }
        // 校验自己复制给自己
        for (Integer copyToStationId : dto.getTargetIds()) {
            for (CopySourceDto sourceDto : dto.getCopySourceDtos()) {
                QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", sourceDto.getHeatCabinetId());
                HeatCabinet heatCabinet = heatCabinetService.getOne(queryWrapper);
                if (Objects.equals(copyToStationId, heatCabinet.getHeatTransferStationId())) {
                    return Response.fail("复制源和现有站点有相同站，请重新选择现有站点！");
                }
            }
        }
        // 批量删除站下面的控制柜和系统
        if (!clear(dto.getTargetIds())) {
            return Response.fail();
        }
        Map<Integer, String> map = new HashMap<>(dto.getTargetIds().size());
        this.userName = !StringUtils.isNullOrEmpty(dto.getUserName()) ? dto.getUserName() : "";
        this.userId = dto.getUserId() != 0 ? dto.getUserId() : null;
        for (Integer heatTransferStationId : dto.getTargetIds()) {
            for (CopySourceDto copySourceDto : dto.getCopySourceDtos()) {
                //1.查询控制柜 信息 2.控制柜下系统信息 3.系统下 采集量或者控制量或者全集 4.复制到新站
                QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(HeatCabinet::getId, copySourceDto.getHeatCabinetId());
                HeatCabinet heatCabinet = heatCabinetService.getOne(queryWrapper);
                int cabitNetId = copyTo(heatCabinet, heatTransferStationId);
                if (cabitNetId == 0) {
                    //复制控制柜复制出错
                    map.put(heatTransferStationId, "复制控制柜信息出错，" + heatCabinet.getName());
                    continue;
                }
                if (copySourceDto.getHeatSystemIds().size() == 0) {
                    map.put(heatTransferStationId, "复制成功");
                }
                for (Integer heatSystemId : copySourceDto.getHeatSystemIds()) {
                    QueryWrapper<HeatSystem> systemQuery = new QueryWrapper<>();
                    systemQuery.lambda().eq(HeatSystem::getId, heatSystemId);
                    HeatSystem heatSystem = heatSystemService.getOne(systemQuery);
                    int systemId = copyTo(heatSystem, cabitNetId);
                    if (systemId == 0) {
                        map.put(heatTransferStationId, "复制系统信息出错，" + heatSystem.getName());
                        //复制系统信息出错
                        continue;
                    }
                    if (dto.getType() != null){
                        if (!copyPointToCollect(systemId, heatSystemId, dto.getType())) {
                            //复制采集点信息出错
                            map.put(heatTransferStationId, "复制采集点信息出错，" + heatSystem.getName());
                            continue;
                        }
                    }


                }
            }

        }
        // 复制工艺图
        copyCraft(dto);
        return Response.success(map);
    }

//    @ApiModelProperty("验证目标热力站合法性")
//    @PostMapping
//    public Map<Integer, String> validate(@RequestBody CopyDto dto) {
//        Map<Integer, String> maps = new HashMap<>();
//        List<String> cabiNames = dto.getCopySourceDtos().stream().map(x -> x.getHeatCabinetName()).collect(Collectors.toList());
//        List<Integer> systemIds = dto.getCopySourceDtos().stream().flatMap(x -> x.getHeatSystemIds().stream()).collect(Collectors.toList());
//        for (Integer heatTransferStationId : dto.getHeatTransferStationIds()) {
//            QueryWrapper<CabinetPoint> queryWrapper = new QueryWrapper();
//            queryWrapper.eq("station.id", heatTransferStationId);
//            List<CabinetPoint> cabinetPoints = heatTransferStationMapper.queryCollectPointAndCabinet(queryWrapper);
//            List<Integer> existPointStandardIds = cabinetPoints.stream().map(x -> x.getPointStandardId()).collect(Collectors.toList());
//            if (cabinetPoints != null && cabinetPoints.size() > 0) {
//                if (cabinetPoints.stream().filter(x -> cabiNames.contains(x.getCabiNetName())).findAny().isPresent()) {
//                    maps.put(heatTransferStationId, "验证失败，已存在控制柜请验证");
//                    continue;
//                } else {
//                    try {
//                        //验证点信息
//                        validate(maps, systemIds, existPointStandardIds, dto.getType(), heatTransferStationId);
//                    } catch (Exception e) {
//                        log.error("validate point cause exception {}", e);
//                        maps.put(heatTransferStationId, "验证失败");
//                    }
//
//                }
//            } else maps.put(heatTransferStationId, "验证通过");
//
//        }
//        return maps;
//    }

//    /**
//     * @param maps
//     * @param systemsIds            dto中的系统id集合
//     * @param existPointStandardIds 已经存在的点id
//     * @param type                  1.采集 2.控制
//     */
//    private void validate(Map<Integer, String> maps, List<Integer> systemsIds, List<Integer> existPointStandardIds, int type, int stationId) {
//
//        switch (type) {
//            case 1:
//                QueryWrapper queryWrapper = new QueryWrapper<PointCollectConfig>();
//                queryWrapper.in("relevanceId", systemsIds);
//                List<PointCollectConfig> list = pointConfigService.list(queryWrapper);
//                if (list != null && list.size() > 0) {
//                    List<Integer> pointIds = list.stream().map(x -> x.getPointStandardId()).collect(Collectors.toList());
//                    existPointStandardIds.retainAll(pointIds);
//                    if (existPointStandardIds.size() > 0)
//                        maps.put(stationId, "验证失败,参量重复");
//                    else maps.put(stationId, "验证通过");
//                } else maps.put(stationId, "验证通过");
//
//                break;
//            case 2:
//                QueryWrapper queryControlWrapper = new QueryWrapper<PointControlConfig>();
//                queryControlWrapper.in("relevanceId", systemsIds);
//                List<PointCollectConfig> listControl = pointConfigService.list(queryControlWrapper);
//                if (listControl != null && listControl.size() > 0) {
//                    List<Integer> pointIds = listControl.stream().map(x -> x.getPointStandardId()).collect(Collectors.toList());
//                    existPointStandardIds.retainAll(pointIds);
//                    if (existPointStandardIds.size() > 0)
//                        maps.put(stationId, "验证失败,参量重复");
//                    else maps.put(stationId, "验证通过");
//                } else maps.put(stationId, "验证通过");
//
//                break;
//        }
//    }

    private int copyTo(HeatCabinet info, int stationId) {
        try {
            info.setHeatTransferStationId(stationId);
            info.setCreateTime(LocalDateTime.now());
            info.setCreateUser(userName);
            info.setUpdateTime(LocalDateTime.now());
            info.setUpdateUser("");
            if (userId != null) {
                info.setUserId(userId);
            }
            return heatCabinetService.save(info) ? info.getId() : 0;
        } catch (Exception e) {
            log.error("copy heatCabinet cause exception {0}", e);
            return 0;
        }

    }

    private int copyTo(HeatSystem info, int cabinetId) {
        try {
            info.setHeatCabinetId(cabinetId);
            info.setCreateTime(LocalDateTime.now());
            info.setCreateUser(userName);
            info.setUpdateTime(LocalDateTime.now());
            info.setUpdateUser("");
            if (userId != null) {
                info.setUserId(userId);
            }
            if(heatSystemService.save(info)){
                AreaManagerDto dto = new AreaManagerDto();
                dto.setLevel(TreeLevel.HeatSystem.level());
                dto.setRelevanceId(info.getId());
                dto.setArea(info.getHeatArea());
                areaManagerJoggle.update(dto);
                return info.getId();
            }else{
                return 0;
            }
        } catch (Exception e) {
            log.error("copy heatSystem cause exception {0}", e);
            return 0;
        }
    }


    private boolean copyPointToCollect(int systemId, int sourceSystemId, Integer type) {
        try {
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pc.relevanceId", sourceSystemId)
                    .eq("pc.deleteFlag", false);
            if (type != 0) {
                queryWrapper.eq("ps.pointConfig", type);
            }
            List<PointConfig> list = pointConfigMapper.queryList(queryWrapper);
            for (PointConfig info : list) {
                info.setRelevanceId(systemId);
                info.setCreateTime(LocalDateTime.now());
                info.setCreateUser(userName);
                info.setUpdateTime(LocalDateTime.now());
                info.setUpdateUser("");
                info.setLevel(TreeLevel.HeatSystem.level());

            }
            if (!CollectionUtils.isEmpty(list)) {
                if (pointConfigService.saveBatch(list)) {
                    redisPointService.saveOrUpdateByrelevanceId(systemId, TreeLevel.HeatSystem.level());
                    return true;
                }
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("saveBatch pointCollectConfig cause exception {}", e);
            return false;
        }
    }

    @Autowired
    private StationFirstNetBaseViewService stationFirstNetBaseViewService;

    @ApiModelProperty("读取复制源控制柜及机组信息")
    @GetMapping
    public Response querySource(@RequestParam int heatStationId) throws MicroException {
        List<StationFirstNetBaseView> firstNetBases = stationFirstNetBaseViewService.list();
        List<StationFirstNetBaseView> bases = firstNetBases.stream().filter(x -> x.getHeatTransferStationId() == heatStationId).distinct().collect(Collectors.toList());
        Map<Integer, List<StationFirstNetBaseView>> collect = bases.stream().collect(Collectors.groupingBy(x -> x.getHeatCabinetId()));

        List<Map<String, Object>> results = new ArrayList<>(collect.size());
        collect.forEach((k, v) -> {
            Map<String, Object> resultItem = new HashMap<>();
            StationFirstNetBaseView firstNetBase = bases.stream().filter(x -> x.getHeatCabinetId() == k).findFirst().orElse(null);
            resultItem.put("heatCabinetName", firstNetBase.getHeatCabinetName());
            resultItem.put("heatCabinetId", firstNetBase.getHeatCabinetId());
            resultItem.put("children", v);
            results.add(resultItem);
        });
        return Response.success(results);
    }

    @Autowired
    HeatTransferStationConverter heatTransferStationConverter;

    @ApiOperation("复制到新建站点")
    @PostMapping("/copyNew")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response copyNew(@RequestBody CopyNewDto dto) {
        this.userName = !StringUtils.isNullOrEmpty(dto.getUserName()) ? dto.getUserName() : "";
        this.userId = dto.getUserId() != 0 ? dto.getUserId() : null;
        // 热力站信息
        HeatTransferStation heatTransferStation = HeatTransferStationConverter.INSTANCE.domain2dto(dto.getHeatTransferStation());
        if (heatTransferStation.getName() != null) {
//            QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("name", heatTransferStation.getName());
//            if (heatTransferStationService.count(queryWrapper) > 0) {
//                return Response.fail("该热力站名称已经存在，请重修输入热力站名称！");
//            }
            String JP = PinYinUtils.toFirstChar(heatTransferStation.getName());//首字母简拼
            heatTransferStation.setLogogram(JP);
            if (userId != null) {
                heatTransferStation.setUserId(userId);
            }
            heatTransferStation.setCreateUser(userName);
            heatTransferStation.setCreateTime(LocalDateTime.now());
            if (heatTransferStationService.save(heatTransferStation)) {

                AreaManagerDto dtoArea = new AreaManagerDto();
                dtoArea.setLevel(TreeLevel.HeatStation.level());
                dtoArea.setRelevanceId(heatTransferStation.getId());
                dtoArea.setArea(heatTransferStation.getHeatArea());
                areaManagerJoggle.update(dtoArea);

                List<Integer> stationId = new ArrayList<>(1);
                stationId.add(heatTransferStation.getId());
                dto.setTargetIds(stationId);
                if(dto.getCopySourceId() == null){
                    return Response.success();
                }
                return toExist(dto);

            }
            return Response.fail("保存热力站失败");
        } else {
            return Response.fail();
        }
    }


    private boolean clear(List<Integer> stationId) {
        try {
            List<CabinetPoint> cabinetPoints =
                    heatTransferStationMapper.queryCollectPointAndCabinet
                            (new QueryWrapper<PointCollectConfig>().in("hts.id", stationId))
//                                    .eq("pc.level",TreeLevel.HeatSystem.level())
                    ;
            if (cabinetPoints != null && cabinetPoints.size() > 0) {
                List<Integer> heatSystemIds = cabinetPoints.stream().filter(x -> x.getHeatSystemId() != 0).map(x -> x.getHeatSystemId()).distinct().collect(Collectors.toList());
                if (heatSystemIds.size() > 0) {
                    QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(PointConfig::getLevel, TreeLevel.HeatSystem.level()).in(PointConfig::getRelevanceId, heatSystemIds);
                    pointConfigMapper.delete(queryWrapper);
                }

            }

            deal(cabinetPoints);
            return true;
        } catch (Exception e) {
            log.error("clear station config cause exception {}", e);
            return false;
        }

    }

    private void deal(List<CabinetPoint> cabinetPoints) {
        List<Integer> cabiNetIds = cabinetPoints.stream().map(CabinetPoint::getId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (cabiNetIds != null && cabiNetIds.size() > 0)
            heatCabinetMapper.deleteBatchIds(cabiNetIds);
        List<Integer> systemIds = cabinetPoints.stream().map(CabinetPoint::getHeatSystemId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (systemIds != null && systemIds.size() > 0)
            heatSystemMapper.deleteBatchIds(systemIds);
    }

//    @ApiOperation("复制到新建站点")
//    @PostMapping("/copyNew")
//    @ClearCache(key = RedisKeys.FIRST_BASE)
//    public Response copyNew(@RequestBody CopyNewDto dto) {
//        this.userName = !StringUtils.isNullOrEmpty(dto.getUserName()) ? dto.getUserName() : "";
//        // 热力站信息
//        HeatTransferStation heatTransferStation = dto.getHeatTransferStation();
//        if (heatTransferStation.getName() != null) {
//            QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("name", heatTransferStation.getName());
//            if (heatTransferStationService.count(queryWrapper) > 0) {
//                return Response.fail("该热力站名称已经存在，请重修输入热力站名称！");
//            }
//        }
//        heatTransferStation.setCreateUser(userName);
//        heatTransferStation.setCreateTime(LocalDateTime.now());
//        if (heatTransferStationService.save(heatTransferStation)) {
//            Map<Integer, String> returnMap = new HashMap<>();
//            // 获取  热力站 id
//            Integer heatTransferStationId = heatTransferStation.getId();
//            // 复制控制柜信息
//            List<CopyCabinetDto> listCabinet = dto.getListCabinet();
//
//            for (CopyCabinetDto cabinetDto : listCabinet) {
//                HeatCabinet heatCabinet = new HeatCabinet();
//                BeanUtils.copyProperties(cabinetDto, heatCabinet);
//                int cabitNetId = copyTo(heatCabinet, heatTransferStationId);
//                if (cabitNetId == 0) {
//                    //复制控制柜复制出错
//                    returnMap.put(heatTransferStationId, "复制控制柜信息出错，" + cabinetDto.getName());
//                    continue;
//                } else {
//                    for (HeatSystem heatSystem : cabinetDto.getListHeatSystem()) {
//
//                        // 复制机组系统信息
//                        int newSystemId = copyTo(heatSystem, cabitNetId);
//                        if (newSystemId == 0) {
//                            returnMap.put(heatTransferStationId, "复制系统信息出错，" + heatSystem.getName());
//                            //复制系统信息出错
//                            continue;
//                        }
//                        if (heatSystem.getId() != null && heatSystem.getId() > 0) {
//                            int sourceSystemId = heatSystem.getId();
//                            switch (dto.getType()) {
//                                case 1:
//                                    if (!copyPointToCollect(newSystemId, sourceSystemId)) {
//                                        //复制采集点信息出错
//                                        returnMap.put(heatTransferStationId, "复制采集点信息出错，" + heatSystem.getName());
//                                        continue;
//                                    }
//                                    break;
//                                case 2:
//                                    if (!copyPointToControl(newSystemId, sourceSystemId)) {
//                                        //复制控制量出错
//                                        returnMap.put(heatTransferStationId, "复制控制量出错，" + heatSystem.getName());
//                                        continue;
//                                    }
//                                case 0:
//                                    if (!copyPointToCollect(newSystemId, sourceSystemId)) {
//                                        //复制采集点信息出错
//                                        returnMap.put(heatTransferStationId, "复制采集点信息出错，" + heatSystem.getName());
//
//                                    }
//                                    if (!copyPointToControl(newSystemId, sourceSystemId)) {
//                                        //复制控制量出错
//                                        returnMap.put(heatTransferStationId, "复制控制量出错，" + heatSystem.getName());
//                                    }
//                                    break;
//                            }
//                        }
//
//
//                    }
//
//
//                }
//            }
//            return Response.success(returnMap);
//        }
//        return Response.fail("新建热力站信息失败！");
//    }
}

























