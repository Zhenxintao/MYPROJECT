package com.bmts.heating.middleware.pattern;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.annotation.Fusing;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.Pattern.PatternGrpc;
import com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass;
import com.bmts.heating.commons.grpc.lib.util.ProtoCompiler;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Astrict( servicename = "pattern-server-one", servicetype = Astrict.EnumService.grpc )
public class PatternSavantGrpcService extends SavantServices {
    private final static Logger LOGGER = LoggerFactory.getLogger(PatternSavantGrpcService.class);

    @Fusing(callbackhandler = PattHo.class)
    public List<Feature> ListFeatures(int id,String name,String address) throws MicroException {
        ConnectionToken cd=null;
        try {
            //获取令牌
            cd= super.getToken("pattern-server-one");
        } catch (MicroException e) {
            throw e;
        }
        ManagedChannel serverChannel= ManagedChannelBuilder.forAddress(cd.getHost(),new Integer(cd.getPort())).usePlaintext().build();
        PatternGrpc.PatternBlockingStub stub = PatternGrpc.newBlockingStub(serverChannel);
        PatternOuterClass.Rectangle request= PatternOuterClass.Rectangle.newBuilder().setPid(id).setPname(name).setAddress(address).build();
        Iterator<PatternOuterClass.Feature> features= stub.listFeatures(request);
        List<Feature> flist = new ArrayList<Feature>();
        while (features.hasNext()){
            try {
                flist.add(ProtoCompiler.toPojoBean(Feature.class,features.next()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverChannel.shutdownNow();
        //释放令牌
        super.backToken("pattern-server-one",cd);
        return flist;
    }
}
