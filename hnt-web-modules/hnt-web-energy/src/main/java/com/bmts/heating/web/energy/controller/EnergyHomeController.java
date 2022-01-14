package com.bmts.heating.web.energy.controller;

import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyHomeChartDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.HomeBaseDto;
import com.bmts.heating.web.energy.pojo.HomeBaseResponse;
import com.bmts.heating.web.energy.pojo.HomeRateDto;
import com.bmts.heating.web.energy.pojo.StationEnergyInfoResponse;
import com.bmts.heating.web.energy.service.EnergyHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "能耗首页")
@RestController
@RequestMapping("energy/home")
public class EnergyHomeController {

	@Autowired
	private EnergyHomeService energyHomeService;

	@ApiOperation(value = "基础数据-昨日与其同环比",response = HomeBaseResponse.class)
	@PostMapping("/base")
	public Response baseData(@RequestBody HomeBaseDto dto){
		return energyHomeService.baseData(dto.getType(), dto.getId());
	}

	@ApiOperation("能耗统计-总公司-今日已生产")
	@GetMapping("/{id}")
	public Response radarCharts(@PathVariable Integer id){
		return energyHomeService.radarCharts(id);
	}

	@ApiOperation("综合能耗")
	@GetMapping("/synthesize")
	public Response synthesizeEnergy(){
		return energyHomeService.synthesizeEnergy();
	}

	@ApiOperation(value = "达标率",response =ReachStandardResponse[].class)
	@PostMapping("/rate")
	public Response rate(@RequestBody HomeRateDto dto){
		return energyHomeService.rate(dto);
	}

	@ApiOperation(value = "能耗top10",response = HashMap.class)
	@PostMapping("/rank")
	public Response energyRank(@RequestBody EnergyHomeChartDto dto){
		return energyHomeService.energyRank(dto);
	}

	/**
	 * 换热站能耗
	 * @param id 换热站Id
	 * @return StationEnergyInfoResponse
	 */
	@ApiOperation(value = "换热站能耗",response = StationEnergyInfoResponse.class)
	@GetMapping("/map/{id}")
	public Response stationEnergy(@PathVariable Integer id){
		return energyHomeService.stationEnergy(id);
	}


}
