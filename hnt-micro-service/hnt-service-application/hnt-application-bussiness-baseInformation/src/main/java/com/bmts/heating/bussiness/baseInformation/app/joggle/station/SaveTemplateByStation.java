package com.bmts.heating.bussiness.baseInformation.app.joggle.station;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.PointTemplateConfigService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.TemplatePointService;
import com.bmts.heating.commons.entiy.baseInfo.request.station.TemplateStationDto;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@RestController
@Api(tags = "保存模板")
@Slf4j
@RequestMapping("/StationTemplate")
public class SaveTemplateByStation {

	@Autowired
	private HeatSystemService heatSystemService;
	@Autowired
	private PointTemplateConfigService pointTemplateConfigService;

	@Autowired
	private TemplatePointService templatePointService;
//	@Autowired
//	private TemplateControlService controlService;
//	@Autowired
//	private TemplateCollectService collectService;

	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private StationFirstNetBaseViewService stationFirstNetBaseViewService;

	@ApiOperation("根据系统获取所有机组")
	@GetMapping("cache/{id}")
	public Response queryHeatSystemInCache(@PathVariable Integer id) throws MicroException {
		JSONObject jsonObject = new JSONObject();
		List<StationFirstNetBaseView> list = stationFirstNetBaseViewService.list();
		Map<String, List<StationFirstNetBaseView>> collect = list.stream().filter(e -> e.getHeatTransferStationId() == id).collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatTransferStationName));
		for (Map.Entry<String, List<StationFirstNetBaseView>> entry : collect.entrySet()) {
			List<JSONObject> childrenLis = new ArrayList<>();
			Map<String, List<StationFirstNetBaseView>> cab = entry.getValue().stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatCabinetName));
			for (Map.Entry<String, List<StationFirstNetBaseView>> map : cab.entrySet()) {
				JSONObject children = new JSONObject();
				children.put("cabinetName",map.getKey());
				children.put("children",map.getValue());
				childrenLis.add(children);
			}
			jsonObject.put("stationName",entry.getKey());
			jsonObject.put("children",childrenLis);
		}
		return Response.success(jsonObject);
	}


	@ApiOperation("根据系统获取所有机组")
	@GetMapping("{id}")
	public Response queryHeatSystem(@PathVariable Integer id) {
		QueryWrapper<HeatSystem> wrapper = new QueryWrapper<>();
		wrapper.eq("hts.id", id);
		return id != null ? Response.success(heatSystemService.querySystemNameByStationId(wrapper)) : Response.paramError();
	}


	@ApiOperation("保存模板")
	@PostMapping
	public Response insert(@RequestBody Collection<TemplateStationDto> dtoList) {
		AtomicInteger succ = new AtomicInteger();
		AtomicInteger num = new AtomicInteger();
		dtoList.forEach(e -> {
			PointTemplateConfig temp = new PointTemplateConfig();
			temp.setCreateUser(e.getUserName());
			temp.setCreateTime(LocalDateTime.now());
			temp.setDescription(e.getDesc());
			temp.setTemplateValue(e.getTemplateName());
			temp.setUserId(e.getUserId());
			QueryWrapper wrapper = new QueryWrapper();
			wrapper.eq("pc.level", TreeLevel.HeatSystem.level());
			if (pointTemplateConfigService.save(temp)) {
				if (e.getType() == 0) {//全部保存
					temp.setTemplateKey("Station");
					wrapper.eq("pc.relevanceId", e.getRelevanceId());
				}
				else if (e.getType() == 1) {//采集量模板
					temp.setTemplateKey("Station");
					wrapper.eq("ps.pointConfig", PointProperties.ReadOnly.type());
					wrapper.eq("pc.relevanceId", e.getRelevanceId());
				}
				else if (e.getType() == 2) {//控制量模板
					temp.setTemplateKey("Station");
					wrapper.eq("ps.pointConfig", PointProperties.ReadAndControl.type());
					wrapper.eq("pc.relevanceId", e.getRelevanceId());
				}
				succ.set(templatePointService.copyCollectTemplate(temp.getId(), wrapper));
				if(pointTemplateConfigService.updateById(temp) && succ.get() > 0)
					num.getAndSet(num.get() + 1);
			}
		});
		return num.get() > 0 ? Response.success(num.get()) : Response.fail("保存失败");
	}

}




