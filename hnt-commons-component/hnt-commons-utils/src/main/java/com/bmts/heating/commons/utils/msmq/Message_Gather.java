package com.bmts.heating.commons.utils.msmq;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ToString
@Description("批量消息")
@Component
public class Message_Gather {

    // 热力站 或 热源  id
    private Integer relevanceId;

    // 关联类型  枚举TreeLevel  3：热力站   4:热源
    private int relevanceType;

    //  热力站 或 热源  下面的所有点 总数
    private int total;

    // 入 kafka 库的  时间戳
    private Long timeStamp;

    private List<Message_Point_Gather> batchPoints;
}
