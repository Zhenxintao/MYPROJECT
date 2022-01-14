package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
public interface PointStandardAlarmService extends IService<PointStandardAlarm> {

    List<Integer> listRelevanceId(QueryWrapper queryWrapper);
}
