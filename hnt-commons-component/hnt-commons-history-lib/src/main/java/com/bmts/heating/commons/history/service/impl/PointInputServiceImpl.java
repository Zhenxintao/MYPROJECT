package com.bmts.heating.commons.history.service.impl;

import com.bmts.heating.commons.history.pojo.EnergyChild;
import com.bmts.heating.commons.history.pojo.EsEnergyData;
import com.bmts.heating.commons.history.service.PointInputService;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PointInputServiceImpl implements PointInputService {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;
	@Autowired
	private RestHighLevelClient client;



	@Override
	public void save(EsIndex esIndex, Map<String, Object> map) {
		IndexQuery indexQuery;
		indexQuery = new IndexQuery();
		indexQuery.setObject(map);
		try {
			String index = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of(esIndex.getIndex()));
			log.info("添加成功---{}", index);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("添加失败---{}", e.getMessage());
			log.warn("失败数据---{}", map);
		}
	}


	@Override
	public void saveBulk(EsIndex esIndex, List<Map<String, Object>> maps) {
		BulkRequest request = new BulkRequest();
		maps.forEach(e ->{
			IndexRequest indexQuery = new IndexRequest();
			indexQuery.source(e).index(esIndex.getIndex());
			request.add(indexQuery);
		});
		try {
			client.bulk(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


//	@Override
//	public final void saveBatch(EsIndex esIndex, int level, List<Map<String, Object>> maps) {
//		IndexQuery indexQuery;
//		List<IndexQuery> list = new ArrayList<>();
//		for (Map<String, Object> map : maps) {
//			indexQuery = new IndexQuery();
//			indexQuery.setObject(map);
//			list.add(indexQuery);
//		}
//		try {
//			List<String> demo = elasticsearchRestTemplate.bulkIndex(list, BulkOptions.defaultOptions(), IndexCoordinates.of(esIndex.getIndex()));
//			log.info("添加成功---{}", demo.size());
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("添加失败---{}", e.getMessage());
//		}
//	}



	@Override
	public Map<String, Object> addFields(EsIndex esIndex, Map<String, String> fieldNames) {
		IndexOperations demo = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(esIndex.getIndex()));
		Document document = Document.create();
		Map<String, Object> fieldsMap = new HashMap<>();
		fieldNames.forEach((key, value) -> {
			Map<String, String> properties = new HashMap<>();
			properties.put("type", value);
			fieldsMap.put(key, properties);
		});
		document.append("properties", fieldsMap);
		try {
			demo.putMapping(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return demo.getMapping();
	}

	@Override
	public Map<String, Object> addEnergyFields(EsEnergyIndex esIndex, String field) {
		IndexOperations demo = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(esIndex.getIndex()));
		Document document = Document.create();
		Map<String, Object> fieldsMap = new HashMap<>();

		Map<String, Map<String, Map<String,Object>>> properties = new HashMap<>();
		properties.put("properties", this.beanToMap());
		fieldsMap.put(field, properties);

		document.append("properties", fieldsMap);
		try {
			demo.putMapping(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return demo.getMapping();
	}

	private Map<String, Map<String, Object>> beanToMap() {
		EnergyChild article = new EnergyChild();
		Field[] fields = article.getClass().getDeclaredFields();
		return this.beanToProperties(fields);
	}

	private  Map<String, Map<String, Object>> beanToProperties(Field[] fields){
		Map<String, Map<String, Object>> properties = new HashMap<>();
		for (Field field : fields) {
			Map<String,Object> map = new HashMap<>();
			if ("extend".equals(field.getName())) {
				map.put("type", "text");
			} else {
				map.put("type", "double");
			}
			properties.put(field.getName(),map);
		}
		return properties;
	}

}
