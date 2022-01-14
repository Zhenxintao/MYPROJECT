package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.converter.PointConfigConverter;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.PointAlarmService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatPointInitDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.restful.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: SyncPointConfigService
 * @Description: 点数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncPointConfigService {

    @Autowired
    private PointConfigConverter pointConfigConverter;

    @Autowired
    private HeatSystemService heatSystemService;

    @Autowired
    private PointConfigService pointConfigService;

    @Autowired
    private PointStandardService pointStandardService;

    @Autowired
    private RedisPointService redisPointService;

    @Autowired
    private PointAlarmService pointAlarmService;

    @Autowired
    private CommonServiceAsync commonServiceAsync;

    /**
     * @Description 添加点数据
     */
    public Response addHeatPoint(List<HeatPointInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            List<PointConfig> listAdd = new ArrayList<>();
            List<Integer> systemIdList = new ArrayList<>();
            List<HeatPointInitDto> loseList = new ArrayList<>();
            List<PointAlarm> pointAlarmList = new ArrayList<>();
            for (HeatPointInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加点数据--同步编号为空！");
                    loseList.add(dto);
                    log.error("addHeatPoint---添加点数据，{} 同步编号为空！", dto.getPointColumnName());

                    continue;
                }
                if (StringUtils.isBlank(dto.getPointColumnName())) {
                    errMsg.add("添加点数据--同步编号--" + dto.getNum() + " 的点名称为空！");
                    loseList.add(dto);
                    log.error("addHeatPoint---添加点数据，同步编号 {} 的点名称为空！", dto.getNum());
                    continue;
                }
                if (dto.getParentNum() == null) {
                    errMsg.add("添加点数据--同步编号--" + dto.getNum() + " 的所属系统为空！");
                    loseList.add(dto);
                    log.error("addHeatPoint---添加点数据，同步编号 {} 的所属系统为空！", dto.getNum());
                    continue;
                }

                PointConfig loadPointConfig = pointConfigService.getOne(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getSyncNumber, dto.getNum()));
                if (loadPointConfig != null) {
                    // 不能重复添加
                    continue;
                }

                PointConfig addPointConfig = pointConfigConverter.INSTANCE.dtoToPo(dto);
                addPointConfig.setSyncNumber(dto.getNum());
                addPointConfig.setSyncParentNum(dto.getParentNum());
                addPointConfig.setCreateUser("数据同步");
                addPointConfig.setCreateTime(LocalDateTime.now());
//                addPointConfig.setUserId(-1);
                // 处理标准参量
                String pointColumnName = dto.getPointColumnName();
                PointStandard loadPointStandard = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, pointColumnName)
                        .eq(PointStandard::getLevel, dto.getLevel()));
                if (loadPointStandard == null) {
                    errMsg.add("添加点数据--平台没有查询到--" + pointColumnName + " 的标准参量数据！");
                    loseList.add(dto);
                    log.error("addHeatPoint---添加点数据，同步编号 {} 的点名称 {},平台没有查询到标准参量数据！", dto.getNum(), pointColumnName);
                    continue;
                }
                addPointConfig.setPointStandardId(loadPointStandard.getId());
                // 处理所属系统
                HeatSystem loadHeatSystem = heatSystemService.getOne(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getSyncNumber, dto.getParentNum()));
                if (loadHeatSystem == null) {
                    errMsg.add("添加点数据--平台没有查询到同步编号为--" + dto.getParentNum() + " 的系统数据！");
                    loseList.add(dto);
                    log.error("addHeatPoint---添加同步编号 {} 的点数据，平台没有查询到同步编号为 {} 的系统数据！", dto.getNum(), dto.getParentNum());

                    continue;
                }
                addPointConfig.setRelevanceId(loadHeatSystem.getId());
                addPointConfig.setLevel(1);

                listAdd.add(addPointConfig);
                systemIdList.add(loadHeatSystem.getId());
                // 配置报警值
                if (dto.getAccidentHigh() == null && dto.getAccidentLower() == null
                        && dto.getRunningHigh() == null && dto.getRunningLower() == null
                        && dto.getIsAlarm() == null && dto.getAlarmValue() == null) {
                    continue;
                }
                PointAlarm pointAlarm = new PointAlarm();
                pointAlarm.setPointSyncNum(dto.getNum());
                if (dto.getAccidentHigh() != null) {
                    pointAlarm.setAccidentHigh(dto.getAccidentHigh());
                }
                if (dto.getAccidentLower() != null) {
                    pointAlarm.setAccidentLower(dto.getAccidentLower());
                }
                if (dto.getRunningHigh() != null) {
                    pointAlarm.setRunningHigh(dto.getRunningHigh());
                }
                if (dto.getRunningLower() != null) {
                    pointAlarm.setRunningLower(dto.getRunningLower());
                }
                if (dto.getIsAlarm() != null) {
                    pointAlarm.setIsAlarm(dto.getIsAlarm());
                }
                if (dto.getAlarmValue() != null) {
                    pointAlarm.setAlarmValue(dto.getAlarmValue());
                }
                if (dto.getGrade() != null) {
                    pointAlarm.setGrade(dto.getGrade());
                }
                if (StringUtils.isNotBlank(dto.getAlarmDesc())) {
                    pointAlarm.setAlarmDesc(dto.getAlarmDesc());
                }
                if (StringUtils.isNotBlank(dto.getRankJson())) {
                    pointAlarm.setRankJson(dto.getRankJson());
                }
                pointAlarm.setCreateTime(LocalDateTime.now());
                pointAlarmList.add(pointAlarm);


            }
            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加
                boolean batchStatus = pointConfigService.saveBatch(listAdd);
                if (!batchStatus) {
                    errMsg.add("添加点数据--平台批量添加点数据异常！");
                    log.error("addHeatPoint---批量添加点数据异常！--{} ", listAdd.toString());

                }

                // 更新缓存数据
                if (batchStatus && !CollectionUtils.isEmpty(systemIdList)) {
                    redisPointService.saveOrUpdateByrelevanceId(systemIdList, TreeLevel.HeatSystem.level());
                }

                // 当点都添加成功后，开始操作点的报警表
                if (batchStatus && !CollectionUtils.isEmpty(pointAlarmList)) {
                    Set<String> alarmErrMsg = addPointAlarm(pointAlarmList);
                    if (alarmErrMsg.size() > 0) {
                        errMsg.addAll(alarmErrMsg);
                    }
                }
                // 进行接口调用，重启采集线程
                commonServiceAsync.reloadMonitor();

            }
            if (errMsg.size() > 0) {
                log.error("addHeatPoint---批量添加点数据丢失的点--{} ", loseList.toString());
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    // 配置报警表数据
    private Set<String> addPointAlarm(List<PointAlarm> pointAlarmList) {
        Set<String> errMsg = new HashSet<>();
        List<PointAlarm> addList = new ArrayList<>();
        List<PointAlarm> loseList = new ArrayList<>();
        pointAlarmList.stream().forEach(e -> {
            // 查询对应的 pointConfigId
            String pointSyncNum = e.getPointSyncNum();
            PointConfig onePointConfig = pointConfigService.getOne(Wrappers.<PointConfig>lambdaQuery()
                    .eq(PointConfig::getSyncNumber, pointSyncNum));
            if (onePointConfig != null) {
                e.setPointConfigId(onePointConfig.getId());
                addList.add(e);
            } else {
                loseList.add(e);
            }
        });
        if (!CollectionUtils.isEmpty(addList)) {
            boolean batchStatus = pointAlarmService.saveBatch(addList);
            if (!batchStatus) {
                errMsg.add("添加点数据--平台批量添加点报警数据异常！");
                log.error("addHeatPoint---批量添加点报警数据异常！--{} ", addList.toString());
            }
        }
        if (!CollectionUtils.isEmpty(loseList)) {
            log.error("addHeatPoint---批量添加点报警数据,没有查询到归属的数据！--{} ", loseList.toString());
        }
        return errMsg;
    }

    /**
     * @Description 编辑点数据
     */
    public Response updateHeatPoint(List<HeatPointInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<PointConfig> updatePointConfigs = new ArrayList<>();
            List<Integer> systemIdList = new ArrayList<>();
            List<PointAlarm> pointAlarmList = new ArrayList<>();
            for (HeatPointInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑点数据--同步编号为空！");
                    log.error("updateHeatPoint---编辑点数据，{} 的 同步编号为空！", dto.getPointColumnName());

                    continue;
                }
                PointConfig loadPointConfig = pointConfigService.getOne(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getSyncNumber, dto.getNum()));
                if (loadPointConfig == null) {
                    errMsg.add("编辑点数据--平台没有查询到同步编号为--" + dto.getNum() + " 的点数据！");
                    log.error("updateHeatPoint---编辑点数据，平台没有查询到同步编号 {} 的点数据！", dto.getNum());
                    continue;
                }
                PointConfig updatePointConfig = pointConfigConverter.INSTANCE.dtoToPo(dto);
                updatePointConfig.setId(loadPointConfig.getId());
                updatePointConfig.setUpdateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(dto.getPointColumnName())) {
                    // 处理标准参量
                    String pointColumnName = dto.getPointColumnName();
                    PointStandard loadPointStandard = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, pointColumnName)
                            .eq(PointStandard::getLevel, dto.getLevel()));
                    if (loadPointStandard != null) {
                        updatePointConfig.setPointStandardId(loadPointStandard.getId());
                    }
                }
                if (dto.getParentNum() != null) {
                    // 处理所属系统
                    HeatSystem loadHeatSystem = heatSystemService.getOne(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getSyncNumber, dto.getParentNum()));
                    if (loadHeatSystem != null) {
                        updatePointConfig.setRelevanceId(loadHeatSystem.getId());
                        systemIdList.add(loadHeatSystem.getId());
                    }
                }
                updatePointConfigs.add(updatePointConfig);

                // 配置报警值
                if (dto.getAccidentHigh() == null && dto.getAccidentLower() == null
                        && dto.getRunningHigh() == null && dto.getRunningLower() == null
                        && dto.getIsAlarm() == null && dto.getAlarmValue() == null) {
                    continue;
                }
                PointAlarm pointAlarm = new PointAlarm();
                pointAlarm.setPointConfigId(loadPointConfig.getId());
                if (dto.getAccidentHigh() != null) {
                    pointAlarm.setAccidentHigh(dto.getAccidentHigh());
                }
                if (dto.getAccidentLower() != null) {
                    pointAlarm.setAccidentLower(dto.getAccidentLower());
                }
                if (dto.getRunningHigh() != null) {
                    pointAlarm.setRunningHigh(dto.getRunningHigh());
                }
                if (dto.getRunningLower() != null) {
                    pointAlarm.setRunningLower(dto.getRunningLower());
                }
                if (dto.getIsAlarm() != null) {
                    pointAlarm.setIsAlarm(dto.getIsAlarm());
                }
                if (dto.getAlarmValue() != null) {
                    pointAlarm.setAlarmValue(dto.getAlarmValue());
                }
                if (dto.getGrade() != null) {
                    pointAlarm.setGrade(dto.getGrade());
                }
                if (StringUtils.isNotBlank(dto.getAlarmDesc())) {
                    pointAlarm.setAlarmDesc(dto.getAlarmDesc());
                }
                if (StringUtils.isNotBlank(dto.getRankJson())) {
                    pointAlarm.setRankJson(dto.getRankJson());
                }
                pointAlarmList.add(pointAlarm);


            }

            if (!CollectionUtils.isEmpty(updatePointConfigs)) {
                // 批量修改
                boolean batchStatus = pointConfigService.updateBatchById(updatePointConfigs);
                if (!batchStatus) {
                    errMsg.add("编辑点数据--平台批量修改点数据异常！");
                    log.error("updateHeatPoint---编辑点数据，批量修改点数据异常！", updatePointConfigs.toString());
                }
                // 更新缓存数据
                if (batchStatus && !CollectionUtils.isEmpty(systemIdList)) {
                    redisPointService.saveOrUpdateByrelevanceId(systemIdList, TreeLevel.HeatSystem.level());
                }
                if (batchStatus && !CollectionUtils.isEmpty(pointAlarmList)) {
                    Set<String> alarmErrMsg = updatePointAlarm(pointAlarmList);
                    if (alarmErrMsg.size() > 0) {
                        errMsg.addAll(alarmErrMsg);
                    }
                }
                // 进行接口调用，重启采集线程
                commonServiceAsync.reloadMonitor();

            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    private Set<String> updatePointAlarm(List<PointAlarm> pointAlarmList) {
        Set<String> errMsg = new HashSet<>();
        List<PointAlarm> updateList = new ArrayList<>();
        List<PointAlarm> addList = new ArrayList<>();
        pointAlarmList.stream().forEach(e -> {
            // 查询对应的 id
            Integer pointConfigId = e.getPointConfigId();
            PointAlarm onePointAlarm = pointAlarmService.getOne(Wrappers.<PointAlarm>lambdaQuery()
                    .eq(PointAlarm::getPointConfigId, pointConfigId));
            if (onePointAlarm != null) {
                e.setId(onePointAlarm.getId());
                e.setUpdateTime(LocalDateTime.now());
                updateList.add(e);
            } else {
                e.setCreateTime(LocalDateTime.now());
                addList.add(e);
            }
        });
        if (!CollectionUtils.isEmpty(updateList)) {
            boolean batchStatus = pointAlarmService.updateBatchById(updateList);
            if (!batchStatus) {
                errMsg.add("编辑点数据--平台批量更改点报警数据异常！");
                log.error("updateHeatPoint---批量更改点报警数据异常！--{} ", updateList.toString());
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            boolean addBatch = pointAlarmService.saveBatch(addList);
            if (!addBatch) {
                errMsg.add("编辑点数据--平台批量添加点报警数据异常！");
                log.error("updateHeatPoint---批量添加点报警数据异常！--{} ", addList.toString());
            }
        }

        return errMsg;
    }


    /**
     * @Description 删除点数据
     */
    public Response delHeatPoint(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delPointConfigList = new ArrayList<>();
            List<Integer> pointConfigIds = new ArrayList<>();
            List<Integer> systemIdList = new ArrayList<>();
            for (Integer num : numList) {
                PointConfig loadPointConfig = pointConfigService.getOne(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getSyncNumber, num));
                if (loadPointConfig == null) {
                    errMsg.add("删除点数据--平台没有查询到同步编号为--" + num + " 的点数据！");
                    log.error("delHeatPoint---删除点数据，台没有查询到同步编号为 {} 的点数据！", num);
                    continue;
                }
                delPointConfigList.add(loadPointConfig.getId());
                systemIdList.add(loadPointConfig.getRelevanceId());
                pointConfigIds.add(loadPointConfig.getId());
            }
            if (!CollectionUtils.isEmpty(delPointConfigList)) {
                boolean delStatus = pointConfigService.removeByIds(delPointConfigList);
                if (!delStatus) {
                    errMsg.add("删除点数据--平台批量删除点数据异常！");
                    log.error("delHeatPoint---批量删除点数据异常！", delPointConfigList.toString());
                }

                // 更新缓存数据
                if (delStatus && !CollectionUtils.isEmpty(systemIdList)) {
                    redisPointService.saveOrUpdateByrelevanceId(systemIdList, TreeLevel.HeatSystem.level());
                }

                // 删除点报警数据
                if (delStatus && !CollectionUtils.isEmpty(pointConfigIds)) {
                    Set<String> alarmErrMsg = delPointAlarm(pointConfigIds);
                    if (alarmErrMsg.size() > 0) {
                        errMsg.addAll(alarmErrMsg);
                    }
                }

                // 进行接口调用，重启采集线程
                commonServiceAsync.reloadMonitor();
            }

            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    // 删除点报警配置
    private Set<String> delPointAlarm(List<Integer> pointConfigIds) {
        Set<String> errMsg = new HashSet<>();

        List<Integer> delList = new ArrayList<>();
        List<Integer> loseList = new ArrayList<>();
        pointConfigIds.stream().forEach(e -> {
            // 查询对应的 id
            PointAlarm onePointAlarm = pointAlarmService.getOne(Wrappers.<PointAlarm>lambdaQuery()
                    .eq(PointAlarm::getPointConfigId, e));
            if (onePointAlarm != null) {
                delList.add(e);
            } else {
                loseList.add(e);
            }
        });
        if (!CollectionUtils.isEmpty(delList)) {
            boolean batchStatus = pointAlarmService.removeByIds(delList);
            if (!batchStatus) {
                errMsg.add("删除点数据--平台批量删除点报警数据异常！");
                log.error("delHeatPoint---批量删除点报警数据异常！--{} ", delList.toString());
            }
        }
        if (!CollectionUtils.isEmpty(loseList)) {
            log.error("delHeatPoint---批量删除点报警数据,没有查询到归属的数据！--{} ", loseList.toString());
        }

        return errMsg;
    }


}
