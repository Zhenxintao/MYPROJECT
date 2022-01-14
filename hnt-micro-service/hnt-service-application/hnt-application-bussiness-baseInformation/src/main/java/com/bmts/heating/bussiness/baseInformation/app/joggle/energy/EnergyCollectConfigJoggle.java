package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfig;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfigChild;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.db.service.EnergyCollectConfigChildService;
import com.bmts.heating.commons.db.service.EnergyCollectConfigService;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigAddDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigParamQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Api(tags = "能耗采集点配置")
@RestController
@RequestMapping("/energy/collect")
public class EnergyCollectConfigJoggle {
	@Autowired
	private EnergyCollectConfigService energyCollectConfigService;
	@Autowired
	private EnergyCollectConfigChildService energyCollectConfigChildService;
	@Autowired
	private HeatSourceService heatSourceService;

	@ApiOperation("分页查询")
	@PostMapping("/page")
	public Response page(@RequestBody EnergyCollectConfigQueryDto dto) {
		Page<HeatTransferStationResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
		QueryWrapper<EnergyCollectConfig> queryWrapper = new QueryWrapper<>();
		if (dto.getStationId() != null) {
			queryWrapper.eq("hc.heatTransferStationId", dto.getStationId());
		} else if (dto.getHeatSourceId() != null) {
			queryWrapper.eq("hc.heatSourceId", dto.getHeatSourceId());
		} else {
			return Response.paramError();
		}
		if (StringUtils.isNotBlank(dto.getKeyWord())) {
			queryWrapper.like("hs.name", dto.getKeyWord()).or().like("hc.name", dto.getKeyWord());
		}
		return Response.success(energyCollectConfigService.page(page, queryWrapper));
	}

	@ApiOperation("加载站下所有系统参量")
	@PostMapping
	public List<EnergyPointResponse> queryAll(@RequestBody EnergyCollectConfigParamQueryDto dto) {
		QueryWrapper<EnergyCollectConfig> queryWrapper = new QueryWrapper<>();
		if (dto.getStationId() != null) {
			queryWrapper.eq("hc.heatTransferStationId", dto.getStationId());
		} else if (dto.getHeatSourceId() != null) {
			queryWrapper.eq("hc.heatSourceId", dto.getHeatSourceId());
		}
		return energyCollectConfigService.queryAll(queryWrapper, dto.getComputeType());
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id) {
		if (energyCollectConfigChildService.remove(Wrappers.<EnergyCollectConfigChild>lambdaQuery().eq(EnergyCollectConfigChild::getTargetId, id))) {
			return energyCollectConfigService.removeById(id) ? Response.success() : Response.fail();
		}
		return Response.fail();
	}


	@ApiOperation("保存")
	@PutMapping("/saveOrUpdate")
	@Transactional(rollbackFor = Exception.class)
	public Response saveOrUpdate(@RequestBody EnergyCollectConfigAddDto dto) {
		if (dto.isValid()) {
			return Response.paramError();
		}
		String jep = dto.getExpression();
		Map<String, EnergyPointResponse> map = dto.getMap();
		String[] split = jep.split("=");
		if (split.length != 2) {
			return Response.paramError("jep表达式无法识别");
		}
		//目标计算量配置
		EnergyCollectConfig energyCollectConfig = new EnergyCollectConfig();
		EnergyPointResponse energyPointResponse = map.remove(split[0]);
		if (energyPointResponse.getId() != null && energyPointResponse.getId() > 0) {
			energyCollectConfig.setId(energyPointResponse.getId());
		}
		energyCollectConfig.setIsConverge(true);
		energyCollectConfig.setColumnName(energyPointResponse.getColumnName());
		energyCollectConfig.setExpression(jep);
		energyCollectConfig.setRelevanceId(energyPointResponse.getSystemId());
		if (energyPointResponse.getPointId() == null || energyPointResponse.getPointId() == 0){
			return Response.warn("不允许添加基础变量为计算量");
		}
		energyCollectConfig.setPointTargetId(energyPointResponse.getPointId());
		energyCollectConfig.setSign(split[0]);
		//切分系统和参量名称 eg: 0系统-系统面积
		energyCollectConfig.setPointTargetName(energyPointResponse.getPointStandardName().split("-")[1]);
		List<EnergyCollectConfig> list = energyCollectConfigService.list(Wrappers
				.<EnergyCollectConfig>lambdaQuery()
				.eq(EnergyCollectConfig::getPointTargetId, energyPointResponse.getPointId()));
		if (list.size()>0){
			return Response.warn("参量已添加过,请勿重复添加");
		}
		if (energyCollectConfigService.saveOrUpdate(energyCollectConfig)) {
			LambdaQueryWrapper<EnergyCollectConfigChild> wrapper = Wrappers
					.<EnergyCollectConfigChild>lambdaQuery().
							eq(EnergyCollectConfigChild::getTargetId, energyCollectConfig.getId());
			energyCollectConfigChildService.remove(wrapper);
			//代数入参
			List<EnergyCollectConfigChild> energyCollectConfigChildren = this.fun1(map, energyCollectConfig.getId());
			if (energyCollectConfigChildService.saveBatch(energyCollectConfigChildren)) {
				return Response.success();
			} else {
				throw new RuntimeException("保存失败");
			}
		}
		throw new RuntimeException("保存失败");
	}

