package com.bmts.heating.middleware.pattern;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.Monitor.MonitorGrpc;
import com.bmts.heating.commons.grpc.lib.services.Monitor.MonitorOuterClass;
import com.bmts.heating.commons.grpc.lib.util.ProtoCompiler;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.distribution.config.GovernConnection;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MonitorSavantGrpcService extends SavantServices {

    @Autowired
    @Qualifier("serviceGovernCenter")
    private DistributionCenterAdapter distributionCenterAdapter;

    public int getMonitorData(int id) {
        GovernConnection gc=distributionCenterAdapter.getConnectionFromConsul("monitor100");
        ManagedChannel serverChannel= ManagedChannelBuilder.forAddress(gc.getService_host(),new Integer(gc.getService_port())).usePlaintext().build();
        MonitorGrpc.MonitorBlockingStub stub =MonitorGrpc.newBlockingStub(serverChannel);
        MonitorOuterClass.RectangleM request= MonitorOuterClass.RectangleM.newBuilder().setPid(id).build();
        Iterator<MonitorOuterClass.MonitorL> mls=stub.getMonitorData(request);
        MonitorL ml = null;
        while (mls.hasNext()){
            try {
                ml =ProtoCompiler.toPojoBean(MonitorL.class,mls.next());
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }
        serverChannel.shutdownNow();
        return ml.getMloop();
    }
}
