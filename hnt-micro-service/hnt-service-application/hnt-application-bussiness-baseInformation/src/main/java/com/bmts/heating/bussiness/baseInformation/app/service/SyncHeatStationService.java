package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.converter.HeatTransferStationConverter;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatStationInitDto;
import com.bmts.heating.commons.utils.restful.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: SyncHeatStationService
 * @Description: 热力站数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncHeatStationService {

    @Autowired
    private HeatOrganizationService heatOrganizationService;


    @Autowired
    private HeatSourceService heatSourceService;

    @Autowired
    private HeatTransferStationService heatTransferStationService;

    @Autowired
    private HeatTransferStationConverter heatTransferStationConverter;

    @Autowired
    private HeatCabinetService heatCabinetService;

    @Autowired
    private HeatSystemService heatSystemService;

    @Autowired
    private PointConfigService pointConfigService;

    @Autowired
    private HeatDeviceConfigService heatDeviceConfigService;

    @Autowired
    private DeviceConfigService deviceConfigService;

    /**
     * @Description 添加热力站数据
     */
    public Response addHeatStation(List<HeatStationInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatTransferStation> listAdd = new ArrayList<>();
            for (HeatStationInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加热力站数据--同步编号为空！");
                    log.error("addHeatStation---添加热力站数据{} 的--同步编号为空！", dto.getName());
                    continue;
                }
                if (StringUtils.isBlank(dto.getName())) {
                    errMsg.add("添加热力站数据--同步编号--" + dto.getNum() + " 的名称为空！");
                    log.error("addHeatStation---添加热力站数据{} 的名称为空！", dto.getNum());
                    continue;
                }
                HeatTransferStation loadHeatStation = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getSyncNumber, dto.getNum()));
                if (loadHeatStation != null) {
                    // 不能重复添加
                    continue;
                }

                HeatTransferStation addHeatStation = new HeatTransferStation();
                addHeatStation.setName(dto.getName());
                addHeatStation.setSyncNumber(dto.getNum());
                addHeatStation.setCreateUser("数据同步");
                addHeatStation.setCreateTime(LocalDateTime.now());
                addHeatStation.setUserId(-1);
                //首字母简拼
                addHeatStation.setLogogram(PinYinUtils.toFirstChar(dto.getName()));
                if (StringUtils.isNotBlank(dto.getCode())) {
                    addHeatStation.setCode(dto.getCode());
                }
                if (StringUtils.isNotBlank(dto.getAddress())) {
                    addHeatStation.setAddress(dto.getAddress());
                }
                if (dto.getBuildTime() != null) {
                    addHeatStation.setBuildTime(dto.getBuildTime());
                }
                if (dto.getTransformTime() != null) {
                    addHeatStation.setTransformTime(dto.getTransformTime());
                }
                if (dto.getHeatArea() != null) {
                    addHeatStation.setHeatArea(dto.getHeatArea());
                }
                if (dto.getNetArea() != null) {
                    addHeatStation.setNetArea(dto.getNetArea());
                }
                if (dto.getLongitude() != null) {
                    addHeatStation.setLongitude(dto.getLongitude());
                }
                if (dto.getLatitude() != null) {
                    addHeatStation.setLatitude(dto.getLatitude());
                }
                if (StringUtils.isNotBlank(dto.getPrincipal())) {
                    addHeatStation.setPrincipal(dto.getPrincipal());
                }
                if (StringUtils.isNotBlank(dto.getPhone())) {
                    addHeatStation.setPhone(dto.getPhone());
                }
                if (dto.getStatus() != null) {
                    addHeatStation.setStatus(dto.getStatus());
                }
