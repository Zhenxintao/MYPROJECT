package com.bmts.heating.bussiness.netBalance.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.netBalance.util.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.balance.response.BalanceControlLog;
import com.bmts.heating.commons.basement.model.balance.response.BalanceRejectVo;
import com.bmts.heating.commons.basement.model.balance.response.BalanceTargetVo;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.BalanceRejectHistory;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.BalanceTargetHistoryService;
import com.bmts.heating.commons.db.service.LogOperationBalanceService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.history.BalanceNetHistoryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "全网平衡")
@Slf4j
@RestController
@RequestMapping("netbalancehistory")
public class NetBalanceHistoryJoggle {

	@Autowired
	private BalanceTargetHistoryService balanceTargetHistoryService;
	@Autowired
	private LogOperationBalanceService logOperationBalanceService;
	@ApiOperation("报警历史")
	@PostMapping("/reject/page")
	public Response pageRe(@RequestBody BalanceNetHistoryDto dto) {
		Page<BalanceRejectHistory> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<BalanceRejectHistory> queryWrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		if (dto.getStart() != null && dto.getEnd() != null) {
			queryWrapper.between("brh.rejectTime",dto.getStart(),dto.getEnd());
		}
		if (dto.getBalanceNetId() != null && dto.getBalanceNetId() > 0){
			queryWrapper.eq("brh.balanceNetId",dto.getBalanceNetId());
		}
		List<StationFirstNetBaseView> firstNetBaseList =  Objects.requireNonNull(this.queryFirstNetBase())
				.stream()
				.filter(e -> filterKeywords(e,dto.getKeyWord()))
				.collect(Collectors.toList());
		Set<Integer> systemIds = firstNetBaseList.stream().map(StationFirstNetBaseView::getHeatSystemId).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(systemIds)) {
			return Response.success(page);
		}
		queryWrapper.in("brh.relevanceId",systemIds);
		IPage<BalanceRejectVo> balanceRejectVoIPage = balanceTargetHistoryService.pageUnionReject(page, queryWrapper);
		List<BalanceRejectVo> collect = balanceRejectVoIPage.getRecords().stream().map(e ->
					filterReject(firstNetBaseList, e)).collect(Collectors.toList());
		balanceRejectVoIPage.setRecords(collect);
		return Response.success(balanceRejectVoIPage);
	}

	@ApiOperation("全网平衡调控日志")
	@PostMapping("/control/page")
	public Response pageLog(@RequestBody BalanceNetHistoryDto dto){
		Page<BalanceControlLog> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<BalanceControlLog> queryWrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		if (dto.getStart() != null && dto.getEnd() != null) {
			queryWrapper.between("bnc.createTime",dto.getStart(),dto.getEnd());
		}
		if (dto.getBalanceNetId() != null && dto.getBalanceNetId() > 0){
			queryWrapper.eq("bnc.balanceNetId",dto.getBalanceNetId());
		}
		List<StationFirstNetBaseView> firstNetBaseList =  Objects.requireNonNull(this.queryFirstNetBase())
				.stream()
				.filter(e -> filterKeywords(e,dto.getKeyWord()))
				.collect(Collectors.toList());
		Set<Integer> systemIds = firstNetBaseList.stream().map(StationFirstNetBaseView::getHeatSystemId).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(systemIds)) {
			return Response.success(page);
		}
		queryWrapper.in("bnc.relevanceId",systemIds);
		IPage<BalanceControlLog> balanceRejectVoIPage = balanceTargetHistoryService.pageUnionControl(page,queryWrapper);
		List<BalanceControlLog> collect = balanceRejectVoIPage.getRecords().stream().map(e ->
				filterControlLog(firstNetBaseList, e)).collect(Collectors.toList());
		balanceRejectVoIPage.setRecords(collect);

		return Response.success(balanceRejectVoIPage);
	}

	@ApiOperation("全网平衡历史列表")
	@PostMapping("/target/page")
	public Response pageList(@RequestBody BalanceNetHistoryDto dto){
		Page<BalanceTargetVo> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<BalanceTargetVo> queryWrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		if (dto.getStart() != null && dto.getEnd() != null) {
			queryWrapper.between("bth.createTime",dto.getStart(),dto.getEnd());
		}
		if (!StringUtils.isEmpty(dto.getKeyWord())) {
			queryWrapper.like("bn.name",dto.getKeyWord());
		}
		if (dto.getBalanceNetId() != null && dto.getBalanceNetId() > 0){
			queryWrapper.eq("bth.balanceNetId",dto.getBalanceNetId());
		}
		return Response.success(balanceTargetHistoryService.pageUnionTarget(page,queryWrapper));
	}

	@ApiOperation("全网平衡操作日志列表")
	@PostMapping("/logOperationBalance/page")
	public Response logOperationBalance(@RequestBody BalanceLogQueryDto dto){
		Page<LogOperationBalance> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<LogOperationBalance> queryWrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		if (dto.getStart() != null && dto.getEnd() != null) {
			queryWrapper.between("createTime",dto.getStart(),dto.getEnd());
		}
		if (!StringUtils.isEmpty(dto.getKeyWord())) {
			queryWrapper.like("description",dto.getKeyWord());
		}
		return Response.success(logOperationBalanceService.page(page,queryWrapper));
	}

	private Boolean filterKeywords(StationFirstNetBaseView e, String keyWord){
		boolean b = StringUtils.containsIgnoreCase(e.getHeatTransferStationName(), keyWord);
		boolean b2 = StringUtils.containsIgnoreCase(e.getHeatCabinetName(), keyWord);
		boolean b3 = StringUtils.containsIgnoreCase(e.getHeatSystemName(), keyWord);
		return b || b2 || b3;
	}

	@Autowired
	private StationFirstNetBaseViewService stationFirstNetBaseViewService;

	private List<StationFirstNetBaseView> queryFirstNetBase() {
		return stationFirstNetBaseViewService.list();
	}

	private BalanceRejectVo filterReject(List<StationFirstNetBaseView> firstNetBaseList,BalanceRejectVo vo){
		for (StationFirstNetBaseView e : firstNetBaseList) {
			if(Objects.equals(e.getHeatSystemId(), vo.getRelevanceId())) {
				vo.setHeatTransferStationName(e.getHeatTransferStationName());
				vo.setHeatCabinetName(e.getHeatCabinetName());
				vo.setHeatSystemName(e.getHeatSystemName());
				break;
			}
		}
		return vo;
	}
	private BalanceControlLog filterControlLog(List<StationFirstNetBaseView> firstNetBaseList,BalanceControlLog vo){
		for (StationFirstNetBaseView e : firstNetBaseList) {
			if(Objects.equals(e.getHeatSystemId(), vo.getRelevanceId())) {
				vo.setHeatTransferStationName(e.getHeatTransferStationName());
				vo.setHeatCabinetName(e.getHeatCabinetName());
				vo.setHeatSystemName(e.getHeatSystemName());
				break;
			}
		}
		return vo;
	}

}
