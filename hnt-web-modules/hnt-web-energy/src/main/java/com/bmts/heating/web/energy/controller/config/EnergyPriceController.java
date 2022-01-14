package com.bmts.heating.web.energy.controller.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.entiy.energy.EnergyPricePageDto;
import com.bmts.heating.web.energy.service.config.EnergyPriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗价格配置")
@RestController
@RequestMapping("energy/price")
public class EnergyPriceController {

	@Autowired
	private EnergyPriceService priceService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyPricePageDto dto){
		return priceService.page(dto);
	}

	@ApiOperation("保存/修改")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyPrice price){
		return priceService.saveOrUpdate(price);
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return priceService.delete(id);
	}

}
