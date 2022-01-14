package com.bmts.heating.middleware.dataCleaing.service;

import com.bmts.heating.commons.container.performance.annotation.Astrict;
import com.bmts.heating.commons.container.performance.annotation.Fusing;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.grpc.lib.services.points.PointGrpc;
import com.bmts.heating.commons.grpc.lib.services.points.PointOuterClass;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.dataCleaing.ho.PointIssueHo;
import com.bmts.heating.middleware.dataCleaing.pojo.GrpcResult;
import com.bmts.heating.middleware.dataCleaing.utils.PointUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Astrict(servicename = "dataclean-server-one", servicetype = Astrict.EnumService.grpc)
public class PointIssueService extends SavantServices {

    @Fusing(callbackhandler = PointIssueHo.class)
    public GrpcResult messageIssue(List<Message_Point_Issue> message) throws MicroException {
        GrpcResult grpcResult = new GrpcResult();
        if (CollectionUtils.isEmpty(message)) {
            grpcResult.setCode("NULL");
            grpcResult.setMsg("请求数据为空!");
            return grpcResult;
        }
        ConnectionToken cd = null;
        try {
            //获取令牌
            cd = super.getToken("dataclean-server-one");
        } catch (MicroException e) {
            throw e;
        }

        ManagedChannel serverChannel = ManagedChannelBuilder.forAddress(cd.getHost(), new Integer(cd.getPort())).usePlaintext().build();
        PointGrpc.PointBlockingStub stub = PointGrpc.newBlockingStub(serverChannel);
        PointOuterClass.PointIssueList.Builder listBuilder = PointOuterClass.PointIssueList.newBuilder();

        for (Message_Point_Issue issue : message) {
            PointOuterClass.MessagePointIssue.Builder messageBuilder = PointOuterClass.MessagePointIssue.newBuilder();
            messageBuilder.setApplicationName(issue.getApplication_name());
            messageBuilder.setDeviceId(issue.getDevice_id());

            List<PointL> pointLS = issue.getPointLS();
            if (!CollectionUtils.isEmpty(pointLS)) {
                for (PointL point : pointLS) {
                    PointOuterClass.PointL.Builder builder = PointOuterClass.PointL.newBuilder();
                    // grpc 得传参
                    PointUtil.toProto(point, builder);
                    messageBuilder.addPointLS(builder);
                }
            }
            listBuilder.addMpilist(messageBuilder);
        }
        PointOuterClass.Result result = stub.messageIssue(listBuilder.build());
        grpcResult.setCode(result.getCode());
        List<PointOuterClass.MessagePointIssue> reslistList = result.getReslistList();
        List<Message_Point_Issue> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reslistList)) {
            for (PointOuterClass.MessagePointIssue messageIssue : reslistList) {
                Message_Point_Issue messagePointIssue = new Message_Point_Issue();
                messagePointIssue.setApplication_name(messageIssue.getApplicationName());
                messagePointIssue.setDevice_id(messageIssue.getDeviceId());

                List<PointL> pointList = new ArrayList<>();
                List<PointOuterClass.PointL> pointLSList = messageIssue.getPointLSList();
                if (!CollectionUtils.isEmpty(pointLSList)) {
                    for (PointOuterClass.PointL po : pointLSList) {
                        // grpc 的返回值处理
                        PointL point = PointUtil.toPojo(po);
                        pointList.add(point);
                    }
                }

                messagePointIssue.setPointLS(pointList);
                newList.add(messagePointIssue);
            }

        }
        grpcResult.setMessageList(newList);

        serverChannel.shutdown();
        //释放令牌
        super.backToken("dataclean-server-one", cd);
        return grpcResult;
    }


}
