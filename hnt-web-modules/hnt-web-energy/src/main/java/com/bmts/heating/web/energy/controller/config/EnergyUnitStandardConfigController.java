package com.bmts.heating.web.energy.controller.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗折算配置")
@RestController
@RequestMapping("energy/unitStandard")
public class EnergyUnitStandardConfigController {

	@Autowired
	private EnergyUnitStandardConfigService energyUnitStandardConfigService;

	@ApiOperation("查询")
	@GetMapping("/info")
	public Response info(){
		return Response.success(energyUnitStandardConfigService.info());
	}

	@ApiOperation("报存或者修改")
	@PutMapping()
	public Response saveOrUpdate(@RequestBody EnergyUnitStandardConfig one){
		return energyUnitStandardConfigService.saveOrUpdate(one);
	}
}
