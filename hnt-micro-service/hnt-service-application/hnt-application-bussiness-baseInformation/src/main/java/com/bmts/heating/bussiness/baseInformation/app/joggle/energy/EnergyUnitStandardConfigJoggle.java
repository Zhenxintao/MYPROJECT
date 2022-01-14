package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.db.service.EnergyUnitStandardConfigService;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Api(tags = "能耗合格配置")
@RestController
@RequestMapping("/energy/unitStandard")
@Slf4j
public class EnergyUnitStandardConfigJoggle {

	@Autowired
	private EnergyUnitStandardConfigService energyUnitStandardConfigService;

	@ApiOperation("查询")
	@GetMapping("/info")
	public EnergyUnitStandardConfig info(){
		EnergyUnitStandardConfig one = energyUnitStandardConfigService.getOne(Wrappers.<EnergyUnitStandardConfig>lambdaQuery().eq(EnergyUnitStandardConfig::getState, true));
		if(one==null) {
			return new EnergyUnitStandardConfig();
		}
		one.setDateTime(null);
		return one;
	}

	@ApiOperation("报存或者修改")
	@PutMapping()
	@Transactional(rollbackFor = Exception.class)
	public Response saveOrUpdate(@RequestBody EnergyUnitStandardConfig one){
		EnergyUnitStandardConfig updateOne = new EnergyUnitStandardConfig();
		updateOne.setDateTime(LocalDateTime.now());
		updateOne.setState(false);
		QueryWrapper<EnergyUnitStandardConfig> wrapper = new QueryWrapper<>();
		energyUnitStandardConfigService.update(updateOne, wrapper);
		one.setDateTime(LocalDateTime.now());
		one.setState(true);
		return energyUnitStandardConfigService.saveOrUpdate(one) ?  Response.success() : Response.fail();
	}


}
