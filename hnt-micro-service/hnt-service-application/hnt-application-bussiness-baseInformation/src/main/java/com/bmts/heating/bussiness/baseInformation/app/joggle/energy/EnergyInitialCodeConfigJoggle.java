package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyInitialCodeConfigResponse;
import com.bmts.heating.commons.db.service.EnergyInitialCodeConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyInitialCodeConfigDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "能耗初始码配置")
@RestController
@RequestMapping("/energy/initial")
@Slf4j
public class EnergyInitialCodeConfigJoggle {

	@Autowired
	private EnergyInitialCodeConfigService energyInitialConfigService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyInitialCodeConfigDto dto) {
		Page<EnergyInitialCodeConfigResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<EnergyInitialCodeConfigResponse> queryWrapper = new QueryWrapper<>();
		WrapperSortUtils.sortWrapper(queryWrapper, dto);
		if (dto.getType() == TreeLevel.HeatStation.level()){
			queryWrapper.eq("hts.status",true).eq("hts.deleteFlag",false);
			queryWrapper.eq("eic.type",TreeLevel.HeatStation.level());
			if (dto.getCommonSeasonId() != null) {
				queryWrapper.eq("eic.commonSeasonId",dto.getCommonSeasonId());
			}
			if (StringUtils.isNotEmpty(dto.getKeyWord())){
				queryWrapper.like("hts.name",dto.getKeyWord())
						.or()
						.like("hts.logogram",dto.getKeyWord());
			}
			return Response.success(energyInitialConfigService.pageStation(page, queryWrapper));
		}else if(dto.getType() == TreeLevel.HeatSource.level()){
			queryWrapper.eq("hs.deleteFlag",false);
			queryWrapper.eq("eic.type",TreeLevel.HeatSource.level());
			if (dto.getCommonSeasonId() != null) {
				queryWrapper.eq("eic.commonSeasonId",dto.getCommonSeasonId());
			}
			if (StringUtils.isNotEmpty(dto.getKeyWord())){
				queryWrapper.like("hs.name",dto.getKeyWord())
						.or()
						.like("hs.logogram",dto.getKeyWord());
			}
			return Response.success(energyInitialConfigService.pageSource(page, queryWrapper));
		}
		return Response.paramError();
	}

	@ApiOperation("添加")
	@PutMapping
	public Response saveOrUpdate(@RequestBody List<EnergyInitialCodeConfig> list){
		try{
			energyInitialConfigService.saveOrUpdateBatch(list);
		}catch (Exception e){
			return Response.warn("变量已添加过");
		}
		return Response.success();
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id){
		return Response.success(energyInitialConfigService.removeById(id));
	}
}
