package com.bmts.heating.web.energy.controller.config;

import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigAddDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigParamQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigQueryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.config.EnergyCollectConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗采集量计算配置")
@RestController
@RequestMapping("energy/collect")
public class EnergyCollectConfigController {

	@Autowired
	private EnergyCollectConfigService energyCollectConfigService;

	@ApiOperation("单站分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyCollectConfigQueryDto dto) {
		return energyCollectConfigService.page(dto);
	}

	@ApiOperation("加载站下所有系统参量")
	@PostMapping()
	public Response queryAll(@RequestBody EnergyCollectConfigParamQueryDto dto) {
		return Response.success(energyCollectConfigService.queryAll(dto));
	}

	@ApiOperation("添加计算规则")
	@PutMapping("/saveOrUpdate")
	public Response saveOrUpdate(@RequestBody EnergyCollectConfigAddDto addRequest) {
		String expression = addRequest.getExpression();
		String pattern = "^(((?<o>\\()[-+]?([0-9]+[-+*/])*)+[0-9]+((?<-o>\\))([-+*/][0-9]+)*)+($|[-+*/]))*(?(o)(?!))$";

		return energyCollectConfigService.saveOrUpdate(addRequest);
	}

	@ApiOperation("详情")
	@GetMapping("/info/{id}")
	public Response info(@PathVariable Integer id){
		return energyCollectConfigService.info(id);
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return energyCollectConfigService.delete(id);
	}

	@ApiOperation("热源列表")
	@GetMapping("/heatSource")
	public Response heatSourceList(){
		return energyCollectConfigService.heatSourceList();
	}
}
