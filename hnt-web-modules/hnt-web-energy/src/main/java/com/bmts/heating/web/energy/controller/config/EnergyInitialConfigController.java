package com.bmts.heating.web.energy.controller.config;

import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.basement.model.db.request.EnergyInitialCodeConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.config.EnergyInitialCodeConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "能耗初始化配置")
@RestController
@RequestMapping("energy/initial")
public class EnergyInitialConfigController {

	@Autowired
	private EnergyInitialCodeConfigService energyInitialConfigService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyInitialCodeConfigDto dto) {
		return energyInitialConfigService.page(dto);
	}

	@ApiOperation("添加")
	@PutMapping
	public Response saveOrUpdate(@RequestBody EnergyInitialCodeConfigAddDto dto, HttpServletRequest request){

		Integer userId = -1;

		List<EnergyInitialCodeConfig> energyInitialConfigs = new ArrayList<>();
		dto.getParentIds().forEach(e ->{
			EnergyInitialCodeConfig en = createObj(dto.getEnergyInitialConfigs());
			if (en.getId() == null) {
				en.setCreateUser(userId);
				en.setCreateTime(LocalDateTime.now());
			}else {
				en.setUpdateUser(userId);
				en.setUpdateTime(LocalDateTime.now());
			}
			en.setTargetId(e);
			energyInitialConfigs.add(en);
		});
		return energyInitialConfigService.saveOrUpdate(energyInitialConfigs);
	}

	private EnergyInitialCodeConfig createObj(EnergyInitialCodeConfig enBase){
		EnergyInitialCodeConfig en = new EnergyInitialCodeConfig();
		BeanUtils.copyProperties(enBase, en);
		return en;

	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return energyInitialConfigService.delete(id);
	}
}
