package com.bmts.heating.middleground.history.service;

import com.alibaba.fastjson.JSON;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass;
import com.bmts.heating.middleware.el.HistorySavantGrpcClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class HistoryDataImpl implements HistoryData{

	@Autowired
	private HistorySavantGrpcClient client;

	@Override
	public List<Map<String, String>> findHistoryData(QueryEsDto dto){
		Map<String, Object> map = client.queryEsData(dto);
		List<Map<String,String>> list = new ArrayList<>();
		try {
			Object o = map.get("data");
			if (o instanceof Iterator) {
				Iterator<ElasticsearchOuterClass.HistoryResponse> data = (Iterator<ElasticsearchOuterClass.HistoryResponse>)o ;
				data.forEachRemaining(e -> list.add(strToMap(e.getResult())));
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error("通信失败");
		}finally {
			ManagedChannel channel = (ManagedChannel) map.get("channel");
			channel.shutdown();
		}
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		if (dto.getIncludeTotal() == null || !dto.getIncludeTotal()) {
			list.remove(list.size()-1);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findHistoryEnergyData(QueryEsDto dto){
		Map<String, Object> map = client.queryEsData(dto);
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			Iterator<ElasticsearchOuterClass.HistoryResponse> data = (Iterator<ElasticsearchOuterClass.HistoryResponse>) map.get("data");
			data.forEachRemaining(e -> list.add(strToMapE(e.getResult())));
		}catch (Exception e){
			e.printStackTrace();
			log.error("通信失败");
		}finally {
			ManagedChannel channel = (ManagedChannel) map.get("channel");
			channel.shutdown();
		}
		if (CollectionUtils.isEmpty(list)) {
			log.warn("返回为空.....................");
			return null;
		}
		if (dto.getIncludeTotal() == null || !dto.getIncludeTotal()) {
			list.remove(list.size()-1);
		}
		return list;
	}

	@Override
	public List<Map<String, String>> bucketEnergyData(QueryEsBucketDto dto){
		Map<String, Object> map = client.queryEsBucketData(dto);
		List<Map<String,String>> list = new ArrayList<>();
		try {
			Iterator<ElasticsearchOuterClass.HistoryResponse> data = (Iterator<ElasticsearchOuterClass.HistoryResponse>) map.get("data");
			data.forEachRemaining(e -> list.add(strToMap(e.getResult())));
			System.out.print(list.size());

		}catch (Exception e){
			e.printStackTrace();
			log.error("通信失败");
		}finally {
			ManagedChannel channel = (ManagedChannel) map.get("channel");
			channel.shutdown();
		}
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}


	private HashMap<String,String> strToMap(String str){
		Gson gson = new Gson();
		HashMap<String, String> res = new HashMap<>();
		try {
			res = gson.fromJson(str, new TypeToken<HashMap<String, String>>() {}.getType());
		}catch (Exception e){
			log.info(str);
			e.printStackTrace();
		}
		return res;
	}

	private HashMap strToMapE(String str){
		HashMap map = new HashMap<>(16);
		try{
			 map = JSON.parseObject(str, HashMap.class);
		log.info(map.toString());
		}catch (Exception e){
			log.error(str);
			e.printStackTrace();
		}
		return map;
	}


//	public static void main(String[] args) {
//		String a = "{area=1.0, level=1, HeatSourceTotalHeat_MtrGunitStandard=42279.875, index=2021-05-10, HeatSourceTotalHeat_MtrG=42279.875}";
//		String b = "{area=Infinity, HeatSourceFTSupply=11062.5, HeatSourceFTSupplyunitStandard=11062.5, HeatSourceTotalHeat_MtrGunitStandard=11065.0, index=表格, relevanceId=820, HeatSourceTotalHeat_MtrG=11065.0, HeatSourceEp=0.0, HeatSourceEpunitStandard=0.0}";
//
//		Gson gson = new Gson();
//		Object o = gson.fromJson(b, new TypeToken<Map<String, String>>() {
//		}.getType());
//		System.out.println(o);
//		Object p = gson.fromJson(a, new TypeToken<Map<String, String>>() {
//		}.getType());
//		System.out.println(p);
//	}
}
