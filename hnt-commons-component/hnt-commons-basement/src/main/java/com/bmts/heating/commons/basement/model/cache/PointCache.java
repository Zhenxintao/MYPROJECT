package com.bmts.heating.commons.basement.model.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointCache {
    /**
     * 事故低报警
     */

    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */

    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */


    private BigDecimal runningLower;

    /**
     * 运行高报警
     */

    private BigDecimal runningHigh;
    private int pointConfig;

    /**
     * 报警值 单选  0 和 1
     */
    private Integer alarmValue;

    /**
     * 量程低报警
     */
    /**
     * 辅助查询
     */
    private BigDecimal rangeLower;
    private Boolean isAlarm;
    private String pointParameterTypeName;
    private String unitValue;
    private String unitName;
    private String systemName;
    private int netFlag;
    private String pointStandardName;
    private String descriptionJson;

    private int pointId;    //cn
    private String pointName; // comment
    private String pointAddress;   //点地址
    private int type;    //点类型(1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double)
    private String deviceId;   //设备id
    private String applicationName;    //采集点来自服务实例名
    private String pointlsSign; //服务单体唯一标识
    private String orderValue;    //字节序
    private String value;   //值
    private Long timeStrap;  //时间戳
    private int qualityStrap;    //质量戳
    private String oldValue;
    private int relevanceId;  //所属系统id
    private String expandDesc;     //扩展字段
    private int level;
    private String expression;
    private Integer number;

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
     * 点的同步编号
     */
    private String syncNumber;

    /**
     * 报警级别
     */
    private Integer grade;

    /**
     * 报警描述
     */
    private String alarmDesc;

}
