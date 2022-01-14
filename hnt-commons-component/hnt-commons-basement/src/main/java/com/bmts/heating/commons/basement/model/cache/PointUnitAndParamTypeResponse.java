package com.bmts.heating.commons.basement.model.cache;

import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointUnitAndParamTypeResponse extends PointConfig {

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

    /**
     * 是否报警
     */
    private Boolean isAlarm;


    /**
     * 报警值 单选  0 和 1
     */
    private Integer alarmValue;

    /**
     * 报警外键
     */
    private Integer alarmConfigId;


    String pointStandardName;
    String columnName;
    String unitName;
    String pointParameterTypeName;
    String value;
    String nodeCode;
    String unitValue;
    String systemName;
    int netFlag;
    int dataLengthType;
    String expression;
    int number;
    int pointConfig;

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
