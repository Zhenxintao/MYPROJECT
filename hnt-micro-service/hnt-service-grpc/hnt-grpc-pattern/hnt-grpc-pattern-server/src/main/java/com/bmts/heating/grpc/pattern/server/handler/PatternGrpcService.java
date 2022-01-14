package com.bmts.heating.grpc.pattern.server.handler;

import com.bmts.heating.commons.grpc.lib.services.Pattern.PatternGrpc;
import com.bmts.heating.commons.grpc.lib.services.Pattern.PatternOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService( PatternOuterClass.class)
public class PatternGrpcService extends PatternGrpc.PatternImplBase {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PatternGrpcService.class);

    @Override
    public void listFeatures(PatternOuterClass.Rectangle rectangle, StreamObserver<PatternOuterClass.Feature> responseObserver){
        LOGGER.info("获得参数panme:{}--------------------------------",rectangle.getPname());
        //准备一个返回的list
        List<Feature> flist= new ArrayList<Feature>();
        Feature f1 = new Feature();
        Feature f2 = new Feature();
        Feature f3 = new Feature();
        f1.setLocation(1);
        f2.setLocation(2);
        f3.setLocation(3);
        flist.add(f1);
        flist.add(f2);
        flist.add(f3);
        //PatternOuterClass.Feature.Builder replayBuilder = PatternOuterClass.Feature.newBuilder();
        for(Feature f: flist){
            if(rectangle.getPid()>10){
                //ProtoCompiler.toProtoBean(replayBuilder,f);
                responseObserver.onNext(PatternOuterClass.Feature.newBuilder().setLocation(f.getLocation()).build());//流式返回
            }
        }
        responseObserver.onCompleted(); //流结束
    }
}
