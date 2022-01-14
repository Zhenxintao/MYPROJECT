package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateConfig;
import com.bmts.heating.commons.db.service.EnergyEvaluateConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyEvaluateDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗评价配置")
@RestController
@RequestMapping("/energy/evaluate")
@Slf4j
public class EnergyEvaluateConfigJoggle {

	@Autowired
	private EnergyEvaluateConfigService energyEvaluateConfigService;

	@ApiOperation("查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyEvaluateDto dto){
		if (dto.getEnergyType() == 3){
			Page<EnergyEvaluateConfig> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
			LambdaQueryWrapper<EnergyEvaluateConfig> wrapper = Wrappers
					.<EnergyEvaluateConfig>lambdaQuery()
					.eq(EnergyEvaluateConfig::getEnergyType, 3)
					.orderByAsc(EnergyEvaluateConfig::getTemperature);
			return Response.success(energyEvaluateConfigService.page(page,wrapper));
		}else{
			LambdaQueryWrapper<EnergyEvaluateConfig> wrapper = Wrappers.<EnergyEvaluateConfig>lambdaQuery().in(EnergyEvaluateConfig::getEnergyType, 1, 2);
			return Response.success(energyEvaluateConfigService.list(wrapper));
		}
	}

	@ApiOperation("添加/修改配置")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyEvaluateConfig energyEvaluateConfig){
		try{
			energyEvaluateConfigService.saveOrUpdate(energyEvaluateConfig);
		}catch (Exception e){
			return Response.warn("变量已添加过");
		}
		return Response.success();
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response remove(@PathVariable int id){
		return energyEvaluateConfigService.removeById(id) ? Response.success():Response.fail();
	}


}
