package com.bmts.heating.commons.utils.msmq;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@ToString
@Description("投递消息")
@Component
public class Message_Point_Gather {
    private int data_type;  //数据类型（一网数据、二网数据、用户数据）
    // 系统 id
    private int relevanceId;  //关联Id
    // 系统编号
    private Integer number;
    private int level;
    private Map<String, PointL> pointLS;    //点集合
    // 控制柜 id
    private Integer heatCabinetId;

}
