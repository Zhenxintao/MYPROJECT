package com.bmts.heating.web.balance.service.impl;

import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.history.BalanceNetHistoryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.balance.service.BalanceNetHistoryService;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class BalanceNetHistoryServiceImpl implements BalanceNetHistoryService {

	@Autowired
	private TSCCRestTemplate tsccRestTemplate;

	private final String baseUrl = "/netbalancehistory";

	private final String applicationNetBalance = "application_netbalance";

	@Override
	public Response rejectPage(BalanceNetHistoryDto dto) {
		return tsccRestTemplate.doHttp(baseUrl + "/reject/page",dto, applicationNetBalance, Response.class, HttpMethod.POST);
	}

	@Override
	public Response controlPage(BalanceNetHistoryDto dto) {
		return tsccRestTemplate.doHttp(baseUrl + "/control/page",dto, applicationNetBalance, Response.class, HttpMethod.POST);
	}

	@Override
	public Response targetPage(BalanceNetHistoryDto dto) {
		return tsccRestTemplate.doHttp(baseUrl + "/target/page",dto, applicationNetBalance, Response.class, HttpMethod.POST);
	}

	@Override
	public Response logOperationBalancePage(BalanceLogQueryDto dto) {
		return tsccRestTemplate.doHttp(baseUrl + "/logOperationBalance/page",dto, applicationNetBalance, Response.class, HttpMethod.POST);
	}
}
