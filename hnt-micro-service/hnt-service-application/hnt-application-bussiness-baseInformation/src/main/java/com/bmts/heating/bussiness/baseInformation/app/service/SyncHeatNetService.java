package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.db.service.HeatNetService;
import com.bmts.heating.commons.db.service.HeatNetSourceService;
import com.bmts.heating.commons.db.service.HeatOrganizationService;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatNetInitDto;
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
 * @ClassName: SyncHeatNetService
 * @Description: 热网数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncHeatNetService {

    @Autowired
    private HeatOrganizationService heatOrganizationService;

    @Autowired
    private HeatNetService heatNetService;

    @Autowired
    private HeatNetSourceService heatNetSourceService;


    /**
     * @Description 添加热网数据
     */
    public Response addHeatNet(List<HeatNetInitDto> addList) {
        if (!CollectionUtils.isEmpty(addList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatNet> listAdd = new ArrayList<>();
            for (HeatNetInitDto dto : addList) {
                if (dto.getNum() == null) {
                    errMsg.add("添加热网数据--同步编号为空！");
                    log.error("addHeatNet---添加热网数据,同步编号为空！");
                    continue;
                }
                if (StringUtils.isBlank(dto.getName())) {
                    errMsg.add("添加热网数据--同步编号--" + dto.getNum() + " 热网名称为空！");
                    log.error("addHeatNet---添加热网数据,同步编号 {} 的热网名称为空！",dto.getNum());
                    continue;
                }

                HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, dto.getNum()));
                if (loadHeatNet != null) {
                    // 不能重复添加
                    continue;
                }

                HeatNet addHeatNet = new HeatNet();
                addHeatNet.setName(dto.getName());
                addHeatNet.setSyncNumber(dto.getNum());
                addHeatNet.setCreateUser("数据同步");
                addHeatNet.setCreateTime(LocalDateTime.now());
                addHeatNet.setUserId(-1);
                //首字母简拼
                addHeatNet.setLogogram(PinYinUtils.toFirstChar(dto.getName()));
                if (StringUtils.isNotBlank(dto.getCode())) {
                    addHeatNet.setCode(dto.getCode());
                }
//                if (StringUtils.isNotBlank(dto.getAddress())) {
//                    addHeatNet.setAddress(dto.getAddress());
//                }
                if (dto.getType() != null) {
                    addHeatNet.setType(dto.getType());
                }
                if (dto.getHeatArea() != null) {
                    addHeatNet.setHeatArea(dto.getHeatArea());
                }
                if (dto.getFlag() != null) {
                    addHeatNet.setFlag(dto.getFlag());
                }
                // 处理所属公司
                int organizeSyncNum = dto.getHeatOrganizationId();
                if (organizeSyncNum <= 0) {
                    errMsg.add("添加热网数据--同步编号为--" + dto.getNum() + " 的所属组织id为空！");
                    log.error("addHeatNet---添加热网数据,同步编号 {} 的热网所属组织id为空！",dto.getNum());
                    continue;
                }
                HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, organizeSyncNum));
                if (loadOrganize == null) {
                    errMsg.add("添加热网数据--平台没有查询到同步编号为--" + organizeSyncNum + " 的组织结构数据！");
                    log.error("addHeatNet---添加 {} 热网数据,平台没有查询到同步编号为 {} 的组织结构数据！",dto.getNum(),organizeSyncNum);
                    continue;
                }
                addHeatNet.setHeatOrganizationId(loadOrganize.getId());

                listAdd.add(addHeatNet);

            }
            if (!CollectionUtils.isEmpty(listAdd)) {
                // 批量添加
                if (!heatNetService.saveBatch(listAdd)) {
                    errMsg.add("添加热网数据--平台批量添加热网数据异常！");
                    log.error("addHeatNet---批量添加热网数据异常！--{}",listAdd.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑热网数据
     */
    public Response updateHeatNet(List<HeatNetInitDto> updateList) {
        if (!CollectionUtils.isEmpty(updateList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatNet> listUpdate = new ArrayList<>();
            for (HeatNetInitDto dto : updateList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑热网数据--同步编号为空！");
                    log.error("updateHeatNet---编辑热网数据,同步编号为空！");
                    continue;
                }
                HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, dto.getNum()));
                if (loadHeatNet == null) {
                    errMsg.add("编辑热网数据--平台没有查询到同步编号为--" + dto.getNum() + " 的热网数据！");
                    log.error("updateHeatNet---编辑热网数据,平台没有查询到同步编号为 {} 的热网数据！",dto.getNum());
                    continue;
                }
                HeatNet updateHeatNet = new HeatNet();
                updateHeatNet.setId(loadHeatNet.getId());
                updateHeatNet.setUpdateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(dto.getName())) {
                    updateHeatNet.setName(dto.getName());
                    //首字母简拼
                    updateHeatNet.setLogogram(PinYinUtils.toFirstChar(dto.getName()));
                }
                if (StringUtils.isNotBlank(dto.getCode())) {
                    updateHeatNet.setCode(dto.getCode());
                }
                if (dto.getType() != null) {
                    updateHeatNet.setType(dto.getType());
                }
                if (dto.getHeatArea() != null) {
                    updateHeatNet.setHeatArea(dto.getHeatArea());
                }
                if (dto.getFlag() != null) {
                    updateHeatNet.setFlag(dto.getFlag());
                }

                // 处理所属公司
                int organizeSyncNum = dto.getHeatOrganizationId();
                if (organizeSyncNum > 0) {
                    HeatOrganization loadOrganize = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, organizeSyncNum));
                    if (loadOrganize == null) {
                        errMsg.add("编辑热网数据--平台没有查询到同步编号为--" + organizeSyncNum + " 的组织结构数据！");
                        log.error("updateHeatNet---编辑 {} 热网数据,平台没有查询到同步编号为 {} 的组织结构数据！",dto.getNum(),organizeSyncNum);
                        continue;
                    }
                    updateHeatNet.setHeatOrganizationId(loadOrganize.getId());
                }
                listUpdate.add(updateHeatNet);
            }
            if (!CollectionUtils.isEmpty(listUpdate)) {
                // 批量修改
                if (!heatNetService.updateBatchById(listUpdate)) {
                    errMsg.add("编辑热网数据--平台批量修改热网数据异常！");
                    log.error("updateHeatNet---批量修改热网数据异常！--{}",listUpdate.toString());
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    /**
     * @Description 删除热网数据
     */
    public Response delHeatNet(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delList = new ArrayList<>();
            for (Integer num : numList) {
                HeatNet loadHeatNet = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getSyncNumber, num));
                if (loadHeatNet == null) {
                    errMsg.add("删除热网数据--平台没有查询到同步编号为--" + num + " 的热网数据！");
                    log.error("delHeatNet---删除热网数据,平台没有查询到同步编号为 {} 的热网数据！",num);
                    continue;
                }
                delList.add(loadHeatNet.getId());
            }
            if (!CollectionUtils.isEmpty(delList)) {
                // 进行批量删除
                if (heatNetService.removeByIds(delList)) {
                    // 进行关联删除
                    heatNetSourceService.remove(Wrappers.<HeatNetSource>lambdaQuery().in(HeatNetSource::getHeatNetId, delList));
                } else {
                    errMsg.add("删除热网数据--批量删除热网数据异常！");
                    log.error("delHeatNet---批量关联删除热网数据异常！--{}",delList.toString());
                }
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


}
