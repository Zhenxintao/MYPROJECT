package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
public interface AlarmRealService extends IService<AlarmReal> {
    //实时报警柱状图数据接口
    List<AlarmRealBarResponse> alarmRealBarResponsesList(QueryWrapper queryWrapper);

    IPage<AlarmRealResponse> pageAlarmReal(Page<AlarmReal> page, Wrapper wrapper);

    AlarmRealResponse queryId(int id);
}
