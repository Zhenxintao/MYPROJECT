package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.converter.HeatCabinetConverter;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatCabinetInitDto;
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
import java.util.stream.Collectors;

/**
 * @ClassName: SyncHeatCabinetService
 * @Description: 控制柜数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncHeatCabinetService {


    @Autowired
    private HeatSourceService heatSourceService;

    @Autowired
    private HeatTransferStationService heatTransferStationService;

    @Autowired
    private HeatCabinetService heatCabinetService;

    @Autowired
    private HeatCabinetConverter heatCabinetConverter;

    @Autowired
    private HeatSystemService heatSystemService;

    @Autowired
    private PointConfigService pointConfigService;

    /**
     * @Description 添加控制柜数据
     */
    public Response addHeatCabinet(List<HeatCabinetInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatCabinet> listAdd = new ArrayList<>();
            for (HeatCabinetInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加控制柜数据--同步编号为空！");
                    log.error("addHeatCabinet---添加 {} 的控制柜数据--同步编号为空！",dto.getParentNum());
                    continue;
                }
                if (StringUtils.isBlank(dto.getName())) {
                    errMsg.add("添加控制柜数据--同步编号--" + dto.getNum() + " 的名称为空！");
                    log.error("addHeatCabinet---添加 {} 的控制柜数据--同步编号 {} 的名称为空！",dto.getParentNum(),dto.getNum());
                    continue;
                }
                if (dto.getType() == null || dto.getParentNum() == null) {
                    errMsg.add("添加控制柜数据--同步编号--" + dto.getNum() + " 的所属类型为空！");
                    log.error("addHeatCabinet---添加 {} 的控制柜数据--同步编号 {} 的所属类型为空！",dto.getParentNum(),dto.getNum());
                    continue;
                }

                HeatCabinet loadHeatCabinet = heatCabinetService.getOne(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getSyncNumber, dto.getNum()));
                if (loadHeatCabinet != null) {
                    // 不能重复添加
                    continue;
                }

                HeatCabinet addHeatCabinet = heatCabinetConverter.INSTANCE.domain2dto(dto);
                addHeatCabinet.setSyncNumber(dto.getNum());
                addHeatCabinet.setSyncParentNum(dto.getParentNum());
                addHeatCabinet.setCreateUser("数据同步");
                addHeatCabinet.setCreateTime(LocalDateTime.now());
                addHeatCabinet.setUserId(-1);
                // 处理所属热力站 还是所属热源
                // 归属 1.热力站 2.热源 3.热网
                Integer parentNum = dto.getParentNum();
                if (dto.getType() == 1) {
                    // 所属热力站
                    HeatTransferStation loadHeatStation = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getSyncNumber, parentNum));
                    if (loadHeatStation == null) {
                        errMsg.add("添加控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热力站数据！");
                        log.error("addHeatCabinet---添加 {} 站的控制柜数据--同步编号 {} 的，平台没有查到 {} 站数据！",parentNum,dto.getNum(),parentNum);
                        continue;
                    }
                    addHeatCabinet.setHeatTransferStationId(loadHeatStation.getId());
                }
                if (dto.getType() == 2) {
                    // 所属热源
                    HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, parentNum));
                    if (loadHeatSource == null) {
                        errMsg.add("添加控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热源数据！");
                        log.error("addHeatCabinet---添加 {} 热源的控制柜数据--同步编号 {} 的，平台没有查到 {} 热源数据！",parentNum,dto.getNum(),parentNum);
                        continue;
                    }
                    addHeatCabinet.setHeatSourceId(loadHeatSource.getId());
                }