//                if (dto.getBuildType() != null) {
//                    addHeatStation.setBuildType(dto.getBuildType());
//                }
//                if (dto.getIsLifeWater() != null) {
//                    addHeatStation.setIsLifeWater(dto.getIsLifeWater());
//                }
//                if (dto.getInsulationConstruction() != null) {
//                    addHeatStation.setInsulationConstruction(dto.getInsulationConstruction());
//                }
                if (dto.getHeatType() != null) {
                    addHeatStation.setHeatType(dto.getHeatType());
                }
                if (dto.getManageType() != null) {
                    addHeatStation.setManageType(dto.getManageType());
                }
//                if (dto.getPipingLayout() != null) {
//                    addHeatStation.setPipingLayout(dto.getPipingLayout());
//                }
//                if (dto.getPayType() != null) {
//                    addHeatStation.setPayType(dto.getPayType());
//                }
                if (dto.getStationType() != null) {
                    addHeatStation.setStationType(dto.getStationType());
                    if (dto.getStationType() == 3) {
                        addHeatStation.setIsLifeWater(true);
                    } else {
                        addHeatStation.setIsLifeWater(false);
                    }
                }
                if (StringUtils.isNotBlank(dto.getTerrain())) {
                    addHeatStation.setTerrain(dto.getTerrain());
                }
                if (dto.getDistanceWithHeatSource() != null) {
                    addHeatStation.setDistanceWithHeatSource(dto.getDistanceWithHeatSource());
                }
                if (dto.getAltitude() != null) {
                    addHeatStation.setAltitude(dto.getAltitude());
                }
                if (dto.getValveControlType() != null) {
                    addHeatStation.setValveControlType(dto.getValveControlType());
                }
                // 处理所属公司
                Integer organizeSyncNum = dto.getHeatOrganizationId();
                if (organizeSyncNum == null) {
                    errMsg.add("添加热力站数据--同步编号为--" + dto.getNum() + " 的公司组织id为空！");
                    log.error("addHeatStation---添加 {} 热力站数据的公司组织id为空！", dto.getNum());
                    continue;
                }
                HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, organizeSyncNum));
                if (loadOrganize == null) {
                    errMsg.add("添加热力站数据--平台没有查询到同步编号为--" + organizeSyncNum + " 的组织结构数据！");
                    log.error("addHeatStation---添加 {} 热力站数据,平台没有查询到同步编号为 {} 的组织结构数据！", dto.getNum(), organizeSyncNum);
                    continue;
                }
                addHeatStation.setHeatOrganizationId(loadOrganize.getId());
                // 处理 所的组织结构
