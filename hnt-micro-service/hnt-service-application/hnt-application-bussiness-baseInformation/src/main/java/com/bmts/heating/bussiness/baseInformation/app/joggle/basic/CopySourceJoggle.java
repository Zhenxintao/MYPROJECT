package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.bussiness.baseInformation.app.converter.HeatSourceConverter;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.CabinetPoint;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.HeatCabinetMapper;
import com.bmts.heating.commons.db.mapper.HeatSourceMapper;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyHeatSourceDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopySourceDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
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
 * @ClassName CopySourceJoggle
 * @Description
 * @Author
 * @Date 2020/11/19 20:01
 **/
@Api(tags = "复制热源")
@Slf4j
@RestController
@RequestMapping("/copySource")
public class CopySourceJoggle {
    @Autowired
    HeatCabinetService heatCabinetService;
    @Autowired
    HeatSystemService heatSystemService;
    @Autowired
    HeatSourceMapper heatSourceMapper;
    @Autowired
    PointConfigMapper pointConfigMapper;
    @Autowired
    PointConfigService pointConfigService;
    @Autowired
    RedisPointService redisPointService;
    @Autowired
    HeatCabinetMapper heatCabinetMapper;
    @Autowired
    HeatSystemMapper heatSystemMapper;
    @Autowired
    HeatSourceService heatSourceService;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    private CraftService craftService;

    private String userName;
    private Integer userId;

    /**
     * 复制工艺图到热源
     */
    private boolean copyCraft(CopyDto dto) {
        // 热源的id
        Integer copySourceId = dto.getCopySourceId();
        if (copySourceId != null && dto.getTargetIds().size() > 0) {
            Craft infoCraft = craftService.getOne(Wrappers.<Craft>lambdaQuery().eq(Craft::getRelevanceId, copySourceId).eq(Craft::getType, 2));
            if (infoCraft != null) {
                // 进行复制
                craftService.remove(Wrappers.<Craft>lambdaQuery().eq(Craft::getType, 2).in(Craft::getRelevanceId, dto.getTargetIds()));
                List<Craft> addList = new ArrayList<>();
                dto.getTargetIds().forEach(x -> {
                    Craft craft = new Craft();
                    craft.setContent(infoCraft.getContent());
                    craft.setRelevanceId(x);
                    craft.setType(2);
                    addList.add(craft);
                });
                return craftService.saveBatch(addList);
            }
        }
        return true;
    }

    @ApiOperation("复制到现有热源")
    @PostMapping("/exist")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response toExist(@RequestBody CopyDto dto) {
        if (CollectionUtils.isEmpty(dto.getTargetIds())) {
            return Response.fail("请先选择需要复制的热源！");
        }
        // 校验自己复制给自己
        for (Integer copyToSourceId : dto.getTargetIds()) {
            for (CopySourceDto sourceDto : dto.getCopySourceDtos()) {
                QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", sourceDto.getHeatCabinetId());
                HeatCabinet heatCabinet = heatCabinetService.getOne(queryWrapper);
                if (Objects.equals(copyToSourceId, heatCabinet.getHeatSourceId())) {
                    return Response.fail("复制源和现有热源不能相同，请重新选择现有热源！");
                }
            }
        }
        // 批量删除热源下面的控制柜和系统
        if (!clear(dto.getTargetIds())) {
            return Response.fail();
        }
        Map<Integer, String> map = new HashMap<>(dto.getTargetIds().size());
        this.userName = !StringUtils.isNullOrEmpty(dto.getUserName()) ? dto.getUserName() : "";
        this.userId = dto.getUserId() != 0 ? dto.getUserId() : null;
        for (Integer heatPropertyId : dto.getTargetIds()) {
            for (CopySourceDto copySourceDto : dto.getCopySourceDtos()) {
                //1.查询控制柜 信息 2.控制柜下系统信息 3.系统下 采集量或者控制量或者全集 4.复制到新热源
                QueryWrapper<HeatCabinet> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(HeatCabinet::getId, copySourceDto.getHeatCabinetId());
                HeatCabinet heatCabinet = heatCabinetService.getOne(queryWrapper);
                int cabitNetId = copyTo(heatCabinet, heatPropertyId);
                if (cabitNetId == 0) {
                    //复制控制柜复制出错
                    map.put(heatPropertyId, "复制控制柜信息出错，" + heatCabinet.getName());
                    continue;
                }
                if (copySourceDto.getHeatSystemIds().size() == 0)
                    map.put(heatPropertyId, "复制成功");
                for (Integer heatSystemId : copySourceDto.getHeatSystemIds()) {
                    QueryWrapper<HeatSystem> systemQuery = new QueryWrapper<>();
                    systemQuery.lambda().eq(HeatSystem::getId, heatSystemId);
                    HeatSystem heatSystem = heatSystemService.getOne(systemQuery);
                    int systemId = copyTo(heatSystem, cabitNetId);
                    if (systemId == 0) {
                        map.put(heatPropertyId, "复制系统信息出错，" + heatSystem.getName());
                        //复制系统信息出错
                        continue;
                    }

                    if (!copyPointToCollect(systemId, heatSystemId, dto.getType())) {
                        //复制采集点信息出错
                        map.put(heatPropertyId, "复制采集点信息出错，" + heatSystem.getName());
                        continue;
                    }
                    break;


                }
            }

        }
        // 复制工艺图
        copyCraft(dto);
        return Response.success(map);
    }

