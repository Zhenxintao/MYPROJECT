package com.bmts.heating.commons.utils.msmq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Description;

import java.util.Map;

@Data
@ToString
@Description("采集点表")
public class PointL {

    // 批号
    private String lotNo;
    private Integer pointId;    //cn
    private String pointName; // comment
    //  标签中文名称
    private String pointStandardName;
    //    private String pointAddress;   //点地址
    private int type;    //点类型(1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double)
    private String deviceId;   //设备id
    private String applicationName;    //采集点来自服务实例名
    private String pointlsSign; //服务单体唯一标识
    private String orderValue;    //字节序
    private String value;   //值
    private Long timeStrap;  //时间戳
    private Integer qualityStrap;    //质量戳
    private String oldValue;
    private Integer relevanceId;  //关联Id
    private int level;
    private int[] washArray;   //上行清洗策略数组(可以为空)
    private int[] washDArray;   //下行清洗策略数组(可以为空)
    private int dataType;  //数据类型（一网数据、二网数据、用户数)
    private String expandDesc;     //扩展字段
    private String expression;
    private Integer number;
    private String hightLower;
    //详见 enum PointProperties 描述
    private int pointConfig;

    // 数据健康状态
    private int healthSign;

    private Map<String, WashPolicyS[]> washUpMap;   //上行清洗策略组
    private Map<String, WashPolicyS[]> washDownMap;   //下行清洗策略组


    @ApiModelProperty("点的同步编号")
    private String syncNumber;

    /**
     * 系统编号
     */
    private Integer systemNum;

    /**
     * 热力站同步编号 或热源同步编号
     */
    private Integer parentSyncNum;

    /**
     * 设备编号
     */
    private String equipmentCode;

    /**
     * 点位所属类型:1.站 2.源
     */
    private Integer heatType;

    /**
     * 报警级别
     */
    private Integer grade;

    /**
     * 报警描述
     */
    private String alarmDesc;

}
