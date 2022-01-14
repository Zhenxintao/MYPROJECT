package com.bmts.heating.web.energy.controller.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyEvaluateDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.config.EnergyEvaluateConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗评价配置")
@RestController
@RequestMapping("energy/evaluate")
public class EnergyEvaluateConfigController {

	@Autowired
	private EnergyEvaluateConfigService energyEvaluateConfigService;

	@ApiOperation("查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyEvaluateDto dto){
			return energyEvaluateConfigService.page(dto);
	}

	@ApiOperation("添加/修改配置")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyEvaluateConfig energyEvaluateConfig){
		return energyEvaluateConfigService.saveOrUpdate(energyEvaluateConfig);
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response remove(@PathVariable int id){
		return energyEvaluateConfigService.remove(id);
	}

}
