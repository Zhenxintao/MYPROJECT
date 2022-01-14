package com.bmts.heating.bussiness.baseInformation.app.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.db.service.HeatOrganizationService;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.AddHeatOrganizationInitDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatOrganizationInitDto;
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
 * @ClassName: SyncOrganization
 * @Description: 组织结构数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
@Slf4j
public class SyncOrganizationService {

    @Autowired
    private HeatOrganizationService heatOrganizationService;


    /**
     * @Description 添加组织机构数据
     */
    public Response addHeatOrganization(AddHeatOrganizationInitDto heatOrganDto) {
        if (!CollectionUtils.isEmpty(heatOrganDto.getOrganizationList()) && heatOrganDto.getLevelTotal() >= 1) {
            Set<String> errMsg = new HashSet<>();
            List<HeatOrganizationInitDto> organizationList = heatOrganDto.getOrganizationList();
            for (int i = 1; i < heatOrganDto.getLevelTotal() + 1; i++) {
                Set<String> msgSet = setOrganizatData(organizationList, i);
                if (!CollectionUtils.isEmpty(msgSet)) {
                    errMsg.addAll(msgSet);
                }
            }
            if (errMsg.size() > 0) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 编辑组织机构数据
     */
    public Response updateOrganization(List<HeatOrganizationInitDto> heatOrganList) {
        if (!CollectionUtils.isEmpty(heatOrganList)) {
            Set<String> errMsg = new HashSet<>();
            List<HeatOrganization> updateList = new ArrayList<>();
            for (HeatOrganizationInitDto dto : heatOrganList) {
                if (dto.getNum() == null) {
                    errMsg.add("编辑组织机构数据--同步编号为空!");
                    log.error("updateOrganization---编辑 {} 组织机构数据，同步编号为空!", dto.getName() );

                    continue;
                }
                HeatOrganization one = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, dto.getNum()));
                if (one == null) {
                    errMsg.add("编辑组织机构数据--平台没有查询到同步编号为--" + dto.getNum() + " 的，名称为" + dto.getName() + " 的组织结构数据！");
                    log.error("updateOrganization---编辑 {} 组织机构数据，平台没有查询到同步编号为 {}的组织结构数据！", dto.getName(), dto.getNum());
                    continue;
                }
                HeatOrganization update = new HeatOrganization();
                if (StringUtils.isNotBlank(dto.getName())) {
                    update.setName(dto.getName());
                }
                if (dto.getIsEnd() != null) {
                    update.setIsEnd(dto.getIsEnd());
                }
                if (dto.getLevel() != null) {
                    update.setLevel(dto.getLevel());
                }
                if (StringUtils.isBlank(dto.getDescription())) {
                    update.setDescription(dto.getDescription());
                }
                // 进行修改操作
                if (one.getSyncParentNum() != dto.getParentNum()) {
                    // 说明更改了上级  需要同步处理修改上级
                    // 根据 parentNum 查询 pid
                    HeatOrganization newParent = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, dto.getParentNum()));
                    if (newParent == null) {
                        errMsg.add("编辑组织机构数据--平台没有查询到同步父级编号为--" + dto.getParentNum() + " 的数据！");
                        log.error("updateOrganization---编辑 {} 组织机构数据，平台没有查询到同步父级编号为 {} 的数据！",dto.getNum(),dto.getParentNum());
                        continue;
                    }
                    update.setSyncParentNum(dto.getParentNum());
                    update.setPid(newParent.getId());
                    update.setCode(newParent.getCode() + ":" + one.getId());
                }
                update.setId(one.getId());
                update.setUpdateTime(LocalDateTime.now());
                updateList.add(update);
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                // 批量更新操作
                if (!heatOrganizationService.updateBatchById(updateList)) {
                    errMsg.add("编辑组织机构数据--平台批量更新组织结构数据异常！");
                    log.error("updateOrganization---批量更新组织结构数据异常！--{}",updateList.toString());

                }
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }

