package com.bmts.heating.middleware.dataCleaing.service;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.annotation.Fusing;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.points.PointGrpc;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.dataCleaing.ho.PointSavantHo;
import com.bmts.heating.middleware.dataCleaing.utils.PointUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Astrict(servicename = "dataclean-server-one", servicetype = Astrict.EnumService.grpc)
public class PointSavantService extends SavantServices {
    private final static Logger LOGGER = LoggerFactory.getLogger(PointSavantService.class);


    @Fusing(callbackhandler = PointSavantHo.class)
    public Boolean getEntity(List<PointL> pointL, String systemType) throws MicroException {
        ConnectionToken cd = null;
        try {
            //获取令牌
            cd = super.getToken("dataclean-server-one");
        } catch (MicroException e) {
            throw e;
        }
        ManagedChannel serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), new Integer(cd.getPort())).usePlaintext().build();
        PointGrpc.PointBlockingStub stub = PointGrpc.newBlockingStub(serverChannel);
        PointOuterClass.PointList.Builder listBuilder = PointOuterClass.PointList.newBuilder();
        listBuilder.setSystemType(systemType);
        for (PointL point : pointL) {
            PointOuterClass.PointL.Builder builder = PointOuterClass.PointL.newBuilder();
            PointUtil.toProto(point, builder);
            listBuilder.addPlist(builder);
        }
        Boolean reply = stub.getEntity(listBuilder.build()).getStatus();
        serverChannel.shutdown();
        //释放令牌
        super.backToken("dataclean-server-one", cd);
        return reply;
    }

}
