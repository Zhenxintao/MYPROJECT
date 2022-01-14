package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyPriceResponse;
import com.bmts.heating.commons.db.service.EnergyPriceService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.energy.EnergyPricePageDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗修改单价")
@Slf4j
@RestController
@RequestMapping("energy/price")
public class EnergyPriceJoggle {
	@Autowired
	private EnergyPriceService priceService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyPricePageDto dto){
		Page<EnergyPriceResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<EnergyPriceResponse> queryWrapper = new QueryWrapper<>();
		if (dto.getCommonSeasonId() != null){
			queryWrapper.eq("chs.id",dto.getCommonSeasonId());
		}
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		return Response.success(priceService.page(page,queryWrapper));
	}

	@ApiOperation("保存/修改")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyPrice price){
		try{
			priceService.saveOrUpdate(price);
		}catch (Exception e){
			return Response.warn("变量已添加过");
		}
		return Response.success();
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return Response.success(priceService.removeById(id));
	}


}
