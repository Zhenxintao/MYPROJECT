package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarmView;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author naming
 * @since 2021-04-28
 */
public interface PointStandardAlarmViewService extends IService<PointStandardAlarmView> {

    List<PointAlarmView> listAlarm(QueryWrapper queryWrapper);

}
