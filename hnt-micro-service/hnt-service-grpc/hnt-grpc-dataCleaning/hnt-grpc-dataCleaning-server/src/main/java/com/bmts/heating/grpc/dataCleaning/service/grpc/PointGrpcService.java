package com.bmts.heating.grpc.dataCleaning.service.grpc;

import com.bmts.heating.commons.grpc.lib.services.points.PointGrpc;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.distribution.config.GovernConnection;
import com.bmts.heating.grpc.dataCleaning.enums.GrpcCode;
import com.bmts.heating.grpc.dataCleaning.service.analyse.PointDataAnalyseService;
import com.bmts.heating.grpc.dataCleaning.service.issue.PointIssueService;
import com.bmts.heating.grpc.dataCleaning.utils.PointGrpcUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@GrpcService(PointOuterClass.class)
public class PointGrpcService extends PointGrpc.PointImplBase {

    @Autowired
    @Qualifier("serviceGovernCenter")
    private DistributionCenterAdapter distributionCenterAdapter;

    private ThreadLocal<GovernConnection> conn = new ThreadLocal<>();

    @Autowired
    private PointDataAnalyseService pointDataAnalyseService;

    @Autowired
    private PointIssueService pointIssueService;

    @Override
    public void messageIssue(PointOuterClass.PointIssueList request, StreamObserver<PointOuterClass.Result> responseObserver) {
        // 接收参数
        List<PointOuterClass.MessagePointIssue> requestList = request.getMpilistList();
        PointOuterClass.Result.Builder replyBuilder = PointOuterClass.Result.newBuilder();
        if (CollectionUtils.isEmpty(requestList)) {
            replyBuilder.setCode(GrpcCode.CODE_NULL.getCode());
            replyBuilder.setMsg(GrpcCode.CODE_NULL.getName());
            List<PointOuterClass.MessagePointIssue> mm = new ArrayList<PointOuterClass.MessagePointIssue>();
            replyBuilder.addAllReslist(mm);
            responseObserver.onNext(replyBuilder.build());
            responseObserver.onCompleted();
            return;
        }
        // 对  JK  和 PVSS 分组 的数据进行清洗 下发的时候数据清洗，直接利用poto 对象进行操作
        for (PointOuterClass.MessagePointIssue messageIssue : requestList) {
            // 判断是不是有效时间戳之内
            if (messageIssue.getValidTimeStamp() >= System.currentTimeMillis()) {
                List<PointOuterClass.PointL> pointLSList = messageIssue.getPointLSList();
                if (!CollectionUtils.isEmpty(pointLSList)) {
                    for (PointOuterClass.PointL po : pointLSList) {
                        // 判断是 JK  还是   PVSS 进行清洗
                        if (StringUtils.isEmpty(po.getPointlsSign())) {
                            pointIssueService.matureToRawJK(po);
                        } else {
                            pointIssueService.matureToRawPVSS(po);
                        }
                    }
                }
            }
        }
        // 返回的list
        List<PointOuterClass.MessagePointIssue> returnList = new ArrayList<>();
        // 调用下发的 grpc  服务  进行业务处理
        Map<String, List<PointOuterClass.MessagePointIssue>> groupMap = requestList.stream().collect(Collectors.groupingBy(e -> e.getApplicationName()));
        for (String appliccationName : groupMap.keySet()) {
            List<PointOuterClass.MessagePointIssue> messagePointIssues = groupMap.get(appliccationName);
            // 根据服务名称进行调用  grpc  服务
            conn.set(distributionCenterAdapter.getConnectionFromConsul(appliccationName));
            if (conn == null) {
                log.error("连接下发 GRPC {} 服务失败！下发的点集合是: [ {} ]", appliccationName, messagePointIssues.toString());
            }
            ManagedChannel serverChannel = ManagedChannelBuilder.forAddress(conn.get().getService_host(), new Integer(conn.get().getService_port())).usePlaintext().build();
            PointGrpc.PointBlockingStub stub = PointGrpc.newBlockingStub(serverChannel);
            PointOuterClass.PointIssueList.Builder listBuilder = PointOuterClass.PointIssueList.newBuilder();
            listBuilder.addAllMpilist(messagePointIssues);
            PointOuterClass.Result result = stub.monitorIssue(listBuilder.build());
            List<PointOuterClass.MessagePointIssue> reslistList = result.getReslistList();
            returnList.addAll(reslistList);
            serverChannel.shutdown();
        }
        String code = "";
        String msg = "";
        if (CollectionUtils.isEmpty(returnList)) {
            code = GrpcCode.CODE_SUCCESS.getCode();
            msg = GrpcCode.CODE_SUCCESS.getName();
        } else {
            code = GrpcCode.CODE_FAIL.getCode();
            msg = GrpcCode.CODE_FAIL.getName();
        }
        // 业务处理  请求返回的  根据不同的实例请求不同的实例服务
        replyBuilder.setCode(code);
        replyBuilder.setMsg(msg);
        replyBuilder.addAllReslist(returnList);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }


    @Override
    public void getEntity(PointOuterClass.PointList request, StreamObserver<PointOuterClass.ResStatus> responseObserver) {
        final PointOuterClass.ResStatus.Builder replyBuilder = PointOuterClass.ResStatus.newBuilder();
        if (CollectionUtils.isEmpty(request.getPlistList())) {
            replyBuilder.setStatus(false);
            responseObserver.onNext(replyBuilder.build());
            responseObserver.onCompleted();
            return;
        }
        Boolean status = false;
        // 把 poroBean   转为POJOBean
        List<PointL> list = new ArrayList<>();
        for (PointOuterClass.PointL po : request.getPlistList()) {
            PointL point = PointGrpcUtil.toPojo(po);
            list.add(point);
        }
        if (!CollectionUtils.isEmpty(list)) {
            // 这是区分  是 jk 还是  PVSS 的
            // 数据清洗
            status = pointDataAnalyseService.pointAnalyse(list, request.getSystemType());
        }
        replyBuilder.setStatus(status);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }


}
