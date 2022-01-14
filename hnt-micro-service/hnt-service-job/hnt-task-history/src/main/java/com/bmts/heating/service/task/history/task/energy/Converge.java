package com.bmts.heating.service.task.history.task.energy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.db.service.EnergyConfigService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.service.task.history.service.QuerySource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public abstract class Converge {
	//获取数据库内需要汇聚的策略管理
	@Autowired
	private EnergyConfigService energyConfigService;
	@Autowired
	private StationFirstNetBaseViewService stationFirstNetBaseViewService;
	@Autowired
	private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
	@Autowired
	private HeatSystemService heatSystemService;
	@Autowired
	private QuerySource querySource;
	@Autowired
	private ElasticsearchRestTemplate template;

	/**
	 * 具体聚合业务实现
	 *
	 * @param heatSystem
	 */
	abstract void convergeFunction(HeatSystemBase heatSystem);

	//获取数据库定义定义策略
	protected List<EnergyConfigResponse> getConfig() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("isConverge", true);
		List<EnergyConfigResponse> list = energyConfigService.queryConfig(wrapper);
		System.out.println(list);
		return list;
	}

	protected List<HeatSystemBase> getSystemIds() {
		LambdaQueryWrapper<StationFirstNetBaseView> stationWrapper = Wrappers.<StationFirstNetBaseView>lambdaQuery()
				.eq(StationFirstNetBaseView::getStatus, true);
		List<StationFirstNetBaseView> stationList = stationFirstNetBaseViewService.list(stationWrapper);
		List<SourceFirstNetBaseView> sourceList = sourceFirstNetBaseViewService.list();
		List<HeatSystemBase> res = new ArrayList<>();
		HeatSystemBase heatSystemBase;
		for (StationFirstNetBaseView e : stationList) {
			heatSystemBase = new HeatSystemBase();
			heatSystemBase.setHeatSystemBase(e);
			res.add(heatSystemBase);
		}
		for (SourceFirstNetBaseView e : sourceList) {
			heatSystemBase = new HeatSystemBase();
			heatSystemBase.setHeatSystemBase(e);
			res.add(heatSystemBase);
		}
		return res;
	}

	//查询历史数据
	protected List<Map<String, Object>> getData(long start, long end, int id, String index) {
		QueryEsDto dto = new QueryEsDto();
		//机组
		dto.setHeatSystemId(new Integer[]{id});
		//汇聚字段
//		dto.setIncludeFields(configs.stream().map(EnergyConfigResponse::getColumnName).distinct().toArray(String[]::new));
		//获取当前小时整点时间
		dto.setStart(start);
		//获取前一小时整点时间段
		dto.setEnd(end);
		SearchHit[] hits = querySource.getDataByHeatSystem(index, dto).getHits().getHits();
		return Arrays.stream(hits).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
	}

	/**
	 * Map 基础信息
	 * @param map 保存对象
	 * @param end 时间
	 * @param relevanceId 关联系统Id
	 * @param area 面积
	 */
	protected void configChildBase(Map<String,Object> map, long end, int relevanceId, double area){
		map.put("timeStrap",end);
		map.put("level", TreeLevel.HeatSystem.level());
		map.put("type","energy");
		map.put("relevanceId",relevanceId);
		map.put("area",area);
	}

	/**
	 * 保存单条数据
	 *
	 * @param map   数据
	 * @param index 历史表
	 */
	protected void save(Map<String, Object> map, IndexCoordinates index) {
		try {
			template.save(map, index);
		} catch (Exception e) {
			log.error("保存数据出错");
		}
	}


}
