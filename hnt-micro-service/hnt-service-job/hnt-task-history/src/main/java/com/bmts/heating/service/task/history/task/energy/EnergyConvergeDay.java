package com.bmts.heating.service.task.history.task.energy;

import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.service.task.history.pojo.EnergyChild;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjw
 */
@EqualsAndHashCode(callSuper = true)
@Component("energy_converge_day")
@Data
@Slf4j
public class EnergyConvergeDay extends Converge implements Job {

	private List<EnergyConfigResponse> configs;
	private List<HeatSystemBase> systems;
	@Override
	public void execute(JobExecutionContext context) {
		compute();
	}

	private void compute() {
		log.info("energy_converge_day--start");
		//从关系库中加载统计逻辑配置
		configs = super.getConfig();
		List<HeatSystemBase> systems = super.getSystemIds();
		systems.forEach(this::convergeFunction);
	}

//	@Scheduled(cron = "0 48 10 * * ? ")
//	public void execute1() throws JobExecutionException {
//		compute();
//	}


	@Override
	void convergeFunction(HeatSystemBase heatSystem) {
		long start = LocalTimeUtils.getDay(-1);
		long end = LocalTimeUtils.getDay(-0);
		List<Map<String, Object>> data = super.getData(start,end,heatSystem.getRelevanceId(), "energy_converge_hour");
		Map<String,Object> map = new HashMap<>(10);
		Double area = 0.0;
		for (EnergyConfigResponse config : configs) {
			EnergyChild child = new EnergyChild();
			Double converge = 0.0;
			Double unitConsumption = 0.0;
			for (Map<String, Object> e : data) {
				Long timeStrap = ParseUtil.parseLong(e.get("timeStrap"));
				Map<String, Object> childMap = (Map<String, Object>) e.get(config.getColumnName());
				if (childMap == null) {
					continue;
				}
				assert timeStrap != null;
				if (timeStrap.equals(start)) {
					child.setBeforeValue(ParseUtil.parseDouble(childMap.get("realValue")));
				}else if(timeStrap.equals(end)){
					child.setRealValue(ParseUtil.parseDouble(childMap.get("beforeValue")));
				}
				converge += ParseUtil.parseDouble(childMap.get("consumption"));
				unitConsumption += ParseUtil.parseDouble(childMap.get("unitConsumption"));
				area += ParseUtil.parseDouble(childMap.get("area"));
			}
			if (converge == 0.0){
				continue;
			}
			child.setConsumption(ParseUtil.parseDouble(converge));
			child.setUnitConsumption(ParseUtil.parseDouble(unitConsumption));
			area = area/data.size();
			map.put(config.getColumnName(),child);
		}
		this.configChildBase(map,start,heatSystem.getRelevanceId(),area);
		super.save(map, IndexCoordinates.of(EsEnergyIndex.ENERGY_CONVERGE_DAY.getIndex()));
	}



}