//                Integer viceOrgSyncNum = dto.getViceOrgId();
//                if (viceOrgSyncNum == null) {
//                    errMsg.add("添加热力站数据--同步编号为--" + dto.getNum() + " 的所的组织id为空！");
//                    continue;
//                }
//                HeatOrganization viceOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, viceOrgSyncNum));
//                if (viceOrganize == null) {
//                    errMsg.add("添加热力站数据--平台没有查询到同步编号为--" + viceOrgSyncNum + " 的所结构数据！");
//                    continue;
//                }
//                addHeatStation.setViceOrgId(viceOrganize.getId());


                // 处理所属热源
                Integer heatSourceSyncNum = dto.getHeatSourceId();
                if (heatSourceSyncNum == null) {
                    errMsg.add("添加热力站数据--同步编号为--" + dto.getNum() + " 的所属热源id为空！");
                    log.error("addHeatStation---添加 {} 热力站数据的所属热源id为空！", dto.getNum());
                    continue;
                }
                HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, heatSourceSyncNum));
                if (loadHeatSource == null) {
                    errMsg.add("添加热力站数据--平台没有查询到同步编号为--" + heatSourceSyncNum + " 的热源数据！");
                    log.error("addHeatStation---添加 {} 热力站数据,平台没有查询到同步编号为 {} 的热源数据！", dto.getNum(), heatSourceSyncNum);
                    continue;
                }
                addHeatStation.setHeatSourceId(loadHeatSource.getId());


                listAdd.add(addHeatStation);


            }
            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加
                if (!heatTransferStationService.saveBatch(listAdd)) {
                    errMsg.add("添加热力站数据--平台批量添加热力站数据异常！");
                    log.error("addHeatStation---批量添加热力站数据异常！--{}", listAdd.toString());
                } else {
                    // 批量热力站添加成功，进行关联设置绑定
                    List<DeviceConfig> pvssDeviceList = deviceConfigService.list(Wrappers.<DeviceConfig>lambdaQuery().eq(DeviceConfig::getName, "PVSS"));
                    if (!CollectionUtils.isEmpty(pvssDeviceList)) {
                        Map<Integer, Integer> countMap = new HashMap<>();
                        pvssDeviceList.stream().forEach(e -> {
                            int count = heatDeviceConfigService.count(Wrappers.<HeatDeviceConfig>lambdaQuery().eq(HeatDeviceConfig::getDeviceConfigId, e.getId()));
                            countMap.put(e.getId(), count);
                        });
                        // 开始绑定关联
                        LocalDateTime nowTime = LocalDateTime.now();
                        List<HeatDeviceConfig> addHeatDeviceConfigList = new ArrayList<>();
                        listAdd.stream().forEach(e -> {
                            HeatDeviceConfig add = new HeatDeviceConfig();
                            add.setCreateTime(nowTime);
                            // 类型：1、所属换热站    2、所属热源
                            add.setType(1);
                            add.setRelevanceId(e.getId());
                            // 取出最小的 值
                            Integer countMin = countMap.values().stream().sorted().findFirst().get();
                            // 取出最小值对应的key
                            Integer deviceConfigId = countMap.entrySet().stream()
                                    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), countMin))
                                    .map(Map.Entry::getKey)
                                    .findFirst().get();
                            if (deviceConfigId == null) {
                                return;
                            }
                            add.setDeviceConfigId(deviceConfigId);
                            addHeatDeviceConfigList.add(add);

                            countMap.put(deviceConfigId, countMin + 1);

                        });
                        if (!CollectionUtils.isEmpty(addHeatDeviceConfigList)) {
                            if (!heatDeviceConfigService.saveBatch(addHeatDeviceConfigList)) {
                                log.error("addHeatStation---批量添加热力站关联数据异异常！--{}", addHeatDeviceConfigList.toString());
                            }
                        }
                    }

                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑热力站数据
     */
    public Response updateHeatStation(List<HeatStationInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatTransferStation> updateHeatStations = new ArrayList<>();
            for (HeatStationInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑热力站数据--同步编号为空！");
                    log.error("updateHeatStation---编辑 {} 热力站数据,同步编号为空！", dto.getName());
                    continue;
                }
                HeatTransferStation loadHeatStation = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getSyncNumber, dto.getNum()));
                if (loadHeatStation == null) {
                    errMsg.add("编辑热力站数据--平台没有查询到同步编号为--" + dto.getNum() + " 的热力站数据！");
                    log.error("updateHeatStation---编辑热力站数据,平台没有查询到同步编号为 {} 的热力站数据！", dto.getNum());
                    continue;
                }
                HeatTransferStation updateHeatStation = heatTransferStationConverter.INSTANCE.dtoToPo(dto);
                updateHeatStation.setId(loadHeatStation.getId());
                updateHeatStation.setUpdateTime(LocalDateTime.now());

                if (dto.getStationType() == 3) {
                    updateHeatStation.setIsLifeWater(true);
                } else {
                    updateHeatStation.setIsLifeWater(false);
                }


                // 处理所属公司
                if (dto.getHeatOrganizationId() != null && dto.getHeatOrganizationId() != loadHeatStation.getHeatOrganizationId()) {
                    HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, dto.getHeatOrganizationId()));
                    if (loadOrganize == null) {
                        errMsg.add("编辑热力站数据--平台没有查询到同步编号为--" + dto.getHeatOrganizationId() + " 的公司组织结构数据！");
                        log.error("updateHeatStation---编辑 {} 热力站数据,平台没有查询到同步编号为 {} 的公司组织结构数据！", dto.getNum(), dto.getHeatOrganizationId());
                        continue;
                    }
                    updateHeatStation.setHeatOrganizationId(loadOrganize.getId());
                }

                // 处理所属 所的组织结构
