package com.bmts.heating.web.energy.controller.energyByTd;

import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyHomeChartDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.HomeBaseDto;
import com.bmts.heating.web.energy.pojo.HomeBaseResponse;
import com.bmts.heating.web.energy.pojo.HomeRateDto;
import com.bmts.heating.web.energy.pojo.StationEnergyInfoResponse;
import com.bmts.heating.web.energy.pojo.energyByTd.HomeEnergyDataResponse;
import com.bmts.heating.web.energy.pojo.energyByTd.HomeEnergyOverallResponse;
import com.bmts.heating.web.energy.service.EnergyHomeService;
import com.bmts.heating.web.energy.service.energyByTd.EnergySystemHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "能耗系统-首页(全新)")
@RestController
@RequestMapping("energy/home")
public class EnergySystemHomeController {

	@Autowired
	private EnergySystemHomeService energySystemHomeService;

	@ApiOperation(value = "昨日能耗数据",response = HomeEnergyDataResponse.class)
	@GetMapping("/energyData")
	public Response energyData(){
		return energySystemHomeService.energyData();
	}

	@ApiOperation(value = "能耗系统--首页地图总耗信息",response = HomeEnergyOverallResponse.class)
	@GetMapping("/energyDataOverall")
	public Response energyDataOverall(){
		return energySystemHomeService.energyDataOverall();
	}

	@ApiOperation(value = "能耗系统--首页单站能耗信息",response = StationEnergyInfoResponse.class)
	@GetMapping("/mapEnergyData/{id}")
	public Response mapEnergyData(@PathVariable Integer id){
		return energySystemHomeService.mapEnergyData(id);
	}

	@ApiOperation(value = "能耗系统--首页能耗排行信息")
	@GetMapping("/energyRankData")
	public Response energyRankData(){
		return energySystemHomeService.energyRankData();
	}




}
