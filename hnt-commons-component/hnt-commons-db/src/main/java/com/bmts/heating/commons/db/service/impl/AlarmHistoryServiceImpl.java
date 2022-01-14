package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.AlarmHistory;
import com.bmts.heating.commons.db.mapper.AlarmHistoryMapper;
import com.bmts.heating.commons.db.service.AlarmHistoryService;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
@Service
public class AlarmHistoryServiceImpl extends ServiceImpl<AlarmHistoryMapper, AlarmHistory> implements AlarmHistoryService {

    @Autowired
    AlarmHistoryMapper alarmHistoryMapper;

    @Override
    public IPage<AlarmHistoryResponse> pageAlarmHis(Page<AlarmHistory> page, Wrapper wrapper) {
        return alarmHistoryMapper.pageAlarmHis(page, wrapper);
    }

    @Override
    public AlarmHistoryResponse queryId(int id) {
        return alarmHistoryMapper.queryId(id);
    }
}
