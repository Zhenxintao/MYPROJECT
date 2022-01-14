package com.bmts.heating.web.energy.controller.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.basement.model.db.request.EnergyRateConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.config.EnergyRateConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "能耗倍率配置")
@RestController
@RequestMapping("energy/rate")
public class EnergyRateConfigController {

	@Autowired
	private EnergyRateConfigService energyRateConfigService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyInitialCodeConfigDto dto) {
		return energyRateConfigService.page(dto);
	}

	@ApiOperation("添加")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyRateConfigAddDto dto, HttpServletRequest request){

		Integer userId = -1;

		List<EnergyRateConfig> energyRateConfigs = new ArrayList<>();
		dto.getParentIds().forEach(e ->{
			EnergyRateConfig en = createObj(dto.getEnergyRateConfig());
			if (en.getId() == null) {
				en.setCreateUser(userId);
				en.setCreateTime(LocalDateTime.now());
			}else {
				en.setUpdateUser(userId);
				en.setUpdateTime(LocalDateTime.now());
			}
			en.setTargetId(e);
			energyRateConfigs.add(en);
		});
		return energyRateConfigService.saveOrUpdate(energyRateConfigs);
	}

	private EnergyRateConfig createObj(EnergyRateConfig enBase){
		EnergyRateConfig en = new EnergyRateConfig();
		BeanUtils.copyProperties(enBase, en);
		return en;

	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return energyRateConfigService.delete(id);
	}
}
