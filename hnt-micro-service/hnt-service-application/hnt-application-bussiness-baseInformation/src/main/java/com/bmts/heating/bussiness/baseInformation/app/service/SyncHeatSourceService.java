package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatSourceInitDto;
import com.bmts.heating.commons.utils.restful.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: SyncHeatSourceService
 * @Description: 热源数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncHeatSourceService {

    @Autowired
    private HeatOrganizationService heatOrganizationService;

    @Autowired
    private HeatNetService heatNetService;

    @Autowired
    private HeatSourceService heatSourceService;

    @Autowired
    private HeatNetSourceService heatNetSourceService;

    @Autowired
    private HeatTransferStationService heatTransferStationService;

    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;

    @Autowired
    private ForecastSourceDetailService forecastSourceDetailService;

    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private DeviceConfigService deviceConfigService;
    @Autowired
    private HeatDeviceConfigService heatDeviceConfigService;

    /**
     * @Description 添加热源数据
     */
    public Response addHeatSource(List<HeatSourceInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            // 添加关联表数据
            List<HeatNetSource> listAdd = new ArrayList<>();

            // 进行关联设置绑定
            //List<DeviceConfig> pvssDeviceList = deviceConfigService.list(Wrappers.<DeviceConfig>lambdaQuery().eq(DeviceConfig::getName, "PVSS"));
            // Map<Integer, Integer> countMap = new HashMap<>();
            //pvssDeviceList.stream().forEach(e -> {
            //    int count = heatDeviceConfigService.count(Wrappers.<HeatDeviceConfig>lambdaQuery().eq(HeatDeviceConfig::getDeviceConfigId, e.getId()));
            //    countMap.put(e.getId(), count);
            //});


            // 开始绑定关联
            //LocalDateTime nowTime = LocalDateTime.now();
            //List<HeatDeviceConfig> addHeatDeviceConfigList = new ArrayList<>();

            for (HeatSourceInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加热源数据--同步编号为空！");
                    log.error("addHeatSource---添加热源数据{} 的--同步编号为空！", dto.getName());
                    continue;
                }
                if (StringUtils.isBlank(dto.getName())) {
                    errMsg.add("添加热源数据--同步编号--" + dto.getNum() + " 热源名称为空！");
                    log.error("addHeatSource---添加 {} 同步编号的热源数据名称为空！", dto.getNum());
                    continue;
                }
                HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, dto.getNum()));
                if (loadHeatSource != null) {
                    // 不能重复添加
                    continue;
                }

                HeatSource addHeatSource = new HeatSource();
                addHeatSource.setName(dto.getName());
                addHeatSource.setSyncNumber(dto.getNum());
                addHeatSource.setCreateUser("数据同步");
                addHeatSource.setCreateTime(LocalDateTime.now());
                addHeatSource.setUserId(-1);
                //首字母简拼
                addHeatSource.setLogogram(PinYinUtils.toFirstChar(dto.getName()));
                if (StringUtils.isNotBlank(dto.getCode())) {
                    addHeatSource.setCode(dto.getCode());
                }
                if (StringUtils.isNotBlank(dto.getAddress())) {
                    addHeatSource.setAddress(dto.getAddress());
                }
                if (dto.getFlag() != null) {
                    addHeatSource.setFlag(dto.getFlag());
                }
                if (dto.getType() != null) {
                    addHeatSource.setType(dto.getType());
                }
                if (dto.getLongitude() != null) {
                    addHeatSource.setLongitude(dto.getLongitude());
                }
                if (dto.getLatitude() != null) {
                    addHeatSource.setLatitude(dto.getLatitude());
                }
                if (dto.getPipeDiameter() != null) {
                    addHeatSource.setPipeDiameter(dto.getPipeDiameter());
                }
                if (dto.getHeatArea() != null) {
                    addHeatSource.setHeatArea(dto.getHeatArea());
                }

                // 处理所属公司
                Integer organizeSyncNum = dto.getHeatOrganizationId();
                if (organizeSyncNum == null) {
                    errMsg.add("添加热源数据--同步编号为--" + dto.getNum() + " 的所属组织id为空！");
                    log.error("addHeatSource---添加 {} 热源数据的所属组织id为空！", dto.getNum());
                    continue;
                }
                HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, organizeSyncNum));
                if (loadOrganize == null) {
                    errMsg.add("添加热源数据--平台没有查询到同步编号为--" + organizeSyncNum + " 的组织结构数据！");
                    log.error("addHeatSource---添加 {} 热源数据，平台没有查询到同步编号为 {} 的组织结构数据！", dto.getNum(), organizeSyncNum);
                    continue;
                }
                addHeatSource.setHeatOrganizationId(loadOrganize.getId());
                // 处理所属热网
                Integer heatNetSyncNum = dto.getHeatNetId();
                if (heatNetSyncNum == null) {
                    errMsg.add("添加热源数据--同步编号为--" + dto.getNum() + " 的所属热网id为空！");
                    log.error("addHeatSource---添加 {} 热源数据的所属热网id为空！", dto.getNum());
                    continue;
                }
                HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, heatNetSyncNum));
                if (loadHeatNet == null) {
                    errMsg.add("添加热源数据--平台没有查询到同步编号为--" + heatNetSyncNum + " 的热网数据！");
                    log.error("addHeatSource---添加 {} 热源数据,平台没有查询到同步编号为 {} 的热网数据！", dto.getNum(), heatNetSyncNum);
                    continue;
                }

                // 处理关联表
                if (heatSourceService.save(addHeatSource)) {
                    Integer insertId = addHeatSource.getId();
                    if (insertId > 0) {
                        HeatNetSource one = heatNetSourceService.getOne(Wrappers.<HeatNetSource>lambdaQuery().eq(HeatNetSource::getHeatNetId, loadHeatNet.getId()).eq(HeatNetSource::getHeatSourceId, insertId));
                        if (one == null) {
                            HeatNetSource addHeatNetSource = new HeatNetSource();
                            addHeatNetSource.setHeatSourceId(insertId);
                            addHeatNetSource.setHeatNetId(loadHeatNet.getId());
                            addHeatNetSource.setMatchupType(1);
                            listAdd.add(addHeatNetSource);
                        }

                        //  HeatDeviceConfig add = new HeatDeviceConfig();
                        //add.setCreateTime(nowTime);
                        //// 类型：1、所属换热站    2、所属热源
                        //add.setType(2);
                        //add.setRelevanceId(insertId);
                        //// 取出最小的 值
                        //Integer countMin = countMap.values().stream().sorted().findFirst().get();
                        //if (countMin != null) {
                        //    // 取出最小值对应的key
                        //    Integer deviceConfigId = countMap.entrySet().stream()
                        //            .filter(kvEntry -> Objects.equals(kvEntry.getValue(), countMin))
                        //            .map(Map.Entry::getKey)
                        //            .findFirst().get();
                        //    if (deviceConfigId == null) {
                        //        continue;
                        //    }
                        //    add.setDeviceConfigId(deviceConfigId);
                        //    addHeatDeviceConfigList.add(add);
                        //    countMap.put(deviceConfigId, countMin + 1);
                        //}
                    }
                }


            }
            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加关联表
                if (!heatNetSourceService.saveBatch(listAdd)) {
                    errMsg.add("添加热源数据--平台批量添加热网热源关联表数据异常！");
                    log.error("addHeatSource---批量添加热源数据异常！--{}", listAdd.toString());
                }
            }

            //if (!CollectionUtils.isEmpty(addHeatDeviceConfigList)) {
            //    if (!heatDeviceConfigService.saveBatch(addHeatDeviceConfigList)) {
            //        log.error("addHeatStation---批量添加热力站关联数据异异常！--{}", addHeatDeviceConfigList.toString());
            //    }
            //}

            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑热源数据
     */
    public Response updateHeatSource(List<HeatSourceInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatSource> updateHeatSources = new ArrayList<>();
            List<HeatNetSource> updateHeatNetSources = new ArrayList<>();
            for (HeatSourceInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑热源数据--同步编号为空！");
                    log.error("updateHeatSource---编辑 {} 热源数据--同步编号为空！", dto.getName());
                    continue;
                }
                HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, dto.getNum()));
                if (loadHeatSource == null) {
                    errMsg.add("编辑热源数据--平台没有查询到同步编号为--" + dto.getNum() + " 的热源数据！");
                    log.error("updateHeatSource---平台没有查询到同步编号为-- {} 的热源数据！", dto.getNum());
                    continue;
                }
                HeatSource updateHeatSource = new HeatSource();
                updateHeatSource.setId(loadHeatSource.getId());
                updateHeatSource.setUpdateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(dto.getCode())) {
                    updateHeatSource.setCode(dto.getCode());
                }
                if (StringUtils.isNotBlank(dto.getName())) {
                    updateHeatSource.setName(dto.getName());
                    //首字母简拼
                    updateHeatSource.setLogogram(PinYinUtils.toFirstChar(dto.getName()));
                }
                if (StringUtils.isNotBlank(dto.getAddress())) {
                    updateHeatSource.setAddress(dto.getAddress());
                }
                if (dto.getFlag() != null) {
                    updateHeatSource.setFlag(dto.getFlag());
                }
                if (dto.getType() != null) {
                    updateHeatSource.setType(dto.getType());
                }
                if (dto.getLongitude() != null) {
                    updateHeatSource.setLongitude(dto.getLongitude());
                }
                if (dto.getLatitude() != null) {
                    updateHeatSource.setLatitude(dto.getLatitude());
                }
                if (dto.getPipeDiameter() != null) {
                    updateHeatSource.setPipeDiameter(dto.getPipeDiameter());
                }
                if (dto.getHeatArea() != null) {
                    updateHeatSource.setHeatArea(dto.getHeatArea());
                }
                // 处理所属公司
                Integer organizeSyncNum = dto.getHeatOrganizationId();
                if (organizeSyncNum != null) {
                    HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, organizeSyncNum));
                    if (loadOrganize == null) {
                        errMsg.add("编辑热源数据--平台没有查询到同步编号为--" + organizeSyncNum + " 的组织结构数据！");
                        log.error("updateHeatSource---编辑 {} 热源数据,平台没有查询到同步编号为-- {} 的组织结构数据！", dto.getNum(), organizeSyncNum);
                        continue;
                    }
                    updateHeatSource.setHeatOrganizationId(loadOrganize.getId());
                }
                // 处理所属热网
                Integer heatNetSyncNum = dto.getHeatNetId();
                if (heatNetSyncNum != null) {
                    HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, heatNetSyncNum));
                    if (loadHeatNet == null) {
                        errMsg.add("编辑热源数据--平台没有查询到同步编号为--" + heatNetSyncNum + " 的热网数据！");
                        log.error("updateHeatSource---编辑 {} 热源数据,平台没有查询到同步编号为-- {} 的热网数据！", dto.getNum(), heatNetSyncNum);
                        continue;
                    }
                    // 处理关联表数据
                    HeatNetSource one = heatNetSourceService.getOne(Wrappers.<HeatNetSource>lambdaQuery().eq(HeatNetSource::getHeatNetId, loadHeatNet.getId()).eq(HeatNetSource::getHeatSourceId, loadHeatSource.getId()));
                    if (one == null) {
                        HeatNetSource addHeatNetSource = new HeatNetSource();
                        addHeatNetSource.setHeatSourceId(loadHeatSource.getId());
                        addHeatNetSource.setHeatNetId(loadHeatNet.getId());
                        updateHeatNetSources.add(addHeatNetSource);
                    }
                }
                updateHeatSources.add(updateHeatSource);
            }
            if (!CollectionUtils.isEmpty(updateHeatSources)) {
                // 批量修改
                if (!heatSourceService.updateBatchById(updateHeatSources)) {
                    errMsg.add("编辑热源数据--平台批量修改热源数据异常！");
                    log.error("updateHeatSource---批量修改热源数据异常！--{} ", updateHeatSources.toString());
                } else {
                    // 更改面积
                    updateForecastArea(updateHeatSources);
                }

            }
            if (!CollectionUtils.isEmpty(updateHeatNetSources)) {
                if (!heatNetSourceService.saveBatch(updateHeatNetSources)) {
                    errMsg.add("编辑热源数据--平台批量添加热网热源关联表数据异常！");
                    log.error("updateHeatSource---编辑热源数据--平台批量添加热网热源关联表数据异常！--{} ", updateHeatNetSources.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    /**
     * @Description 删除热源数据
     */
    public Response delHeatSource(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delList = new ArrayList<>();
            for (Integer num : numList) {
                HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, num));
                if (loadHeatSource == null) {
                    errMsg.add("删除热源数据--平台没有查询到同步编号为--" + num + " 的热网数据！");
                    log.error("delHeatSource---删除热源数据--平台没有查询到同步编号为 {} 的热网数据！", num);
                    continue;
                }
                delList.add(loadHeatSource.getId());
            }
            if (!CollectionUtils.isEmpty(delList)) {
                // 批量删除热源表数据
                if (heatSourceService.removeByIds(delList)) {
                    // 进行关联删除
                    heatNetSourceService.remove(Wrappers.<HeatNetSource>lambdaQuery().in(HeatNetSource::getHeatSourceId, delList));
                    // 操作关联的热力站数据信息
                    List<HeatTransferStation> listStation = heatTransferStationService.list(Wrappers.<HeatTransferStation>lambdaQuery().in(HeatTransferStation::getHeatSourceId, delList));
                    if (!CollectionUtils.isEmpty(listStation)) {
                        for (HeatTransferStation station : listStation) {
                            UpdateWrapper<HeatTransferStation> updateWrapper = new UpdateWrapper();
                            updateWrapper.eq("id", station.getId());
                            updateWrapper.set("heatSourceId", null);
                            heatTransferStationService.update(updateWrapper);
                        }
                    }
                } else {
                    errMsg.add("删除热源数据--批量删除热源数据异常！");
                    log.error("delHeatSource---批量关联删除热源数据异常！--{}", delList.toString());
                }
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * 修改负荷预测面积
     */
    public void updateForecastArea(Integer syncNumber, BigDecimal area) {
        try {
            List<SourceFirstNetBaseView> sourceFirstNetBaseViewAll = sourceFirstNetBaseViewService.list();
            SourceFirstNetBaseView sourceFirstNetBaseView = sourceFirstNetBaseViewAll.stream().filter(s -> Objects.equals(syncNumber, s.getSourceSyncNum())).findFirst().orElse(null);
            List<ForecastSourceDetail> forecastSourceDetailsAll = forecastSourceDetailService.list();
            List<ForecastSourceDetail> forecastSourceDetailList = forecastSourceDetailsAll.stream().filter(s -> Objects.equals(sourceFirstNetBaseView.getHeatSourceId(), s.getHeatSourceId())).collect(Collectors.toList());
            for (ForecastSourceDetail f : forecastSourceDetailList) {
                List<ForecastSourceDetail> info = forecastSourceDetailsAll.stream().filter(s -> Objects.equals(s.getForcastSourceCoreId().toString(), f.getForcastSourceCoreId().toString())).collect(Collectors.toList());
                UpdateWrapper<ForecastSourceCore> forecastSourceCoreUpdateWrapper = new UpdateWrapper<>();
                forecastSourceCoreUpdateWrapper.eq("id", f.getForcastSourceCoreId());
                if (info.size() == 1) {
                    forecastSourceCoreUpdateWrapper.set("heatArea", area);
                    forecastSourceCoreService.update(forecastSourceCoreUpdateWrapper);
                } else if (info.size() > 1) {
                    List<Integer> sourceIds = info.stream().map(s -> s.getHeatSourceId()).collect(Collectors.toList());
                    sourceIds.remove(sourceFirstNetBaseView.getHeatSourceId());
                    List<SourceFirstNetBaseView> elseSourceInfo = sourceFirstNetBaseViewAll.stream().filter(s -> sourceIds.contains(s.getHeatSourceId())).collect(Collectors.toList());
                    BigDecimal elseSumArea = elseSourceInfo.stream().map(SourceFirstNetBaseView::getHeatSourceArea).reduce(BigDecimal.ZERO, BigDecimal::add);
                    forecastSourceCoreUpdateWrapper.set("heatArea", elseSumArea.add(area));
                    forecastSourceCoreService.update(forecastSourceCoreUpdateWrapper);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


    public void updateForecastArea(List<HeatSource> updateHeatSources) {
        // 判断哪些预测 用到的热源，根据 热源id 进行查询匹配
        List<SourceFirstNetBaseView> sourceFirstNetBaseViewAll = sourceFirstNetBaseViewService.list();
        List<ForecastSourceDetail> forecastDetaiListAll = forecastSourceDetailService.list();
        List<ForecastSourceCore> updateForecastSourceCoreList = new ArrayList<>();
        updateHeatSources.stream().forEach(e -> {
            if (e.getHeatArea() != null) {
                // 进行关联查询
                List<ForecastSourceDetail> forecastDetaiList = forecastDetaiListAll.stream()
                        .filter(s -> Objects.equals(e.getId(), s.getHeatSourceId())).collect(Collectors.toList());

                forecastDetaiList.stream().forEach(x -> {
                    ForecastSourceCore forecastCorebyId = forecastSourceCoreService.getById(x.getForcastSourceCoreId());
                    if (forecastCorebyId != null) {
                        ForecastSourceCore core = new ForecastSourceCore();
                        core.setId(x.getForcastSourceCoreId());
                        // 重新计算面积
                        List<Integer> sourceIds = forecastDetaiListAll.stream().filter(s -> Objects.equals(s.getForcastSourceCoreId(), x.getForcastSourceCoreId()))
                                .map(ForecastSourceDetail::getHeatSourceId).collect(Collectors.toList());
                        sourceIds.remove(e.getId());
                        List<SourceFirstNetBaseView> elseSourceInfo = sourceFirstNetBaseViewAll.stream().filter(s -> sourceIds.contains(s.getHeatSourceId())).collect(Collectors.toList());
                        BigDecimal elseSumArea = elseSourceInfo.stream().map(SourceFirstNetBaseView::getHeatSourceArea).reduce(BigDecimal.ZERO, BigDecimal::add);
                        core.setHeatArea(elseSumArea.add(e.getHeatArea()));
                        updateForecastSourceCoreList.add(core);
                    }

                });
            }
        });
        if (!CollectionUtils.isEmpty(updateForecastSourceCoreList)) {
            if (!forecastSourceCoreService.updateBatchById(updateForecastSourceCoreList)) {
                log.error("updateHeatSource---编辑热源数据--平台批量更改同步负荷预测热源面积异常！--{} ", updateForecastSourceCoreList.toString());
            }
        }


    }
}
