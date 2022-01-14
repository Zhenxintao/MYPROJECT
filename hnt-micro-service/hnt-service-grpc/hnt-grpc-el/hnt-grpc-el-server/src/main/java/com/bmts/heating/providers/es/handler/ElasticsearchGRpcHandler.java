package com.bmts.heating.providers.es.handler;


import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchGrpc;
import com.bmts.heating.commons.grpc.lib.services.elasticsearch.ElasticsearchOuterClass;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;
import com.bmts.heating.providers.es.service.impl.QuerySourceImpl;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@GrpcService(ElasticsearchOuterClass.class)
public class ElasticsearchGRpcHandler extends ElasticsearchGrpc.ElasticsearchImplBase {

	@Autowired
	private QuerySourceImpl querySource;

	//region 基础查询
	@Override
	public void findByHeatSystem(ElasticsearchOuterClass.HistoryRequest request, StreamObserver<ElasticsearchOuterClass.HistoryResponse> responseObserver){
		Map<String, Object> map = querySource.getData(route(request.getDoc(), request.getType()), this.processParameters(request));
		Stream<String> data;
		String total;
		try {
			if (map.size() > 0){
				data = (Stream<String>) map.get("stream");
				data.forEach(e -> responseObserver.onNext(ElasticsearchOuterClass.HistoryResponse.newBuilder().setResult(e).build()));
			}
		}catch (Exception e){
			log.warn("数据为空或不可查询");
		}finally {
			if (map.get("total")!=null) {
				total = map.get("total").toString();
				responseObserver.onNext(ElasticsearchOuterClass.HistoryResponse.newBuilder().setResult(total).build());
			}
			responseObserver.onCompleted(); //流结束
		}
	}

	private QueryEsDto processParameters(ElasticsearchOuterClass.HistoryRequest request){
		QueryEsDto queryDto = new QueryEsDto();
		/* 基础参数*/
		queryDto.setStart(request.getStart());
		queryDto.setEnd(request.getEnd());
		Integer[] systemIds = new Integer[request.getHeatSystemIdCount()];
		for (int i = 0; i < request.getHeatSystemIdCount(); i++) {
			systemIds[i] = request.getHeatSystemId(i);
		}
		queryDto.setHeatSystemId(systemIds);
		//包含列
		String [] includeFields = new String[ request.getIncludeFieldsCount()];
		for (int i = 0; i < request.getIncludeFieldsCount(); i++) {
			includeFields[i] = request.getIncludeFields(i);
		}
		queryDto.setIncludeFields(includeFields);
		//剔除列
		String [] excludeFields = new String[ request.getExcludeFieldsCount()];
		for (int i = 0; i < request.getExcludeFieldsCount(); i++) {
			excludeFields[i] = request.getExcludeFields(i);
		}
		queryDto.setExcludeFields(excludeFields);
		/*分页参数*/
		queryDto.setCurrentPage(request.getCurrentPage());
		queryDto.setSize(request.getSize());
		queryDto.setField(request.getField());
		queryDto.setSortType(request.getSortType());
		return queryDto;
	}
	//endregion

	//region common
	private String route(ElasticsearchOuterClass.HistoryDocument doc,ElasticsearchOuterClass.HistorySourceType type){
		if (!type.name().isEmpty() && !doc.name().isEmpty()) {
			if ("ENERGY_CONVERGE".equals(type.name())) {
				return EsEnergyIndex.valueOf(type.name() + "_" + doc.name()).getIndex();
			}
			return EsIndex.valueOf(type.name() + "_" + doc.name()).getIndex();
		}
		return null;
	}
	//endregion

	//region 能耗聚合查询
	@Override
	public void bucket(ElasticsearchOuterClass.BucketRequest request,StreamObserver<ElasticsearchOuterClass.HistoryResponse> responseObserver){
		try {
			for (int i = 0; i < request.getTimeRangeCount(); i++) {
				TimeRange timeRange = new TimeRange();
				timeRange.setStart(request.getTimeRange(i).getStart());
				timeRange.setEnd(request.getTimeRange(i).getEnd());
				timeRange.setIndex(request.getTimeRange(i).getIndex());
				Stream<Map<String,Object>> bucketData = querySource.getBucketData(route(request.getDoc(), request.getType()),this.processParameters(request), timeRange).stream();
				bucketData.forEach(e -> responseObserver.onNext(ElasticsearchOuterClass.HistoryResponse.newBuilder().setResult(String.valueOf(e)).build()));
			}
		}catch (Exception e){
			log.warn("数据为空或不可查询");
		}finally {
			responseObserver.onCompleted(); //流结束
		}

	}



	private QueryEsBucketDto processParameters(ElasticsearchOuterClass.BucketRequest request){
		QueryEsBucketDto queryEsBucketDto = new QueryEsBucketDto();
		/* 基础参数 时间*/
		Integer[] systemIds = new Integer[request.getHeatSystemIdCount()];
		for (int i = 0; i < request.getHeatSystemIdCount(); i++) {
			systemIds[i] = request.getHeatSystemId(i);
		}
		queryEsBucketDto.setHeatSystemId(systemIds);
		//包含列
		String [] includeFields = new String[ request.getIncludeFieldsCount()];
		for (int i = 0; i < request.getIncludeFieldsCount(); i++) {
			includeFields[i] = request.getIncludeFields(i);
		}
		queryEsBucketDto.setIncludeFields(includeFields);
		//剔除列
		String [] excludeFields = new String[ request.getExcludeFieldsCount()];
		for (int i = 0; i < request.getExcludeFieldsCount(); i++) {
			excludeFields[i] = request.getExcludeFields(i);
		}
		queryEsBucketDto.setExcludeFields(excludeFields);
		queryEsBucketDto.setIndex(request.getIndex());
		return queryEsBucketDto;
	}

	//endregion
}
