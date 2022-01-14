package com.bmts.heating.commons.utils.msmq;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ToString
@Description("下发消息")
@Component
public class Message_Point_Issue {
    private String application_name;   // 服务名称
    private String device_id;   //设备id
    private List<PointL> pointLS;   //点集合
}
