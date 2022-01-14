package com.bmts.heating.service.task.history.task.energy;

import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.service.task.history.pojo.EnergyChild;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hjw
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Component("energy_converge_hour")
@Data
public class EnergyConvergeHour extends Converge implements Job {

	private List<EnergyConfigResponse> configs;
	private List<HeatSystemBase> systems;


	/**
	 * 定时任务启动入口
	 */
	@Override
	public void execute(JobExecutionContext context) {
		compute();
	}

	private void compute() {
		log.info("energy_converge_hour--start");
		//从关系库中加载统计逻辑配置
		configs = super.getConfig();
		List<HeatSystemBase> systems = super.getSystemIds();
		systems.forEach(this::convergeFunction);
	}

//	private void configChildBase(Map<String,Object> map, long end,int relevanceId,BigDecimal area){
//		map.put("timeStrap",end);
//		map.put("level", TreeLevel.HeatSystem.level());
//		map.put("type","energy");
//		map.put("relevanceId",relevanceId);
//		map.put("area",ParseUtil.parseDouble(area));
//	}

	@Override
	void convergeFunction(HeatSystemBase heatSystem){
		long start = LocalTimeUtils.getHour(-1);
		long end = LocalTimeUtils.getHour(0);
		List<Map<String, Object>> data = super.getData(start,end,heatSystem.getRelevanceId(), EsIndex.FIRST_HOUR.getIndex());
		Map<String,Object> map = new HashMap<>(10);
		super.configChildBase(map,start, heatSystem.getRelevanceId(),  ParseUtil.parseDouble(heatSystem.getArea()));
		boolean index = false;
		for (EnergyConfigResponse config : configs) {
			EnergyChild child = new EnergyChild();
			for (Map<String, Object> e : data) {
				Long timeStrap = ParseUtil.parseLong(e.get("timeStrap"));
				Object v = e.get(config.getColumnName());
				if (v == null) {
					continue;
				}
				assert timeStrap != null;
				if (timeStrap.equals(start)) {
					child.setBeforeValue(ParseUtil.parseDouble(v));
				}else if(timeStrap.equals(end)){
					child.setRealValue(ParseUtil.parseDouble(v));
				}
			}
			if(child.verify()){
				continue;
			}else {
				index = true;
			}
			child.setConsumption(ParseUtil.parseDouble(child.getRealValue()-child.getBeforeValue()));
			if (heatSystem.getArea() != null && !Objects.equals(heatSystem.getArea(), BigDecimal.ZERO)) {
				Double area = ParseUtil.parseDouble(heatSystem.getArea());
				child.setUnitConsumption(ParseUtil.parseDouble(area != null && area != 0 ? child.getConsumption() / area : 0));
			}
			map.put(config.getColumnName(),child);
		}
		if(index) {
			if (heatSystem.getRelevanceId() == 842){
				log.info("================842:{}",map);
			}
			super.save(map, IndexCoordinates.of(EsEnergyIndex.ENERGY_CONVERGE_HOUR.getIndex()));
		}
	}
}