//                if (dto.getType() == 3) {
//                    // 所属热网
//                    HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, parentNum));
//                    if (loadHeatNet == null) {
//                        errMsg.add("添加控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热网数据！");
//                        continue;
//                    }
//                    addHeatCabinet.setHeatNetId(loadHeatNet.getId());
//                }

                listAdd.add(addHeatCabinet);

            }

            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加
                if (!heatCabinetService.saveBatch(listAdd)) {
                    errMsg.add("添加控制柜数据--平台批量添加控制柜数据异常！");
                    log.error("addHeatCabinet---批量添加数据异常-- {}",listAdd.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑控制柜数据
     */
    public Response updateHeatCabinet(List<HeatCabinetInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatCabinet> updateHeatCabinets = new ArrayList<>();
            for (HeatCabinetInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑控制柜数据--同步编号为空！");
                    log.error("updateHeatCabinet---编辑 {} 的控制柜数据--同步编号为空！",dto.getParentNum());
                    continue;
                }
                HeatCabinet loadHeatCabinet = heatCabinetService.getOne(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getSyncNumber, dto.getNum()));
                if (loadHeatCabinet == null) {
                    errMsg.add("编辑控制柜数据--平台没有查询到同步编号为--" + dto.getNum() + " 的控制柜数据！");
                    log.error("updateHeatCabinet---编辑 {} 的控制柜数据--平台没有查询到同步编号 {} 的控制柜数据！",dto.getParentNum(),dto.getNum());
                    continue;
                }
                HeatCabinet updateHeatCabinet = heatCabinetConverter.INSTANCE.domain2dto(dto);
                updateHeatCabinet.setId(loadHeatCabinet.getId());
                updateHeatCabinet.setUpdateTime(LocalDateTime.now());
                // 更改所属关系
                // 归属 1.热力站 2.热源 3.热网
                if (dto.getType() != null && dto.getParentNum() != null) {
                    Integer parentNum = dto.getParentNum();
                    if (dto.getType() == 1 && parentNum != loadHeatCabinet.getSyncParentNum()) {
                        HeatTransferStation loadHeatStation = heatTransferStationService.getOne(Wrappers.<HeatTransferStation>lambdaQuery().eq(HeatTransferStation::getSyncNumber, parentNum));
                        if (loadHeatStation == null) {
                            errMsg.add("编辑控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热力站数据！");
                            log.error("updateHeatCabinet---编辑 {} 的控制柜数据--平台没有查询到同步编号 {} 的热力站数据！",parentNum,dto.getNum());
                            continue;
                        }
                        updateHeatCabinet.setHeatTransferStationId(loadHeatStation.getId());
                    }
                    if (dto.getType() == 2 && parentNum != loadHeatCabinet.getSyncParentNum()) {
                        // 所属热源
                        HeatSource loadHeatSource = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getSyncNumber, parentNum));
                        if (loadHeatSource == null) {
                            errMsg.add("编辑控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热源数据！");
                            log.error("updateHeatCabinet---编辑 {} 的控制柜数据--平台没有查询到同步编号 {} 的热源数据！",parentNum,dto.getNum());
                            continue;
                        }
                        updateHeatCabinet.setHeatSourceId(loadHeatSource.getId());
                    }
//                    if (dto.getType() == 3 && parentNum != loadHeatCabinet.getSyncParentNum()) {
//                        // 所属热网
//                        HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, parentNum));
//                        if (loadHeatNet == null) {
//                            errMsg.add("编辑控制柜数据--平台没有查询到同步编号为--" + parentNum + " 的热网数据！");
//                            continue;
//                        }
//                        updateHeatCabinet.setHeatNetId(loadHeatNet.getId());
//                    }
                    updateHeatCabinet.setSyncParentNum(parentNum);
                }
                updateHeatCabinets.add(updateHeatCabinet);
            }

            if (!CollectionUtils.isEmpty(updateHeatCabinets)) {
                // 批量修改
                if (!heatCabinetService.updateBatchById(updateHeatCabinets)) {
                    errMsg.add("编辑控制柜数据--平台批量修改控制柜数据异常！");
                    log.error("updateHeatCabinet---批量更新数据异常！--{}",updateHeatCabinets.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    /**
     * @Description 删除控制柜数据
     */
    public Response delHeatCabinet(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delHeatCabinetList = new ArrayList<>();
            List<Integer> delHeatSystemList = new ArrayList<>();
            for (Integer num : numList) {
                HeatCabinet loadHeatCabinet = heatCabinetService.getOne(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getSyncNumber, num));
                if (loadHeatCabinet == null) {
                    errMsg.add("删除控制柜数据--平台没有查询到同步编号为--" + num + " 的控制柜数据！");
                    log.error("delHeatCabinet---删除控制柜数据,平台没有查询到同步编号为 {} 的控制柜数据！",num);
                    continue;
                }
                delHeatCabinetList.add(loadHeatCabinet.getId());
                // 同步控制柜下面的系统、点数据
                // 删除系统
                List<HeatSystem> listSystem = heatSystemService.list(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getHeatCabinetId, loadHeatCabinet.getId()));
                if (!CollectionUtils.isEmpty(listSystem)) {
                    List<Integer> systemIds = listSystem.stream().map(e -> e.getId()).collect(Collectors.toList());
                    delHeatSystemList.addAll(systemIds);
                }
            }
            if (!CollectionUtils.isEmpty(delHeatCabinetList)) {
                // 同步删除站下面的控制柜、系统、点数据
                pointConfigService.remove(Wrappers.<PointConfig>lambdaQuery().in(PointConfig::getRelevanceId, delHeatSystemList));
                heatSystemService.removeByIds(delHeatSystemList);
                heatCabinetService.removeByIds(delHeatCabinetList);
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
//                log.error("delHeatCabinet---删除控制柜数据,异常数据--{}",errMsg.toString());
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


}
