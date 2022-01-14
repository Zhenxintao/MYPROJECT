package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.db.mapper.PointAlarmMapper;
import com.bmts.heating.commons.db.service.PointAlarmService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
@Service
public class PointAlarmServiceImpl extends ServiceImpl<PointAlarmMapper, PointAlarm> implements PointAlarmService {

}
