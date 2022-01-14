package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.HeatAreaChangeHistory;
import com.bmts.heating.commons.basement.model.db.response.HeatAreaChangeHistoryResponse;
import com.bmts.heating.commons.db.mapper.HeatAreaChangeHistoryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

public interface HeatAreaChangeHistoryService extends IService<HeatAreaChangeHistory> {
    Page<HeatAreaChangeHistoryResponse> queryHeatAreaChangeHistory(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper, String type);
}
