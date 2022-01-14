//package com.bmts.heating.middleware.cache.services;
//
//import com.bmts.heating.commons.container.performance.annotation.Astrict;
//import com.bmts.heating.commons.container.performance.config.ConnectionToken;
//import com.bmts.heating.commons.container.performance.config.SavantServices;
//import com.bmts.heating.commons.container.performance.exception.MicroException;
//import com.bmts.heating.commons.utils.msmq.PointL;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import lombok.extern.slf4j.Slf4j;
//import netpoint.NetPointGrpc;
//import netpoint.NetPointOuterClass;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@Astrict(servicename = "NetRealData", servicetype = Astrict.EnumService.grpc)
//public class RealDataNetEntryService extends SavantServices {
//    public Object pushPoint(PointL pointL) throws MicroException {
//        NetPointGrpc.NetPointBlockingStub stub = buildStub();
//        NetPointOuterClass.NetPointL.Builder builder=NetPointOuterClass.NetPointL.newBuilder();
//        builder.setPointAddress("123");
//        builder.setPointId(123);
//        builder.setDataType(1);
//        builder.setQualityStrap(1);
//        builder.setHeatingSystemId(3);
//        builder.setType(2);
//        return stub.push(builder.build());
//    }
//
//    private NetPointGrpc.NetPointBlockingStub buildStub() throws MicroException {
////        ConnectionToken cd = null;
////        try {
////            //获取令牌
////            cd = super.getToken("NetRealData");
////        } catch (MicroException e) {
////            throw e;
////        }
//        ManagedChannel serverChannel = ManagedChannelBuilder.forAddress("10.0.16.66", new Integer(8092)).usePlaintext().build();
//        NetPointGrpc.NetPointBlockingStub stub = NetPointGrpc.newBlockingStub(serverChannel);
//        return stub;
//    }
//}
