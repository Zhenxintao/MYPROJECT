package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.db.mapper.PointStandardAlarmMapper;
import com.bmts.heating.commons.db.service.PointStandardAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
@Service
public class PointStandardAlarmServiceImpl extends ServiceImpl<PointStandardAlarmMapper, PointStandardAlarm> implements PointStandardAlarmService {

    @Autowired
    private PointStandardAlarmMapper mapper;

    @Override
    public List<Integer> listRelevanceId(QueryWrapper queryWrapper) {
        return mapper.listRelevanceId(queryWrapper);
    }
}