//                if (dto.getViceOrgId() != null && dto.getViceOrgId() != loadHeatStation.getViceOrgId()) {
//                    HeatOrganization viceOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, dto.getViceOrgId()));
//                    if (viceOrganize == null) {
//                        errMsg.add("编辑热力站数据--平台没有查询到同步编号为--" + dto.getViceOrgId() + " 的所组织结构数据！");
//                        continue;
//                    }
//                    updateHeatStation.setViceOrgId(viceOrganize.getId());
//                }


                // 处理所属热源
                if (dto.getHeatSourceId() != null && dto.getHeatSourceId() != loadHeatStation.getHeatSourceId()) {
                    HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, dto.getHeatSourceId()));
                    if (loadHeatSource == null) {
                        errMsg.add("编辑热力站数据--平台没有查询到同步编号为--" + dto.getHeatSourceId() + " 的热源数据！");
                        log.error("updateHeatStation---编辑 {} 热力站数据,平台没有查询到同步编号为 {} 的热源数据！", dto.getNum(), dto.getHeatSourceId());
                        continue;
                    }
                    updateHeatStation.setHeatSourceId(loadHeatSource.getId());
                }
                updateHeatStations.add(updateHeatStation);
            }

            if (!CollectionUtils.isEmpty(updateHeatStations)) {
                // 批量修改
                if (!heatTransferStationService.updateBatchById(updateHeatStations)) {
                    errMsg.add("编辑热力站数据--平台批量修改热力站数据异常！");
                    log.error("updateHeatStation---批量修改热力站数据异常！--{}", updateHeatStations.toString());
                }
            }

            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    /**
     * @Description 删除热力站数据
     */
    public Response delHeatStation(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delHeatStationList = new ArrayList<>();
            List<Integer> delHeatCabinetList = new ArrayList<>();
            List<Integer> delHeatSystemList = new ArrayList<>();
            for (Integer num : numList) {
                HeatTransferStation loadHeatStation = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getSyncNumber, num));
                if (loadHeatStation == null) {
                    errMsg.add("删除热力站数据--平台没有查询到同步编号为--" + num + " 的热力站数据！");
                    log.error("delHeatStation---删除热力站数据,平台没有查询到同步编号为 {} 的热力站数据！", num);
                    continue;
                }
                delHeatStationList.add(loadHeatStation.getId());
                // 同步删除站下面的控制柜、系统、点数据
                // 删除控制柜
                List<HeatCabinet> listCabinet = heatCabinetService.list(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getHeatTransferStationId, loadHeatStation.getId()));
                if (!CollectionUtils.isEmpty(listCabinet)) {
                    List<Integer> cabinetIds = listCabinet.stream().map(e -> e.getId()).collect(Collectors.toList());
                    delHeatCabinetList.addAll(cabinetIds);
                    // 删除系统
                    List<HeatSystem> listSystem = heatSystemService.list(Wrappers.<HeatSystem>lambdaQuery().in(HeatSystem::getHeatCabinetId, cabinetIds));
                    if (!CollectionUtils.isEmpty(listSystem)) {
                        List<Integer> systemIds = listSystem.stream().map(e -> e.getId()).collect(Collectors.toList());
                        delHeatSystemList.addAll(systemIds);
                    }

                }
            }
            if (!CollectionUtils.isEmpty(delHeatStationList)) {
                // 同步删除站下面的控制柜、系统、点数据
                pointConfigService.remove(Wrappers.<PointConfig>lambdaQuery().in(PointConfig::getRelevanceId, delHeatSystemList));
                heatSystemService.removeByIds(delHeatSystemList);
                heatCabinetService.removeByIds(delHeatCabinetList);
                heatTransferStationService.removeByIds(delHeatStationList);
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


}
