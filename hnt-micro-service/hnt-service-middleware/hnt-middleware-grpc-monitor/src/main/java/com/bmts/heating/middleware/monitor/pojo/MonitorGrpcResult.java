package com.bmts.heating.middleware.monitor.pojo;

import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import lombok.Data;

import java.util.List;

@Data
public class MonitorGrpcResult {
    private String code;
    private String msg;
    private List<Message_Point_Issue> messageList;
}
