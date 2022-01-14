package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-10
 */
public interface HeatSystemService extends IService<HeatSystem> {

    List<FirstNetBase> querySystem(QueryWrapper queryWrapper);

    Map<String, Object> querySystemNameByStationId(QueryWrapper queryWrapper);

    List<Integer> querySystemIdByHeatSourceId(QueryWrapper queryWrapper);
}
