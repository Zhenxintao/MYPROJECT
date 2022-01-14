package com.bmts.heating.commons.redis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.response.EquipmentInfoResponse;
import com.bmts.heating.commons.db.mapper.EquipmentInfoMapper;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zxt
 * @description
 * @date 2021/7/22
 **/
@Service
@Slf4j
public class RedisEquipmentService {
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private PointConfigService pointConfigService;
    @Autowired
    private RedisPointService redisPointService;

    public Map<String, List<String>> queryEquipmentInfo(List<String> dto) {
        try {
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("p.columnName", dto);
            List<EquipmentInfoResponse> equipmentInfoResponseList = equipmentInfoMapper.EquipmentInfoResponse(queryWrapper);
            Map<String, List<String>> map = new HashMap<>();
            for (EquipmentInfoResponse equipmentInfoResponse : equipmentInfoResponseList) {
                List<String> pointName = new ArrayList<>();
                pointName.add(equipmentInfoResponse.getColumnName());
                map.put(equipmentInfoResponse.getEquipmentName(), pointName);
            }
            return map;

        } catch (Exception e) {
            return null;
        }
    }


    // 同步设备编号到缓存
    public void syncPointStandardIds(List<Integer> standardIds) {
        // 根据标准点表的id ，查询配置的点 id pointConfig 表的 id
        if (!CollectionUtils.isEmpty(standardIds)) {
            QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("pointStandardId", standardIds);
            List<PointConfig> listPointConfig = pointConfigService.list(queryWrapper);
            Set<Integer> relevanceLisr = listPointConfig.stream().map(PointConfig::getRelevanceId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(relevanceLisr)) {
                redisPointService.syncByRelevanceIds(new ArrayList<>(relevanceLisr), TreeLevel.HeatSystem.level());
            }
        }
    }

}
