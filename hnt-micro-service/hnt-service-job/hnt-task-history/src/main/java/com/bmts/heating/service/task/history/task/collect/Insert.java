package com.bmts.heating.service.task.history.task.collect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.service.task.history.service.QuerySource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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
@Component
public class Insert extends EsCalcParent {
	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	@Autowired
	private HeatSystemService heatSystemService;
	@Autowired
	private QuerySource querySource;



//	@Scheduled(cron = "0 34 17 * * ?")
//	public void execute() {
//		log.info("造数据");
//		QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("deleteFlag",false);
//		//获取系统信息
//		int[] systemIds = new int[20];
//		for (int i = 0; i < systemIds.length; i++) {
//			systemIds[i] = 226+i;
//		}
//		long s = 1621539720000L;
//		long end = 1306388400000L;
//		while(s > end) {
//			String localDateTime = LocalTimeUtils.getLocalDateTime(s, "yyyy-MM-dd HH:mm:ss");
//			log.info(localDateTime);
//			long finalS = s;
//			Arrays.stream(systemIds).forEach(e -> convergeFunction(e,finalS));
//			s -= (1000*60*2);
//		}
//	}

	private void convergeFunction(int system, long s) {
		QueryEsDto dto = new QueryEsDto();
		//机组
		dto.setHeatSystemId(new Integer[]{system});
		//汇聚字段
		//获取前?分钟整点时间
		dto.setStart(LocalTimeUtils.getMinute(-4));
		//获取当前分钟时间
		dto.setEnd(LocalTimeUtils.getMinute(0));
		SearchResponse response = querySource.getDataByHeatSystem(EsIndex.FIRST_REAL_DATA.getIndex(), dto);
		Stream<Map<String, Object>> mapStream = Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap);
		Comparator<Map<String, Object>> timeStrap = Comparator.comparing(e -> ParseUtil.parseLong(e.get("timeStrap")));
		mapStream.max(timeStrap).ifPresent(e -> this.setTimeStamp(e,s));
	}

	private void setTimeStamp(Map<String,Object> map,long s){
		map.put("timeStrap",s);
		super.save(EsIndex.FIRST_REAL_DATA, map);
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
