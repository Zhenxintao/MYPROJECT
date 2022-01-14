package com.bmts.heating.middleware.dataCleaing.pojo;

import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import lombok.Data;

import java.util.List;

@Data
public class GrpcResult {
    private String code;
    private String msg;
    private List<Message_Point_Issue> messageList;
}
