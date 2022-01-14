package com.bmts.heating.web.balance.controller;

import com.bmts.heating.commons.basement.model.balance.response.BalanceControlLog;
import com.bmts.heating.commons.basement.model.balance.response.BalanceRejectVo;
import com.bmts.heating.commons.basement.model.balance.response.BalanceTargetVo;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.history.BalanceNetHistoryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.balance.service.BalanceNetHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "全网平衡历史")
@Slf4j
@RestController
@RequestMapping("netbalancehistory")
public class BalanceNetHistoryController {

	@Autowired
	private BalanceNetHistoryService historyService;

	@ApiOperation(value = "报警查询列表",response = BalanceRejectVo.class)
	@PostMapping("/reject/page")
	public Response rejectPage(@RequestBody BalanceNetHistoryDto dto){
		return historyService.rejectPage(dto);
	}

	@ApiOperation(value = "调控日志查询列表",response = BalanceControlLog.class)
	@PostMapping("/control/page")
	public Response controlPage(@RequestBody BalanceNetHistoryDto dto){
		return historyService.controlPage(dto);
	}

	@ApiOperation(value = "历史查询列表",response =  BalanceTargetVo.class)
	@PostMapping("/target/page")
	public Response targetPage(@RequestBody BalanceNetHistoryDto dto){
		return historyService.targetPage(dto);
	}

	@ApiOperation(value = "全网平衡操作日志列表",response =  LogOperationBalance.class)
	@PostMapping("/logOperationBalance/page")
	public Response logOperationBalancePage(@RequestBody BalanceLogQueryDto dto){
		return historyService.logOperationBalancePage(dto);
	}
}
