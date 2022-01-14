package com.bmts.heating.web.balance.service;

import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.history.BalanceNetHistoryDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface BalanceNetHistoryService {
	Response rejectPage(BalanceNetHistoryDto dto);

	Response controlPage(BalanceNetHistoryDto dto);

	Response targetPage(BalanceNetHistoryDto dto);

	Response logOperationBalancePage(BalanceLogQueryDto dto);
}
