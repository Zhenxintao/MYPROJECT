package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarmView;
import com.bmts.heating.commons.db.mapper.PointStandardAlarmViewMapper;
import com.bmts.heating.commons.db.service.PointStandardAlarmViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-04-28
 */
@Service
public class PointStandardAlarmViewServiceImpl extends ServiceImpl<PointStandardAlarmViewMapper, PointStandardAlarmView> implements PointStandardAlarmViewService {

    @Autowired
    private PointStandardAlarmViewMapper pointStandardAlarmViewMapper;

    @Override
    public List<PointAlarmView> listAlarm(QueryWrapper queryWrapper) {
        return pointStandardAlarmViewMapper.listAlarm(queryWrapper);
    }
}
