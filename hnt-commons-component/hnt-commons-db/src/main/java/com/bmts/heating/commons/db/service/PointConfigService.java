package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface PointConfigService extends IService<PointConfig> {

    Page<PointConfigResponse> page(Page page, Wrapper wrapper);

    PointConfigResponse emptyInfo(Integer id);

    Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId);

    Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId, String keWord);

    List<CabinetPointResponse> pointsAndSystem(int cabinetId);

    Boolean updateBatchStandard(String descriptionJson, int pointStandardId);


    List<PointAlarmView> queryAlarm(QueryWrapper queryWrapper);
}
