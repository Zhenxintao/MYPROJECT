package com.bmts.heating.middleware.monitor.service;

import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import com.bmts.heating.middleware.monitor.pojo.MonitorGrpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: GrpcIssueService
 * @Description: 任务下发
 * @Author: pxf
 * @Date: 2020/11/24 11:42
 * @Version: 1.0
 */
@Service
public class GrpcIssueService {

    @Autowired
    private MonitorIssueService monitorIssueService;

    public Response setGrpc(List<PointL> list) {
        Response response = new Response();
        if (CollectionUtils.isEmpty(list)) {
            return Response.fail("下发数据为空！");
        }
        Map<String, List<PointL>> collectMap = list.stream().collect(Collectors.groupingBy(e -> e.getDeviceId() + e.getApplicationName()));
        List<Message_Point_Issue> message = new ArrayList<>();
        for (String key : collectMap.keySet()) {
            List<PointL> pointLS = collectMap.get(key);

            Message_Point_Issue pointIssue = new Message_Point_Issue();
            pointIssue.setApplication_name(pointLS.get(0).getApplicationName());
            pointIssue.setDevice_id(pointLS.get(0).getDeviceId());
            pointIssue.setPointLS(pointLS);
            message.add(pointIssue);
        }
        MonitorGrpcResult monitorGrpcResult = monitorIssueService.monitorIssue(message);
        if (Objects.equals(monitorGrpcResult.getCode(), "SUCCESS")) {
            return Response.success();
        }
        if (Objects.equals(monitorGrpcResult.getCode(), "FAIL")) {
            List<PointL> returnList = new ArrayList<>();
            List<Message_Point_Issue> messageList = monitorGrpcResult.getMessageList();
            for (Message_Point_Issue messgIssue : messageList) {
                returnList.addAll(messgIssue.getPointLS());
            }
            if (returnList.size() == list.size()) {
                response.setData(returnList);
                response.setCode(ResponseCode.FAIL.getCode());
                response.setMsg("下发任务失败！");
                return response;
            }
            if (returnList.size() > 0) {
                response.setData(returnList);
                response.setCode(ResponseCode.MIDDLE.getCode());
                response.setMsg("下发任务部分成功部分失败！");
                return response;
            }

        }

        return Response.success();
    }

}
