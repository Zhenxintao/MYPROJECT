package com.bmts.heating.middleware.td;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
//import com.bmts.heating.commons.container.performance.config.GrpcClientPool;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.Datas;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.InsertRequest;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.PointValue;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.Tag;
import com.bmts.heating.commons.grpc.lib.services.common.Common;
import com.bmts.heating.commons.grpc.lib.services.energyConsumption.EnergyHourGrpc;
import com.bmts.heating.commons.grpc.lib.services.energyConsumption.EnergyHourOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Astrict(servicename = "td",servicetype = Astrict.EnumService.grpc)
public class EnergyConsumptionClient extends SavantServices {
//	@Autowired
//	private GrpcClientPool grpcClientPool;

	public Boolean insertEnergyConsumption(InsertRequest insertRequest) {
		String serverName = "td";
		ConnectionToken cd = null;
		ManagedChannel serverChannel = null;
		EnergyHourGrpc.EnergyHourBlockingStub stub = null;
		try {
			cd = super.getToken(serverName);
			serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
//			serverChannel = grpcClientPool.getManagedChannel(cd);
			stub = EnergyHourGrpc.newBlockingStub(serverChannel).withMaxInboundMessageSize(Integer.MAX_VALUE).withMaxOutboundMessageSize(Integer.MAX_VALUE);
		} catch (MicroException e) {
			e.printStackTrace();
			super.clearToken(serverName, e);
		} finally {
			super.backToken(serverName, cd);
		}
		log.info("开始调用td服务");
		Common.BoolResult mls = stub.insert(this.insertEnergy(insertRequest));
		log.info("调用td的insert得方法执行完成");
		serverChannel.shutdown();
		return mls.getIsOk();
	}

	public EnergyHourOuterClass.InsertRequest insertEnergy(InsertRequest insertRequest) {
		EnergyHourOuterClass.InsertRequest.Builder builder = EnergyHourOuterClass.InsertRequest.newBuilder();
		builder.setTableName(insertRequest.getTableName());
		builder.setStableName(insertRequest.getStableName());
		builder.setTag(insertTag(insertRequest.getTag()));
		builder.setStartTime(insertRequest.getStartTime());
		builder.setEndTime(insertRequest.getEndTime());
		List<Datas> datasList = insertRequest.getDatasList();
		List<EnergyHourOuterClass.Data> data = new ArrayList<>();
		for (Datas datas : datasList) {
			 data = insertData(insertRequest.getDatasList(),datas.getPointValues());
		}
		builder.addAllData(data);
		return builder.build();
	}

	public List<EnergyHourOuterClass.PointValue> insertPointValue(List<PointValue> pointValueList) {
		List<EnergyHourOuterClass.PointValue> res=new ArrayList<>();
		if (!CollectionUtils.isEmpty(pointValueList)){
			for (PointValue value : pointValueList) {
				EnergyHourOuterClass.PointValue.Builder pointValue = EnergyHourOuterClass.PointValue.newBuilder();
				pointValue.setName(value.getName());
				pointValue.setValue(value.getValue());
				res.add(pointValue.build());
			}
		}
		return res;
	}

	public EnergyHourOuterClass.Tag insertTag(Tag tags) {
		EnergyHourOuterClass.Tag.Builder tag = EnergyHourOuterClass.Tag.newBuilder();
		if (tags != null){
			tag.setGroupId(tags.getGroupId());
			tag.setLevel(tags.getLevel());
		}
		return tag.build();
	}

	public  List<EnergyHourOuterClass.Data>	insertData(List<Datas> datas,List<PointValue> pointValueList) {
		List<EnergyHourOuterClass.Data> res = new ArrayList<>();
		if (!CollectionUtils.isEmpty(datas)){
			List<EnergyHourOuterClass.PointValue> pointValue = insertPointValue(pointValueList);
			for (Datas data1 : datas) {
				EnergyHourOuterClass.Data.Builder data = EnergyHourOuterClass.Data.newBuilder();
				data.setTs(data1.getTs());
				data.addAllPoint(pointValue);
				res.add(data.build());
			}
		}
		return res;
	}

}
