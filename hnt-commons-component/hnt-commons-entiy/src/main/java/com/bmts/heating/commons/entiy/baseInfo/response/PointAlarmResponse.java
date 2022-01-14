package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PointAlarmResponse {

    /**
     * 事故低报警
     */
    @ApiModelProperty("事故低报警")
    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */
    @ApiModelProperty("事故高报警")
    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */
    @ApiModelProperty("运行低报警")
    private BigDecimal runningLower;

    /**
     * 运行高报警
     */
    @ApiModelProperty("运行高报警")
    private BigDecimal runningHigh;


    /**
     * 是否报警
     */
    @ApiModelProperty("是否报警")
    private Boolean isAlarm;

    /**
     * 报警值 单选  0 和 1
     */
    @ApiModelProperty("报警值 单选  0 和 1")
    private Integer alarmValue;

    /**
     * 报警外键
     */
    @ApiModelProperty("报警外键")
    private Integer alarmConfigId;

    /**
     * 报警外键
     */
    @ApiModelProperty("关联 pointConfig 表 id")
    private Integer pointConfigId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    private Integer id;

    /**
     * 关联id
     */
    @ApiModelProperty("关联id")
    private Integer relevanceId;


    /**
     * 报警级别
     */
    @ApiModelProperty("报警级别")
    private Integer grade;

    /**
     * 报警描述
     */
    @ApiModelProperty("报警描述")
    private String alarmDesc;


    /**
     * 报警级别 json
     */
    @ApiModelProperty("报警级别 json")
    private String rankJson;


    /*---标准点信息---*/
    @ApiModelProperty("关联参量")
    private String pointStandardName;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("单位Id")
    private String pointUnitId;

    @ApiModelProperty("单位名称")
    private String unitValue;

    @ApiModelProperty("网侧类型")
    private String netFlag;


    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    private Integer dataType;


    /*———关联组织信息———*/
    @ApiModelProperty("系统分区/机组 名称")
    private String heatSystemName;

    @ApiModelProperty("换热站id")
    private Integer heatTransferStationId;

    @ApiModelProperty("换热站 名称")
    private String heatTransferStationName;

    @ApiModelProperty("热源id")
    private Integer heatSourceId;

    @ApiModelProperty("热源 名称")
    private String heatSourceName;

    @ApiModelProperty("热网id")
    private Integer heatNetId;

    @ApiModelProperty("热网名称")
    private String heatNetName;

    @ApiModelProperty("控制柜id")
    private Integer heatCabinetId;

    @ApiModelProperty("控制柜名称")
    private String heatCabinetName;

    @ApiModelProperty("参量类型名称")
    private String pointParameterTypeName;
}
