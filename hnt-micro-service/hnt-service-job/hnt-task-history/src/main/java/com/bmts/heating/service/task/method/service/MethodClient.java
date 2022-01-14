package com.bmts.heating.service.task.method.service;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.common.Common;
import com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodGrpc;
import com.bmts.heating.commons.grpc.lib.services.method.ExecuteMethodOuterClass;
import com.bmts.heating.service.task.method.pojo.MethodName;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Astrict(servicename = "td",servicetype = Astrict.EnumService.grpc)
public class MethodClient extends SavantServices {

	public Boolean executeMethods(MethodName methodName) {
		String serverName = "td";
		ConnectionToken cd = null;
		ManagedChannel serverChannel = null;
		ExecuteMethodGrpc.ExecuteMethodBlockingStub stub = null;
		try {
			cd = super.getToken(serverName);
			serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), Integer.valueOf(cd.getPort())).usePlaintext().build();
			stub = ExecuteMethodGrpc.newBlockingStub(serverChannel);
		} catch (MicroException e) {
			e.printStackTrace();
			super.clearToken(serverName, e);
		} finally {
			super.backToken(serverName, cd);
		}
		Common.BoolResult mls = stub.executeMethod(this.insertMethodName(methodName));
		serverChannel.shutdown();
		return mls.getIsOk();
	}

	private ExecuteMethodOuterClass.MethodName insertMethodName(MethodName methodName) {
		ExecuteMethodOuterClass.MethodName.Builder builder = ExecuteMethodOuterClass.MethodName.newBuilder();
		builder.setName(methodName.getName());
		return builder.build();
	}

	/*public Boolean executeMethods(MethodName methodName) {
		String serverName = "td";
		//ConnectionToken cd = null;
		ManagedChannel serverChannel = null;
		ExecuteMethodGrpc.ExecuteMethodBlockingStub stub = null;
		try {
			//cd = super.getToken(serverName);
			serverChannel = ManagedChannelBuilder.forAddress("10.0.2.51", 50051).usePlaintext().build();
			stub = ExecuteMethodGrpc.newBlockingStub(serverChannel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.BoolResult mls = stub.executeMethod(this.insertMethodName(methodName));
		serverChannel.shutdown();
		return mls.getIsOk();
	}*/

}