    /**
     * @Description 删除组织结构数据
     */
    public Response delOrganization(List<Integer> numList) {
        // 进行删除操作
        if (!CollectionUtils.isEmpty(numList)) {
            Set<String> errMsg = new HashSet<String>();
            List<Integer> delList = new ArrayList<>();
            // 根据syncNum 查询id,然后根据id,进行删除
            for (Integer num : numList) {
                HeatOrganization one = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, num));
                if (one == null) {
                    errMsg.add("删除组织机构数据--平台没有查询到同步编号为--" + num + " 的数据！");
                    log.error("delOrganization---删除组织机构数据--平台没有查询到同步编号为 {} 的数据！",num);
                    continue;
                }
                delList.add(one.getId());
            }
            if (!CollectionUtils.isEmpty(delList)) {
                if (!heatOrganizationService.removeByIds(delList)) {
                    errMsg.add("删除组织机构数据--批量删除组织结构数据异常！");
                    log.error("delOrganization---批量删除组织结构数据异常！--{}",delList.toString());
                }
            }
            if (!CollectionUtils.isEmpty(errMsg)) {
                return Response.fail(errMsg.toString());
            }
        }
        return Response.success();
    }


    private Set<String> setOrganizatData(List<HeatOrganizationInitDto> organizationList, int i) {
        Set<String> setMsg = new HashSet<String>();
        int finalI = i;
        if (i == 1) {
            List<HeatOrganizationInitDto> parentCollect = organizationList.stream().filter(e -> e.getLevel() == finalI).collect(Collectors.toList());
            // 先取出是最高父级的数据
            for (HeatOrganizationInitDto dto : parentCollect) {
                HeatOrganization heatOrganization = new HeatOrganization();
                heatOrganization.setCreateUser("数据同步");
                heatOrganization.setCreateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(dto.getDescription())) {
                    heatOrganization.setDescription(dto.getDescription());
                }
                if (StringUtils.isBlank(dto.getName())) {
                    setMsg.add("同步组织机构数据--同步编号---" + dto.getNum() + "---组织结构名称为空!");
                    log.error("addHeatOrganization---添加 {} 组织机构数据，组织结构名称为空!",dto.getNum());
                    continue;
                }
                heatOrganization.setName(dto.getName());
                heatOrganization.setIsEnd(dto.getIsEnd());
                heatOrganization.setLevel(dto.getLevel());
                heatOrganization.setSyncNumber(dto.getNum());
                heatOrganization.setPid(0);
                heatOrganization.setUserId(-1);
                heatOrganization.setSyncParentNum(dto.getParentNum());


                QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pid", 0);
                queryWrapper.eq("syncNumber", dto.getNum());
                queryWrapper.eq("syncParentNum", dto.getParentNum());
                HeatOrganization oneHeatOrganization = heatOrganizationService.getOne(queryWrapper);
                if (oneHeatOrganization == null) {

                    if (heatOrganizationService.save(heatOrganization)) {
                        Integer insertId = heatOrganization.getId();
                        if (insertId > 0) {
                            HeatOrganization update = new HeatOrganization();
                            update.setId(insertId);
                            update.setCode("root:" + insertId);
                            heatOrganizationService.updateById(update);
                        }


                    }


                }


            }
        } else {
            // 取出子级数据
            List<HeatOrganizationInitDto> collect = organizationList.stream().filter(e -> e.getLevel() == finalI).collect(Collectors.toList());
            for (HeatOrganizationInitDto dto : collect) {
                HeatOrganization heatOrganization = new HeatOrganization();
                heatOrganization.setCreateUser("数据同步");
                heatOrganization.setCreateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(dto.getDescription())) {
                    heatOrganization.setDescription(dto.getDescription());
                }
                if (StringUtils.isBlank(dto.getName())) {
                    setMsg.add("同步组织机构数据--同步编号---" + dto.getNum() + "---组织结构名称为空!");
                    log.error("addHeatOrganization---添加 {} 组织机构数据，组织结构名称为空!",dto.getNum());
                    continue;
                }
                heatOrganization.setName(dto.getName());
                heatOrganization.setIsEnd(dto.getIsEnd());
                heatOrganization.setLevel(dto.getLevel());
                if (dto.getNum() == null || dto.getParentNum() == null) {
                    setMsg.add("同步组织机构数据--" + dto.getName() + "--同步编号为空！或同步父级编号为空！");
                    log.error("addHeatOrganization---添加 {} 组织机构数据，同步编号为空！或同步父级编号为空！", dto.getName());
                    continue;
                }
                heatOrganization.setSyncNumber(dto.getNum());
                heatOrganization.setSyncParentNum(dto.getParentNum());
                heatOrganization.setUserId(-1);
                // 根据 parentNum 查询 pid
                HeatOrganization one = heatOrganizationService.getOne(Wrappers.<HeatOrganization>lambdaQuery().eq(HeatOrganization::getSyncNumber, dto.getParentNum()));
                if (one == null) {
                    setMsg.add("同步组织机构数据--平台没有查询到（" + dto.getName() + " ）的同步父级编号为--" + dto.getParentNum() + " 的数据！");
                    log.error("addHeatOrganization---添加 {} 组织机构数据，平台没有查询到同步父级编号为 {} 的数据！", dto.getName(),dto.getParentNum() );
                    continue;
                }
                heatOrganization.setPid(one.getId());

                // 返回的id
//                int insertId = heatOrganizationService.insert(heatOrganization);

                QueryWrapper<HeatOrganization> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("pid", one.getId());
                queryWrapper.eq("syncNumber", dto.getNum());
                queryWrapper.eq("syncParentNum", dto.getParentNum());
                HeatOrganization oneHeatOrganization = heatOrganizationService.getOne(queryWrapper);
                if (oneHeatOrganization == null) {

                    if (heatOrganizationService.save(heatOrganization)) {
                        Integer insertId = heatOrganization.getId();
                        if (insertId > 0) {
                            HeatOrganization update = new HeatOrganization();
                            update.setId(insertId);
                            update.setCode(one.getCode() + ":" + insertId);
                            heatOrganizationService.updateById(update);
                        }
                    }
                }
            }
        }
        return setMsg;
    }

}
