package com.bmts.heating.service.task.history.task.collect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.db.service.EsDocConfigService;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.service.task.history.task.common.CalcRule;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public abstract class EsCalcParent {

	@Autowired
	private ElasticsearchRestTemplate template;
	@Autowired
	private EsDocConfigService esDocConfigService;

	public List<EsDocConfig> getConfig() {
		QueryWrapper<EsDocConfig> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("isConverge",true);
		return esDocConfigService.list(queryWrapper);
	}

	protected void mapCollect(Map<String,Object> map, List<EsDocConfig> config, Map<String,List<Object>> listMap){
		for (EsDocConfig esDocConfig : config) {
			String pointName = esDocConfig.getPointName();
			Object data = map.get(pointName);
			if (data == null) {
				continue;
			}
			if(listMap.get(pointName) != null){
				listMap.get(pointName).add(data);
			}else{
				ArrayList<Object> objs = new ArrayList<>();
				objs.add(data);
				listMap.put(pointName,objs);
			}
		}
	}
	//根据pointName分组
	protected Map<String,Object> historyAvgByHeatSystemId(SearchResponse response, List<EsDocConfig> configs){
		Map<String, Object> res = new HashMap<>();
		Map<String,List<Object>> resMap = new HashMap<>();
		Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap)
				.forEach(e -> this.mapCollect(e,configs,resMap));
		for (EsDocConfig c : configs) {
			Aggregations aggregations = response.getAggregations();
			if (aggregations == null)
				break;
			ExtendedStats aggregation = aggregations.get(c.getPointName());
			//根据策略计算平均值
			Object converges = CalcRule.converges(aggregation, resMap.get(c.getPointName()), c.getConvergeTypeHour(), c.getDataType());
			if (converges != null)
				res.put(c.getPointName(), converges);
		}
		return res;
	}
//	//保存计算好的数据
//	protected void save(String index, Map<String, Object> map) {
//		IndexQuery indexQuery;
//		List<IndexQuery> list = new ArrayList<>();
//		indexQuery = new IndexQuery();
//		indexQuery.setObject(map);
//		list.add(indexQuery);
//		try {
//			List<String> demo = client.bulkIndex(list, BulkOptions.defaultOptions(), IndexCoordinates.of(index));
//			log.info("添加数据成功---{}", demo);
//		}catch (Exception e){
//			log.error("添加失败---{0}",e);
//		}
//	}
	protected  void save(EsIndex esIndex, Map<String, Object> map) {
		try {
			Map<String, Object> save = template.save(map, IndexCoordinates.of(esIndex.getIndex()));
//			log.info("添加成功---{}", save.size());
		}catch (Exception e){
			e.printStackTrace();
			log.error("添加失败---{}",e.getMessage());
		}
	}
}
