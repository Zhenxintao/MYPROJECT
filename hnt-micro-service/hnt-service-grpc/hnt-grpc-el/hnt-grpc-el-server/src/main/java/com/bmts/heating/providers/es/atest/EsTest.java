package com.bmts.heating.providers.es.atest;

import com.bmts.heating.providers.es.pojo.EnergyChild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EsTest {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

//	@Scheduled(cron = "0 8 15 * * ?")
	public void insert(){
		for (int i = 0; i < 500; i++) {
			Map<String,Object> map = new HashMap<>();
			map.put("heatCabinetId",1);
			map.put("timeStrap",System.currentTimeMillis());
			map.put("heatingSystemId",1);
			map.put("level",1);
			for (int j = 0; j < 3; j++) {
				EnergyChild child = new EnergyChild();
				double v1 = Math.random() * 100;
				double v2 = Math.random() * 100;
				if (v1 > v2) {
					child.setBeforeValue(v2);
					child.setRealValue(v1);
				} else {
					child.setBeforeValue(v1);
					child.setRealValue(v2);
				}
				double abs = Math.abs(v2 - v1);
				child.setConsumption(abs);
				child.setUnitConsumption(abs / 10);
				map.put("HW_"+j,child);
			}
			elasticsearchRestTemplate.save(map, IndexCoordinates.of("energy"));
		}
	}

}
