package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.db.mapper.AlarmRealMapper;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
@Service
public class AlarmRealServiceImpl extends ServiceImpl<AlarmRealMapper, AlarmReal> implements AlarmRealService {
    @Autowired
    AlarmRealMapper alarmRealMapper;

    @Override
    public List<AlarmRealBarResponse> alarmRealBarResponsesList(QueryWrapper queryWrapper) {
        return alarmRealMapper.alarmRealBarList(queryWrapper);
    }

    @Override
    public IPage<AlarmRealResponse> pageAlarmReal(Page<AlarmReal> page, Wrapper wrapper) {
        return alarmRealMapper.pageAlarmReal(page, wrapper);
    }

    @Override
    public AlarmRealResponse queryId(int id) {
        return alarmRealMapper.queryId(id);
    }
}
