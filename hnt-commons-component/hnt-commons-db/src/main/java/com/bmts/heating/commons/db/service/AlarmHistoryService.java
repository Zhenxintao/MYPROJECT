package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.AlarmHistory;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmHistoryResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
public interface AlarmHistoryService extends IService<AlarmHistory> {

    IPage<AlarmHistoryResponse> pageAlarmHis(Page<AlarmHistory> page, Wrapper wrapper);

    AlarmHistoryResponse queryId(int id);
}
