package com.bmts.heating.middleware.monitor.service;

import com.bmts.heating.commons.grpc.lib.services.points.PointGrpc;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.distribution.config.GovernConnection;
import com.bmts.heating.middleware.monitor.pojo.MonitorGrpcResult;
import com.bmts.heating.middleware.monitor.utils.MonitorPointUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MonitorIssueService
 * @Description: 任务下发
 * @Author: pxf
 * @Date: 2020/11/24 11:42
 * @Version: 1.0
 */
@Service
public class MonitorIssueService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorIssueService.class);

    @Autowired
    @Qualifier("serviceGovernCenter")
    private DistributionCenterAdapter distributionCenterAdapter;

    private ThreadLocal<GovernConnection> conn = new ThreadLocal<GovernConnection>();

    public MonitorGrpcResult monitorIssue(List<Message_Point_Issue> message) {
        MonitorGrpcResult grpcResult = new MonitorGrpcResult();
        if (CollectionUtils.isEmpty(message)) {
            grpcResult.setCode("NULL");
            grpcResult.setMsg("请求数据为空!");
            return grpcResult;
        }

        //获取连接信息
        conn.set(distributionCenterAdapter.getConnectionFromConsul("monitor71"));
        if (conn == null) {
            //获取失败,降级处理,从本地数据库获取连接信息
            LOGGER.error("连接 MonitorIssue GRPC 服务 {} 失败！下发的点集合是: [ {} ]", "monitor71", message.toString());
        }
        ManagedChannel serverChannel = ManagedChannelBuilder.forAddress(conn.get().getService_host(), new Integer(conn.get().getService_port())).usePlaintext().build();
        PointGrpc.PointBlockingStub stub = PointGrpc.newBlockingStub(serverChannel);
        PointOuterClass.PointIssueList.Builder listBuilder = PointOuterClass.PointIssueList.newBuilder();

        for (Message_Point_Issue issue : message) {
            PointOuterClass.MessagePointIssue.Builder messageBuilder = PointOuterClass.MessagePointIssue.newBuilder();
            messageBuilder.setApplicationName(issue.getApplication_name());
//            messageBuilder.setValidTimeStamp(issue.getValidTimeStamp());
            messageBuilder.setDeviceId(issue.getDevice_id());

            List<PointL> pointLS = issue.getPointLS();
            if (!CollectionUtils.isEmpty(pointLS)) {
                for (PointL point : pointLS) {
                    PointOuterClass.PointL.Builder builder = PointOuterClass.PointL.newBuilder();
                    MonitorPointUtil.toProto(point, builder);
                    messageBuilder.addPointLS(builder);
                }
            }
            listBuilder.addMpilist(messageBuilder);
        }
        PointOuterClass.Result result = stub.monitorIssue(listBuilder.build());
        grpcResult.setCode(result.getCode());
        grpcResult.setMsg(result.getMsg());
        List<PointOuterClass.MessagePointIssue> reslistList = result.getReslistList();
        List<Message_Point_Issue> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reslistList)) {
            for (PointOuterClass.MessagePointIssue messageIssue : reslistList) {
                Message_Point_Issue messagePointIssue = new Message_Point_Issue();
                messagePointIssue.setApplication_name(messageIssue.getApplicationName());
                messagePointIssue.setDevice_id(messageIssue.getDeviceId());
//                messagePointIssue.setValidTimeStamp(messageIssue.getValidTimeStamp());
                List<PointL> pointList = new ArrayList<>();
                List<PointOuterClass.PointL> pointLSList = messageIssue.getPointLSList();
                if (!CollectionUtils.isEmpty(pointLSList)) {
                    for (PointOuterClass.PointL po : pointLSList) {
                        PointL point = MonitorPointUtil.toPojo(po);
                        pointList.add(point);
                    }
                }
                messagePointIssue.setPointLS(pointList);
                newList.add(messagePointIssue);
            }

        }
        grpcResult.setMessageList(newList);
        serverChannel.shutdown();
        return grpcResult;
    }
}
