package com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil;

import lombok.Data;
import org.springframework.context.annotation.Description;

@Data
@Description("pvss返回数据接收类")
public class PointLTemp {

    // 点 id
    private int pointId; // 原样回复
    // 点地址
    private String pointAddress; // 原样回复

    // 点类型(1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double)
    private Integer type; // 需要
    // 时间戳
    private Long timeStamp; // 需要
    // 质量戳
    private Integer qualityStamp; // 需要
    // 值
    private String orgValue; // 需要
    // 数据类型（一网数据 1、二网数据 2、用户数 3)
    private Integer dataType;  // 需要-对于既定系统该值为固定值
    // 扩展字段    包括(事故低报警 accidentLower, 事故高报警 accidentHigh ,运行低报警 runningLower,运行高报警  runningHigh )
    private String expandDesc;     //扩展字段

}
