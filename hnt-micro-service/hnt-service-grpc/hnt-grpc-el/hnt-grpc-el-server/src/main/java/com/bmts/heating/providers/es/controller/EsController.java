//package com.bmts.heating.providers.es.controller;
//
//import com.bmts.heating.commons.entiy.gathersearch.request.HistoryDocument;
//import com.bmts.heating.commons.entiy.gathersearch.request.HistorySourceType;
//import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
//import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
//import com.bmts.heating.providers.es.service.QuerySource;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class EsController {
//
//	@Autowired
//	QuerySource querySource;
//	@GetMapping
//	public void getDataByHeatSystem(){
//		QueryEsBucketDto dto = new QueryEsBucketDto();
////		dto.setStart(1619265600000L-86400L);
////		dto.setEnd(1619265600000L);
//		dto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
//		dto.setHeatSystemId(new Integer[]{222, 240, 246});
//		dto.setIncludeFields(new String[]{"WM_FT", "HM_HT","Ep"});
////		List<Map<String,Object>> energy_converge_hour = querySource.getBucketData("energy_converge_hour", dto);
////		System.out.println(energy_converge_hour);
//	}
//}
