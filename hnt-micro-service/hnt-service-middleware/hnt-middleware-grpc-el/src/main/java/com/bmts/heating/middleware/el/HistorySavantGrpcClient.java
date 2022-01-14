package com.bmts.heating.middleware.el;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchGrpc;
import com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Slf4j
@Astrict(servicename = "elasticsearch-server", servicetype = Astrict.EnumService.grpc)
public class HistorySavantGrpcClient extends SavantServices {

	//region 基础历史数据
	public Map<String, Object> queryEsData(QueryEsDto dto) {
		String serverName = "elasticsearch-server";
		ConnectionToken cd = null;
		ManagedChannel serverChannel = null;
		ElasticsearchGrpc.ElasticsearchBlockingStub stub = null;
		try {
			cd = super.getToken(serverName);
			serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), new Integer(cd.getPort())).usePlaintext().build();
			stub = ElasticsearchGrpc.newBlockingStub(serverChannel);

		} catch (MicroException e) {
			e.printStackTrace();
			super.clearToken(serverName, e);
		} finally {
			super.backToken(serverName, cd);
		}
		assert stub != null;
		Iterator<ElasticsearchOuterClass.HistoryResponse> mls = stub.findByHeatSystem(this.buildHistoryRequest(dto));
		assert serverChannel != null;
		Map<String,Object> result = new HashMap<>();
		result.put("data",mls);
		result.put("channel",serverChannel);
		return result;
	}

	private ElasticsearchOuterClass.HistoryRequest buildHistoryRequest(QueryEsDto dto){
		ElasticsearchOuterClass.HistoryRequest.Builder builder = ElasticsearchOuterClass.HistoryRequest.newBuilder();
		if (dto.getStart() <= dto.getEnd()) {
			builder.setStart(dto.getStart())
					.setEnd(dto.getEnd());
		}
		if(dto.getHeatSystemId()!=null && dto.getHeatSystemId().length > 0){
			builder.addAllHeatSystemId(Arrays.asList(dto.getHeatSystemId()));
		}
		if (dto.getIncludeFields()!=null && dto.getIncludeFields().length>0) {
			builder.addAllIncludeFields(Arrays.asList(dto.getIncludeFields()));
		}
		if (dto.getExcludeFields()!=null && dto.getExcludeFields().length>0) {
			builder.addAllExcludeFields(Arrays.asList(dto.getExcludeFields()));
		}
		//路由数据
		this.route(dto.getDocument().name(), dto.getSourceType().name(), builder);
		if (dto.getCurrentPage()!=null && dto.getCurrentPage()>0) {
			builder.setCurrentPage(dto.getCurrentPage());
		}
		if (dto.getSize() != null && dto.getSize() > 0){
			builder.setSize(dto.getSize());
		}
		if ( !StringUtils.isEmpty(dto.getField()) && dto.getSortType() != null){
			builder.setField(dto.getField())
					.setSortType(dto.getSortType());
		}
		return builder.build();
	}

	private void route(String documentName,String sourceTypeName, ElasticsearchOuterClass.HistoryRequest.Builder builder){
		switch (documentName){
			case "REAL_DATA":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.REAL_DATA); break;
			case "HOUR":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.HOUR);break;
			case "DAY":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.DAY); break;
			case "HOUR_AVG":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.HOUR_AVG); break;
			default:
				throw new RuntimeException("param is error");
		}
		switch (sourceTypeName){
			case "FIRST":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.FIRST); break;
			case "SECOND":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.SECOND); break;
			case "INDOOR_TEMP":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.INDOOR_TEMP); break;
			case "ENERGY_CONVERGE":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.ENERGY_CONVERGE); break;
			default:
				throw new RuntimeException("param is error");
		}
	}
	//endregion

	//region 能耗聚合历史数据

	public Map<String, Object> queryEsBucketData(QueryEsBucketDto dto) {
		String serverName = "elasticsearch-server";
		ConnectionToken cd = null;
		ManagedChannel serverChannel = null;
		ElasticsearchGrpc.ElasticsearchBlockingStub stub = null;
		try {
			cd = super.getToken(serverName);
			serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), new Integer(cd.getPort())).usePlaintext().build();
			stub = ElasticsearchGrpc.newBlockingStub(serverChannel);

		} catch (MicroException e) {
			e.printStackTrace();
			super.clearToken(serverName, e);
		} finally {
			super.backToken(serverName, cd);
		}
		assert stub != null;
		Iterator<ElasticsearchOuterClass.HistoryResponse> mls = stub.bucket(this.buildBucketRequest(dto));
		assert serverChannel != null;
		Map<String,Object> result = new HashMap<>();
		result.put("data",mls);
		result.put("channel",serverChannel);
		return result;
	}

	private ElasticsearchOuterClass.BucketRequest buildBucketRequest(QueryEsBucketDto dto){
		ElasticsearchOuterClass.BucketRequest.Builder builder = ElasticsearchOuterClass.BucketRequest.newBuilder();
		for (int i = 0; i < dto.getTimeRanges().size(); i++) {
			ElasticsearchOuterClass.TimeRange.Builder timeRangBuild = ElasticsearchOuterClass.TimeRange.newBuilder();
			timeRangBuild.setStart(dto.getTimeRanges().get(i).getStart());
			timeRangBuild.setEnd(dto.getTimeRanges().get(i).getEnd());
			timeRangBuild.setIndex(dto.getTimeRanges().get(i).getIndex());
			builder.addTimeRange(timeRangBuild);
		}

		if(dto.getHeatSystemId()!=null && dto.getHeatSystemId().length > 0){
			builder.addAllHeatSystemId(Arrays.asList(dto.getHeatSystemId()));
		}
		if (dto.getIncludeFields()!=null && dto.getIncludeFields().length>0) {
			builder.addAllIncludeFields(Arrays.asList(dto.getIncludeFields()));
		}
		if (dto.getExcludeFields()!=null && dto.getExcludeFields().length>0) {
			builder.addAllExcludeFields(Arrays.asList(dto.getExcludeFields()));
		}
		builder.setIndex(dto.getIndex());
		//路由数据
		this.route(dto.getDocument().name(), dto.getSourceType().name(), builder);

		return builder.build();
	}

	private void route(String documentName,String sourceTypeName, ElasticsearchOuterClass.BucketRequest.Builder builder){
		switch (documentName){
			case "REAL_DATA":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.REAL_DATA); break;
			case "HOUR":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.HOUR);break;
			case "DAY":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.DAY); break;
			case "HOUR_AVG":
				builder.setDoc(ElasticsearchOuterClass.HistoryDocument.HOUR_AVG); break;
		}
		switch (sourceTypeName){
			case "FIRST":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.FIRST); break;
			case "SECOND":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.SECOND); break;
			case "INDOOR_TEMP":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.INDOOR_TEMP); break;
			case "ENERGY_CONVERGE":
				builder.setType(ElasticsearchOuterClass.HistorySourceType.ENERGY_CONVERGE); break;
		}
	}

	//endregion
}
