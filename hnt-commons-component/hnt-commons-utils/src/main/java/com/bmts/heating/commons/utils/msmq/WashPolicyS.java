package com.bmts.heating.commons.utils.msmq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WashPolicyS {
    private int wpId;   //策略id
    private String wpName;  //策略名称
    private String boltName; //所属清洗节点名称
}
