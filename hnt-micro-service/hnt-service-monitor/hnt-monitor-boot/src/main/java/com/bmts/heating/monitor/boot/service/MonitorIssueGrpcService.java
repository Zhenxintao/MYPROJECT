package com.bmts.heating.monitor.boot.service;

import com.bmts.heating.commons.grpc.lib.services.points.PointGrpc;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.monitor.utils.MonitorPointUtil;
import com.bmts.heating.monitor.boot.enums.MonitorGrpcCode;
import com.bmts.heating.monitor.plugins.jk.constructors.JkConstructors;
import com.bmts.heating.monitor.plugins.pvss.constructors.PvssConstructors;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@GrpcService(PointOuterClass.class)
public class MonitorIssueGrpcService extends PointGrpc.PointImplBase {

    @Autowired
    private PvssConstructors pvssConstructors;
    @Autowired
    private JkConstructors jkConstructors;

    @Override
    public void monitorIssue(PointOuterClass.PointIssueList request, StreamObserver<PointOuterClass.Result> responseObserver) {
        // 接收参数
        List<PointOuterClass.MessagePointIssue> requestList = request.getMpilistList();
        final PointOuterClass.Result.Builder replyBuilder = PointOuterClass.Result.newBuilder();
        if (CollectionUtils.isEmpty(requestList)) {
            replyBuilder.setCode(MonitorGrpcCode.CODE_NULL.getCode());
            replyBuilder.setMsg(MonitorGrpcCode.CODE_NULL.getName());
            List<PointOuterClass.MessagePointIssue> mm = new ArrayList<PointOuterClass.MessagePointIssue>();
            replyBuilder.addAllReslist(mm);
            responseObserver.onNext(replyBuilder.build());
            responseObserver.onCompleted();
            return;
        }
        // 对  JK  和 PVSS 进行分组下发
        List<PointL> jkPointList = new ArrayList<>();
        List<PointL> pvssPointList = new ArrayList<>();
        for (PointOuterClass.MessagePointIssue messageIssue : requestList) {
            List<PointOuterClass.PointL> pointLSList = messageIssue.getPointLSList();
            if (!CollectionUtils.isEmpty(pointLSList)) {
                for (PointOuterClass.PointL po : pointLSList) {
                    // 根据 pointls_sign 判断 是 JK  还是   PVSS
                    // JK 的  pointls_sign  是空，不为空的是 PVSSs
                    if (StringUtils.isEmpty(po.getPointlsSign())) {
                        // PointL jkPointL = setProperty(po);
                        PointL jkPointL = MonitorPointUtil.toPojo(po);
                        jkPointList.add(jkPointL);
                    } else {
                        // PointL pvssPointL = setProperty(po);
                        PointL pvssPointL = MonitorPointUtil.toPojo(po);
                        pvssPointList.add(pvssPointL);
                    }
                }
            }

        }
        List<PointL> returnAllList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(pvssPointList)) {
                List<PointL> pointLS = pvssConstructors.handIssue(pvssPointList);
                returnAllList.addAll(pointLS);
            }
            if (!CollectionUtils.isEmpty(jkPointList)) {
                List<PointL> pointLS = jkConstructors.handIssue(jkPointList);
                returnAllList.addAll(pointLS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String code = "";
        String msg = "";
        if (CollectionUtils.isEmpty(returnAllList)) {
            code = MonitorGrpcCode.CODE_SUCCESS.getCode();
            msg = MonitorGrpcCode.CODE_SUCCESS.getName();
        } else {
            code = MonitorGrpcCode.CODE_FAIL.getCode();
            msg = MonitorGrpcCode.CODE_FAIL.getName();
        }

        List<PointOuterClass.MessagePointIssue> listMessagePointIssue = new ArrayList<>();
        Map<String, List<PointL>> groupMap = returnAllList.stream().collect(Collectors.groupingBy(e -> e.getApplicationName() + e.getDeviceId()));
        for (String key : groupMap.keySet()) {
            PointOuterClass.MessagePointIssue.Builder messageBuilder = PointOuterClass.MessagePointIssue.newBuilder();
            List<PointL> pointLS = groupMap.get(key);
            if (!CollectionUtils.isEmpty(pointLS)) {
                messageBuilder.setApplicationName(pointLS.get(0).getApplicationName());
                messageBuilder.setDeviceId(pointLS.get(0).getDeviceId());
                for (PointL pL : pointLS) {
                    PointOuterClass.PointL.Builder builder = PointOuterClass.PointL.newBuilder();
                    // grpc 得传参
                    MonitorPointUtil.toProto(pL, builder);
                    messageBuilder.addPointLS(builder);
                }
                listMessagePointIssue.add(messageBuilder.build());
            }
        }
        // 业务处理  请求返回的  根据不同的实例请求不同的实例服务
        replyBuilder.setCode(code);
        replyBuilder.setMsg(msg);
        replyBuilder.addAllReslist(listMessagePointIssue);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }


}
