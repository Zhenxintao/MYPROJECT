package com.bmts.heating.commons.db.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.balance.response.BalanceControlLog;
import com.bmts.heating.commons.basement.model.balance.response.BalanceRejectVo;
import com.bmts.heating.commons.basement.model.balance.response.BalanceTargetVo;
import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface BalanceTargetHistoryService extends IService<BalanceTargetHistory> {

	//报警历史
	IPage<BalanceRejectVo> pageUnionReject(Page page, QueryWrapper queryWrapper);

	//调控日志
	IPage<BalanceControlLog> pageUnionControl(Page page, QueryWrapper queryWrapper);

	//历史记录
	IPage<BalanceTargetVo> pageUnionTarget(Page page, QueryWrapper queryWrapper);
}
