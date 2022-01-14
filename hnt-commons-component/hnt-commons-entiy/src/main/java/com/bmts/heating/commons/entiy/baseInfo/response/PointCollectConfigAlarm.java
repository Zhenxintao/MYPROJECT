package com.bmts.heating.commons.entiy.baseInfo.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointCollectConfigAlarm {

    private int id;


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

//    /**
//     * 量程低报警
//     */
//    private BigDecimal rangeLower;
//
//
//    private BigDecimal rangeHigh;

    /**
     * 是否报警
     */

    private Boolean isAlarm;

    /**
     * 是否确认报警
     */
    private Boolean isConfirmAlarm;

//    /**
//     * 是否取反
//     */
//    private Boolean isNegation;

    /**
     * 报警值 单选  0 和 1
     */
    private Short alarmValue;

    /**
     * 报警外键
     */
    private Integer alarmConfigId;

    /**
     * 关联参量id
     */

    private Integer pointStandardId;

    /**
     * 系统分区/机组 id
     */

    private Integer heatSystemId;
    /**
     * 参量类型
     */

    private Integer pointParameterTypeId;

    private String heatSystemName;


    private String heatTransferStationName;
    private int heatTransferStationId;

    private Integer heatCabinetId;


    private String heatCabinetName;


    private String pointStandardName;

    private String columnName;

    private String heatStationOrgName;
}
