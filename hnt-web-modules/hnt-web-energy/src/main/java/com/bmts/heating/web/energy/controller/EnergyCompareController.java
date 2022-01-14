package com.bmts.heating.web.energy.controller;

import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.EnergyChartResponse;
import com.bmts.heating.web.energy.pojo.EnergyCompareDto;
import com.bmts.heating.web.energy.pojo.EnergyInfoResponse;
import com.bmts.heating.web.energy.service.EnergyCompareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "能耗对比")
@RestController
@RequestMapping("energy/compare")
public class EnergyCompareController {

	@Autowired
	private EnergyCompareService energyCompareService;

	@ApiOperation(value = "表格", response = EnergyInfoResponse.class)
	@PostMapping("/table")
	public Response table(@RequestBody EnergyCompareDto dto){
		return energyCompareService.page(dto);
	}

	@ApiOperation(value = "图表",response = EnergyChartResponse.class)
	@PostMapping("/charts")
	public Response charts(@RequestBody EnergyCompareDto dto){
		return energyCompareService.charts(dto);
	}

}
