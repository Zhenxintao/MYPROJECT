package com.bmts.heating.web.backStage.controller.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EnergyConvergeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗基础计算配置")
@RestController
@RequestMapping("/energy/config")
public class EnergyConvergeController {

	@Autowired
	private EnergyConvergeService energyConvergeService;

	@ApiOperation("查询带搜索")
	@PostMapping("/page")
	public Response page(@RequestBody BaseDto dto) {
		return energyConvergeService.page(dto);
	}

	@ApiOperation("保存或修改")
	@PostMapping("/saveOrUpdate")
	public Response saveOrUpdate(@RequestBody EnergyConfig entity) {
		return energyConvergeService.saveOrUpdate(entity);
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete/{id}")
	public Response delete(@PathVariable Integer id) {
		return energyConvergeService.delete(id);
	}

}
