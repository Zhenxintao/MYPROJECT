package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.converter.HeatSystemConverter;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatSystemInitDto;
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
 * @ClassName: SyncHeatSystemService
 * @Description: 系统数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncHeatSystemService {


    @Autowired
    private HeatCabinetService heatCabinetService;

    @Autowired
    private HeatSystemConverter heatSystemConverter;

    @Autowired
    private HeatSystemService heatSystemService;

    @Autowired
    private PointConfigService pointConfigService;

    /**
     * @Description 添加系统数据
     */
    public Response addHeatSystem(List<HeatSystemInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatSystem> listAdd = new ArrayList<>();
            for (HeatSystemInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加系统数据--同步编号为空！");
                    log.error("addHeatSystem---添加 {} 控制柜的的系统数据，同步编号为空！",dto.getParentNum());
                    continue;
                }
                if (StringUtils.isBlank(dto.getName())) {
                    errMsg.add("添加系统数据--同步编号--" + dto.getNum() + " 的名称为空！");
                    log.error("addHeatSystem---添加 {} 控制柜的的系统数据，同步编号为 {} 的系统名称为空！",dto.getParentNum(),dto.getNum() );
                    continue;
                }
                if (dto.getParentNum() == null) {
                    errMsg.add("添加系统数据--同步编号--" + dto.getNum() + " 的所属控制柜为空！");
                    log.error("addHeatSystem---添加 {} 的系统数据，所属控制柜为空！",dto.getNum() );
                    continue;
                }
                HeatSystem loadHeatSystem = heatSystemService.getOne(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getSyncNumber, dto.getNum()));
                if (loadHeatSystem != null) {
                    // 不能重复添加
                    continue;
                }


                HeatSystem addHeatSystem = heatSystemConverter.INSTANCE.domain2dto(dto);
                addHeatSystem.setSyncNumber(dto.getNum());
                addHeatSystem.setSyncParentNum(dto.getParentNum());
                addHeatSystem.setCreateUser("数据同步");
                addHeatSystem.setCreateTime(LocalDateTime.now());
                addHeatSystem.setUserId(-1);
                // 处理所属控制柜
                HeatCabinet loadHeatCabinet = heatCabinetService.getOne(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getSyncNumber, dto.getParentNum()));
                if (loadHeatCabinet == null) {
                    errMsg.add("添加系统数据--平台没有查询到同步编号为--" + dto.getParentNum() + " 的控制柜数据！");
                    log.error("addHeatSystem---添加 {} 的系统数据，平台没有查询到同步编号为 {} 的控制柜数据！",dto.getNum(), dto.getParentNum());
                    continue;
                }
                addHeatSystem.setHeatCabinetId(loadHeatCabinet.getId());

                listAdd.add(addHeatSystem);

            }
            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加
                if (!heatSystemService.saveBatch(listAdd)) {
                    errMsg.add("添加系统数据--平台批量添加系统数据异常！");
                    log.error("addHeatSystem---批量添加系统数据异常！--{}",listAdd.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑系统数据
     */
    public Response updateHeatSystem(List<HeatSystemInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatSystem> updateHeatSystems = new ArrayList<>();
            for (HeatSystemInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑系统数据--同步编号为空！");
                    log.error("updateHeatSystem---编辑 {} 控制柜的的系统数据，同步编号为空！",dto.getParentNum());
                    continue;
                }
                HeatSystem loadHeatSystem = heatSystemService.getOne(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getSyncNumber, dto.getNum()));
                if (loadHeatSystem == null) {
                    errMsg.add("编辑系统数据--平台没有查询到同步编号为--" + dto.getNum() + " 的系统数据！");
                    log.error("updateHeatSystem---编辑 {} 控制柜的的系统数据，平台没有查询到同步编号为 {} 的系统数据！",dto.getParentNum(), dto.getNum());
                    continue;
                }

                HeatSystem updateHeatSystem = heatSystemConverter.INSTANCE.domain2dto(dto);
                updateHeatSystem.setId(loadHeatSystem.getId());
                updateHeatSystem.setUpdateTime(LocalDateTime.now());
                // 更改所属关系
                if (dto.getParentNum() != null && dto.getParentNum() != loadHeatSystem.getSyncParentNum()) {
                    HeatCabinet loadHeatCabinet = heatCabinetService.getOne(Wrappers.<HeatCabinet>lambdaQuery().eq(HeatCabinet::getSyncNumber, dto.getParentNum()));
                    if (loadHeatCabinet == null) {
                        errMsg.add("编辑系统数据--平台没有查询到同步编号为--" + dto.getParentNum() + " 的控制柜数据！");
                        log.error("updateHeatSystem---编辑 {} 系统数据，平台没有查询到同步编号为 {} 的控制柜数据！", dto.getNum(),dto.getParentNum());

                        continue;
                    }
                    updateHeatSystem.setHeatCabinetId(loadHeatCabinet.getId());
                }
                updateHeatSystems.add(updateHeatSystem);
            }

            if (!CollectionUtils.isEmpty(updateHeatSystems)) {
                // 批量修改
                if (!heatSystemService.updateBatchById(updateHeatSystems)) {
                    errMsg.add("编辑系统数据--平台批量修改系统数据异常！");
                    log.error("updateHeatSystem---批量修改系统数据异常！--{}", updateHeatSystems.toString());

                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    /**
     * @Description 删除系统数据
     */
    public Response delHeatSystem(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delHeatSystemList = new ArrayList<>();
            for (Integer num : numList) {
                HeatSystem loadHeatSystem = heatSystemService.getOne(Wrappers.<HeatSystem>lambdaQuery().eq(HeatSystem::getSyncNumber, num));
                if (loadHeatSystem == null) {
                    errMsg.add("删除系统数据--平台没有查询到同步编号为--" + num + " 的系统数据！");
                    log.error("delHeatSystem---删除系统数据，平台没有查询到同步编号为 {} 的系统数据！", num);

                    continue;
                }
                delHeatSystemList.add(loadHeatSystem.getId());
            }
            if (!CollectionUtils.isEmpty(delHeatSystemList)) {
                // 同步系统、点数据
                pointConfigService.remove(Wrappers.<PointConfig>lambdaQuery().in(PointConfig::getRelevanceId, delHeatSystemList));
                heatSystemService.removeByIds(delHeatSystemList);
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


}
