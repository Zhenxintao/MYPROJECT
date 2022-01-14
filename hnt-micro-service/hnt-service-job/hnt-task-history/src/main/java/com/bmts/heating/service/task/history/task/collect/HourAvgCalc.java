package com.bmts.heating.service.task.history.task.collect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.service.task.history.service.QuerySource;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("collect_converge_hour_avg")
@EnableScheduling//开启定时
@Slf4j
public class HourAvgCalc extends EsCalcParent implements Job {

	@Autowired
	private HeatSystemService heatSystemService;
	@Autowired
	private QuerySource querySource;



	@Override
	public void execute(JobExecutionContext context) {
		log.warn("hour-avg - aggregation - start");
		this.calculate();
	}

	private void calculate(){
		//获取汇聚策略
		List<EsDocConfig> configs = super.getConfig();
		QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("deleteFlag",false);
		//获取系统信息
		List<HeatSystem> heatSystemList = heatSystemService.list(queryWrapper);
		//遍历系统
		heatSystemList.forEach(e -> historyAvgByHeatSystemId(e.getId(),configs));
	}
//
//	@Scheduled(cron = "0 29 13 * * ?")
//	public void sdsd(){
//		this.calculate();
//	}

	private void historyAvgByHeatSystemId(Integer heatSystemId, List<EsDocConfig> configs){
		QueryEsDto dto = new QueryEsDto();
		//机组
		dto.setHeatSystemId(new Integer[]{heatSystemId});
		//汇聚字段
		dto.setIncludeFields(configs.stream().map(EsDocConfig::getPointName).distinct().toArray(String[]::new));
		//获取当前小时整点时间
		dto.setStart(LocalTimeUtils.getHour(-1));
		//获取前一小时整点时间段
		dto.setEnd(LocalTimeUtils.getHour(0));
		//获取历史数据  返回结果中包含数据最大值、最小值、平均值
		SearchResponse response = querySource.getDataByHeatSystem(EsIndex.FIRST_REAL_DATA.getIndex(), dto);
		//对于单系统开始汇聚小时平均数据
		try{
			Map<String, Object> map = super.historyAvgByHeatSystemId(response, configs);
			if (map.size() == 0) {
				return;
			}
			map.put("relevanceId",heatSystemId);
			map.put("timeStrap", LocalTimeUtils.getHour(-1));
			map.put("level",1);
			super.save(EsIndex.FIRST_HOUR_AVG,map);
		}catch (Exception e){
			e.printStackTrace();
		}
	}



}
