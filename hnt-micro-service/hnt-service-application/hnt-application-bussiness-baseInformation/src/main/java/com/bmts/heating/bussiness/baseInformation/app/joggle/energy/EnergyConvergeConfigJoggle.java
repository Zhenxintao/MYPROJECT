package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.handler.EsHandler;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.db.service.EnergyConfigService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "能耗基础配置")
@RestController
@RequestMapping("/energy/config")
@Slf4j
public class EnergyConvergeConfigJoggle {

	@Autowired
	private EnergyConfigService energyConfigService;
	@Autowired
	private PointStandardService pointStandardService;

	@Autowired
	private EsHandler esHandler;

	@ApiOperation("查询带搜索")
	@PostMapping("/page")
	public Response page(@RequestBody BaseDto dto){
		Page<EnergyConfigResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<Object> wrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(wrapper, dto);
		if (dto.getKeyWord() != null) {
			wrapper.like("ps.name", dto.getKeyWord()).or().like("ps.columnName",dto.getKeyWord());
		}
		return Response.success(energyConfigService.page(page,wrapper));
	}

	@ApiOperation("保存或修改")
	@PostMapping("/saveOrUpdate")
	public Response add(@RequestBody EnergyConfig entity){
		if (entity.getId() == null){
			try {
				PointStandard pointStandard = pointStandardService.getById(entity.getPointStandardId());
				if(pointStandard != null) {
					esHandler.configEnergyColumn(pointStandard.getColumnName());
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return Response.success(energyConfigService.saveOrUpdate(entity));
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		if (id != null && id != 0){
			return Response.success(energyConfigService.removeById(id));
		}
		return Response.paramError();
	}

}
