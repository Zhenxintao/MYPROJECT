package com.bmts.heating.service.task.history.task.collect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.utils.common.DateTimeUtil;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.service.task.history.service.QuerySource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@Component("collect_converge_hour")
public class HourCalc extends EsCalcParent implements Job {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	@Autowired
	private HeatSystemService heatSystemService;
	@Autowired
	private QuerySource querySource;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		compute();
	}

	private void compute() {
		log.info("整点数据");
		QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("deleteFlag",false);
		//获取系统信息
		List<HeatSystem> heatSystemList = heatSystemService.list(queryWrapper);
		if(heatSystemList.size()>0) {
			heatSystemList.parallelStream().forEach(this::convergeFunction);
		}
	}

//	@Scheduled(cron = "0 08 10 * * ?")
//	public void sdsd(){
//  	compute();
//	}

	private void convergeFunction(HeatSystem system) {
		QueryEsDto dto = new QueryEsDto();
		//机组
		dto.setHeatSystemId(new Integer[]{system.getId()});
		//汇聚字段
//		dto.setIncludeFields(configs.stream().map(EsDocConfig::getPointName).distinct().toArray(String[]::new));
		//获取前?分钟整点时间
		dto.setStart(LocalTimeUtils.getMinute(-10));
		//获取当前分钟时间
		dto.setEnd(LocalTimeUtils.getMinute(0));
		SearchResponse response = querySource.getDataByHeatSystem(EsIndex.FIRST_REAL_DATA.getIndex(), dto);
		Stream<Map<String, Object>> mapStream = Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap);
		Comparator<Map<String, Object>> timeStrap = Comparator.comparing(e -> ParseUtil.parseLong(e.get("timeStrap")));
		mapStream.min(timeStrap).ifPresent(this::setTimeStamp);
	}

	private void setTimeStamp(Map<String,Object> map){
		map.put("timeStrap",getHour());
		super.save(EsIndex.FIRST_HOUR, map);
	}

	private long getHour() {
		LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(0);
		return getTimestampOfDateTime(localDateTime);
	}

	private long getTimestampOfDateTime(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		return instant.toEpochMilli();
	}


}
