package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.balance.response.BalanceControlLog;
import com.bmts.heating.commons.basement.model.balance.response.BalanceRejectVo;
import com.bmts.heating.commons.basement.model.balance.response.BalanceTargetVo;
import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;
import com.bmts.heating.commons.db.mapper.BalanceTargetHistoryMapper;
import com.bmts.heating.commons.db.service.BalanceTargetHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Service
public class BalanceTargetHistoryServiceImpl extends ServiceImpl<BalanceTargetHistoryMapper, BalanceTargetHistory> implements BalanceTargetHistoryService {

	@Autowired
	private BalanceTargetHistoryMapper mapper;

	@Override
	public IPage<BalanceRejectVo> pageUnionReject(Page page, QueryWrapper queryWrapper) {
		return mapper.pageUnionReject(page,queryWrapper);
	}

	@Override
	public IPage<BalanceControlLog> pageUnionControl(Page page, QueryWrapper queryWrapper) {
		return mapper.pageUnionControl(page,queryWrapper);
	}

	@Override
	public IPage<BalanceTargetVo> pageUnionTarget(Page page, QueryWrapper queryWrapper) {
		return mapper.pageUnionTarget(page,queryWrapper);
	}
}