	private List<EnergyCollectConfigChild> fun1(Map<String, EnergyPointResponse> map, Integer targetId) {
		List<EnergyCollectConfigChild> list = new LinkedList<>();
		map.forEach((k, v) -> {
			EnergyCollectConfigChild child = new EnergyCollectConfigChild();
			child.setPointId(v.getPointId());
			child.setRelevanceId(v.getSystemId());
			child.setSign(k);
			child.setTargetId(targetId);
			child.setColumnName(v.getColumnName());
			if (v.getPointId() == 0) {
				child.setVal(v.getVal());
				child.setStatus(true);
			}
			list.add(child);
		});
		return list;
	}


	@ApiOperation("详情")
	@GetMapping("/info/{id}")
	public Response info(@PathVariable Integer id) {
		EnergyCollectConfigAddDto dto = new EnergyCollectConfigAddDto();
		EnergyCollectConfig target = energyCollectConfigService.getById(id);
		LambdaQueryWrapper<EnergyCollectConfigChild> wrapper = Wrappers.<EnergyCollectConfigChild>lambdaQuery().eq(EnergyCollectConfigChild::getTargetId, id);
		Map<Integer, String> constantIds = new HashMap<>(20);//常量relevanceIds,sign
		Map<Integer, String> variableIds = new HashMap<>(20);//变量pointIds,sign
		//获取所有计算相关参数
		variableIds.put(target.getPointTargetId(), target.getSign());
		energyCollectConfigChildService.list(wrapper).forEach(e -> {
			if (e.getPointId() == 0 || e.getPointId() == null) {
				//常量
				constantIds.put(e.getRelevanceId(), e.getSign());
			} else {
				//变量
				variableIds.put(e.getPointId(), e.getSign());
			}
		});
		Map<String, EnergyPointResponse> resMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(variableIds)) {
			resMap.putAll(this.variableLoad(variableIds, id));
		}
		if (CollectionUtils.isNotEmpty(constantIds)) {
			resMap.putAll(this.constantLoad(constantIds, id));
		}
		dto.setMap(resMap);
		dto.setExpression(target.getExpression());
		return Response.success(dto);
	}

	@ApiOperation("热源列表")
	@GetMapping("/heatSource")
	public Response heatSourceList() {
		return Response.success(heatSourceService.list());
	}


	//加载变量
	private Map<String, EnergyPointResponse> variableLoad(Map<Integer, String> maps, Integer id) {
		Map<String, EnergyPointResponse> resMap = new HashMap<>();
		Set<Integer> ids = maps.keySet();
		QueryWrapper<Object> wrapper = new QueryWrapper<>();
		wrapper.in("pc.id", ids);
		List<EnergyPointResponse> energyPointResponses = energyCollectConfigService.queryVariable(wrapper);
		for (EnergyPointResponse e : energyPointResponses) {
			e.setId(id);
			resMap.put(maps.get(e.getPointId()), e);
		}
		return resMap;
	}

	//加载常量
	private Map<String, EnergyPointResponse> constantLoad(Map<Integer, String> maps, Integer id) {
		Map<String, EnergyPointResponse> resMap = new HashMap<>();
		Set<Integer> ids = maps.keySet();
		QueryWrapper<Object> wrapper = new QueryWrapper<>();
		wrapper.in("hs.id", ids);
		List<EnergyPointResponse> energyPointResponses = energyCollectConfigService.queryConstant(wrapper);
		for (EnergyPointResponse e : energyPointResponses) {
			e.setId(id);
			resMap.put(maps.get(e.getSystemId()), e);
		}
		return resMap;
	}
}
