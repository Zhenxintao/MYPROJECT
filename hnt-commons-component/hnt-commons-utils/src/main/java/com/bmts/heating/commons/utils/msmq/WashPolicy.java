package com.bmts.heating.commons.utils.msmq;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

@Data
@ToString
@Description("清洗策略")
@Component
public class WashPolicy {

    private int w_id;   //清洗id
    private String desc;    //描述
}