    private int copyTo(HeatCabinet info, int sourceId) {
        try {
            info.setHeatSourceId(sourceId);
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
            return heatSystemService.save(info) ? info.getId() : 0;
        } catch (Exception e) {
            log.error("copy heatSystem cause exception {0}", e);
            return 0;
        }
    }

    private boolean clear(List<Integer> sourceId) {
        try {
            List<CabinetPoint> cabinetPoints =
                    heatSourceMapper.queryCollectPointAndCabinet
                            (new QueryWrapper<PointCollectConfig>().in("hts.id", sourceId))
//                                    .eq("pc.level",TreeLevel.HeatSystem.level())
                    ;
            if (cabinetPoints != null && cabinetPoints.size() > 0) {
                List<Integer> heatSystemIds = cabinetPoints.stream().filter(x -> x.getHeatSystemId() != 0).map(CabinetPoint::getHeatSystemId).distinct().collect(Collectors.toList());
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

    private boolean copyPointToCollect(int systemId, int sourceSystemId, int type) {
        try {
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pc.relevanceId", sourceSystemId)
                    .eq("pc.deleteFlag", false);
            if (type != 0)
                queryWrapper.eq("ps.pointConfig", type);
            List<PointConfig> list = pointConfigMapper.queryList(queryWrapper);
            for (PointConfig info : list) {
                info.setRelevanceId(systemId);
                info.setCreateTime(LocalDateTime.now());
                info.setCreateUser(userName);
                info.setUpdateTime(LocalDateTime.now());
                info.setUpdateUser("");
                info.setLevel(TreeLevel.HeatSystem.level());
//                if (userId != null) {
//                    info.setUserId(userId);
//                }
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

    private void deal(List<CabinetPoint> cabinetPoints) {
        List<Integer> cabiNetIds = cabinetPoints.stream().map(CabinetPoint::getId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (cabiNetIds != null && cabiNetIds.size() > 0)
            heatCabinetMapper.deleteBatchIds(cabiNetIds);
        List<Integer> systemIds = cabinetPoints.stream().map(CabinetPoint::getHeatSystemId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (systemIds != null && systemIds.size() > 0)
            heatSystemMapper.deleteBatchIds(systemIds);
    }


    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

    @ApiOperation("复制到新建热源")
    @PostMapping("/copyNew")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response copyNew(@RequestBody CopyHeatSourceDto dto) {
        this.userName = !StringUtils.isNullOrEmpty(dto.getUserName()) ? dto.getUserName() : "";
        this.userId = dto.getUserId() != 0 ? dto.getUserId() : null;
        // 热源 信息
        HeatSource heatSource = HeatSourceConverter.INSTANCE.domain2dto(dto.getHeatSource());
        if (heatSource.getName() != null) {
            HeatSource one = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getName, heatSource.getName()));
            if (one != null) {
                return Response.fail("该热源名称已存在！请更改热源名称！");
            }
            String JP = PinYinUtils.toFirstChar(heatSource.getName());//首字母简拼
            heatSource.setLogogram(JP);
            if (userId != null) {
                heatSource.setUserId(userId);
            }
            heatSource.setCreateUser(userName);
            heatSource.setCreateTime(LocalDateTime.now());
            if (heatSourceService.save(heatSource)) {
                List<Integer> sourceId = new ArrayList<>(1);
                sourceId.add(heatSource.getId());
                dto.setTargetIds(sourceId);
                return toExist(dto);

            }
            return Response.fail("保存热源失败");
        } else {
            return Response.fail("新建热源失败！");
        }

    }

    @ApiModelProperty("读取复制源控制柜及机组信息")
    @GetMapping
    public Response querySource(@RequestParam int sourceId) throws MicroException {

//        List<StationFirstNetBaseView> firstNetBases = sourceFirstNetBaseViewService.list();
//        List<StationFirstNetBaseView> bases = firstNetBases.stream().filter(x -> x.getHeatSourceId() == sourceId).distinct().collect(Collectors.toList());
//        Map<Integer, List<StationFirstNetBaseView>> collect = bases.stream().collect(Collectors.groupingBy(x -> x.getHeatCabinetId()));

        // 读取热源的信息
        List<SourceFirstNetBaseView> firstNetBases = sourceFirstNetBaseViewService.list();
        List<SourceFirstNetBaseView> bases = firstNetBases.stream().filter(x -> x.getHeatSourceId() == sourceId).distinct().collect(Collectors.toList());
        Map<Integer, List<SourceFirstNetBaseView>> collect = bases.stream().collect(Collectors.groupingBy(x -> x.getHeatCabinetId()));
        List<Map<String, Object>> results = new ArrayList<>(collect.size());
        collect.forEach((k, v) -> {
            Map<String, Object> resultItem = new HashMap<>();
            SourceFirstNetBaseView sourceFirstNetBaseView = bases.stream().filter(x -> x.getHeatCabinetId() == k).findFirst().orElse(null);

//            StationFirstNetBaseView firstNetBase = bases.stream().filter(x -> x.getHeatCabinetId() == k).findFirst().orElse(null);

            resultItem.put("heatCabinetName", sourceFirstNetBaseView.getHeatCabinetName());
            resultItem.put("heatCabinetId", sourceFirstNetBaseView.getHeatCabinetId());
            resultItem.put("children", v);
            results.add(resultItem);
        });
        return Response.success(results);
    }
}

























